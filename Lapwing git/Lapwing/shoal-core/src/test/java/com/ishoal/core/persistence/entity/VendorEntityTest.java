package com.ishoal.core.persistence.entity;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class VendorEntityTest {

    @Test
    public void shouldBeEqualIfSameInstance() {
        VendorEntity entity = entity(1);
        assertThat(entity, is(equalTo(entity)));
    }

    @Test
    public void shouldBeEqualIfIdsSame() {
        assertThat(entity(1), is(equalTo(entity(1))));
    }

    @Test
    public void shouldNotBeEqualIfIdsDiffer() {
        assertThat(entity(1), is(not(equalTo(entity(2)))));
    }

    @Test
    public void shouldNotBeEqualWhenToNull() {
        assertThat(entity(1), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualWhenToOtherObjectType() {
        assertThat(entity(1), is(not(equalTo("hello"))));
    }

    @Test
    public void hashCodeIsEqualWhenIdsSame() {
        assertThat(entity(1).hashCode(), is(equalTo(entity(1).hashCode())));
    }

    private VendorEntity entity(long id) {
        VendorEntity entity = new VendorEntity();
        entity.setId(id);
        return entity;
    }
}