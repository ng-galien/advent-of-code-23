package com.github.nggalien.advent;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day8 {

    int VALUE = 0;
    int LEFT = 1;
    int RIGHT = 2;

    static long pgcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    static long ppcm(long a, long b) {
        return a * (b / pgcd(a, b));
    }

    static String[] parseNode(String input) {
        String[] parts = input.split("=");
        String[] lr = parts[1].replace("(", "").replace(")", "").split(",");
        return new String[] { parts[0].trim(), lr[0].trim(), lr[1].trim() };
    }

    static long walk(List<String[]> networks, String path, String[] startNode, Predicate<String> stop) {
        var currentNode = startNode;
        var nbStep = 0;
        while (currentNode != null) {
            char dir = path.charAt(nbStep % path.length());
            nbStep ++;
            var nextNode = currentNode[dir == 'L' ? LEFT : RIGHT];
            if (stop.test(nextNode)) {
                break;
            }
            currentNode = networks.stream()
                    .filter(node -> Objects.equals(node[VALUE], nextNode))
                    .findFirst().orElse(null);
        }
        return nbStep;
    }

    static long solves(String input, Predicate<String> startNodeTest, Predicate<String> endNodeTest) {
        List<String> lines = input.lines().toList();
        String navigation = lines.getFirst();
        var network = lines.stream().skip(2)
                .map(Day8::parseNode)
                .toList();
        return network.stream()
                .filter(node -> startNodeTest.test(node[VALUE]))
                .map(startNode -> walk(network, navigation, startNode, endNodeTest))
                .reduce(1L, Day8::ppcm);
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
            return Day8.solves(input, s -> s.equals("AAA"), s -> s.equals("ZZZ"));
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
            return 13334102464297L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day8.txt");
            return Day8.solves(input, s -> s.endsWith("A"), s -> s.endsWith("Z"));
        }
    }


    static Day8.Part1 findPart1() {
        return new Day8.Part1();
    }

    static Day8.Part2 findPart2() {
        return new Day8.Part2();
    }
    
}
