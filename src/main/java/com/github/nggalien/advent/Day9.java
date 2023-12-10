package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day9 {

    static long[] diff(long[] input) {
        var output = new long[input.length - 1];
        for (int i = 0; i < input.length - 1; i++) {
            output[i] = input[i + 1] - input[i];
        }
        return output;
    }

    static boolean allZeros(long[] input) {
        return Arrays.stream(input).allMatch(i -> i == 0);
    }

    static List<long[]> expandDiff(long[] input) {
        List<long[]> output = new ArrayList<>();
        output.addFirst(input);
        while (!allZeros(output.getFirst())) {
            output.addFirst(diff(output.getFirst()));
        }
        return output;
    }

    static long addLastNumber(List<long[]> input) {
        long last = 0;
        for (int i = 1; i < input.size(); i++) {
            long[] current = input.get(i);
            last += current[current.length - 1];
        }
        return last;
    }

    static long addFirstNumber(List<long[]> input) {
        long first = 0;
        for (int i = 1; i < input.size(); i++) {
            long[] current = input.get(i);
            first = current[0] - first;
        }
        return first;
    }

    static long solve(String input, Function<List<long[]>, Long> addFunction) {
        var lines = input.lines().toList();
        var addedNumbers = lines.stream().mapToLong(line -> {
            var numbers = DayUtils.parseLongArray(line);
            var expanded = expandDiff(numbers);
            return addFunction.apply(expanded);
        });
        return addedNumbers.sum();
    }

    record Part1() implements Day9, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 9;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 1898776583L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day9.txt");
            return solve(input, Day9::addLastNumber);
        }
    }

    record Part2() implements Day9, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 9;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 1100L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day9.txt");
            return solve(input, Day9::addFirstNumber);
        }
    }


    static Day9.Part1 findPart1() {
        return new Day9.Part1();
    }

    static Day9.Part2 findPart2() {
        return new Day9.Part2();
    }
    
}
