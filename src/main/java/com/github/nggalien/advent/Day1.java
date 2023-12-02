package com.github.nggalien.advent;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

/**
 * Interface representing the solution for "Day 1: Trebuchet?!" challenge.
 * The challenge involves solving puzzles related to snow production issues.
 * Each puzzle input line has calibration values which are extracted and summed up.
 */
public interface Day1 {

    /**
     * Record to hold two integers and combine them into a two-digit number.
     */
    record TwoNumbers(int first, int second) {

        /**
         * Combines the two numbers into a single two-digit integer.
         * @return Combined two-digit number.
         */
        public int combine() {
            return first * 10 + second;
        }
    }

    /**
     * Reverses the characters in the string and returns their Unicode code points as an IntStream.
     * @param line The string to be reversed.
     * @return IntStream of reversed Unicode code points.
     */
    default IntStream reverse(String line) {
        return IntStream.rangeClosed(1, line.length())
                .map(i -> line.codePointAt(line.length() - i));
    }

    /**
     * Finds the first digit in a stream of integers.
     * @param stream The stream of integers.
     * @return Optional containing the first digit, if found.
     */
    default Optional<Integer> findFirstDigit(IntStream stream) {
        return stream.filter(Character::isDigit)
                .mapToObj(Character::getNumericValue)
                .findFirst();
    }

    /**
     * Finds the first and last digits in a string and returns them as a TwoNumbers record.
     * @param line The string to search for digits.
     * @return Stream of TwoNumbers found in the string.
     */
    default Stream<TwoNumbers> findTwoNumbers(String line) {
        return findFirstDigit(line.chars())
                .flatMap(first -> findFirstDigit(reverse(line))
                        .map(second -> new TwoNumbers(first, second)))
                .stream();
    }

    /**
     * Calculates the sum of all calibration values from the input.
     * Calibration values are determined by combining the first and last digits of each line.
     * @param input Multiline string input containing calibration values.
     * @return Sum of all calibration values.
     */
    default int sumOfAllCalibrationValues(String input) {
        return input.lines().flatMap(this::findTwoNumbers).mapToInt(TwoNumbers::combine).sum();
    }

    record Solution() implements Day1, AdventOfCode2023.SolutionOfDay {
        @Override
        public int day() {
            return 1;
        }

        @Override
        public boolean solved() {
            return true;
        }

        @Override
        public Supplier<?> solve() {
            return () -> {
                String input = readFileOfResource("day1.txt");
                return sumOfAllCalibrationValues(input);
            };
        }
    }

    static Solution find() {
        return new Solution();
    }
}
