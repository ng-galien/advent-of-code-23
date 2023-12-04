package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.*;

class Day4Test {
    @ParameterizedTest
    @MethodSource("games")
    void givenGame_WhenScore_ThenScoreIsCorrect(String input, long expectedScore, long expectedCopiesWin) {
        //Given
        Day4.Game game = Day4.parseGame(input);
        //When
        long score = game.score();
        //Then
        assertEquals(expectedScore, score, "Score should be " + expectedScore);
        //When
        long copiesWin = game.copiesWin();
        //Then
        assertEquals(expectedCopiesWin, copiesWin, "Copies win should be " + expectedCopiesWin);
    }


    static Stream<Arguments> games() {
        return Stream.of(
                Arguments.of(
                        """
                                Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                                Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                                Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                                Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                                Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                                Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                                """, 13L, 30L
                ),
                Arguments.of(readFileOfResource("day4.txt"), 19855L, 10378710L)
        );
    }

}