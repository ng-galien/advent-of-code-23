package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    @ParameterizedTest(name = "testParts: {0}")
    @MethodSource("data")
    void testDay(String input, long expectedPart1, long expectedPart2) {
        //When
        long day13Part1 = Day13.solvesDay1(input);
        //Then
        assertEquals(expectedPart1, day13Part1, "Day13 Part1 failed");

        //When
        long day13Part2 = Day13.solvesDay2(input);
        //Then
        assertEquals(expectedPart2, day13Part2, "Day13 Part2 failed");
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("""
                        #.##..##.
                        ..#.##.#.
                        ##......#
                        ##......#
                        ..#.##.#.
                        ..##..##.
                        #.#.##.#.
                        """, 5, 300),
                Arguments.of("""
                        #...##..#
                        #....#..#
                        ..##..###
                        #####.##.
                        #####.##.
                        ..##..###
                        #....#..#
                        """, 400, 100),
                Arguments.of("""
                        #.##..##.
                        ..#.##.#.
                        ##......#
                        ##......#
                        ..#.##.#.
                        ..##..##.
                        #.#.##.#.

                        #...##..#
                        #....#..#
                        ..##..###
                        #####.##.
                        #####.##.
                        ..##..###
                        #....#..#
                        """, 405, 400),
                Arguments.of("""
                        #.#.###
                        #.#####
                        ##.###.
                        .....##
                        #####..
                        #####..
                        .....##
                        ##.###.
                        #.#####
                        #.#.###
                        #..#.#.
                        .##...#
                        ##.##.#
                        .#####.
                        .....#.
                        """, 500, 100),
                Arguments.of(readFileOfResource("day13.txt"), 29213, 37453),
                null
        ).filter(Objects::nonNull);
    }
}