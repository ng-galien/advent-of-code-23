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
    void testDay(String input, long expectedPart1) {
        //When
        long result = Day13.solvesDay(input);
        //Then
        assertEquals(expectedPart1, result, "Part 1");
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
                        """, 5),
                Arguments.of("""
                        #...##..#
                        #....#..#
                        ..##..###
                        #####.##.
                        #####.##.
                        ..##..###
                        #....#..#
                        """, 400),
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
                        """, 405),
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
                        """, 500),
                Arguments.of(readFileOfResource("day13.txt"), 29213),
                null
        ).filter(Objects::nonNull);
    }
}