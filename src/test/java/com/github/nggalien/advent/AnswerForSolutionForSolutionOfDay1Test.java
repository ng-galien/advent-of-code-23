package com.github.nggalien.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerForSolutionForSolutionOfDay1Test {

    @Test
    void givenExpectedResult_WhenSumOfAllCalibrationValues_ThenResultIsSameAsExpected() {
        //Given
        Day1 day1 = new Day1() {};
        int expected = 142;
        String input = """
                1abc2
                pqr3stu8vwx
                a1b2c3d4e5f
                treb7uchet
                """;
        //When
        int sum = day1.sumOfAllCalibrationValues(input);
        //Then
        assertEquals(expected, sum, "Sum of all calibration values should be " + expected);
    }

    @Test
    void givenInput_WhenSumOfAllCalibrationValues_ThenTheDay1AnswerIsCorrect() {
        //Given
        Day1 day1 = new Day1() {};
        int expected = 55123;
        String input = AdventOfCode2023.readFileOfResource("day1.txt");
        //When
        int sum = day1.sumOfAllCalibrationValues(input);
        //Then
        assertEquals(expected, sum, "Sum of all calibration values should be " + expected);
    }
}