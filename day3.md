### Java Class Documentation: `Day3` Interface in `com.github.nggalien.advent` Package

#### Overview
The `Day3` interface is part of the "Advent of Code 2023" challenge, focused on solving a puzzle titled "Gear Ratios". This puzzle involves analyzing engine schematics to find missing parts and correct gear ratios.

#### Class Structure
1. **Inner Records and Interfaces:**
    - `Position`: Represents a coordinate with x and y values.
    - `Zone`: Represents a rectangular area defined by top left and bottom right positions.
    - `MotoPart`: An interface for engine parts.
    - `Number`: Represents a part number in the engine with a value and its zone.
    - `Gear`: Represents a gear defined by two numbers.
    - `Symbol`: Represents a symbol in the engine schematic.
    - `Motor`: Represents the engine, containing a list of `MotoPart`.

2. **Functionality:**
    - The `Motor` record encapsulates logic for analyzing engine schematics, including finding part numbers and calculating gear ratios.
    - `sumOfAllPartNumbers`: Calculates the sum of all part numbers in the engine schematic.
    - `sumOfAllGearRatios`: Calculates the sum of all gear ratios in the engine schematic.

3. **Parsing Logic:**
    - `parseLine`: Parses a line of the engine schematic into `MotoPart` objects.

4. **Solution Records:**
    - `Part1` and `Part2`: Implements solutions for parts one and two of the "Day 3" puzzle.

5. **Static Methods:**
    - `findPart1`: Returns an instance of `Part1`.
    - `findPart2`: Returns an instance of `Part2`.

#### Usage
The interface and its inner records are used to analyze an engine schematic (provided as a string input), identify part numbers and gears, and perform calculations based on the puzzle's requirements.

#### Example
```java
String schematicInput = "engine schematic string";
Day3.Motor motor = Day3.Motor.of(schematicInput);
long sumOfPartNumbers = motor.sumOfAllPartNumbers();
long sumOfGearRatios = motor.sumOfAllGearRatios();
```

#### Exceptions
The class includes validation checks, throwing `IllegalArgumentException` for invalid data such as negative values or incorrect zone specifications.

---

Documentation provided by ChatGPT.