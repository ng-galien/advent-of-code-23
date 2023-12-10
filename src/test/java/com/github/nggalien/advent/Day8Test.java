package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    static Predicate<String> equalsAAA = s -> s.equals("AAA");
    static Predicate<String> equalsZZZ = s -> s.equals("ZZZ");
    static Predicate<String> endWithA = s -> s.endsWith("A");
    static Predicate<String> endWithZ = s -> s.endsWith("Z");

    @ParameterizedTest(name = "testParts: {0}")
    @MethodSource("parts")
    void testParts(String input, Predicate<String> startNode, Predicate<String> endNode, long expectedResult) {
        long result = Day8.solves(input, startNode, endNode);
        assertEquals(expectedResult, result, STR."Result should be \{expectedResult}");
    }

    static Stream<Arguments> parts() {
        return Stream.of(
                Arguments.of("""
                        RL

                        AAA = (BBB, CCC)
                        BBB = (DDD, EEE)
                        CCC = (ZZZ, GGG)
                        DDD = (DDD, DDD)
                        EEE = (EEE, EEE)
                        GGG = (GGG, GGG)
                        ZZZ = (ZZZ, ZZZ)
                        """,
                        equalsAAA, equalsZZZ, 2L),
                Arguments.of("""
                        LLR

                        AAA = (BBB, BBB)
                        BBB = (AAA, ZZZ)
                        ZZZ = (ZZZ, ZZZ)
                        """,
                        equalsAAA, equalsZZZ, 6L),
                Arguments.of(readFileOfResource("day8.txt"),
                        equalsAAA, equalsZZZ, 22199L),
                Arguments.of("""
                        LR

                        11A = (11B, XXX)
                        11B = (XXX, 11Z)
                        11Z = (11B, XXX)
                        22A = (22B, XXX)
                        22B = (22C, 22C)
                        22C = (22Z, 22Z)
                        22Z = (22B, 22B)
                        XXX = (XXX, XXX)
                        """,
                        endWithA, endWithZ, 6L),
                Arguments.of(readFileOfResource("day8.txt"), endWithA, endWithZ, 13334102464297L),
                null
        ).filter(Objects::nonNull);
    }
}