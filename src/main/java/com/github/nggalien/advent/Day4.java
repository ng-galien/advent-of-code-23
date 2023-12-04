package com.github.nggalien.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public interface Day4 {

    record Card(int number, int copies, int[] goodNumbers, int[] myNumbers) {

        static Card of(int number, int copies, int[] goodNumbers, int[] myNumbers) {
            return new Card(number, copies, goodNumbers, myNumbers);
        }

        long numberOfMatches() {
            return IntStream.of(goodNumbers)
                    .filter(n -> IntStream.of(myNumbers).anyMatch(m -> m == n))
                    .count();
        }

        long score() {
            return (long) Math.pow(2, numberOfMatches() - 1);
        }

        /**
         * Parses a card from a string.
         * example: "//Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
         * @param str the string to parse
         * @return the card
         */
        static Card parseFromStr(String str) {
            String[] parts = str.split(":");
            int gameNumber = Integer.parseInt(parts[0].trim()
                    .replace("Card", " ")
                    .replace(" ", ""));
            String[] numbers = parts[1].split("\\|");
            String[] goodNumbers = numbers[0].trim().split(" ");
            String[] myNumbers = numbers[1].trim().split(" ");
            return Card.of(gameNumber, 1, parseNumbers(goodNumbers), parseNumbers(myNumbers));
        }

        Card duplicate(int nTimes) {
            return Card.of(number, copies + nTimes, goodNumbers, myNumbers);
        }

    }

    record Game(List<Card> cards) {
        long score() {
            return cards.stream().mapToLong(Card::score).sum();
        }

        void copyCard(int cardNumber, int nTimes) {
            if (cardNumber < cards.size()) {
                cards.set(cardNumber, cards.get(cardNumber).duplicate(nTimes));
            }
        }


        void copyCards(int from, int count, int nTimes) {
            IntStream.rangeClosed(from, from + count - 1).forEach(i -> copyCard(i, nTimes));
        }


        long copiesWin() {
            cards.forEach(card -> {
                int numberOfMatches = (int) card.numberOfMatches();
                if (numberOfMatches > 0) {
                    copyCards(card.number(), numberOfMatches, card.copies());
                }
            });
            return cards.stream().mapToLong(Card::copies).sum();
        }
        
    }



    private static int[] parseNumbers(String[] strings) {
        String[] sanitized = Stream.of(strings).filter(s -> !s.isBlank()).toArray(String[]::new);
        int[] numbers = new int[sanitized.length];
        for (int i = 0; i < sanitized.length; i++) {
            numbers[i] = Integer.parseInt(sanitized[i].trim());
        }
        return numbers;
    }

    static Game parseGame(String input) {
        String[] lines = input.split("\n");
        List<Card> cards = new ArrayList<>();
        for (String line : lines) {
            cards.add(Card.parseFromStr(line));
        }
        return new Game(cards);
    }

    /**
     * Represents the solution for part 1 of the puzzle.
     */
    record Part1() implements Day4, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 4;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 286L;
        }

        @Override
        public Long test() {
            return 537732L;
        }
    }

    /**
     * Represents the solution for part 2 of the puzzle.
     */
    record Part2() implements Day4, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 3;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 363L;
        }

        @Override
        public Long test() {
            return 84883664L;
        }
    }

    static Part1 findPart1() {
        return new Part1();
    }

    static Part2 findPart2() {
        return new Part2();
    }

}
