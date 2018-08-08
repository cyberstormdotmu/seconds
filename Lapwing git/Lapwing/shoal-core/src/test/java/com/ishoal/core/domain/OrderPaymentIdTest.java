package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OrderPaymentIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(OrderPaymentId.from(null), is(OrderPaymentId.emptyOrderPaymentId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderPaymentId id = OrderPaymentId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(OrderPaymentId.from(456L), is(equalTo(OrderPaymentId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OrderPaymentId.from(457L), is(not(equalTo(OrderPaymentId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OrderPaymentId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OrderPaymentId.from(456L), is(not(equalTo("hello"))));
    }    
}