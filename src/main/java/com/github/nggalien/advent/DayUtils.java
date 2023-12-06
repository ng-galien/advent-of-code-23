package com.github.nggalien.advent;

import java.util.regex.Pattern;
import java.util.stream.LongStream;

public interface DayUtils {

    Pattern BLANK_SPACE = Pattern.compile("\\s+");

    static LongStream parseLongStream(String numbersSeparatedByBlankSpace) {
        return BLANK_SPACE.splitAsStream(numbersSeparatedByBlankSpace.trim()).mapToLong(Long::parseLong);
    }

    static long[] parseLongArray(String numbersSeparatedByBlankSpace) {
        return parseLongStream(numbersSeparatedByBlankSpace).toArray();
    }
}
