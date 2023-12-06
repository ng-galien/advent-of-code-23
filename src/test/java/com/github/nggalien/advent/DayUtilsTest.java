package com.github.nggalien.advent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
}