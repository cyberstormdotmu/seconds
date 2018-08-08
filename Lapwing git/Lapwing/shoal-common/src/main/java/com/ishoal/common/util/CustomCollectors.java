package com.ishoal.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public interface CustomCollectors {

    static <T> Collector<List<T>, List<T>, List<T>> toMergedList() {
        return Collector.of(ArrayList::new, List::addAll, CustomCollectors::combineLists);
    }

    static <T> List<T> combineLists(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}
