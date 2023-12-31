package com.github.nggalien.advent;

import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

/**
 * Main class to run the Advent of Code 2023
 */
public class AdventOfCode2023 {

    public enum DayPart {
        ONE, TWO
    }

    /**
     * Interface representing the solution for a day's challenge.
     */
    sealed interface SolutionOfDay<T> permits Day1.Solution, Day10.Part1, Day10.Part2, Day11.Part1, Day11.Part2, Day12.Part1, Day12.Part2, Day13.Part1, Day13.Part2, Day2.Part1, Day2.Part2, Day3.Part1, Day3.Part2, Day4.Part1, Day4.Part2, Day5.Part1, Day5.Part2, Day6.Part1, Day6.Part2, Day7.Part1, Day7.Part2, Day8.Part1, Day8.Part2, Day9.Part1, Day9.Part2 {
        int day();

        DayPart part();

        T rightAnswer();

        /**
         * Solve the challenge for the day.
         * @return Supplier of the answer.
         */
        T test();

        default String status() {
            return test().equals(rightAnswer()) ? "\uD83D\uDE0E" : "\uD83D\uDE2D";
        }

        /**
         * Print the answer for the day.
         */
        default String message() {
            return STR."""
                    \uD83D\uDE80 Day \{day() } part \{part().name()}: answer is \{ test()} \{ status()}
                    """;
        }
    }

    /**
     * Read a file from resources
     * @param resourceName the name of the file to read
     * @return the content of the file
     */
    static String readFileOfResource(String resourceName) {
        return Stream.of(AdventOfCode2023.class.getResourceAsStream("/"+resourceName)).filter(Objects::nonNull)
                .map(inputStream -> new java.util.Scanner(inputStream).useDelimiter("\\A").next())
                .findFirst()
                .orElseThrow();
    }

    /**
     * Main method to run the Advent of Code 2023
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Stream.of(Day1.find(), Day2.findPart1(), Day2.findPart2(), Day3.findPart1(), Day3.findPart2(),
                        Day4.findPart1(), Day4.findPart2(), Day5.findPart1(), Day5.findPart2(),
                        Day6.findPart1(), Day6.findPart2(), Day7.findPart1(), Day7.findPart2(),
                        Day8.findPart1(), Day8.findPart2(), Day9.findPart1(), Day9.findPart2(),
                        Day10.findPart1(), Day10.findPart2(), Day11.findPart1(), Day11.findPart2(),
                        Day12.findPart1(), Day12.findPart2(), Day13.findPart1(), Day13.findPart2())
                .map(SolutionOfDay::message)
                .forEach(System.out::print);
    }
}