package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class BuyerAppliedCreditIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(BuyerAppliedCreditId.from(null), is(BuyerAppliedCreditId.EMPTY_BUYER_APPLIED_CREDIT_ID));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        BuyerAppliedCreditId id = BuyerAppliedCreditId.from(345L);
        assertThat(id, is(equalTo(id)));
    }
    
    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(BuyerAppliedCreditId.from(456L), is(equalTo(BuyerAppliedCreditId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(BuyerCreditId.from(457L), is(not(equalTo(BuyerAppliedCreditId.from(783L)))));
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