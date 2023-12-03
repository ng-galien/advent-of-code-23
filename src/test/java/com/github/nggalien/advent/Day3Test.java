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
    @MethodSource("day3Args")
    void testDay3(String input, long sumOfPartNumbers, long sumGearRatio) {
        //Given
        Motor motor = Motor.of(input);
        //When
        long partNumbers = motor.sumOfAllPartNumbers();
        //Then
        assertEquals(sumOfPartNumbers, partNumbers, "Sum of all part numbers should be " + sumOfPartNumbers);
        //When
        long gearRatio = motor.sumOfAllGearRatios();
        //Then
        assertEquals(sumGearRatio, gearRatio, "Sum of all gear ratios should be " + sumGearRatio);
    }

    static Stream<Arguments> day3Args() {
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
                        """, 4361L, 467835L),
                Arguments.of(readFileOfResource("day3.txt"), 537732L, 84883664L)
        );
    }
}