package com.github.nggalien.advent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.nggalien.advent.Day7.Hand;
import static com.github.nggalien.advent.Day7.Card;

class Day7Test {

    @ParameterizedTest
    @MethodSource("bets")
    void testTotalWiningPart1(String input, long expectedTotalWiningPart1, long expectedTotalWiningPart2) {
        //Given
        //When
        long actualTotalWiningPart1 = Day7.totalWiningPart(input, AdventOfCode2023.DayPart.ONE);
        //Then
        assertEquals(expectedTotalWiningPart1, actualTotalWiningPart1, STR."Total wining should be \{expectedTotalWiningPart1}");
        //When
        long actualTotalWiningPart2 = Day7.totalWiningPart(input, AdventOfCode2023.DayPart.TWO);
        //Then
        assertEquals(expectedTotalWiningPart2, actualTotalWiningPart2, STR."Total wining should be \{expectedTotalWiningPart2}");
    }

    static Stream<Arguments> bets() {
        return Stream.of(
                Arguments.of("""
                        32T3K 765
                        T55J5 684
                        KK677 28
                        KTJJT 220
                        QQQJA 483
                        """, 6440L, 5905L),
                Arguments.of(readFileOfResource("day7.txt"), 248812215L, 250057090L),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("hands")
    void testParseHand(String input, Hand expectedHand) {
        //Given
        //When
        Hand actualHand = Hand.parseFromStr(input, false);
        //Then
        assertEquals(expectedHand, actualHand, STR."Hand should be \{expectedHand}");
    }

    static Stream<Arguments> hands() {
        return Stream.of(
                Arguments.of("AAAAA", new Day7.FiveOfAKind(List.of(
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('A')
                ))),
                Arguments.of("2AAAA", new Day7.FourOfAKind(List.of(
                        Card.of('2'),
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('A')
                ))),
                Arguments.of("AAKKK", new Day7.FullHouse(List.of(
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('K'),
                        Card.of('K'),
                        Card.of('K')
                ))),
                Arguments.of("AAKQA", new Day7.ThreeOfAKind(List.of(
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('K'),
                        Card.of('Q'),
                        Card.of('A')
                ))),
                Arguments.of("AAJKJ", new Day7.TwoPairs(List.of(
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('J'),
                        Card.of('K'),
                        Card.of('J')
                ))),
                Arguments.of("AAKJQ", new Day7.OnePair(List.of(
                        Card.of('A'),
                        Card.of('A'),
                        Card.of('K'),
                        Card.of('J'),
                        Card.of('Q')
                ))),
                Arguments.of("AJ358", new Day7.HighCard(List.of(
                        Card.of('A'),
                        Card.of('J'),
                        Card.of('3'),
                        Card.of('5'),
                        Card.of('8')
                ))),
                null
        ).filter(Objects::nonNull);
    }

    @Test
    void testCardOrder() {
        assertTrue(Card.of('A').compareTo(Card.of('K')) > 0, STR."A should be greater than K");
        assertTrue(Card.of('K').compareTo(Card.of('Q')) > 0, STR."K should be greater than Q");
        assertTrue(Card.of('Q').compareTo(Card.of('J')) > 0, STR."Q should be greater than J");
        assertTrue(Card.of('J').compareTo(Card.of('T')) > 0, STR."J should be greater than T");
        assertTrue(Card.of('T').compareTo(Card.of('9')) > 0, STR."T should be greater than 9");
        assertTrue(Card.of('9').compareTo(Card.of('8')) > 0, STR."9 should be greater than 8");
        assertTrue(Card.of('8').compareTo(Card.of('7')) > 0, STR."8 should be greater than 7");
        assertTrue(Card.of('7').compareTo(Card.of('6')) > 0, STR."7 should be greater than 6");
        assertTrue(Card.of('6').compareTo(Card.of('5')) > 0, STR."6 should be greater than 5");
        assertTrue(Card.of('5').compareTo(Card.of('4')) > 0, STR."5 should be greater than 4");
        assertTrue(Card.of('4').compareTo(Card.of('3')) > 0, STR."4 should be greater than 3");
        assertTrue(Card.of('3').compareTo(Card.of('2')) > 0, STR."3 should be greater than 2");
        assertTrue(Card.of('2').compareTo(Card.of('A')) < 0, STR."2 should be greater than A");
    }

    @Test
    void testHandOrder() {
        greaterThan(Hand.parseFromStr("AAAAA", false), Hand.parseFromStr("2AAAA", false));
        greaterThan(Hand.parseFromStr("2AAAA", false), Hand.parseFromStr("AAKKK", false));
        greaterThan(Hand.parseFromStr("AAKKK", false), Hand.parseFromStr("24356", false));
        greaterThan(Hand.parseFromStr("AAKQA", false), Hand.parseFromStr("AAJKJ", false));
        greaterThan(Hand.parseFromStr("AAJKJ", false), Hand.parseFromStr("AAKJQ", false));
        greaterThan(Hand.parseFromStr("AAKJQ", false), Hand.parseFromStr("AJ358", false));
        //Day1
        greaterThan(Hand.parseFromStr("33332", false), Hand.parseFromStr("2AAAA", false));
        greaterThan(Hand.parseFromStr("77888", false), Hand.parseFromStr("77788", false));
        greaterThan(Hand.parseFromStr("KK677", false), Hand.parseFromStr("KTJJT", false));
    }

    static void greaterThan(Hand hand1, Hand hand2) {
        assertTrue(hand1.compareTo(hand2) > 0, STR."Hand \{hand1} should be greater than \{hand2}");
    }
}