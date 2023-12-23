package com.github.nggalien.advent;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;
import static com.github.nggalien.advent.DayUtils.columnsOfInput;
import static com.github.nggalien.advent.DayUtils.convertBinary;
import static com.github.nggalien.advent.DayUtils.linesOfInput;

public interface Day13 {

    static long[][] splitInTwoParts(long[] ls, int index) {
        long[][] res = new long[2][];
        //First part is the big one
        if (index > ls.length / 2) {
            res[0] = reverse(Arrays.copyOfRange(ls, 0, index));
            res[1] = Arrays.copyOfRange(ls, index, ls.length);
        } else {
            res[0] = Arrays.copyOfRange(ls, index, ls.length);
            res[1] = reverse(Arrays.copyOfRange(ls, 0, index));
        }
        return res;
    }

    static long[] reverse(long[] ls) {
        long[] res = new long[ls.length];
        for (int i = 0; i < ls.length; i++) {
            res[i] = ls[ls.length - i - 1];
        }
        return res;
    }

    static int numSame(long[] big, long[] small) {
        int res = 0;
        for (int i = 0; i < small.length; i++) {
            if (big[i] == small[i]) {
                res++;
            }
        }
        return res;
    }


    static int numSameWithOneDifferent(long[] big, long[] small) {
        boolean canBeDifferent = true;
        int res = 0;
        for (int i = 0; i < small.length; i++) {
            if (big[i] == small[i]) {
                res++;
                continue;
            }
            long xor = big[i] ^ small[i];
            if (xor != 0 && (xor & (xor - 1)) == 0 && canBeDifferent) {
                canBeDifferent = false;
                res++;
            }
        }
        return !canBeDifferent ? res : -1;
    }

    static boolean isSymmetric(long[] ls, int splitIndex) {
        var split = splitInTwoParts(ls, splitIndex);
        long[] big = split[0];
        long[] small = split[1];
        int numSame = numSame(big, small);
        return numSame == small.length;
    }

    static boolean isSymmetricWithOneDifferent(long[] ls, int splitIndex) {
        var split = splitInTwoParts(ls, splitIndex);
        long[] big = split[0];
        long[] small = split[1];
        int numSame = numSameWithOneDifferent(big, small);
        return numSame == small.length;
    }


    static IntStream findSymmetric(long[] ls) {
        return IntStream.range(1, ls.length).filter(i -> isSymmetric(ls, i));
    }

    static IntStream findSymmetricWithOneDifferent(long[] ls) {
        return IntStream.range(1, ls.length).filter(i -> isSymmetricWithOneDifferent(ls, i)).findFirst().stream();
    }

    static long solvesPatternSymmetric(String input) {
        long[] hs = columnsOfInput(input).stream().mapToLong(
                s -> convertBinary(s, c -> c == '#')
        ).toArray();
        long[] vs = linesOfInput(input).stream().mapToLong(
                s -> convertBinary(s, c -> c == '#')
        ).toArray();
        return findSymmetric(hs).sum()
                + findSymmetric(vs).map(i -> i * 100).sum();
    }

    static long solvesPatternSymmetricButOneDifferent(String input) {
        long[] hs = columnsOfInput(input).stream().mapToLong(
                s -> convertBinary(s, c -> c == '#')
        ).toArray();
        long[] vs = linesOfInput(input).stream().mapToLong(
                s -> convertBinary(s, c -> c == '#')
        ).toArray();
        return findSymmetricWithOneDifferent(hs).sum()
                + findSymmetricWithOneDifferent(vs).map(i -> i * 100).sum();
    }

    static long solvesDay1(String input) {
        //Patterns are separated by a blank line
        String[] parts = input.split("\n\n");
        return Arrays.stream(parts).mapToLong(Day13::solvesPatternSymmetric).sum();
    }

    static long solvesDay2(String input) {
        //Patterns are separated by a blank line
        String[] parts = input.split("\n\n");
        return Arrays.stream(parts).mapToLong(Day13::solvesPatternSymmetricButOneDifferent).sum();
    }

    record Part1() implements Day13, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 13;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 29213L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day13.txt");
            return solvesDay1(input);
        }
    }

    record Part2() implements Day13, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 13;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 37453L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day13.txt");
            return solvesDay2(input);
        }
    }


    static Day13.Part1 findPart1() {
        return new Day13.Part1();
    }

    static Day13.Part2 findPart2() {
        return new Day13.Part2();
    }
    
}
