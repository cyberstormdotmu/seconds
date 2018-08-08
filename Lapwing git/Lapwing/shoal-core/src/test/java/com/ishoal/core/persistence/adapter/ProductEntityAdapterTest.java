package com.ishoal.core.persistence.adapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.ishoal.core.persistence.entity.ProductEntity;
import org.junit.Before;
import org.junit.Test;

public class ProductEntityAdapterTest {

    private ProductEntityAdapter adapter;

    @Before
    public void before() {
        this.adapter = new ProductEntityAdapter();
    }

    @Test
    public void adaptNullShouldReturnNull() {
        assertThat(this.adapter.adapt((ProductEntity)null), is(nullValue()));
    }
}