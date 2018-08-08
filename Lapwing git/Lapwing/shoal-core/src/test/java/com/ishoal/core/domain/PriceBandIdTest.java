package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PriceBandIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(PriceBandId.from(null), is(PriceBandId.emptyPriceBandId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        PriceBandId id = PriceBandId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(PriceBandId.from(456L), is(equalTo(PriceBandId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(PriceBandId.from(457L), is(not(equalTo(PriceBandId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(PriceBandId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(PriceBandId.from(456L), is(not(equalTo("hello"))));
    }    
}