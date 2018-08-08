package com.ishoal.core.orders;

import com.ishoal.common.util.Streamable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NewOrderRequestOrderLines implements Streamable<NewOrderRequestOrderLine> {

    private final List<NewOrderRequestOrderLine> lines;

    private NewOrderRequestOrderLines(List<NewOrderRequestOrderLine> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }
    
    public static NewOrderRequestOrderLines over(List<NewOrderRequestOrderLine> lines) {
        return new NewOrderRequestOrderLines(lines);
    }

    @Override
    public Iterator<NewOrderRequestOrderLine> iterator() {
        return this.lines.iterator();
    }

    public int size() {
        return this.lines.size();
    }
}