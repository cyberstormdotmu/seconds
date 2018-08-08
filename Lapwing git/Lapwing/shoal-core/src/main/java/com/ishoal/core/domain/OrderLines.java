package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OrderLines implements Streamable<OrderLine> {

    private final List<OrderLine> lines;

    private OrderLines(List<OrderLine> lines) {
        this.lines = Collections.unmodifiableList(new ArrayList<>(lines));
    }
    
    public static OrderLines over(List<OrderLine> lines) {
        return new OrderLines(lines);
    }

    @Override
    public Iterator<OrderLine> iterator() {
        return this.lines.iterator();
    }

    public int size() {
        return this.lines.size();
    }

    public List<OrderLine> list() {
        return Collections.unmodifiableList(lines);
    }

    public TaxableAmount getTotal() {
        return TaxableAmount.sum(stream(), OrderLine::getAmount);
    }

    public TaxableAmount getCreditTotal() {
        return TaxableAmount.sum(stream(), OrderLine::getCreditTotal);
    }

    public TaxableAmount getAvailableCreditBalance() {
        return TaxableAmount.sum(stream(), OrderLine::getAvailableCreditBalance);
    }
}