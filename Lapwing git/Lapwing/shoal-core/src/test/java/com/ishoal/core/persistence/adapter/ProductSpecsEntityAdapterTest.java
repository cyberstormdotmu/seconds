package com.ishoal.core.persistence.adapter;

import com.ishoal.core.persistence.entity.ProductEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ProductSpecsEntityAdapterTest {
    private ProductSpecsEntityAdapter entityAdapter = new ProductSpecsEntityAdapter();

    @Test
    public void shouldReturnEmptySpecsWhenAdaptingNullEntity() {

        assertThat(entityAdapter.adapt(null).iterator().hasNext(), is(false));
    }

    @Test
    public void shouldReturnEmptyListWhenAdaptingNullSpecs() {

        assertThat(entityAdapter.adapt(null, new ProductEntity()).iterator().hasNext(), is(false));
    }
}