package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 --- Day 3: Gear Ratios ---
 You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.

 It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.

 "Aaah!"

 You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.

 The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.

 The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand,
 but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)

 Here is an example engine schematic:

 467..114..
 ...*......
 ..35..633.
 ......#...
 617*......
 .....+.58.
 ..592.....
 ......755.
 ...$.*....
 .664.598..
 In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

 Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?

 --- Part Two ---
 The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.

 You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.

 Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.

 The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.

 This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.

 Consider the same engine schematic again:

 467..114..
 ...*......
 ..35..633.
 ......#...
 617*......
 .....+.58.
 ..592.....
 ......755.
 ...$.*....
 .664.598..
 In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.

 What is the sum of all of the gear ratios in your engine schematic?

 */
public interface Day3 {

    record Position(int x, int y) {
        public Position {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Position must be positive");
            }
        }
    }

    record Zone(Position topLeft, Position bottomRight) {
        public Zone {
            if (topLeft.x() > bottomRight.x() || topLeft.y() > bottomRight.y()) {
                throw new IllegalArgumentException("Top left must be above and to the left of bottom right");
            }
        }

        boolean contains(Zone zone) {
            return zone.topLeft().x() >= topLeft.x() && zone.topLeft().y() >= topLeft.y()
                    && zone.bottomRight().x() <= bottomRight.x() && zone.bottomRight().y() <= bottomRight.y();

        }

        static Zone of(Position topLeft, Position bottomRight) {
            return new Zone(topLeft, bottomRight);
        }

        Zone inflate(int amount) {
            int minX = Math.max(0, topLeft().x() - amount);
            int minY = Math.max(0, topLeft().y() - amount);
            int maxX = bottomRight().x() + amount;
            int maxY = bottomRight().y() + amount;
            return new Zone(new Position(minX, minY), new Position(maxX, maxY));
        }
    }

    interface MotoPart {
        Zone zone();
    }

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

    record Symbol(char symbol, Zone zone) implements MotoPart {
        public Symbol {
            if (symbol == '.') {
                throw new IllegalArgumentException("Symbol cannot be a period");
            }
        }

        static Symbol of(char symbol, Zone zone) {
            return new Symbol(symbol, zone);
        }
    }

    record Motor(List<MotoPart> parts) {

        Stream<Number> numberParts() {
            return parts.stream()
                    .filter(Number.class::isInstance)
                    .map(Number.class::cast);
        }

        Stream<Symbol> symbolParts() {
            return parts.stream()
                    .filter(Symbol.class::isInstance)
                    .map(Symbol.class::cast);
        }

        Stream<Number> numberPartsAdjacentToSymbols() {
            return numberParts()
                    .filter(number ->
                            symbolParts().anyMatch(symbol ->
                                    number.zone().inflate(1).contains(symbol.zone()))
                    );
        }

        long sumOfAllPartNumbers() {
            return numberPartsAdjacentToSymbols()
                    .mapToLong(Number::value)
                    .sum();
        }

        static Motor of(String input) {
            List<MotoPart> parts = new ArrayList<>();
            String[] lines = input.split("\n");
            for (int i = 0; i < lines.length; i++) {
                parts.addAll(parseLine(lines[i], i));
            }
            return new Motor(parts);
        }
    }

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
     * Implements the `Day3` interface and `AdventOfCode2023.SolutionOfDay` for part one of the puzzle.
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
     * Returns an instance of `part1`.
     */
    static Part1 findPart1() {
        return new Part1();
    }

}
