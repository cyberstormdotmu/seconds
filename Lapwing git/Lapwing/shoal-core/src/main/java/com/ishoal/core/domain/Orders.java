package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Orders implements Streamable<Order> {

    private final List<Order> orders;

    private Orders(Collection<Order> orders) {
        this.orders = Collections.unmodifiableList(new ArrayList<>(orders));
    }
    
    public static Orders over(Collection<Order> orders) {
        return new Orders(orders);
    }
    
    public static Orders over(Order... orders) {
        return new Orders(Arrays.asList(orders));
    }

    public Orders add(Order order) {
        List<Order> extendedOrders = new ArrayList<>(this.orders);
        extendedOrders.add(order);
        return over(extendedOrders);
    }

    public Orders orderedByDateDescending() {
        List<Order> sorted = new ArrayList<>(orders);
        sorted.sort(Comparator.comparing((Order t) -> t.getCreated()).reversed());
        return new Orders(sorted);
    }

    public Orders combineWith(Orders other) {
        Set<Order> combined = new HashSet<>(this.orders);
        combined.addAll(other.orders);
        return over(combined);
    }

    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    public int size() {
        return this.orders.size();
    }
}