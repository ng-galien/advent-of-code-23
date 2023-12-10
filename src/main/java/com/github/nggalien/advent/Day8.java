package com.github.nggalien.advent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day8 {

    static String[] parseNode(String input) {
        String[] parts = input.split("=");
        String[] lr = parts[1].replace("(", "").replace(")", "").split(",");
        return new String[] { parts[0].trim(), lr[0].trim(), lr[1].trim() };
    }

    static Map<String, String[]> parseNetwork(Stream<String> input) {
        return input
                .filter(s -> !s.isBlank())
                .map(Day8::parseNode)
                .collect(Collectors.toMap(parts -> parts[0], Function.identity()));
    }

    static long countSteps(String input) {
        List<String> lines = input.lines().toList();
        String navigation = lines.getFirst();
        var nbStep = 0L;
        var network = parseNetwork(lines.stream().skip(1));
        boolean end = false;
        int navigationIndex = 0;
        var currentNode = network.get("AAA");
        while (!end) {
            nbStep ++;
            if(navigationIndex > navigation.length()-1) {
                navigationIndex = 0;
            }
            char dir = navigation.charAt(navigationIndex);
            int targetDir = dir == 'L'? 1: 2;
            var targetNode = currentNode[targetDir];
            if (targetNode .equals("ZZZ")) {
                break;
            }
            currentNode = network.get(currentNode[targetDir]);
            navigationIndex ++;
        }
        return nbStep;
    }


    record Part1() implements Day8, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 8;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 22199L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day8.txt");
            return Day8.countSteps(input);
        }
    }

    record Part2() implements Day8, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 8;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 0L;
        }

        @Override
        public Long test() {
            return 250057090L;
        }
    }


    static Day8.Part1 findPart1() {
        return new Day8.Part1();
    }

    static Day8.Part2 findPart2() {
        return new Day8.Part2();
    }
    
}
