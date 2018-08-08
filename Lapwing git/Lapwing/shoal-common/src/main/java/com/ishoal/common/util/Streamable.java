package com.ishoal.common.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Streamable<T> extends Iterable<T> {
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
