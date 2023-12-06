package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedResult")
    void getMinThingIdAtTheEnd(String input, long minThingIdAtTheEndForSeedListExpected,
                               long minThingIdAtTheEndForSeedRange) {
        //Given
        //When
        long minThingIdAtTheEndForSeedListResult = Day5.getMinThingIdAtTheEndForSeedList(input);
        //Then
        assertEquals(minThingIdAtTheEndForSeedListExpected, minThingIdAtTheEndForSeedListResult,
                "Min thing id at the end for seed list should be " + minThingIdAtTheEndForSeedListExpected);

        //Given
        //When
        long minThingIdAtTheEndForSeedRangeResult = Day5.getMinThingIdAtTheEndForSeedRange(input);
        //Then
        assertEquals(minThingIdAtTheEndForSeedRange, minThingIdAtTheEndForSeedRangeResult,
                "Min thing id at the end for seed range should be " + minThingIdAtTheEndForSeedRange);

    }

    static Stream<Arguments> provideInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("""
                        seeds: 79 14 55 13
                                                
                        seed-to-soil map:
                        50 98 2
                        52 50 48
                                                
                        soil-to-fertilizer map:
                        0 15 37
                        37 52 2
                        39 0 15
                                                
                        fertilizer-to-water map:
                        49 53 8
                        0 11 42
                        42 0 7
                        57 7 4
                                                
                        water-to-light map:
                        88 18 7
                        18 25 70
                                                
                        light-to-temperature map:
                        45 77 23
                        81 45 19
                        68 64 13
                                                
                        temperature-to-humidity map:
                        0 69 1
                        1 0 69
                                                
                        humidity-to-location map:
                        60 56 37
                        56 93 4
                        """, 35L, 46L)
//                Arguments.of(AdventOfCode2023.readFileOfResource("day5.txt"),
//                        579439039L, 0L)
        );

    }
}