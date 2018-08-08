package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductSpecs implements Streamable<ProductSpec> {

    private final List<ProductSpec> productSpecs;

    private ProductSpecs(Builder builder) {
        this.productSpecs = new ArrayList<>(builder.productSpecs);
    }

    private ProductSpecs(List<ProductSpec> productSpecs) {
        this.productSpecs = Collections.unmodifiableList(productSpecs);
    }

    public static Builder someProductSpecs() {
        return new Builder();
    }

    public static ProductSpecs emptyProductSpecs() {
        return new Builder().build();
    }

    public static ProductSpecs over(List<ProductSpec> productSpecs) {
        return new ProductSpecs(productSpecs);
    }

    @Override
    public Iterator<ProductSpec> iterator() {
        return productSpecs.iterator();
    }

    public boolean isEmpty() {

        return productSpecs.isEmpty();
    }

    public static class Builder {
        private List<ProductSpec> productSpecs = new ArrayList<>();

        private Builder() {
        }

        public Builder productSpec(ProductSpec productSpec) {
            this.productSpecs.add(productSpec);
            return this;
        }

        public ProductSpecs build() {
            return new ProductSpecs(this);
        }
    }

    public int size() {
        return productSpecs.size();
    }
}
