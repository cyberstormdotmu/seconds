package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.persistence.entity.PriceBandEntity;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


public class PriceBandsEntityAdapterTest {

    private PriceBandsEntityAdapter adapter = new PriceBandsEntityAdapter();

    @Test
    public void shouldAdaptANullPriceBand() {

        PriceBands priceBands = adapter.adapt((List<PriceBandEntity>) null);

        assertThat(priceBands, is(notNullValue()));
    }
}