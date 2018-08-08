package com.ishoal.core.persistence.entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class CategoryEntityTest {

    public static final String LAPTOPS = "Laptops";
    public static final String PRINTERS = "Printers";

    @Test
    public void shouldBeEqualIfSameInstance() {
        CategoryEntity entity = entity(LAPTOPS);
        assertThat(entity, is(equalTo(entity)));
    }

    @Test
    public void shouldBeEqualIfNamesSame() {
        assertThat(entity(LAPTOPS), is(equalTo(entity(LAPTOPS))));
    }

    @Test
    public void shouldNotBeEqualIfNamesDiffer() {
        assertThat(entity(LAPTOPS), is(not(equalTo(entity(PRINTERS)))));
    }

    @Test
    public void shouldNotBeEqualWhenToNull() {
        assertThat(entity(LAPTOPS), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualWhenToOtherObjectType() {
        assertThat(entity(LAPTOPS), is(not(equalTo(BigDecimal.ONE))));
    }

    @Test
    public void hashCodeIsEqualWhenNamesSame() {
        assertThat(entity(LAPTOPS).hashCode(), is(equalTo(entity(LAPTOPS).hashCode())));
    }

    private CategoryEntity entity(String name) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(name);
        return entity;
    }
}