package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day11 {

    static long solvesDay(String input, int expander) {
        List<String> lines = Stream.of(input.split("\n")).toList();
        List<long[]> galaxies = new ArrayList<>();
        long y = 0;
        for (String line : lines) {
            long x = 0;
            int nbx = 0;
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == '#') {
                    galaxies.add(new long[]{x, y});
                    nbx++;
                    x++;
                } else {
                    int a = j;
                    long nby = lines.stream().filter(l -> l.charAt(a) == '#').count();
                    x += nby == 0 ? expander : 1;
                }
            }
            y += nbx == 0 ? expander : 1;
        }
        long distance = 0;
        while (galaxies.size() != 1) {
            long[][] pairOfGalaxies = new long[2][];
            pairOfGalaxies[0] = galaxies.getFirst();
            for (int i = 1; i < galaxies.size(); i++) {
                pairOfGalaxies[1] = galaxies.get(i);
                distance += Math.abs(pairOfGalaxies[0][0] - pairOfGalaxies[1][0]) + Math.abs(pairOfGalaxies[0][1] - pairOfGalaxies[1][1]);
            }
            galaxies.removeFirst();
        }
        return distance;
    }


    record Part1() implements Day11, AdventOfCode2023.SolutionOfDay<Long> {

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
            return 10422930L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day11.txt");
            return solvesDay(input, 2);
        }
    }

    record Part2() implements Day11, AdventOfCode2023.SolutionOfDay<Long> {

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
            return 699909023130L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day11.txt");
            return solvesDay(input, 1000000);
        }
    }


    static Day11.Part1 findPart1() {
        return new Day11.Part1();
    }

    static Day11.Part2 findPart2() {
        return new Day11.Part2();
    }
    
}
