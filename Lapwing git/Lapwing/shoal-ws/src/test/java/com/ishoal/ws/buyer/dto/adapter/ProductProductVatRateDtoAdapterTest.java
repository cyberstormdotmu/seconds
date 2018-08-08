package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.VatRate;
import com.ishoal.ws.buyer.dto.ProductVatRateDto;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


public class ProductProductVatRateDtoAdapterTest {

    private ProductVatRateDtoAdapter productVatRateDtoAdapter = new ProductVatRateDtoAdapter();

    @Test
    public void shouldReturnNullWhenNullDtoAdapted() {

        ProductVatRate rate = productVatRateDtoAdapter.adapt((ProductVatRateDto) null);
        assertThat(rate, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenNullObjectAdapted() {
        ProductVatRateDto rate = productVatRateDtoAdapter.adapt((VatRate) null);
        assertThat(rate, is(nullValue()));
    }
}