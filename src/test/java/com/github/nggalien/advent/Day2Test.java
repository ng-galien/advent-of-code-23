package com.github.nggalien.advent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class Day2Test {

    @Test
    void cubeRepositoryTest() {
        //Given
        String hand = """
                12 red, 13 green, 14 blue
                """;

        var tooManyRed = Day2.CubesOfColor.of(Day2.Cube.of("red"), Day2.Quantity.of(13));
        var tooManyGreen = Day2.CubesOfColor.of(Day2.Cube.of("green"), Day2.Quantity.of(14));
        var tooManyBlue = Day2.CubesOfColor.of(Day2.Cube.of("blue"), Day2.Quantity.of(15));
        var justRightRed = Day2.CubesOfColor.of(Day2.Cube.of("red"), Day2.Quantity.of(12));
        var justRightGreen = Day2.CubesOfColor.of(Day2.Cube.of("green"), Day2.Quantity.of(13));
        var justRightBlue = Day2.CubesOfColor.of(Day2.Cube.of("blue"), Day2.Quantity.of(14));
        var notEnoughRed = Day2.CubesOfColor.of(Day2.Cube.of("red"), Day2.Quantity.of(11));
        var notEnoughGreen = Day2.CubesOfColor.of(Day2.Cube.of("green"), Day2.Quantity.of(12));
        var notEnoughBlue = Day2.CubesOfColor.of(Day2.Cube.of("blue"), Day2.Quantity.of(13));

        String gameTooManyRed = """
                Game 1: 13 red, 4 green; 1 red, 2 green, 6 blue; 2 green
                """;
        String gameTooManyGreen = """
                Game 1: 12 red, 14 green; 1 red, 2 green, 6 blue; 2 green
                """;
        String gameTooManyBlue = """
                Game 1: 12 red, 13 green; 1 red, 2 green, 20 blue; 2 green
                """;
        String enoughRed = """
                Game 1: 12 red, 4 green; 1 red, 2 green, 6 blue; 2 green
                """;
        String enoughGreen = """
                Game 1: 12 red, 13 green; 1 red, 2 green, 6 blue; 2 green
                """;
        String enoughBlue = """
                Game 1: 12 red, 13 green; 1 red, 2 green, 6 blue; 2 green
                """;

        //When
        Day2.CubeRepository repository = Day2.CubeRepository.parse(hand);
        //Then
        assertEquals(Day2.Quantity.of(12), repository.quantityOf(Day2.Cube.of("red")), "Quantity of red cube should be 12");
        assertEquals(Day2.Quantity.of(13), repository.quantityOf(Day2.Cube.of("green")), "Quantity of green cube should be 13");
        assertEquals(Day2.Quantity.of(14), repository.quantityOf(Day2.Cube.of("blue")), "Quantity of blue cube should be 14");

        assertFalse(repository.canPick(tooManyRed), "Should not be able to pick too many red cubes");
        assertFalse(repository.canPick(tooManyGreen), "Should not be able to pick too many green cubes");
        assertFalse(repository.canPick(tooManyBlue), "Should not be able to pick too many blue cubes");
        assertTrue(repository.canPick(justRightRed), "Should be able to pick just right red cubes");
        assertTrue(repository.canPick(justRightGreen), "Should be able to pick just right green cubes");
        assertTrue(repository.canPick(justRightBlue), "Should be able to pick just right blue cubes");
        assertTrue(repository.canPick(notEnoughRed), "Should be able to pick not enough red cubes");
        assertTrue(repository.canPick(notEnoughGreen), "Should be able to pick not enough green cubes");
        assertTrue(repository.canPick(notEnoughBlue), "Should be able to pick not enough blue cubes");

        assertFalse(repository.canPick(Day2.Game.parse(gameTooManyRed)), "Should not be able to pick game with too many red cubes");
        assertFalse(repository.canPick(Day2.Game.parse(gameTooManyGreen)), "Should not be able to pick game with too many green cubes");
        assertFalse(repository.canPick(Day2.Game.parse(gameTooManyBlue)), "Should not be able to pick game with too many blue cubes");

        assertTrue(repository.canPick(Day2.Game.parse(enoughRed)), "Should be able to pick game with enough red cubes");
        assertTrue(repository.canPick(Day2.Game.parse(enoughGreen)), "Should be able to pick game with enough green cubes");
        assertTrue(repository.canPick(Day2.Game.parse(enoughBlue)), "Should be able to pick game with enough blue cubes");
    }

    @ParameterizedTest
    @MethodSource("provideInputAndExpectedResult")
    void testPowerOfTheRepository(String game, long expected) {
        //When
        Day2 day2 = new Day2() {};
        long power = day2.powerOffAllGames(game);
        //Then
        assertEquals(expected, power, "Power of the game should be " + expected);
    }

    static Stream<Arguments> provideInputAndExpectedResult() {
        return Stream.of(
                Arguments.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green", 48),
                Arguments.of("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue", 12),
                Arguments.of("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red", 1560),
                Arguments.of("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red", 630),
                Arguments.of("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green", 36)
        );
    }

    @Test
    void givenHand_whenTestAllDayTest_ThenResultIsOk() {
        //Given
        Day2 day2 = new Day2() {};
        String hand = """
                12 red, 13 green, 14 blue
                """;
        String games = """
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                """;
        int expectedSumOfAllPlayableGamesNumber = 8;
        long expectedPowerOfAllGames = 2286;
        //When
        int sum = day2.sumOfAllPlayableGamesNumber(hand, games);
        long power = day2.powerOffAllGames(games);
        //Then
        assertEquals(expectedSumOfAllPlayableGamesNumber, sum, "Sum of all playable games number should be " + expectedSumOfAllPlayableGamesNumber);
        assertEquals(expectedPowerOfAllGames, power, "Power of all games should be " + expectedPowerOfAllGames);
    }

    @Test
    void testPart1() {
        //Given
        Day2.Part1 part1 = Day2.findPart1();
        int rightAnswer = part1.rightAnswer();
        //When
        int test = part1.test();
        //Then
        assertEquals(rightAnswer, test, "Test should be " + rightAnswer);
    }

    @Test
    void testPart2() {
        //Given
        Day2.Part2 part2 = Day2.findPart2();
        long rightAnswer = part2.rightAnswer();
        //When
        long test = part2.test();
        //Then
        assertEquals(rightAnswer, test, "Test should be " + rightAnswer);
    }
}