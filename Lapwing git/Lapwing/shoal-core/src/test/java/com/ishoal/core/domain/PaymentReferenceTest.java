package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class PaymentReferenceTest {

    @Test
    public void paymentReferenceShouldBe36CharactersOrLessInLength() {
        assertThat(PaymentReference.create().asString().length(), lessThanOrEqualTo(36));
    }

    @Test
    public void shouldBeEqualIfSameInstance() {
        PaymentReference reference = PaymentReference.create();
        assertThat(reference, is(equalTo(reference)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        PaymentReference reference1 = PaymentReference.create();
        PaymentReference reference2 = PaymentReference.from(reference1.asString());
        assertThat(reference1, is(equalTo(reference2)));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(PaymentReference.create(), is(not(equalTo(PaymentReference.create()))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(PaymentReference.create(), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(PaymentReference.create(), is(not(equalTo("hello"))));
    }
}