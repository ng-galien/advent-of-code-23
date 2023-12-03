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
    sealed interface SolutionOfDay<T> permits Day1.Solution, Day2.part1, Day2.part2 {
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
                    \uD83D\uDE80 Day \{ day() } part \{part().name()}: answer is \{ test()} \{ status()}
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
        Stream.of(Day1.find(), Day2.findPart1(), Day2.findPart2())
                .map(SolutionOfDay::message)
                .forEach(System.out::println);
    }
}