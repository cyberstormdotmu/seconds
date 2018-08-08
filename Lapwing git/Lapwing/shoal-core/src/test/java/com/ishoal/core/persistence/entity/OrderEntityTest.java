package com.ishoal.core.persistence.entity;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class OrderEntityTest {
    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderEntity entity = entity("123");
        assertThat(entity, is(equalTo(entity)));
    }

    @Test
    public void shouldBeEqualIfReferencesSame() {
        assertThat(entity("123"), is(equalTo(entity("123"))));
    }

    @Test
    public void shouldNotBeEqualIfReferencesDiffer() {
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
    public void hashCodeIsEqualWhenIdsSame() {
        assertThat(entity("123").hashCode(), is(equalTo(entity("123").hashCode())));
    }

    private OrderEntity entity(String reference) {
        OrderEntity entity = new OrderEntity();
        entity.setReference(reference);
        return entity;
    }
}