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
    void testTotalWining(String input, long expectedTotalWining) {
        //Given
        //When
        long actualTotalWining = Day7.totalWiningPart1(input);
        //Then
        assertEquals(expectedTotalWining, actualTotalWining, STR."Total wining should be \{expectedTotalWining}");
    }

    static Stream<Arguments> bets() {
        return Stream.of(
                Arguments.of("""
                        32T3K 765
                        T55J5 684
                        KK677 28
                        KTJJT 220
                        QQQJA 483
                        """, 6440L),
                Arguments.of(readFileOfResource("day7.txt"), 248812215L),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("hands")
    void testParseHand(String input, Hand expectedHand) {
        //Given
        //When
        Hand actualHand = Hand.parseFromStrPart1(input);
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
                Arguments.of("24356", new Day7.Straight(List.of(
                        Card.of('2'),
                        Card.of('4'),
                        Card.of('3'),
                        Card.of('5'),
                        Card.of('6')
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
        greaterThan(Hand.parseFromStrPart1("AAAAA"), Hand.parseFromStrPart1("2AAAA"));
        greaterThan(Hand.parseFromStrPart1("2AAAA"), Hand.parseFromStrPart1("AAKKK"));
        greaterThan(Hand.parseFromStrPart1("AAKKK"), Hand.parseFromStrPart1("24356"));
        greaterThan(Hand.parseFromStrPart1("24356"), Hand.parseFromStrPart1("AAKQA"));
        greaterThan(Hand.parseFromStrPart1("AAKQA"), Hand.parseFromStrPart1("AAJKJ"));
        greaterThan(Hand.parseFromStrPart1("AAJKJ"), Hand.parseFromStrPart1("AAKJQ"));
        greaterThan(Hand.parseFromStrPart1("AAKJQ"), Hand.parseFromStrPart1("AJ358"));
        //Day1
        greaterThan(Hand.parseFromStrPart1("33332"), Hand.parseFromStrPart1("2AAAA"));
        greaterThan(Hand.parseFromStrPart1("77888"), Hand.parseFromStrPart1("77788"));
        greaterThan(Hand.parseFromStrPart1("KK677"), Hand.parseFromStrPart1("KTJJT"));
    }

    static void greaterThan(Hand hand1, Hand hand2) {
        assertTrue(hand1.compareTo(hand2) > 0, STR."Hand \{hand1} should be greater than \{hand2}");
    }
}