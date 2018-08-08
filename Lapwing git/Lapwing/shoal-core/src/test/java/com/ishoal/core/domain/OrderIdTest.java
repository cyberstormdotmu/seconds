package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OrderIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(OrderId.from(null), is(OrderId.emptyOrderId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderId id = OrderId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(OrderId.from(456L), is(equalTo(OrderId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OrderId.from(457L), is(not(equalTo(OrderId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OrderId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OrderId.from(456L), is(not(equalTo("hello"))));
    }    
}