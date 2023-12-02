package com.github.nggalien.advent;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

/**
 * Interface representing the solution for "Day 1: Trebuchet?!" challenge.
 * The challenge involves solving puzzles related to snow production issues.
 * Each puzzle input line has calibration values which are extracted and summed up.
 */
public interface Day1 {

    Map<String, Integer> digitsAsString = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    Map<String, Integer> reverseDigitsAsString = Map.of(
            "eno", 1,
            "owt", 2,
            "eerht", 3,
            "ruof", 4,
            "evif", 5,
            "xis", 6,
            "neves", 7,
            "thgie", 8,
            "enin", 9
    );

    /**
     * Record to hold the index and digit of a found digit.
     */
    record Found(int index, int digit) {
        public static Found of(int index, int digit) {
            return new Found(index, digit);
        }
    }

    /**
     * Record to hold two integers and combine them into a two-digit number.
     */
    record TwoNumbers(Found first, Found second) {

        /**
         * Combines the two numbers into a single two-digit integer.
         * @return Combined two-digit number.
         */
        public int combine() {
            return first.digit() * 10 + second.digit();
        }
    }

    /**
     * Reverses the characters in the string and returns their Unicode code points as an IntStream.
     * @param line The string to be reversed.
     * @return IntStream of reversed Unicode code points.
     */
    default String reverse(String line) {
        return new StringBuilder(line).reverse().toString();
    }


    /**
     * Finds the first digit as a number or as a string and returns the first one found.
     * @param line The string to search for digits.
     * @return Optional of Found digit.
     */
    default Optional<Found> findFirstDigit(String line, Map<String, Integer> digitsMap) {
        Optional<Found> foundDigitAsString = digitsMap.entrySet().stream()
                .filter(entry -> line.contains(entry.getKey()))
                .map(entry -> Found.of(line.indexOf(entry.getKey()), entry.getValue()))
                .reduce((first, second) -> first.index() < second.index() ? first : second);

        Optional<Found> foundDigitAsNumber = line.chars()
                .filter(Character::isDigit)
                .mapToObj(Character::getNumericValue)
                .map(digit -> Found.of(line.indexOf(String.valueOf(digit)), digit))
                .findFirst();

        return Stream.of(foundDigitAsString, foundDigitAsNumber)
                .flatMap(Optional::stream)
                .reduce((first, second) -> first.index() < second.index() ? first : second);
    }

    /**
     * Finds the first and last digits in a string and returns them as a TwoNumbers record.
     * @param line The string to search for digits.
     * @return Stream of TwoNumbers found in the string.
     */
    default Stream<TwoNumbers> findTwoNumbers(String line) {
        return findFirstDigit(line, digitsAsString)
                .flatMap(first -> findFirstDigit(reverse(line), reverseDigitsAsString)
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

    record Solution() implements Day1, AdventOfCode2023.SolutionOfDay<Integer> {
        @Override
        public int day() {
            return 1;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Integer rightAnswer() {
            return 55260;
        }

        @Override
        public Integer test() {
            String input = readFileOfResource("day1.txt");
            return sumOfAllCalibrationValues(input);
        }
    }

    static Solution find() {
        return new Solution();
    }
}
