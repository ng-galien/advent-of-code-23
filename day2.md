# Java Class Documentation: Day2 Interface

## Overview
The Day2 interface is part of a series of solutions for "Advent of Code 2023". This particular interface deals with a puzzle named "Cube Conundrum" which involves a series of games with colored cubes. The objective is to determine the possible games based on the number of cubes and calculate the power of cube sets.

## Class Structure

### Inner Records:

* `Quantity`: Represents a non-negative quantity of items.
* `Cube`: Represents a cube with a non-blank color attribute.
* `CubesOfColor`: Represents a set of cubes of a certain color and quantity.
* `Hand`: Represents a collection of CubesOfColor.
* `Game`: Represents a game with an ID and a collection of Hand.
* `CubeRepository`: Manages a collection of cubes, supporting various operations like adding cubes, checking if a certain quantity of cubes can be picked, etc.
* `Part1` and `Part2`: Implements the `Day2` interface and `AdventOfCode2023.SolutionOfDay` for the specific parts of the puzzle.

### Interface Methods:

* `sumOfAllPlayableGamesNumber`: Determines the sum of the IDs of all playable games based on a given hand and set of games.
* `powerOfGame`: Calculates the power of a given game.
* `powerOffAllGames`: Calculates the total power of all games.
* `parse`: Parses a string input into a collection of Game objects.

### Static Methods:

* `findPart1`: Returns an instance of `Part1`.
* `findPart2`: Returns an instance of `Part2`.

## Usage

The interface is designed to solve two parts of the Cube Conundrum puzzle. It calculates which games are possible with a given set of cubes and computes the power of the minimum set of cubes required for each game.

### Example

```java
String hand = "12 red, 13 green, 14 blue";
String games = Day2.readFileOfResource("day2.txt");
Day2.part1 solutionPart1 = Day2.findPart1();
int sumOfPlayableGames = solutionPart1.sumOfAllPlayableGamesNumber(hand, games);

Day2.part2 solutionPart2 = Day2.findPart2();
long powerOfAllGames = solutionPart2.powerOffAllGames(games);
```

### Exceptions

The class includes several validations, throwing `IllegalArgumentException` for invalid or missing data, such as negative quantities, blank colors, or incorrectly formatted game inputs.

> Documentation provided by ChatGPT with `Can you document the following class? Thanks!` | `CTRL+Enter` | `CTRL+V` prompt.