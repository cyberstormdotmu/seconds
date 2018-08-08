package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductVatRates implements Streamable<ProductVatRate> {

    private final List<ProductVatRate> productVatRates;

    private ProductVatRates(List<ProductVatRate> productVatRates) {
        this.productVatRates = Collections.unmodifiableList(productVatRates);
    }

    public static ProductVatRates over(List<ProductVatRate> productVatRates) {
        return new ProductVatRates(productVatRates);
    }

    public static ProductVatRates over(ProductVatRate productVatRates) {
        if (productVatRates == null) {
            return emptyVatRates();
        }
        return new ProductVatRates(Collections.singletonList(productVatRates));
    }

    @Override
    public Iterator<ProductVatRate> iterator() {
        return this.productVatRates.iterator();
    }

    public int size() {
        return this.productVatRates.size();
    }

    public VatRate current() {
        DateTime now = DateTime.now();

        for(ProductVatRate vatRate : productVatRates) {
            if(!now.isBefore(vatRate.getStartDateTime()) && !now.isAfter(vatRate.getEndDateTime())) {
                return vatRate.getVatRate();
            }
        }
        return null;
    }

    public static ProductVatRates emptyVatRates() {

        return new ProductVatRates(Collections.emptyList());
    }

    public boolean isEmpty() {

        return productVatRates.isEmpty();
    }
}
