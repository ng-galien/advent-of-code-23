package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day12 {

    static int count(String s, boolean fill) {
        int res = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '#') {
                res++;
            }
            if (s.charAt(i) == '?') {
                if(fill) {
                    res++;
                } else {
                    break;
                }
            }
            if (s.charAt(i) == '.') {
                break;
            }
        }
        return res;
    }

    static int nbPermutations(int n, int k) {
        return factorial(n) / factorial(n - k);
    }

    static int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }

    static int nbPermutations(String s, int nb) {
        int n = s.length();
        return nbPermutations(n, 0);
    }

    static String nextCandidate(String s, int nb) {
        return s;
    }

    static int countArr(String test, int[] perms) {
        int res = 0;
        int sumPerm = Arrays.stream(perms).sum();
        return res;
    }

    static long solvesDay(String input) {
        List<String> lines = Stream.of(input.split("\n")).toList();
        int result = 0;
        for (String line : lines) {
            String[] parts = line.split(" ");
            int[] nbs = Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).toArray();
            result += countArr(parts[0], nbs);
        }
        return 0;
    }
    record Part1() implements Day12, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 12;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 0L;
        }

        @Override
        public Long test() {
            return -1L;
        }
    }

    record Part2() implements Day12, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 12;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return -1L;
        }

        @Override
        public Long test() {
            return 0L;
        }
    }


    static Day12.Part1 findPart1() {
        return new Day12.Part1();
    }

    static Day12.Part2 findPart2() {
        return new Day12.Part2();
    }
    
}
