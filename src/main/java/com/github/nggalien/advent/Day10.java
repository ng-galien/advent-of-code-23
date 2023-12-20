package com.github.nggalien.advent;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.nggalien.advent.AdventOfCode2023.readFileOfResource;

public interface Day10 {

    Symbol V = new Symbol.V();
    Symbol H = new Symbol.H();
    Symbol BL = new Symbol.BL();
    Symbol BR = new Symbol.BR();
    Symbol TL = new Symbol.TL();
    Symbol TR = new Symbol.TR();
    Symbol S = new Symbol.S();
    Symbol None = new Symbol.None();

    sealed interface Symbol permits Symbol.BL, Symbol.BR, Symbol.H, Symbol.None, Symbol.S, Symbol.TL, Symbol.TR, Symbol.V {
        record V() implements Symbol {}
        record H() implements Symbol {}
        record BL() implements Symbol {}
        record BR() implements Symbol {}
        record TL() implements Symbol {}
        record TR() implements Symbol {}
        record S() implements Symbol {}

        record None() implements Symbol {}

        static Symbol of(char c) {
            return switch (c) {
                case '|' -> V;
                case '-' -> H;
                case 'L' -> BL;
                case 'J' -> BR;
                case 'F' -> TL;
                case '7' -> TR;
                case 'S' -> S;
                default -> None;
            };
        }
    }

    static boolean isAngle(Symbol symbol) {
        return symbol instanceof Symbol.BL ||
                symbol instanceof Symbol.BR ||
                symbol instanceof Symbol.TL ||
                symbol instanceof Symbol.TR ||
                symbol instanceof Symbol.S;
    }

    Dir UP = new Dir.Up();
    Dir DOWN = new Dir.Down();
    Dir LEFT = new Dir.Left();
    Dir RIGHT = new Dir.Right();

    Dir STOP = new Dir.Stop();

    sealed interface Dir permits Dir.Down, Dir.Left, Dir.Right, Dir.Stop, Dir.Up {
        record Up() implements Dir {}
        record Down() implements Dir {}
        record Left() implements Dir {}
        record Right() implements Dir {}

        record Stop() implements Dir {}
    }

    record Point(int x, int y) {
        public Point move(Dir dir) {
            return switch (dir) {
                case Dir.Up _ -> new Point(x, y-1);
                case Dir.Down _ -> new Point(x, y+1);
                case Dir.Left _ -> new Point(x-1, y);
                case Dir.Right _ -> new Point(x+1, y);
                default -> this;
            };
        }
    }

    record Location(Point point, Symbol symbol) {
        static Location of(Point point, Symbol symbol) {
            return new Location(point, symbol);
        }
    }

    record PathVisitor(List<Location> locations, Dir from, boolean closed) {
        public PathVisitor move(Dir dir, Function<Point, Character> matrix) {
            var nextPoint = currentLocation().point().move(dir);
            Symbol nextSymbol = Symbol.of(matrix.apply(nextPoint));
            boolean closed = nextSymbol instanceof Symbol.S;
            if(closed) {
                var firstPoint = locations.getFirst().point();
                nextSymbol = findEnclosure(currentLocation().point, firstPoint);
            }
            var nextLocation = Location.of(nextPoint, nextSymbol);
            var nextLocations = new ArrayList<>(locations);
            nextLocations.add(nextLocation);
            return new PathVisitor(nextLocations, dir, closed);
        }

        private Symbol findEnclosure(Point point, Point nextPoint) {
            Point vect = new Point(nextPoint.x- point.x, nextPoint.y - point.y);
            return switch (vect) {
                case Point p when p.x != 0 && p.y == 0 -> H;
                case Point p when p.x == 0 && p.y != 0 -> V;
                default -> S;
            };
        }

        Location currentLocation() {
            return locations.getLast();
        }

        boolean canMove(Dir dir, Function<Point, Character> matrix) {
            if (dir == STOP) {
                return false;
            }
            if (currentLocation().symbol() instanceof Symbol.S) {
                return false;
            }
            var nextCurrent = currentLocation().point().move(dir);
            var nextChar = matrix.apply(nextCurrent);
            var nextSymbol = Symbol.of(nextChar);
            return !(nextSymbol instanceof Symbol.None);
        }

        Dir nextDir() {
            var symbol = currentLocation().symbol();
            return switch (symbol) {
                case Symbol.V _ when from instanceof Dir.Down -> DOWN;
                case Symbol.V _ when from instanceof Dir.Up -> UP;
                case Symbol.H _ when from instanceof Dir.Right -> RIGHT;
                case Symbol.H _ when from instanceof Dir.Left -> LEFT;
                case Symbol.BL _ when from instanceof Dir.Left -> UP;
                case Symbol.BL _ when from instanceof Dir.Down -> RIGHT;
                case Symbol.BR _ when from instanceof Dir.Down -> LEFT;
                case Symbol.BR _ when from instanceof Dir.Right -> UP;
                case Symbol.TL _ when from instanceof Dir.Up -> RIGHT;
                case Symbol.TL _ when from instanceof Dir.Left -> DOWN;
                case Symbol.TR _ when from instanceof Dir.Up -> LEFT;
                case Symbol.TR _ when from instanceof Dir.Right -> DOWN;
                default -> STOP;
            };
        }

