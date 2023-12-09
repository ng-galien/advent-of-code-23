package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day6 {

    record Race(long time, long distance) {
        LongStream computeBestTimes() {
            return LongStream.range(1, time)
                    //.peek(i -> System.out.println(STR."pause: \{i}: distance: \{i * (time - i)}"))
                    .filter(i -> i * (time - i) > distance);
        }
    }

    static long numbersOfWins(String input, Function<String, String> parse) {
        List<String> data = input.lines()
                .map(s -> s.split(":")[1])
                .map(parse::apply)
                .toList();
        long[] times = DayUtils.parseLongArray(data.getFirst());
        long[] distances = DayUtils.parseLongArray(data.get(1));
        Race[] races = new Race[times.length];
        for (int i = 0; i <races.length; i ++) {
            races[i] = new Race(times[i], distances[i]);
        }
        return Arrays.stream(races)
                .mapToLong(value -> value.computeBestTimes().count())
                .reduce(1, (left, right) -> left * right);
    }



    record Part1() implements Day6, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 6;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 345015L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day6.txt");
            return Day6.numbersOfWins(input, s -> s);
        }
    }

    record Part2() implements Day6, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 6;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 42588603L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day6.txt");
            return Day6.numbersOfWins(input, s -> s.replace(" ", ""));
        }
    }


    static Day6.Part1 findPart1() {
        return new Day6.Part1();
    }

    static Day6.Part2 findPart2() {
        return new Day6.Part2();
    }
    
}
