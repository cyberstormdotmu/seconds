package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OrderLineIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(OrderLineId.from(null), is(OrderLineId.emptyOrderLineId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderLineId id = OrderLineId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(OrderLineId.from(456L), is(equalTo(OrderLineId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OrderLineId.from(457L), is(not(equalTo(OrderLineId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OrderLineId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OrderLineId.from(456L), is(not(equalTo("hello"))));
    }    
}