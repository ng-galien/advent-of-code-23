package com.github.nggalien.advent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

/**
 * The `Day2` interface is part of a series of solutions for "Advent of Code 2023".
 * This particular interface deals with a puzzle named "Cube Conundrum" which involves a series
 * of games with colored cubes. The objective is to determine the possible games based on the
 * number of cubes and calculate the power of cube sets.
 */
public interface Day2 {

    /**
     * Represents a non-negative quantity of items.
     */
    record Quantity(int value) {

        public Quantity {
            if (value < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
        }

        boolean isLessThan(Quantity other) {
            return value < other.value;
        }

        public static Quantity of(int value) {
            return new Quantity(value);
        }

        public Quantity add(Quantity other) {
            return new Quantity(value + other.value);
        }
    }

    /**
     * Represents a cube with a non-blank color attribute.
     */
    record Cube(String color) {

        public Cube {
            if (color == null || color.isBlank()) {
                throw new IllegalArgumentException("Color cannot be blank");
            }
        }

        public static Cube of(String red) {
            return new Cube(red);
        }
    }

    /**
     * Represents a set of cubes of a certain color and quantity.
     */
    record CubesOfColor(Cube cube, Quantity quantity) {

        int powerOfCubes() {
            return quantity.value();
        }

        static CubesOfColor of(Cube cube, Quantity quantity) {
            return new CubesOfColor(cube, quantity);
        }

        static CubesOfColor parse(String input) {
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid cube input");
            }
            return CubesOfColor.of(new Cube(parts[1]), Quantity.of(Integer.parseInt(parts[0])));
        }
    }

    /**
     * Represents a collection of `CubesOfColor`.
     */
    record Hand(Collection<CubesOfColor> cubes) {

        public Hand {
            if (cubes == null || cubes.isEmpty()) {
                throw new IllegalArgumentException("Hand cannot be empty");
            }
        }

        static Hand parse(String input) {
            return new Hand(Stream.of(input.split(","))
                    .map(String::trim)
                    .map(CubesOfColor::parse)
                    .toList());
        }
    }

    /**
     * Represents a game with an ID and a collection of `Hand`.
     */
    record Game(int id, Collection<Hand> hands) {

        public Game {
            if (hands == null || hands.isEmpty()) {
                throw new IllegalArgumentException("Game cannot be empty");
            }
        }

        static Game parse(String input) {
            String[] parts = input.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid game input");
            }
            String[] gameIdentifiers = parts[0].split(" ");
            if (gameIdentifiers.length != 2) {
                throw new IllegalArgumentException("Invalid game identifier");
            }
            if(!gameIdentifiers[0].equals("Game")) {
                throw new IllegalArgumentException("Invalid game identifier");
            }
            if (!gameIdentifiers[1].matches("\\d+")) {
                throw new IllegalArgumentException("Invalid game identifier");
            }
            int id = Integer.parseInt(gameIdentifiers[1]);
            Collection<Hand> handList = Stream.of(parts[1].split(";"))
                    .map(String::trim)
                    .map(Hand::parse)
                    .toList();
            return new Game(id, handList);
        }
    }

    /**
     * Manages a collection of cubes, supporting various operations like adding cubes,
     * checking if a certain quantity of cubes can be picked, etc.
     */
    record CubeRepository(Map<Cube, Quantity> cubes) {

        public CubeRepository() {
            this(new HashMap<>());
        }

        void add(CubesOfColor set) {
            cubes.compute(set.cube(), (_, quantity) -> Optional.ofNullable(quantity)
                    .map(q -> q.add(set.quantity()))
                    .orElse(set.quantity()));
        }

        void addMissingToFill(CubesOfColor set) {
            var existing = Optional.ofNullable(cubes.get(set.cube())).orElse(Quantity.of(0));
            if(existing.isLessThan(set.quantity())) {
                cubes.put(set.cube(), set.quantity());
            }
        }

        void addMissingToFill(Hand hand) {
            hand.cubes().forEach(this::addMissingToFill);
        }

        void addMissingToFill(Game game) {
            game.hands().forEach(this::addMissingToFill);
        }

        Quantity quantityOf(Cube cube) {
            return Optional.ofNullable(cubes.get(cube)).orElse(Quantity.of(0));
        }

        boolean canPick(CubesOfColor set) {
            return !quantityOf(set.cube()).isLessThan(set.quantity());
        }

        boolean canPick(Hand hand) {
            return hand.cubes().stream().allMatch(this::canPick);
        }

        boolean canPick(Game game) {
            return game.hands().stream().allMatch(this::canPick);
        }

        Collection<CubesOfColor> cubesForColor() {
            return cubes.entrySet().stream()
                    .map(entry -> CubesOfColor.of(entry.getKey(), entry.getValue()))
                    .toList();
        }

        static CubeRepository parse(String hand) {
            CubeRepository repository = new CubeRepository();
            Stream.of(hand.split(","))
                    .map(String::trim)
                    .map(CubesOfColor::parse)
                    .forEach(repository::add);
            return repository;
        }
    }

    /**
     * Determines the sum of the IDs of all playable games based on a given hand and set of games.
     */
    default int sumOfAllPlayableGamesNumber(String hand, String games) {
        CubeRepository repository = CubeRepository.parse(hand);
        Collection<Game> gamesToPlay = parse(games);
        return gamesToPlay.stream()
                .filter(repository::canPick)
                .mapToInt(Game::id)
                .sum();

    }

    /**
     * Calculates the power of a given game.
     */
    default long powerOfGame(Game game) {
        CubeRepository repository = new CubeRepository();
        repository.addMissingToFill(game);
        return repository.cubesForColor().stream()
                .map(CubesOfColor::powerOfCubes)
                .reduce(1, (first, second) -> first * second);
    }

    /**
     * Calculates the total power of all games.
     */
    default long powerOffAllGames(String games) {

        Collection<Game> gamesToPlay = parse(games);
        return gamesToPlay.stream().
                mapToLong(this::powerOfGame)
                .sum();
    }

    /**
     * Parses a string input into a collection of `Game` objects.
     */
    static Collection<Game> parse(String input) {
        return Stream.of(input.split("\n"))
                .map(String::trim)
                .map(Game::parse)
                .toList();
    }

    /**
     * Implements the `Day2` interface and `AdventOfCode2023.SolutionOfDay` for part one of the puzzle.
     */
    record Part1() implements Day2, AdventOfCode2023.SolutionOfDay<Integer> {
        @Override
        public int day() {
            return 2;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Integer rightAnswer() {
            return 1867;
        }

        @Override
        public Integer test() {
            String hand = """
                12 red, 13 green, 14 blue
                """;
            String games = readFileOfResource("day2.txt");
            return sumOfAllPlayableGamesNumber(hand, games);
        }
    }

    /**
     * Implements the `Day2` interface and `AdventOfCode2023.SolutionOfDay` for part two of the puzzle.
     */
    record Part2() implements Day2, AdventOfCode2023.SolutionOfDay<Long> {
        @Override
        public int day() {
            return 2;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 84538L;
        }

        @Override
        public Long test() {
            String games = readFileOfResource("day2.txt");
            return powerOffAllGames(games);
        }
    }

    /**
     * Returns an instance of `part1`.
     */
    static Part1 findPart1() {
        return new Part1();
    }

    /**
     * Returns an instance of `part2`.
     */
    static Part2 findPart2() {
        return new Part2();
    }

}
