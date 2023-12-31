package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    @ParameterizedTest(name = "testParts: {0}")
    @MethodSource("data")
    void testDay(String input, int expander, long expectedPart1) {
        //When
        long result = Day11.solvesDay(input, expander);
        //Then
        assertEquals(expectedPart1, result, "Part 1");
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("""
                        ...#......
                        .......#..
                        #.........
                        ..........
                        ......#...
                        .#........
                        .........#
                        ..........
                        .......#..
                        #...#.....
                        """, 2, 374),
                Arguments.of("""
                        ...#......
                        .......#..
                        #.........
                        ..........
                        ......#...
                        .#........
                        .........#
                        ..........
                        .......#..
                        #...#.....
                        """, 10, 1030),
                Arguments.of("""
                        ...#......
                        .......#..
                        #.........
                        ..........
                        ......#...
                        .#........
                        .........#
                        ..........
                        .......#..
                        #...#.....
                        """, 100, 8410),
                Arguments.of(readFileOfResource("day11.txt"), 2, 10422930),
                Arguments.of(readFileOfResource("day11.txt"), 1000000, 699909023130L),
                null
        ).filter(Objects::nonNull);
    }
}