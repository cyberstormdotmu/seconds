package com.ishoal.core.persistence.adapter;

import com.ishoal.core.persistence.entity.ProductVatRateEntity;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ProductVatRateEntityAdapterTest {

    private ProductVatRateEntityAdapter entityAdapter = new ProductVatRateEntityAdapter();

    @Test
    public void shouldReturnEmptyVATRatesWhenAdaptingNull() {

        assertThat(entityAdapter.adapt((List<ProductVatRateEntity>)null).iterator().hasNext(), is(false));
    }
}