package com.ishoal.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableUtils {
    private IterableUtils() {

    }
    public static <T, U> List<U> mapToList(Iterable<T> iterable, Function<T, U> adapter) {
        return stream(iterable).map(it -> adapter.apply(it)).filter(noNulls()).collect(Collectors.toList());
    }

    private static <U> Predicate<U> noNulls() {

        return u -> u != null;
    }

    public static <T, U, V> V mapToCollection(Iterable<T> iterable, Function<T, U> adapter, Function<List<U>, V> finisher) {
        return stream(iterable).map(it -> adapter.apply(it)).collect(collector(finisher));
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T, U> Collector<T, List<T>, U> collector(Function<List<T>, U> finisher) {
        return Collector.of(ArrayList::new, List::add, CustomCollectors::combineLists, finisher);
    }
}
