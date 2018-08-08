package com.ishoal.core.persistence.adapter;

import com.ishoal.core.persistence.entity.VendorEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class VendorEntityAdapterTest {

    private final VendorEntityAdapter adapter = new VendorEntityAdapter();

    @Test
    public void returnsNullWhenGivenNull() {
        assertThat(adapter.adapt((VendorEntity)null), is(nullValue()));
    }
}