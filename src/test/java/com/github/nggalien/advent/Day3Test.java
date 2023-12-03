package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static com.github.nggalien.advent.Day3.Motor;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

    @ParameterizedTest
    @MethodSource("testMotorPartsSum")
    void testMotorPartsSum(String input, long expected) {
        //Given
        Motor motor = Motor.of(input);
        //When
        long sum = motor.sumOfAllPartNumbers();
        //Then
        assertEquals(expected, sum, "Sum of all part numbers should be " + expected);
    }

    static Stream<Arguments> testMotorPartsSum() {
        return Stream.of(
                Arguments.of("""
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
                        """, 4361L),
                Arguments.of(readFileOfResource("day3.txt"), 537732L)
        );
    }
}