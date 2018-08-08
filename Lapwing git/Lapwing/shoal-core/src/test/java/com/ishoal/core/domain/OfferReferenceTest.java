package com.ishoal.core.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class OfferReferenceTest {

    @Test
    public void offerReferenceShouldBe36CharactersOrLessInLength() {
        assertThat(OfferReference.create().asString().length(), lessThanOrEqualTo(36));
    }

    @Test
    public void shouldBeEqualIfSameInstance() {
        OfferReference reference = OfferReference.create();
        assertThat(reference, is(equalTo(reference)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        OfferReference reference1 = OfferReference.create();
        OfferReference reference2 = OfferReference.from(reference1.asString());
        assertThat(reference1, is(equalTo(reference2)));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OfferReference.create(), is(not(equalTo(OfferReference.create()))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OfferReference.create(), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OfferReference.create(), is(not(equalTo("hello"))));
    }

    @Test
    public void currentTime() {
        long current = DateTime.now().getMillis();
        System.out.println(current);
        long future = DateTime.now().plusYears(20).getMillis();
        System.out.println(future);
    }
}