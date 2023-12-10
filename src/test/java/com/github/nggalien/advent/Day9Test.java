package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {


    @ParameterizedTest(name = "testParts: {0}")
    @MethodSource("parts")
    void testSolves(String input, long expectedLastNumber, long expectedFirstNumber) {
        //When
        long result = Day9.solve(input, Day9::addLastNumber);
        //Then
        assertEquals(expectedLastNumber, result);
        //When
        result = Day9.solve(input, Day9::addFirstNumber);
        //Then
        assertEquals(expectedFirstNumber, result);
    }

    static Stream<Arguments> parts() {
        return Stream.of(
                Arguments.of("10  13  16  21  30  45", 68L, 5L),
                Arguments.of("""
                        0 3 6 9 12 15
                        1 3 6 10 15 21
                        10 13 16 21 30 45
                        """,
                        114L, 2L),
                Arguments.of(readFileOfResource("day9.txt"), 1898776583L, 1100L),
                null
        ).filter(Objects::nonNull);
    }
}