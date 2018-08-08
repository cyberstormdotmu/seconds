package com.ishoal.core.domain;

import com.ishoal.common.domain.OrderReference;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class OrderReferenceTest {

    @Test
    public void orderReferenceShouldBe36CharactersOrLessInLength() {
        assertThat(OrderReference.create().asString().length(), lessThanOrEqualTo(36));
    }

    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderReference reference = OrderReference.create();
        assertThat(reference, is(equalTo(reference)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        OrderReference reference1 = OrderReference.create();
        OrderReference reference2 = OrderReference.from(reference1.asString());
        assertThat(reference1, is(equalTo(reference2)));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OrderReference.create(), is(not(equalTo(OrderReference.create()))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OrderReference.create(), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OrderReference.create(), is(not(equalTo("hello"))));
    }

    @Test
    public void currentTime() {
        long current = DateTime.now().getMillis();
        System.out.println(current);
        long future = DateTime.now().plusYears(20).getMillis();
        System.out.println(future);
    }
}