package com.github.nggalien.advent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day7 {

    record Card(char symbol, int score, boolean joker) implements Comparable<Card> {
        @Override
        public int compareTo(Card o) {
            return Integer.compare(score, o.score);
        }

        static Card fromCodePoint(int codePoint, boolean joker) {
            var symbol = Character.toChars(codePoint)[0];
            return of(symbol, joker);
        }

        static Card of(char symbol, boolean joker) {
            int score = switch (symbol) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> joker ? 1 : 11;
                case 'T' -> 10;
                default -> symbol - '0';
            };
            return new Card(symbol, score, joker);
        }

        static Card of(char symbol) {
            return of(symbol, false);
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

        static Hand parseFromStr(String str, boolean joker) {
            var cards = str.chars()
                    .mapToObj(value -> Card.fromCodePoint(value, joker))
                    .toList();
            if (cards.size() != 5) {
                throw new IllegalArgumentException("A hand should have 5 cards");
            }
            long jokerCount = cards.stream()
                    .filter(card -> card.symbol() == 'J')
                    .count();
            if (!joker) {
                jokerCount = 0;
            }
            Map<Card, List<Card>> cardsByScore = cards.stream()
                    .collect(Collectors.groupingBy(Function.identity()));
            int cardByScoreSize = cardsByScore.size();
            var cardCounts = cardsByScore.values().stream()
                    .mapToInt(List::size)
                    .sorted()
                    .toArray();
            if (cardByScoreSize == 1) {
                return new FiveOfAKind(cards);
            } else if (cardByScoreSize == 2) {

                if (Arrays.equals(cardCounts, new int[]{1, 4})) {
                    if (jokerCount == 1|| jokerCount == 4) {
                        return new FiveOfAKind(cards);
                    }
                    return new FourOfAKind(cards);
                }
                if (Arrays.equals(cardCounts, new int[]{2, 3})) {
                    if (jokerCount == 2|| jokerCount == 3) {
                        return new FiveOfAKind(cards);
                    }
                    return new FullHouse(cards);
                }
            } else if (cardByScoreSize == 3) {
                if (Arrays.equals(cardCounts, new int[]{1, 1, 3})) {
                    if (jokerCount == 1 || jokerCount == 3) {
                        return new FourOfAKind(cards);
                    }
                    return new ThreeOfAKind(cards);
                }
                if (Arrays.equals(cardCounts, new int[]{1, 2, 2})) {
                    if (jokerCount == 1) {
                        return new FullHouse(cards);
                    }
                    if (jokerCount == 2) {
                        return new FourOfAKind(cards);
                    }
                    return new TwoPairs(cards);
                }
            } else if (cardsByScore.size() == 4) {
                if (jokerCount == 1 || jokerCount == 2) {
                    return new ThreeOfAKind(cards);
                }
                return new OnePair(cards);
            }
            if (jokerCount == 1) {
                return new OnePair(cards);
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

    record ThreeOfAKind(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 4;
        }
    }

    record TwoPairs(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 3;
        }
    }

    record OnePair(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 2;
        }
    }

    record HighCard(List<Card> cards) implements Hand {
        @Override
        public int score() {
            return 1;
        }
    }

    record Bet(Hand hand, int bid) implements Comparable<Bet> {
        static Bet parseFromStr(String str, boolean joker) {
            var parts = str.split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("A bet should have a hand and a bid");
            }
            var hand = Hand.parseFromStr(parts[0].trim(), joker);
            var bid = Integer.parseInt(parts[1].trim());
            return new Bet(hand, bid);
        }

        @Override
        public int compareTo(Bet o) {
            return hand.compareTo(o.hand);
        }
    }

    static long totalWiningPart(String input, AdventOfCode2023.DayPart part) {
        boolean joker = part == AdventOfCode2023.DayPart.TWO;
        List<Bet> bets = input.lines()
                .map(s -> Bet.parseFromStr(s, joker))
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
            return 7;
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
            return Day7.totalWiningPart(input, part());
        }
    }

    record Part2() implements Day7, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 7;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            var input = readFileOfResource("day7.txt");
            return Day7.totalWiningPart(input, part());
        }

        @Override
        public Long test() {
            return 250057090L;
        }
    }


    static Day7.Part1 findPart1() {
        return new Day7.Part1();
    }

    static Day7.Part2 findPart2() {
        return new Day7.Part2();
    }
    
}
