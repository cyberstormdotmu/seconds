package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.ProductImage;
import com.ishoal.core.domain.ProductImages;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


public class ProductImageDtoAdapterTest {

    private ProductImageDtoAdapter dtoAdapter = new ProductImageDtoAdapter();

    @Test
    public void shouldReturnEmptyListIfNullProductImagesAdapted() {

        assertThat(dtoAdapter.adapt((ProductImages)null), is(Collections.EMPTY_LIST));
    }

    @Test
    public void shouldReturnNullIfNullProductImageAdapted() {
        assertThat(dtoAdapter.adapt((ProductImage)null), is(nullValue()));
    }
}