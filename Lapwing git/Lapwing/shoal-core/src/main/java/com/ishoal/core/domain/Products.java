package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static com.ishoal.common.util.IterableUtils.collector;

public class Products implements Streamable<Product> {

    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = Collections.unmodifiableList(new ArrayList<>(products));
    }
    
    public static Products over(List<Product> products) {
        return new Products(products);
    }
    
    public static Products over(Product... products) {
        return new Products(Arrays.asList(products));
    }

    public Products add(Product order) {
        List<Product> extendedProducts = new ArrayList<>(this.products);
        extendedProducts.add(order);
        return over(extendedProducts);
    }

    @Override
    public Iterator<Product> iterator() {
        return this.products.iterator();
    }

    public int size() {
        return this.products.size();
    }

    public Products filter(Predicate<Product> predicate) {
        return stream()
            .filter(predicate)
            .collect(collector(Products::over));
    }
}