package com.ishoal.ws.admin.dto.adapter;

import static com.ishoal.core.domain.BuyerAppliedCreditTestData.randomBuyerAppliedCredit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdminAppliedCreditDtoAdapterTest {

    private AdminAppliedCreditDtoAdapter adapter;
    
    @Before
    public void before() {
        this.adapter = new AdminAppliedCreditDtoAdapter();
    }
    
    @Test
    public void shouldAdaptToNotNull() {
        assertThat(this.adapter.adapt(randomBuyerAppliedCredit().build()), is(notNullValue()));
    }
}