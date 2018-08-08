package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class BuyerCreditIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(BuyerCreditId.from(null), is(BuyerCreditId.EMPTY_BUYER_CREDIT_ID));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        BuyerCreditId id = BuyerCreditId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(BuyerCreditId.from(456L), is(equalTo(BuyerCreditId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(BuyerCreditId.from(457L), is(not(equalTo(BuyerCreditId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(BuyerCreditId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(BuyerCreditId.from(456L), is(not(equalTo("hello"))));
    }    
}