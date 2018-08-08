package com.ishoal.core.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class OfferIdTest {

    @Test
    public void shouldConstructNullIdFromNull() {
        assertThat(OfferId.from(null), is(nullValue()));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        OfferId id = OfferId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(OfferId.from(456L), is(equalTo(OfferId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OfferId.from(457L), is(not(equalTo(OfferId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OfferId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OfferId.from(456L), is(not(equalTo("hello"))));
    }    
}