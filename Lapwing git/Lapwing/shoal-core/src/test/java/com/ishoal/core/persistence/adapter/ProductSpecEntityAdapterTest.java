package com.ishoal.core.persistence.adapter;

import com.ishoal.core.persistence.entity.ProductEntity;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ProductSpecEntityAdapterTest {

    private ProductSpecEntityAdapter adapter;

    @Before
    public void before() {
        this.adapter = new ProductSpecEntityAdapter();
    }

    @Test
    public void adaptNullEntityShouldReturnNullObject() {
        assertThat(this.adapter.adapt(null), is(nullValue()));
    }

    @Test
    public void adaptNullObjectShouldReturnNUllEntity() {

        assertThat(this.adapter.adapt(null, new ProductEntity()), is(nullValue()));
    }
}