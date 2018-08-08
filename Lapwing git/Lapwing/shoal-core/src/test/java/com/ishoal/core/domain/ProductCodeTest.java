package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ProductCodeTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(ProductCode.from(null), is(ProductCode.EMPTY_PRODUCT_CODE));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        ProductCode id = ProductCode.from("LAPTOP");
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(ProductCode.from("LAPTOP"), is(equalTo(ProductCode.from("LAPTOP"))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(ProductCode.from("LAPTOP"), is(not(equalTo(ProductCode.from("PHONE")))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(ProductCode.from("LAPTOP"), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(ProductCode.from("LAPTOP"), is(not(equalTo("hello"))));
    }    
}