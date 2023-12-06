package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for "Day 3: Gear Ratios" puzzle in Advent of Code 2023.
 * This interface manages the analysis of engine schematics, identifying part numbers and gears,
 * and calculating the sum of part numbers and gear ratios.
 */
public interface Day3 {

    /**
     * Represents a coordinate with x (horizontal) and y (vertical) values.
     */
    record Position(int x, int y) {
        public Position {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Position must be positive");
            }
        }
    }

    /**
     * Represents a rectangular area of the schematic, with a top left and bottom right coordinate.
     */
    record Zone(Position topLeft, Position bottomRight) {
        public Zone {
            if (topLeft.x() > bottomRight.x() || topLeft.y() > bottomRight.y()) {
                throw new IllegalArgumentException("Top left must be above and to the left of bottom right");
            }
        }

        /**
         * Returns true if the given zone is completely contained within this zone.
         *
         * @param zone the zone to check
         * @return true if the given zone is completely contained within this zone
         */
        boolean contains(Zone zone) {
            return zone.topLeft().x() >= topLeft().x()
                    && zone.topLeft().y() >= topLeft().y()
                    && zone.bottomRight().x() <= bottomRight().x()
                    && zone.bottomRight().y() <= bottomRight().y();

        }

        /**
         * Returns true if the given zone intersects this zone.
         *
         * @param zone the zone to check
         * @return true if the given zone intersects this zone
         */
        boolean intersects(Zone zone) {
            return zone.topLeft().x() <= bottomRight().x()
                    && zone.topLeft().y() <= bottomRight().y()
                    && zone.bottomRight().x() >= topLeft().x()
                    && zone.bottomRight().y() >= topLeft().y();
        }

        /**
         * Returns a new zone that is the same as this zone, but with the top left and bottom right
         * coordinates moved outwards by the given amount.
         *
         * @param amount the amount to inflate the zone by
         * @return a new zone that is the same as this zone, but with the top left and bottom right
         * coordinates moved outwards by the given amount
         */
        Zone inflate(int  amount) {
            int minX = Math.max(0, topLeft().x() - amount);
            int minY = Math.max(0, topLeft().y() - amount);
            int maxX = bottomRight().x() + amount;
            int maxY = bottomRight().y() + amount;
            return new Zone(new Position(minX, minY), new Position(maxX, maxY));
        }
    }

    /**
     * Represents a part of the schematic, either a number or a symbol.
     */
    sealed interface MotoPart {
    }

    /**
     * Represents a number in the schematic.
     */
    record Number(int value, Zone zone) implements MotoPart {
        public Number {
            if (value < 0) {
                throw new IllegalArgumentException("Value must be positive");
            }
        }

        static Number of(int value, Zone zone) {
            return new Number(value, zone);
        }
    }

    /**
     * Represents a gear in the schematic, which is a pair of numbers separated by an asterisk.
     */
    record Gear(Number number1, Number number2) {
        int gearRatio() {
            return number1.value() * number2.value();
        }

        static Gear of(Number number1, Number number2) {
            return new Gear(number1, number2);
        }
    }

    /**
     * Represents a symbol in the schematic.
     */
    record Symbol(char symbol, Zone zone) implements MotoPart {
        public Symbol {
            if (symbol == '.') {
                throw new IllegalArgumentException("Symbol cannot be a period");
            }
        }

        /**
         * Returns true if the symbol is an asterisk, which represents a gear.
         *
         * @return true if the symbol is an asterisk, which represents a gear
         */
        boolean isGear() {
            return symbol == '*';
        }

        static Symbol of(char symbol, Zone zone) {
            return new Symbol(symbol, zone);
        }
    }

    /**
     * Represents a motor schematic, which is a collection of parts.
     */
    record Motor(List<MotoPart> parts) {

        /**
         * Returns a stream of all the number parts in the schematic.
         *
         * @return a stream of all the number parts in the schematic
         */
        Stream<Number> numberParts() {
            return parts.stream()
                    .filter(Number.class::isInstance)
                    .map(Number.class::cast);
        }

        /**
         * Returns a stream of all the symbol parts in the schematic.
         *
         * @return a stream of all the symbol parts in the schematic
         */
        Stream<Symbol> symbolParts() {
            return parts.stream()
                    .filter(Symbol.class::isInstance)
                    .map(Symbol.class::cast);
        }

        /**
         * Returns a stream of all the number parts that are adjacent to a symbol part.<br>
         * A number part is adjacent to a symbol part if there is one or more symbol parts
         * that are contained within the zone of the number part inflated by 1.
         *
         * @return a stream of all the number parts that are adjacent to a symbol part
         */
        Stream<Number> numberPartsAdjacentToSymbols() {
            return numberParts()
                    .filter(number ->
                            symbolParts().anyMatch(symbol ->
                                    number.zone().inflate(1).contains(symbol.zone()))
                    );
        }

        /**
         * Returns the sum of all the part numbers in the schematic.
         *
         * @return the sum of all the part numbers in the schematic
         */
        long sumOfAllPartNumbers() {
            return numberPartsAdjacentToSymbols()
                    .mapToLong(Number::value)
                    .sum();
        }

        /**
         * Returns a stream of all the number parts that intersect a gear candidate.<br>
         *
         * @param symbol the symbol to check for gear candidates
         * @return a stream of all the number parts that intersect a gear candidate
         */
        Stream<Number> numbersThatIntersectGearCandidate(Symbol symbol) {
            return numberParts()
                    .filter(number -> symbol.zone().inflate(1).intersects(number.zone())).distinct();
        }

        /**
         * Returns a stream of all the gear candidates for the given symbol.<br>
         * A gear candidate is a pair of numbers that intersect the given symbol.
         * If there are not exactly two numbers that intersect the given symbol, then an empty stream is returned.
         *
         * @param symbol the symbol to check for gear candidates
         * @return a stream of all the gear candidates for the given symbol
         */
        Stream<Gear> gearCandidatesForSymbol(Symbol symbol) {
            var partList = numbersThatIntersectGearCandidate(symbol)
                    .toList();
            if (partList.size() != 2) {
                return Stream.empty();
            }
            return Stream.of(Gear.of(partList.get(0), partList.get(1)));
        }

        /**
         * Returns a stream of all the gears in the schematic.
         *
         * @return a stream of all the gears in the schematic
         */
        Stream<Gear> gears() {
            return symbolParts()
                    .filter(Symbol::isGear)
                    .flatMap(this::gearCandidatesForSymbol);
        }

        /**
         * Returns the sum of all the gear ratios in the schematic.
         *
         * @return the sum of all the gear ratios in the schematic
         */
        long sumOfAllGearRatios() {
            return gears()
                    .mapToLong(Gear::gearRatio)
                    .sum();
        }

        /**
         * Returns a motor schematic parsed from the given input.
         *
         * @param input the input to parse
         * @return a motor schematic parsed from the given input
         */
        static Motor of(String input) {
            List<MotoPart> parts = new ArrayList<>();
            String[] lines = input.split("\n");
            for (int i = 0; i < lines.length; i++) {
                parts.addAll(parseLine(lines[i], i));
            }
            return new Motor(parts);
        }
    }

    /**
     * Parses a line of the schematic into a collection of parts.<br>
     * A part is either a number or a symbol.
     * A number is a sequence of digits, and a symbol is any other character.
     * A period is ignored.
     * The line number is used to determine the vertical position of the parts.
     * The horizontal position of the parts is determined by the order of the characters in the line.
     * The combination of the horizontal and vertical position of a part is its {@code Zone}.
     *
     * @param line       the line to parse
     * @param lineNumber the line number of the line to parse
     * @return a collection of parts parsed from the given line
     */
    static Collection<MotoPart> parseLine(String line, int lineNumber) {
        List<MotoPart> items = new ArrayList<>();
        int index = 0;
        while (index < line.length()) {
            char c = line.charAt(index);
            if (c == '.') {
                index++;
                continue;
            }
            Zone zone = new Zone(new Position(index, lineNumber), new Position(index, lineNumber));
            if (Character.isDigit(c)) {
                int value = 0;
                while (index < line.length() && Character.isDigit(line.charAt(index))) {
                    value = value * 10 + Character.getNumericValue(line.charAt(index));
                    zone = new Zone(zone.topLeft(), new Position(index, lineNumber));
                    index++;
                }
                items.add(Number.of(value, zone));
            } else {
                items.add(Symbol.of(c, zone));
                index++;
            }
        }
        return items;
    }

    /**
     * Represents the solution for part 1 of the puzzle.
     */
    record Part1() implements Day3, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 3;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            var input = AdventOfCode2023.readFileOfResource("day3.txt");
            Motor motor = Motor.of(input);
            return motor.sumOfAllPartNumbers();
        }

        @Override
        public Long test() {
            return 537732L;
        }
    }

    /**
     * Represents the solution for part 2 of the puzzle.
     */
    record Part2() implements Day3, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 3;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            var input = AdventOfCode2023.readFileOfResource("day3.txt");
            Motor motor = Motor.of(input);
            return motor.sumOfAllGearRatios();
        }

        @Override
        public Long test() {
            return 84883664L;
        }
    }

    static Part1 findPart1() {
        return new Part1();
    }

    static Part2 findPart2() {
        return new Part2();
    }

}
