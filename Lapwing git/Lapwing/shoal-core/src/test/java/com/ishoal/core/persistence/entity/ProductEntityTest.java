package com.ishoal.core.persistence.entity;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ProductEntityTest {
    @Test
    public void shouldBeEqualIfSameInstance() {
        ProductEntity entity = entity("123");
        assertThat(entity, is(equalTo(entity)));
    }

    @Test
    public void shouldBeEqualIfCodesSame() {
        assertThat(entity("123"), is(equalTo(entity("123"))));
    }

    @Test
    public void shouldNotBeEqualIfCodesDiffer() {
        assertThat(entity("123"), is(not(equalTo(entity("456")))));
    }

    @Test
    public void shouldNotBeEqualWhenToNull() {
        assertThat(entity("123"), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualWhenToOtherObjectType() {
        assertThat(entity("123"), is(not(equalTo("hello"))));
    }

    @Test
    public void hashCodeIsEqualWhenCodesSame() {
        assertThat(entity("123").hashCode(), is(equalTo(entity("123").hashCode())));
    }

    private ProductEntity entity(String code) {
        ProductEntity entity = new ProductEntity();
        entity.setCode(code);
        return entity;
    }
}