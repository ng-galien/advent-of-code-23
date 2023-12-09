package com.github.nggalien.advent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day7 {

    record Card(char symbol, int score) implements Comparable<Card> {
        @Override
        public int compareTo(Card o) {
            return Integer.compare(score, o.score);
        }

        static Card fromCodePoint(int codePoint) {
            var symbol = Character.toChars(codePoint)[0];
            return of(symbol);
        }

        static Card of(char symbol) {
            int score = switch (symbol) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> 11;
                case 'T' -> 10;
                default -> symbol - '0';
            };
            return new Card(symbol, score);
        }

    }

    sealed interface Hand extends Comparable<Hand> {
        List<Card> cards();
        int score();

        default int compareTo(Hand o) {
            var handComparison = Integer.compare(score(), o.score());
            if (handComparison != 0) {
                return handComparison;
            }
            var cardIndex = 0;
            while (cardIndex < cards().size()) {
                var cardComparison = cards().get(cardIndex).compareTo(o.cards().get(cardIndex));
                if (cardComparison != 0) {
                    return cardComparison;
                }
                cardIndex ++;
            }
            return 0;
        }

        static Hand parseFromStrPart1(String str) {
            var cards = str.chars()
                    .mapToObj(Card::fromCodePoint).toList();
            if (cards.size() != 5) {
                throw new IllegalArgumentException("A hand should have 5 cards");
            }
            Map<Card, List<Card>> cardsByScore = cards.stream()
                    .collect(Collectors.groupingBy(Function.identity()));
            if (cardsByScore.size() == 1) {
                return new FiveOfAKind(cards);
            } else if (cardsByScore.size() == 2) {
                var cardCounts = cardsByScore.values().stream()
                        .mapToInt(List::size)
                        .sorted()
                        .toArray();
                if (Arrays.equals(cardCounts, new int[]{1, 4})) {
                    return new FourOfAKind(cards);
                }
                if (Arrays.equals(cardCounts, new int[]{2, 3})) {
                    return new FullHouse(cards);
                }
            }else if (cardsByScore.size() == 3) {
                var cardCounts = cardsByScore.values().stream()
                        .mapToInt(List::size)
                        .sorted()
                        .toArray();
                if (Arrays.equals(cardCounts, new int[]{1, 1, 3})) {
                    return new ThreeOfAKind(cards);
                }
                if (Arrays.equals(cardCounts, new int[]{1, 2, 2})) {
                    return new TwoPairs(cards);
                }
            } else if (cardsByScore.size() == 4) {
                return new OnePair(cards);
            } else if (cardsByScore.size() == 5) {
                var cardScores = cards.stream()
                        .mapToInt(Card::score)
                        .sorted()
                        .toArray();
                var isStraight = IntStream.range(0, 4)
                        .allMatch(i -> cardScores[i] + 1 == cardScores[i + 1]);
                if (isStraight) {
                    return new Straight(cards);
                }
            }
            return new HighCard(cards);
        }
    }

    record FiveOfAKind(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 7;
        }
    }

    record FourOfAKind(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 6;
        }
    }

    record FullHouse(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 5;
        }
    }

    record Straight(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 4;
        }
    }

    record ThreeOfAKind(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 3;
        }
    }

    record TwoPairs(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 2;
        }
    }

    record OnePair(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 1;
        }
    }

    record HighCard(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 0;
        }
    }

    record Bet(Hand hand, int bid) implements Comparable<Bet> {
        static Bet parseFromStrPart1(String str) {
            var parts = str.split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("A bet should have a hand and a bid");
            }
            var hand = Hand.parseFromStrPart1(parts[0].trim());
            var bid = Integer.parseInt(parts[1].trim());
            return new Bet(hand, bid);
        }

        @Override
        public int compareTo(Bet o) {
            return hand.compareTo(o.hand);
        }
    }

    static long totalWiningPart1(String input) {
        List<Bet> bets = input.lines()
                .map(Bet::parseFromStrPart1)
                .sorted()
                .toList();
        long result = 0;
        for (int i = 0; i < bets.size(); i++) {
            Bet bet = bets.get(i);
            result += (long) bet.bid() * (i +1);
        }
        return result;
    }


    record Part1() implements Day7, AdventOfCode2023.SolutionOfDay<Long> {

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
            return 248812215L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day7.txt");
            return Day7.totalWiningPart1(input);
        }
    }

    record Part2() implements Day7, AdventOfCode2023.SolutionOfDay<Long> {

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
            return 0L;
        }

        @Override
        public Long test() {
            return -1L;
        }
    }


    static Day7.Part1 findPart1() {
        return new Day7.Part1();
    }

    static Day7.Part2 findPart2() {
        return new Day7.Part2();
    }
    
}
