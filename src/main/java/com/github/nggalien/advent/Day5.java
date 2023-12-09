package com.github.nggalien.advent;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Day5 {

    record Interval(long start, long end) {

        boolean intersects(Interval other) {
            return end < other.start || other.end < start;
        }

        public static Interval of(long start, long end) {
            return new Interval(start, end);
        }

        public static Interval of(long value) {
            return new Interval(value, value);
        }

        public static Interval shift(Interval interval, long shift) {
            return new Interval(interval.start + shift, interval.end + shift);
        }

        public static Optional<Interval> intersection(Interval first, Interval second) {
            if (first.intersects(second)) {
                return Optional.empty();
            }
            return Optional.of(new Interval(Math.max(first.start, second.start), Math.min(first.end, second.end)));
        }

        /**
         * Extracts the parts of the first interval that is not covered by the intersection with the second interval.
         * Examples:
         * <pre>
         *     extractSubParts([1, 10], [2, 3]) = [[1, 1], [4, 10]]
         *     extractSubParts([1, 10], [1, 1]) = [[2, 10]]
         *     extractSubParts([1, 10], [1, 10]) = []
         *     extractSubParts([1, 5], [6, 10]) = [[1, 5]]
         * </pre>
         *
         * @param interval  first interval
         * @param canBeContainedIn second interval
         * @return a list of intervals that are not covered by the intersection
         */
        public static Collection<Interval> extractSubParts(Interval interval, Interval canBeContainedIn) {
            Optional<Interval> mayBeContainedIn = intersection(interval, canBeContainedIn);
            if (mayBeContainedIn.isEmpty()) {
                return Collections.singletonList(interval);
            }
            Interval intersect = mayBeContainedIn.get();
            if (intersect.start == interval.start && intersect.end == interval.end) {
                return Collections.emptyList();
            }
            if (intersect.start == interval.start) {
                return Collections.singletonList(Interval.of(intersect.end + 1, interval.end));
            }
            if (intersect.end == interval.end) {
                return Collections.singletonList(Interval.of(interval.start, intersect.start - 1));
            }
            return List.of(Interval.of(interval.start, intersect.start - 1), Interval.of(intersect.end + 1, interval.end));
        }
    }

    record ShiftedInterval(Interval interval, long shift) {
        public static ShiftedInterval of(Interval interval, long shift) {
            return new ShiftedInterval(interval, shift);
        }

        /**
         * Create a shifted interval from a destination range start, a source range start and a range length.<br>
         * The interval will be from sourceRangeStart to sourceRangeStart + rangeLength - 1.<br>
         * The interval will be shifted by destinationRangeStart - sourceRangeStart.
         * <pre>
         *     of(10, 5, 5) = [5, 9] shifted by 5 = [10, 14]
         *     of(0, 5, 5) = [5, 9] shifted by -5 = [0, 4]
         * </pre>
         * @param destinationRangeStart the start of the destination range
         * @param sourceRangeStart      the start of the source range
         * @param rangeLength           the length of the range
         * @return the shifted interval
         */
        public static ShiftedInterval of(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
            return new ShiftedInterval(Interval.of(sourceRangeStart, sourceRangeStart + rangeLength - 1),
                    destinationRangeStart - sourceRangeStart);
        }

        /**
         * Find the intersection between the interval and the intersectCandidate, and shift the intersectCandidate.<br>
         * <p>
         *     Returns the shifted intersectCandidate and the sub parts of the interval that are not covered by the intersection.<br>
         *     If supplied intersectCandidate is already shifted, it is returned as is.<br>
         *     If the interval and the intersectCandidate do not intersect, the intersectCandidate is returned as is.<br>
         * </p>
         * Examples:
         * <pre>
         *     //With ShiftedInterval {interval=[1, 10], shift=10}
         *     shiftIntersectCandidate({[2, 3], shift=0}) = {[12, 13], shift=10}
         *     shiftIntersectCandidate({[2, 3], shift=1}) = {[2, 3], shift=1}
         *     shiftIntersectCandidate([6, 10], shift=0) = {[6, 10], shift=0}
         * </pre>
         *
         * @param intersectCandidate the interval to intersect with the interval
         * @return the shifted intersectCandidate and the sub parts of the interval that are not covered by the intersection
         */
        Collection<ShiftedInterval> shiftIntersectCandidate(ShiftedInterval intersectCandidate) {
            if (intersectCandidate.shift != 0) {
                return Collections.singletonList(intersectCandidate);
            }
            Optional<Interval> mayBeIntersect = Interval.intersection(interval, intersectCandidate.interval());
            if (mayBeIntersect.isEmpty()) {
                return Collections.singletonList(intersectCandidate);
            }
            Interval shifted = Interval.shift(mayBeIntersect.get(), shift);

            var subParts = Interval.extractSubParts(intersectCandidate.interval(), interval).stream()
                    .map(interval -> ShiftedInterval.of(interval, 0));
            return Stream.concat(subParts, Stream.of(ShiftedInterval.of(shifted, shift))).toList();
        }
    }

    static ShiftedInterval shiftedIntervalOf(String line) {
        long[] values = DayUtils.parseLongArray(line);
        return ShiftedInterval.of(values[0], values[1], values[2]);
    }

    /**
     * Converts a line of longs to a stream of intervals.
     * <pre>
     *     lineToSingleIntervals("1 2 3 4 5") = [[1, 1], [2, 2], [3, 3], [4, 4], [5, 5]]
     * </pre>
     *
     * @param line the line to convert
     * @return a stream of intervals
     */
    static Stream<Interval> lineToSingleIntervals(String line) {
        return DayUtils.parseLongStream(line)
                .mapToObj(Interval::of);
    }

    /**
     * Converts a line of longs to a stream of intervals<br>
     * The interval is represented by a two longs, the start and the length.<br>
     * <pre>
     *     lineToIntervals("1 2 10 5") = [[1, 2], [10, 14]]
     * </pre>
     *
     * @param line the line to convert
     * @return a stream of intervals
     */
    static Stream<Interval> lineToIntervals(String line) {
        long[] values = DayUtils.parseLongArray(line);
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < values.length; i += 2) {
            if (i + 1 >= values.length) {
                break;
            }
            intervals.add(Interval.of(values[i], values[i] + values[i + 1] - 1));
        }
        return intervals.stream();
    }

    static Stream<ShiftedInterval> shiftList(Stream<ShiftedInterval> inputs, ShiftedInterval shift) {
        return inputs.flatMap(input -> shift.shiftIntersectCandidate(input).stream());
    }

    static Stream<Interval> shiftIntervals(ShiftedInterval source, List<ShiftedInterval> shifts) {
        List<ShiftedInterval> result = new ArrayList<>();
        List<ShiftedInterval> inputs = new ArrayList<>();
        inputs.add(source);
        for (ShiftedInterval shift : shifts) {
            Map<Boolean, List<ShiftedInterval>> map = shiftList(inputs.stream(), shift)
                    .collect(Collectors.partitioningBy(shiftedInterval -> shiftedInterval.shift == 0));
            inputs = map.get(true);
            result.addAll(map.get(false));
        }
        result.addAll(inputs);
        return result.stream().map(ShiftedInterval::interval);
    }

    static List<ShiftedInterval> fromLine(Stream<String> lines) {
        return lines
                .filter(line -> !line.isBlank())
                .filter(line -> !line.endsWith(":"))
                .map(Day5::shiftedIntervalOf).toList();
    }

    Function<String, Stream<Interval>> lineToSingleIntervalProvider = (line) -> lineToSingleIntervals(line.split(":")[1].strip());

    Function<String, Stream<Interval>> lineToIntervalsProvider = (line) -> lineToIntervals(line.split(":")[1].strip());

    static long findMinIntervals(String input, Function<String, Stream<Interval>> lineToInterval) {
        String[] sections = input.split("\n\n");
        List<Interval> result = new ArrayList<>();
        List<Interval> intervals = lineToInterval.apply(sections[0]).toList();
        for (Interval interval : intervals) {
            List<Interval> inputIntervals = Collections.singletonList(interval);
            for (int i = 1; i < sections.length; i++) {
                List<ShiftedInterval> shifts = fromLine(sections[i].lines());
                inputIntervals = inputIntervals.stream()
                        .flatMap(inputInterval -> shiftIntervals(ShiftedInterval.of(inputInterval, 0), shifts))
                        .toList();
            }
            result.addAll(inputIntervals.stream().distinct().toList());
        }

        return result.stream().mapToLong(interval -> interval.start).min().orElseThrow();
    }

    record Part1() implements Day5, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 5;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.ONE;
        }

        @Override
        public Long rightAnswer() {
            return 579439039L;
        }

        @Override
        public Long test() {
            var input = AdventOfCode2023.readFileOfResource("day5.txt");
            return findMinIntervals(input, lineToSingleIntervalProvider);
        }
    }

    record Part2() implements Day5, AdventOfCode2023.SolutionOfDay<Long> {

        @Override
        public int day() {
            return 5;
        }

        @Override
        public AdventOfCode2023.DayPart part() {
            return AdventOfCode2023.DayPart.TWO;
        }

        @Override
        public Long rightAnswer() {
            return 7873084L;
        }

        @Override
        public Long test() {
            var input = AdventOfCode2023.readFileOfResource("day5.txt");
            return findMinIntervals(input, lineToIntervalsProvider);
        }
    }


    static Day5.Part1 findPart1() {
        return new Day5.Part1();
    }

    static Day5.Part2 findPart2() {
        return new Day5.Part2();
    }
    
}
