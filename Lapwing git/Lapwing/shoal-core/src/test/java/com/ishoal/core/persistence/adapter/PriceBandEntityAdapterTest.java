package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.persistence.entity.PriceBandEntity;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PriceBandEntityAdapterTest {

    private PriceBandEntityAdapter adapter = new PriceBandEntityAdapter();

    @Before
    public void before() {
        this.adapter = new PriceBandEntityAdapter();
    }

    @Test
    public void adaptNullEntityShouldReturnNull() {
        assertThat(adapter.adapt((PriceBandEntity)null), is(nullValue()));
    }

    @Test
    public void adaptNullObjectShouldReturnNull() {
        assertThat(adapter.adapt((PriceBand)null), is(nullValue()));
    }
}