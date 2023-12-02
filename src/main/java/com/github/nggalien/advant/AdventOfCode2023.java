package com.github.nggalien.advant;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

/**
 * Main class to run the Advent of Code 2023
 */
public class AdventOfCode2023 {

    /**
     * Interface representing the solution for a day's challenge.
     */
    sealed interface SolutionOfDay permits Day1.Solution {
        int day();

        boolean solved();

        /**
         * Solve the challenge for the day.
         * @return Supplier of the answer.
         */
        Supplier<?> solve();

        default String solvedStatus() {
            return solved() ? "\uD83D\uDE0E" : "\uD83D\uDE2D";
        }

        /**
         * Print the answer for the day.
         */
        default String answer() {
            return STR."""
                    \uD83D\uDE80 Day \{ day() } answer is \{solve().get()} \{solvedStatus()}
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
        Stream.of(Day1.find())
                .map(SolutionOfDay::answer)
                .forEach(System.out::println);
    }
}