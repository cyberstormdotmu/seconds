package com.ishoal.core.domain;

import org.junit.Test;

import static com.ishoal.core.domain.OrderStatus.CANCELLED;
import static com.ishoal.core.domain.OrderStatus.CONFIRMED;
import static com.ishoal.core.domain.OrderStatus.PROCESSING;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OrderStatusTest {

    @Test
    public void shouldBeAbleToTransitionFromRequestedToConfirmed() {
        assertTrue(PROCESSING.canTransitionTo(CONFIRMED));
    }

    @Test
    public void shouldBeAbleToTransitionFromRequestedToCancelled() {
        assertTrue(PROCESSING.canTransitionTo(CANCELLED));
    }

    @Test
    public void shouldNotBeAbleToTransitionFromConfirmedToRequested() {
        assertFalse(CONFIRMED.canTransitionTo(PROCESSING));
    }

    @Test
    public void shouldNotBeAbleToTransitionFromConfirmedToCancelled() {
        assertFalse(CONFIRMED.canTransitionTo(CANCELLED));
    }

    @Test
    public void shouldNotBeAbleToTransitionFromCancelledToRequested() {
        assertFalse(CANCELLED.canTransitionTo(PROCESSING));
    }

    @Test
    public void shouldNotBeAbleToTransitionFromCancelledToConfirmed() {
        assertFalse(CANCELLED.canTransitionTo(CONFIRMED));
    }

    @Test
    public void fromDisplayNameReturnsOrderStatusWhenValid() {
        assertThat(OrderStatus.fromDisplayName(OrderStatus.CONFIRMED.getDisplayName()), is(OrderStatus.CONFIRMED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromDisplayNameThrowsWhenInvalid() {
        OrderStatus.fromDisplayName("Not a valid credit type");
    }

}