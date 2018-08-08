package com.ishoal.ws.admin.dto.adapter;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductTestData;
import com.ishoal.core.domain.Products;
import com.ishoal.ws.admin.dto.OfferListingDto;

public class OfferListingDtoAdapterTest {

    private OfferListingDtoAdapter adapter;

    @Before
    public void before() {
        this.adapter = new OfferListingDtoAdapter();
    }

    @Test
    public void shouldAdaptCorrectNumberOfProducts() {
        assertThat(adapt(randomProduct(), randomProduct()), hasSize(2));
    }
    
    private List<OfferListingDto> adapt(Product... products) {
        return this.adapter.adapt(Products.over(products));
    }

    private Product randomProduct() {
        return ProductTestData.productBuilder(RandomStringUtils.randomAlphanumeric(10)).build();
    }
}