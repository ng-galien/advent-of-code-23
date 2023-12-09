package com.github.nggalien.advent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

    @ParameterizedTest
    @MethodSource("games")
    void numbersOfWins(String input, long numberOfWins, long longRaceWins) {
        //Given
        //When
        long actualNumberOfWins = Day6.numbersOfWins(input, s -> s);
        //Then
        assertEquals(numberOfWins, actualNumberOfWins, STR."Number of wins should be \{numberOfWins}");

        //When
        long actualNumberOfLongRaceWins = Day6.numbersOfWins(input, s -> s.replace(" ", ""));
        //Then
        assertEquals(actualNumberOfLongRaceWins, longRaceWins);
    }

    static Stream<Arguments> games() {
        return Stream.of(
                Arguments.of(
                        """
                                Time:      7  15   30
                                Distance:  9  40  200
                                """, 288L, 71503L
                ),
                Arguments.of(readFileOfResource("day6.txt"),
                        345015L, 42588603L
                ),
                null
        ).filter(Objects::nonNull);
    }
}