package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedResult")
    void givenExpectedResult_WhenSumOfAllCalibrationValues_ThenResultIsSameAsExpected(
            String input, int expected
    ) {
        //Given
        Day1 day1 = new Day1() {};
        //When
        int sum = day1.sumOfAllCalibrationValues(input);
        //Then
        assertEquals(expected, sum, "Sum of all calibration values should be " + expected);
    }

    static Stream<Arguments> provideInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("""
                        1abc2
                        pqr3stu8vwx
                        a1b2c3d4e5f
                        treb7uchet
                        """, 142),
                Arguments.of("""
                        two1nine
                        eightwothree
                        abcone2threexyz
                        xtwone3four
                        4nineeightseven2
                        zoneight234
                        7pqrstsixteen
                        """, 281)
        );
    }
}