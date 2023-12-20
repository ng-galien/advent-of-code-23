package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DayUtilsTest {

    @ParameterizedTest(name = "Given input \"{0}\" then parseLongStream should return {1}")
    @MethodSource("provideInputAndExpectedResult")
    void parseLongStream(String input, long[] expected) {
        //Given
        //When
        long[] result = DayUtils.parseLongArray(input);
        //Then
        assertArrayEquals(expected, result, "Parsed long stream should be " + Arrays.toString(expected));
    }

    static Stream<Arguments> provideInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("1 2 3 4 5 6 7 8 9", new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9}),
                Arguments.of("1 2 3 4 5 6 7 8 9 10", new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}),
                Arguments.of("1 123   456 789", new long[]{1, 123, 456, 789}),
                Arguments.of("  1 123   456 789", new long[]{1, 123, 456, 789})
        );
    }

    @ParameterizedTest(name = "Given input \"{0}\" then convertBinary should return {1}")
    @MethodSource("convertBinarySource")
    void convertBinary(String input, long expected) {
        //Given
        //When
        long result = DayUtils.convertBinary(input, c -> c == '#');
        //Then
        assertEquals(expected, result, STR."Parsed input should be \{expected}");
    }

    static Stream<Arguments> convertBinarySource() {
        return Stream.of(
                Arguments.of("#", 1),
                Arguments.of("##", 3),
                Arguments.of("#...#", 17),
                null
        ).filter(Objects::nonNull);
    }

    @ParameterizedTest(name = "Given input \"{0}\" then convertBinary should return {1}")
    @MethodSource("invertSource")
    void invertTest(long input, long expected) {
        //Given
        //When
        long result = DayUtils.invert(input);
        //Then
        assertEquals(expected, result, "Inverted input should be " + expected);
    }

    static Stream<Arguments> invertSource() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(12, 21),
                Arguments.of(123, 321),
                Arguments.of(1234, 4321),
                Arguments.of(12345, 54321),
                Arguments.of(123456, 654321),
                Arguments.of(1234567, 7654321),
                Arguments.of(12345678, 87654321),
                Arguments.of(123456789, 987654321),
                Arguments.of(1234567890L, 987654321L)
        );
    }

}