        PathVisitor walk(Function<Point, Character> matrix) {
            var nextDir = nextDir();
            var visitor = this;
            var canMove = visitor.canMove(nextDir, matrix);
            while (nextDir != STOP && canMove) {
                visitor = visitor.move(nextDir, matrix);
                nextDir = visitor.nextDir();
                canMove = visitor.canMove(nextDir, matrix);
            }
            return visitor;
        }
    }

    static Stream<PathVisitor> findPaths(String input) {
        String[] lines = input.split("\n");
        //Find the start: The S symbol
        Point start = null;
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            var spos = line.indexOf('S');
            if (spos != -1) {
                start = new Point(spos, y);
                break;
            }
        }
        Point current = start;
        if (start == null) {
            throw new IllegalStateException("No start found");
        }

        Function<Point, Character> matrix = point -> {
            if (point.x() < 0 || point.y() < 0 || point.x() >= lines[0].length() || point.y() >= lines.length) {
                return ' ';
            }
            return lines[point.y()].charAt(point.x());
        };

        return Stream.of(UP, DOWN, LEFT, RIGHT).map(dir -> {
            var targetPoint = current.move(dir);
            var startSymbol = Symbol.of(matrix.apply(targetPoint));
            var targetDir = dir;
            if (startSymbol instanceof Symbol.None) {
                targetDir = STOP;
            }
            var locations = List.of(new Location(targetPoint, startSymbol));
            return new PathVisitor(locations, targetDir, false).walk(matrix);
        });
    }

    static long solvePart1(String input) {

        double max = findPaths(input)
                .filter(PathVisitor::closed)
                .map(PathVisitor::locations)
                .mapToInt(List::size)
                .max().orElse(0);
        return (long) Math.floor(max/2);
    }

    static long solvePart2(String input) {
        List<String> lines = Stream.of(input.split("\n")).toList();
        int width = lines.getFirst().length();
        int height = lines.size();
        List<PathVisitor> locations = findPaths(input)
                .filter(PathVisitor::closed)
                .toList();
        if (locations.isEmpty()) {
            throw new IllegalStateException(STR."Expected 1 location, found \{locations.size()}");
        }
        List<Point> loop = locations.getFirst().locations().stream()
                .map(Location::point).toList();
//        System.out.println("Loop:");
//        printGridAsString(width, height, locations.getFirst().locations());
        List<Point> polygon = locations.getFirst()
                .locations().stream()
                .filter(location -> isAngle(location.symbol()))
                .map(Location::point).toList();
//        System.out.println("Polygon:");
        List<Point> testPoints = IntStream.range(0, height)
                .boxed()
                .flatMap(y -> IntStream.range(0, width)
                        .mapToObj(x -> new Point(x, y)))
                .filter(point -> !loop.contains(point))
                .filter(point -> isPointInPolygon(point, polygon))
                .toList();
        return testPoints.size();
    }

    static void printGridAsString(int width, int height, Collection<Location> loop) {
        Map<Point, Symbol> map = loop.stream()
                .collect(Collectors.toMap(Location::point, Location::symbol));
        for (int y = 0; y < height; y++) {
            var y1 = y;
            var line = IntStream.range(0, width)
                    .mapToObj(x -> new Point(x, y1))
                    .map(point -> map.getOrDefault(point, None))
                    .map(symbol -> switch (symbol) {
                        case Symbol.BL _ -> "L";
                        case Symbol.BR _ -> "J";
                        case Symbol.TL _ -> "F";
                        case Symbol.TR _ -> "7";
                        case Symbol.S _ -> "S";
                        default -> ".";
                    })
                    .collect(Collectors.joining());
            System.out.println(line);
        }
    }

    static boolean intersects(int[] A, int[] B, double[] P) {
        if (A[1] > B[1])
            return intersects(B, A, P);

        if (P[1] == A[1] || P[1] == B[1])
            P[1] += 0.0001;

        if (P[1] > B[1] || P[1] < A[1] || P[0] >= Math.max(A[0], B[0]))
            return false;

        if (P[0] < Math.min(A[0], B[0]))
            return true;

        double red = (P[1] - A[1]) / (double) (P[0] - A[0]);
        double blue = (B[1] - A[1]) / (double) (B[0] - A[0]);
        return red >= blue;
    }

    static boolean contains(int[][] shape, double[] pnt) {
        boolean inside = false;
        int len = shape.length;
        for (int i = 0; i < len; i++) {
            if (intersects(shape[i], shape[(i + 1) % len], pnt))
                inside = !inside;
        }
        return inside;
    }

    static boolean isPointInPolygon(Point point, List<Point> polygon) {
        int[][] shape = polygon.stream()
                .map(p -> new int[]{p.x(), p.y()})
                .toArray(int[][]::new);
        double[] pnt = new double[]{point.x(), point.y()};
        return contains(shape, pnt);
    }


    record Part1() implements Day10, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 10;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 6956L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day10.txt");
            return Day10.solvePart1(input);
        }
    }

    record Part2() implements Day10, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 10;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 455L;
        }

        @Override
        public Long test() {
            var input = readFileOfResource("day10.txt");
            return Day10.solvePart2(input);
        }
    }


    static Day10.Part1 findPart1() {
        return new Day10.Part1();
    }

    static Day10.Part2 findPart2() {
        return new Day10.Part2();
    }
    
}
