package com.github.nggalien.advent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

/**
 --- Day 2: Cube Conundrum ---
 You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow. An Elf runs over to greet you.

 The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow. He'll be happy to explain the situation, but it's a bit of a walk, so you have some time. They don't get many visitors up here; would you like to play a game in the meantime?

 As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.

 To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.

 You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).

 For example, the record of a few games might look like this:

 Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.

 The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?

 In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8.

 Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?
 */
public interface Day2 {

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

    record Cube(String color) {
        public static Cube of(String red) {
            return new Cube(red);
        }
    }

    record CubesOfColor(Cube cube, Quantity quantity) {
        static CubesOfColor of(Cube cube, Quantity quantity) {
            return new CubesOfColor(cube, quantity);
        }

        static CubesOfColor parse(String input) {
            String[] parts = input.split(" ");
            return CubesOfColor.of(new Cube(parts[1]), Quantity.of(Integer.parseInt(parts[0])));
        }
    }

    record Hand(Collection<CubesOfColor> sets) {
        static Hand parse(String input) {
            return new Hand(Stream.of(input.split(","))
                    .map(String::trim)
                    .map(CubesOfColor::parse)
                    .toList());
        }
    }

    record Game(int id, Collection<Hand> hands) {
        static Game parse(String input) {
            String[] parts = input.split(":");
            int id = Integer.parseInt(parts[0].split(" ")[1]);
            Collection<Hand> handList = Stream.of(parts[1].split(";"))
                    .map(String::trim)
                    .map(Hand::parse)
                    .toList();
            return new Game(id, handList);
        }
    }

    record CubeRepository(Map<Cube, Quantity> cubes) {

        public CubeRepository() {
            this(new HashMap<>());
        }

        void parse(String hand) {
            Stream.of(hand.split(","))
                    .map(String::trim)
                    .map(CubesOfColor::parse)
                    .forEach(this::add);
        }

        void add(CubesOfColor set) {
            cubes.compute(set.cube(), (cube, quantity) -> Optional.ofNullable(quantity)
                    .map(q -> q.add(set.quantity()))
                    .orElse(set.quantity()));
        }

        Quantity quantityOf(Cube cube) {
            return Optional.ofNullable(cubes.get(cube)).orElse(Quantity.of(0));
        }

        boolean canPick(CubesOfColor set) {
            return !quantityOf(set.cube()).isLessThan(set.quantity());
        }

        boolean canPick(Hand hand) {
            return hand.sets().stream().allMatch(this::canPick);
        }

        boolean canPick(Game game) {
            return game.hands().stream().allMatch(this::canPick);
        }
    }


    default int sumOfAllPlayableGamesNumber(String hand, String games) {
        CubeRepository repository = new CubeRepository();
        Stream.of(hand.split(","))
                .map(String::trim)
                .map(CubesOfColor::parse)
                .forEach(repository::add);
        Collection<Game> gamesToPlay = Stream.of(games.split("\n"))
                .map(String::trim)
                .map(Game::parse)
                .toList();
        return gamesToPlay.stream()
                .filter(repository::canPick)
                .mapToInt(Game::id)
                .sum();
    }

    record Solution() implements Day2, AdventOfCode2023.SolutionOfDay<Integer> {
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
            String game = readFileOfResource("day2.txt");
            return sumOfAllPlayableGamesNumber(hand, game);
        }
    }

    static Solution find() {
        return new Solution();
    }
}
