package com.github.nggalien.advent;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.nggalien.advent.Day5.*;

class Day5Test {

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedResult")
    void getMinThingIdAtTheEnd(String input, long expectedMinIntervalFromSingleValues,
                               long expectedMinIntervalFromIntervals) {
        //When
        long minIntervalFromSingleValues = findMinIntervals(input, lineToSingleIntervalProvider);
        //Then
        assertEquals(expectedMinIntervalFromSingleValues, minIntervalFromSingleValues,
                STR."Min interval from single values should be \{expectedMinIntervalFromSingleValues}");

        //When
        long minIntervalFromIntervals = findMinIntervals(input, lineToIntervalsProvider);
        //Then
        assertEquals(expectedMinIntervalFromIntervals, minIntervalFromIntervals,
                STR."Min interval from intervals should be \{expectedMinIntervalFromIntervals}");

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
                        """, 35L, 46L),
                Arguments.of(AdventOfCode2023.readFileOfResource("day5.txt"),
                        579439039L, 7873084L),
                null
        ).filter(Objects::nonNull);

    }

    @ParameterizedTest
    @MethodSource("shiftInputAndExpectedResult")
    void testIntervalShift(Interval interval, long shift, Interval expectedInterval) {
        //Given
        //When
        Interval shifted = Interval.shift(interval, shift);
        //Then
        assertEquals(expectedInterval, shifted, STR."Shift of \{interval} by \{shift} should be \{expectedInterval}");
    }

    static Stream<Arguments> shiftInputAndExpectedResult() {
        return Stream.of(
                Arguments.of(Interval.of(1, 5), 2, Interval.of(3, 7)),
                Arguments.of(Interval.of(1, 5), -2, Interval.of(-1, 3)),
                Arguments.of(Interval.of(2, 3), 2, Interval.of(4, 5)),
                Arguments.of(Interval.of(1, 5), 0, Interval.of(1, 5)),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("intersectInputAndExpectedResult")
    void testIntervalIntersection(Interval interval1, Interval interval2, Optional<Interval> expectedInterval) {
        //Given
        //When
        Optional<Interval> intersect = Interval.intersection(interval1, interval2);
        //Then
        assertEquals(expectedInterval, intersect, STR."Intersection of \{interval1} and \{interval2} should be \{expectedInterval}");
    }

    static Stream<Arguments> intersectInputAndExpectedResult() {
        return Stream.of(
                Arguments.of(Interval.of(1, 5), Interval.of(2, 6), Optional.of(Interval.of(2, 5))),
                Arguments.of(Interval.of(1, 5), Interval.of(2, 3), Optional.of(Interval.of(2, 3))),
                Arguments.of(Interval.of(2, 3), Interval.of(1, 5), Optional.of(Interval.of(2, 3))),
                Arguments.of(Interval.of(1, 5), Interval.of(3, 3), Optional.of(Interval.of(3, 3))),
                Arguments.of(Interval.of(3, 3), Interval.of(1, 5), Optional.of(Interval.of(3, 3))),
                Arguments.of(Interval.of(1, 5), Interval.of(6, 10), Optional.empty()),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("extractSubPartsInputAndExpectedResult")
    void testExtractSubParts(Interval interval, Interval part, Collection<Interval> expectedIntervals) {
        //Given
        //When
        Collection<Interval> subParts = Interval.extractSubParts(interval, part);
        //Then
        assertEquals(expectedIntervals, subParts, STR."Sub parts of \{interval} and \{part} should be \{expectedIntervals}");
    }

    static Stream<Arguments> extractSubPartsInputAndExpectedResult() {
        return Stream.of(
                Arguments.of(Interval.of(1, 5), Interval.of(1, 1), List.of(Interval.of(2, 5))),
                Arguments.of(Interval.of(1, 5), Interval.of(2, 3), List.of(Interval.of(1, 1), Interval.of(4, 5))),
                Arguments.of(Interval.of(1, 5), Interval.of(1, 5), List.of()),
                Arguments.of(Interval.of(1, 5), Interval.of(5, 5), List.of(Interval.of(1, 4))),
                Arguments.of(Interval.of(1, 5), Interval.of(6, 10), List.of(Interval.of(1, 5))),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("shiftedIntervalInputAndExpectedResult")
    void testShiftedIntervalOf(long[] values, ShiftedInterval expectedShiftedInterval) {
        //Given
        //When
        ShiftedInterval shiftedInterval = ShiftedInterval.of(values[0], values[1], values[2]);
        //Then
        assertEquals(expectedShiftedInterval, shiftedInterval, STR."Shifted interval of \{values} should be \{expectedShiftedInterval}");
    }

    static Stream<Arguments> shiftedIntervalInputAndExpectedResult() {
        return Stream.of(
                Arguments.of(new long[]{1, 2, 3}, new ShiftedInterval(Interval.of(2, 4), -1)),
                Arguments.of(new long[]{10, 5, 5}, new ShiftedInterval(Interval.of(5, 9), 5)),
                Arguments.of(new long[]{0, 5, 5}, new ShiftedInterval(Interval.of(5, 9), -5)),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("shiftedIntersectInputAndExpectedResult")
    void testShiftIntersectCandidate(ShiftedInterval shiftedInterval, ShiftedInterval intersectCandidate, Collection<ShiftedInterval> expectedIntervals) {
        //When
        Collection<ShiftedInterval> subParts = shiftedInterval.shiftIntersectCandidate(intersectCandidate);
        //Then
        assertEquals(expectedIntervals, subParts, STR."Sub parts of \{shiftedInterval} and \{intersectCandidate} should be \{expectedIntervals}");
    }

    static Stream<Arguments> shiftedIntersectInputAndExpectedResult() {
        return Stream.of(
                Arguments.of(
                        ShiftedInterval.of(Interval.of(1, 5), 10),
                        ShiftedInterval.of(Interval.of(2, 3), 0),
                        List.of(ShiftedInterval.of(Interval.of(12, 13), 10))),
                Arguments.of(
                        ShiftedInterval.of(Interval.of(1, 5), 10),
                        ShiftedInterval.of(Interval.of(2, 3), 1),
                        List.of(ShiftedInterval.of(Interval.of(2, 3), 1))),
                Arguments.of(
                        ShiftedInterval.of(Interval.of(1, 5), 10),
                        ShiftedInterval.of(Interval.of(6, 10), 0),
                        List.of(ShiftedInterval.of(Interval.of(6, 10), 0))),
                Arguments.of(
                        ShiftedInterval.of(Interval.of(2, 3), 10),
                        ShiftedInterval.of(Interval.of(1, 5), 0),
                        List.of(ShiftedInterval.of(Interval.of(1, 1), 0),
                                ShiftedInterval.of(Interval.of(4, 5), 0),
                                ShiftedInterval.of(Interval.of(12, 13), 10))),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("lineToSingleIntervalsInputAndExpectedResult")
    void testLineToSingleIntervals(String line, Collection<Interval> expectedIntervals) {
        //Given
        //When
        Collection<Interval> intervals = lineToSingleIntervals(line).toList();
        //Then
        assertEquals(expectedIntervals, intervals, STR."Intervals of \{line} should be \{expectedIntervals}");
    }

    static Stream<Arguments> lineToSingleIntervalsInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("1 5", List.of(Interval.of(1, 1), Interval.of(5, 5))),
                Arguments.of(" 1 5  7", List.of(Interval.of(1, 1), Interval.of(5, 5), Interval.of(7, 7))),
                Arguments.of(" 1  5   7  ", List.of(Interval.of(1, 1), Interval.of(5, 5), Interval.of(7, 7))),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest
    @MethodSource("lineToIntervalsInputAndExpectedResult")
    void testLineToIntervals(String line, Collection<Interval> expectedIntervals) {
        //Given
        //When
        Collection<Interval> intervals = lineToIntervals(line).toList();
        //Then
        assertEquals(expectedIntervals, intervals, STR."Intervals of \{line} should be \{expectedIntervals}");
    }

    static Stream<Arguments> lineToIntervalsInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("1 2", List.of(Interval.of(1, 2))),
                Arguments.of(" 1 2 10 5", List.of(Interval.of(1, 2), Interval.of(10, 14))),
                null
        ).filter(Objects::nonNull);
    }

}