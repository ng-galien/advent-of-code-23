package com.github.nggalien.advent;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface DayUtils {

    Pattern BLANK_SPACE = Pattern.compile("\\s+");

    static LongStream parseLongStream(String numbersSeparatedByBlankSpace) {
        return BLANK_SPACE.splitAsStream(numbersSeparatedByBlankSpace.trim()).mapToLong(Long::parseLong);
    }

    static long[] parseLongArray(String numbersSeparatedByBlankSpace) {
        return parseLongStream(numbersSeparatedByBlankSpace).toArray();
    }

    static long convertBinary(String s, Predicate<Character> binaryTest) {
        long res = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            res <<= 1;
            if (binaryTest.test(c)) {
                res |= 1;
            }
        }
        return res;
    }

    static List<String> linesOfInput(String input) {
        return List.of(input.split("\n"));
    }

    static List<String> columnsOfInput(String input) {
        var lines = linesOfInput(input);
        var columns = new StringBuilder[lines.getFirst().length()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new StringBuilder();
        }
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                columns[i].append(line.charAt(i));
            }
        }
        return Stream.of(columns).map(StringBuilder::toString).toList();
    }

    /**
     * Inverts a long value like 1234 in 4321
     */
    static long invert(long n) {
        long res = 0;
        while (n > 0) {
            res *= 10;
            res += n % 10;
            n /= 10;
        }
        return res;
    }

    static String[] splitByIndex(String s, int i) {
        return new String[]{s.substring(0, i), s.substring(i)};
    }
}
