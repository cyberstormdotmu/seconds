package com.ishoal.core.domain;

import com.ishoal.common.Result;
import org.junit.Test;

import static com.ishoal.core.domain.Order.anOrder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OrderTest {

    @Test
    public void referenceShouldBeCreatedForNewObjectIfNoneSpecified() {
        assertThat(anOrder().build().getReference(), is(notNullValue()));
    }

    @Test
    public void statusShouldDefaultToRequestedForNewObjectIfNoneSpecified() {
        assertThat(anOrder().build().getStatus(), is(OrderStatus.PROCESSING));
    }

    @Test
    public void orderTotalShouldBeCalculatedFromTheOrderLine() {
        Order order = anOrder().line(orderLine()).line(orderLineWithCredit()).build();
        assertThat(order.getTotal(), is(expectedOrderTotal()));
    }

    @Test
    public void creditTotalShouldBeCalculatedFromTheOrderLineCredits() {
        Order order = anOrder().line(orderLineWithCredit()).line(orderLineWithCredit()).build();
        assertThat(order.getCreditTotal(), is(expectedCreditTotal()));
    }

    @Test
    public void orderCanBeConfirmedWhenRequested() {
        Order order = anOrder().status(OrderStatus.PROCESSING).build();

        Result result = order.confirm(30);
        assertTrue(result.isSuccess());
    }

    @Test
    public void orderInitiallyHasNoInvoiceDate() {
        Order order = anOrder().status(OrderStatus.PROCESSING).build();

        assertThat(order.getInvoiceDate(), is(nullValue()));
    }

    @Test
    public void orderGetsInvoiceDateWhenConfirmed() {
        Order order = anOrder().status(OrderStatus.PROCESSING).build();

        order.confirm(30);
        assertThat(order.getInvoiceDate(), is(notNullValue()));
    }

    @Test
    public void orderCannotBeConfirmedWhenCancelled() {
        Order order = anOrder().status(OrderStatus.CANCELLED).build();

        Result result = order.confirm(30);
        assertFalse(result.isSuccess());
    }

    @Test
    public void orderCanBeCancelledWhenRequested() {
        Order order = anOrder().status(OrderStatus.PROCESSING).build();

        Result result = order.cancel();
        assertTrue(result.isSuccess());
    }
    @Test
    public void orderCannotBeCancelledWhenConfirmed() {
        Order order = anOrder().status(OrderStatus.CONFIRMED).build();

        Result result = order.cancel();
        assertFalse(result.isSuccess());
    }

    private OrderLine orderLineWithCredit() {
        return OrderTestData.orderLineWithCredit().build();
    }

    private OrderLine orderLine() {
        return OrderTestData.orderLine().build();
    }

    private TaxableAmount expectedOrderTotal() {
        return orderLine().getAmount().add(orderLineWithCredit().getAmount());
    }

    private TaxableAmount expectedCreditTotal() {
        return orderLineWithCredit().getCreditTotal().add(orderLineWithCredit().getCreditTotal());
    }

}