package com.ishoal.core.persistence.adapter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProductImagesEntityAdapterTest {
    private ProductImagesEntityAdapter entityAdapter = new ProductImagesEntityAdapter();

    @Test
    public void shouldReturnEmptyImagesWhenAdaptingNull() {

        assertThat(entityAdapter.adapt(null).iterator().hasNext(), is(false));
    }
}