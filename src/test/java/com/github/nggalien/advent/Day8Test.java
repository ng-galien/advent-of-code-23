package com.github.nggalien.advent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    @ParameterizedTest
    @MethodSource("bets")
    void testCountSteps(String input, long nbSteps) {
        long result = Day8.countSteps(input);
        assertEquals(result, nbSteps);

    }

    static Stream<Arguments> bets() {
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
                        """, 2L),
                Arguments.of("""
                        LLR
                                                
                        AAA = (BBB, BBB)
                        BBB = (AAA, ZZZ)
                        ZZZ = (ZZZ, ZZZ)
                        """, 6L),
                Arguments.of(readFileOfResource("day8.txt"), 22199L),
                null
        ).filter(Objects::nonNull);
    }
}