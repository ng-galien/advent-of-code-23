package com.github.nggalien.advent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface Day5 {
    interface Thing {
        String name();

        long id();
        record Seed(long id) implements Thing {
            @Override
            public String name() {
                return "seed";
            }
        }

        record Soil(long id) implements Thing {
            @Override
            public String name() {
                return "soil";
            }
        }

        record Fertilizer(long id) implements Thing {
            @Override
            public String name() {
                return "fertilizer";
            }
        }

        record Water(long id) implements Thing {
            @Override
            public String name() {
                return "water";
            }
        }

        record Light(long id) implements Thing {
            @Override
            public String name() {
                return "light";
            }
        }

        record Temperature(long id) implements Thing {
            @Override
            public String name() {
                return "temperature";
            }
        }

        record Humidity(long id) implements Thing {
            @Override
            public String name() {
                return "humidity";
            }
        }

        record Location(long id) implements Thing {
            @Override
            public String name() {
                return "location";
            }
        }
    }

    static Map<String, Class<? extends Thing>> thingClasses() {
        return Map.of(
                "seed", Thing.Seed.class,
                "soil", Thing.Soil.class,
                "fertilizer", Thing.Fertilizer.class,
                "water", Thing.Water.class,
                "light", Thing.Light.class,
                "temperature", Thing.Temperature.class,
                "humidity", Thing.Humidity.class,
                "location", Thing.Location.class
        );
    }

    @FunctionalInterface
    interface IdMapper {
        Long mapId(Long fromId);
    }

    record IdMapperRecord(Class<? extends Thing> fromClass, Class<? extends Thing> toClass, long fromId, long toId, int count)
            implements IdMapper {

        public IdMapperRecord {
            if (fromClass == null || toClass == null) {
                throw new IllegalArgumentException("from and to must not be null");
            }
            if(fromId < 0 || toId < 0 || count < 0) {
                throw new IllegalArgumentException("idFrom, idTo and count must be positive");
            }
        }

        @Override
        public Long mapId(Long id) {
            return id-fromId + toId;
        }
    }

    static Optional<? extends IdMapper> findIdMapper(Thing candidate, Class<? extends Thing> to, Collection<IdMapperRecord> idMappers) {
        return idMappers.stream()
                .filter(idMapper -> idMapper.fromClass.equals(candidate.getClass()) && idMapper.toClass.equals(to))
                .filter(idMapper -> candidate.id() >= idMapper.fromId && candidate.id() < idMapper.fromId + idMapper.count)
                .findFirst();
    }

    static Collection<IdMapperRecord> parseIdMappers(Collection<String> lines) {
        Collection<IdMapperRecord> idMappers = new ArrayList<>();
        Class<? extends Thing> current = null;
        Class<? extends Thing> next = null;
        for (String line : lines) {
            if(line.isBlank()) {
                current = null;
                next = null;
                continue;
            }
            if (line.endsWith("map:")) {
               String[] parts = line.substring(0, line.length() - 4).split("-to-");
               current = thingClasses().get(parts[0].trim());
               next = thingClasses().get(parts[1].trim());
            } else {
                long[] ids = DayUtils.parseLongArray(line);
                if (current != null && next != null && ids.length == 3) {
                    idMappers.add(new IdMapperRecord(current, next, ids[1], ids[0], (int) ids[2]));
                }
            }
        }
        return idMappers;
    }

    static Stream<Thing.Seed> parseSeedList(String seedLine, Collection<IdMapperRecord> mapperRecords) {
        String [] parts = seedLine.split(":");
        if(parts.length == 2) {
            return DayUtils.parseLongStream(parts[1])
                    .mapToObj(Thing.Seed::new);
        } else {
            return Stream.empty();
        }
    }

    static Stream<Thing.Seed>parseSeedRange(String seedLine, Collection<IdMapperRecord> mapperRecords) {
        //TODO for part 2
        //Here we need to compute the global range
        //Then we need to compute for each mapped record the relevant ids in the range
        //At the end we only takes the relevant values that can be converted and the min of the global range
        String [] parts = seedLine.split(":");
        LongStream seedStream = LongStream.empty();
        if(parts.length == 2) {
            long[] values = DayUtils.parseLongArray(parts[1]);
            if(values.length % 2 == 0) {
                for (int i = 0; i < values.length; i+=2) {
                    long begin = values[i];
                    long end = values[i] + values[i+1];
                    seedStream = LongStream.concat(seedStream, LongStream.rangeClosed(begin, end));
                }
            }
        }
        return seedStream.distinct().mapToObj(Thing.Seed::new);
    }

    static Map<Class<? extends Thing>, Class<? extends Thing>> thingOrder() {
        return Map.of(
                Thing.Seed.class, Thing.Soil.class,
                Thing.Soil.class, Thing.Fertilizer.class,
                Thing.Fertilizer.class, Thing.Water.class,
                Thing.Water.class, Thing.Light.class,
                Thing.Light.class, Thing.Temperature.class,
                Thing.Temperature.class, Thing.Humidity.class,
                Thing.Humidity.class, Thing.Location.class
        );
    }

    static Thing newThing(Class<? extends Thing> thingClass, long id) {
        try {
            return thingClass.getConstructor(long.class).newInstance(id);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static Thing next(Thing thing, Collection<IdMapperRecord> idMappers) {
        Optional<Class<? extends Thing>> mayBeNext = Optional.ofNullable(thingOrder().get(thing.getClass()));
        if (mayBeNext.isPresent()) {
            Class<? extends Thing> nextThing = mayBeNext.get();
            Function<Long, Long> toId = findIdMapper(thing, nextThing, idMappers)
                    .map(idMapper -> (Function<Long, Long>) idMapper::mapId)
                    .orElse(Function.identity());
            return newThing(nextThing, toId.apply(thing.id()));
        } else {
            return thing;
        }
    }

    static Thing nextAtTheEnd(Thing thing, Collection<IdMapperRecord> idMappers) {
        Thing next = next(thing, idMappers);
        return next.equals(thing) ? thing : nextAtTheEnd(next, idMappers);
    }

    static Stream<Thing> getThingsAtTheEnd(String input, BiFunction<String, Collection<IdMapperRecord>, Stream<Thing.Seed>> seedProvider) {
        List<String> lines = Arrays.stream(input.split("\n"))
                .map(String::strip)
                .toList();
        Collection<IdMapperRecord> idMappers = parseIdMappers(lines.subList(1, lines.size()));
        Stream<Thing.Seed> seedStream = seedProvider.apply(lines.get(0), idMappers);
        return seedStream
                .parallel()
                .map(seed -> nextAtTheEnd(seed, idMappers));
    }

    static long getMinThingIdAtTheEndForSeedList(String input) {
        return getThingsAtTheEnd(input, Day5::parseSeedList)
                .mapToLong(Thing::id)
                .min()
                .orElse(0);
    }
    static long getMinThingIdAtTheEndForSeedRange(String input) {
        return getThingsAtTheEnd(input, Day5::parseSeedRange)
                .mapToLong(Thing::id)
                .min()
                .orElse(0);
    }
}
