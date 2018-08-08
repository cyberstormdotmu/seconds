package com.ishoal.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderTestData.orderLine;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OrderLineTest {

    @Test
    public void shouldBeEqualIfSameInstance() {
        OrderLine orderLine = orderLineWithId(67L);
        assertThat(orderLine, is(equalTo(orderLine)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(orderLineWithId(23L), is(equalTo(orderLineWithId(23L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(orderLineWithId(45L), is(not(equalTo(orderLineWithId(64L)))));
    }
    
    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(orderLineWithId(87L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualToProductWithNullCode() {
        assertThat(orderLineWithId(877L), is(not(equalTo(orderLineWithId(null)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(orderLineWithId(4557L), is(not(equalTo("hello"))));
    }

    @Test
    public void shouldCalculateTheInitialPriceFromThePriceBandAndQuantity() {
        OrderLine orderLine = orderLine().build();

        long quantity = orderLine.getQuantity();
        BigDecimal unitPrice = orderLine.getInitialPriceBand().getBuyerPrice();
        BigDecimal expectedInitialPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        assertThat(orderLine.getAmount().net(), is(expectedInitialPrice));
    }

    @Test
    public void shouldCalculateTheTotalCredits() {
        BuyerCredit firstCredit = aBuyerCredit().creditType(CreditType.PRICE_MOVEMENT).amount(fromNetAndVat(new BigDecimal("200.00"), new BigDecimal("40.00"))).build();
        BuyerCredit secondCredit = aBuyerCredit().creditType(CreditType.PRICE_MOVEMENT).amount(fromNetAndVat(new BigDecimal("50.00"), new BigDecimal("10.00"))).build();

        OrderLine orderLine = orderLine().credit(firstCredit).credit(secondCredit).build();

        assertThat(orderLine.getCreditTotal().net(), is(new BigDecimal("250.00")));
    }
    
    private OrderLine orderLineWithId(Long id) {
        return anOrderLine().id(OrderLineId.from(id)).build();
    }

}