package com.ishoal.core.orders;

import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBandId;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderTestData.randomOrderLineId;
import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.PriceBands.somePriceBands;
import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceMovementProcessorTest {

    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    public static final long QUANTITY = 1;
    public static final BigDecimal NET_AMOUNT = new BigDecimal("1000.00");
    public static final BigDecimal VAT_AMOUNT = new BigDecimal("200.00");

    private PriceMovementProcessor processor;

    private PriceBand priceBand1;
    private PriceBand priceBand2;
    private PriceBand priceBand3;
    private PriceBands priceBands;
    private Offer offer;
    private Product product;

    @Before
    public void before() {
        this.processor = new PriceMovementProcessor(new VatCalculator());

        this.priceBand1 = aPriceBand().id(PriceBandId.from(1L)).buyerPrice(new BigDecimal("1000.00")).minVolume(0L)
                .maxVolume(100L).build();
        this.priceBand2 = aPriceBand().id(PriceBandId.from(2L)).buyerPrice(new BigDecimal("900.00")).minVolume(101L)
                .maxVolume(200L).build();
        this.priceBand3 = aPriceBand().id(PriceBandId.from(3L)).buyerPrice(new BigDecimal("800.00")).minVolume(201L)
                .build();
        this.priceBands = somePriceBands().priceBand(this.priceBand1).priceBand(this.priceBand2)
                .priceBand(this.priceBand3).build();

        this.offer = anExistingOpenOffer().priceBands(this.priceBands).build();
        this.product = productBuilder(ProductCode.from("LAPTOP")).offers(Offers.over(this.offer))
                .build();
    }

    @Test
    public void shouldNotMoveCurrentPriceBandIfAlreadySameAsOfferPriceBand() {
        OrderLine orderLine = processAnOrderWithNoPriceMovement();
        assertThat(orderLine.getCurrentPriceBand(), is(this.priceBand1));
    }

    @Test
    public void shouldMoveCurrentPriceBandIfNotSameAsOfferPriceBand() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        assertThat(orderLine.getCurrentPriceBand(), is(this.priceBand2));
    }

    @Test
    public void shouldNotCreateCreditIfNoPriceMovement() {
        OrderLine orderLine = processAnOrderWithNoPriceMovement();
        assertThat(orderLine.getCredits().size(), is(0));
    }

    @Test
    public void shouldCreateCreditIfPriceMovement() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        assertThat(orderLine.getCredits().size(), is(1));
    }

    @Test
    public void shouldCreateSingleCreditIfPriceMovementByMoreThanOneBand() {
        OrderLine orderLine = processAnOrderWithPriceMovementOfTwoBands();
        assertThat(orderLine.getCredits().size(), is(1));
    }

    @Test
    public void creditShouldHaveNetAmount() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        BuyerCredit credit = orderLine.getCredits().iterator().next();
        assertThat(credit.getAmount().net(), is(new BigDecimal("100.00")));
    }

    @Test
    public void creditShouldHaveVatAmount() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        BuyerCredit credit = orderLine.getCredits().iterator().next();
        assertThat(credit.getAmount().vat(), is(new BigDecimal("20.00")));
    }

    @Test
    public void netAmountShouldNotChangeWhenCreditApplied() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        assertThat(orderLine.getAmount().net(), is(NET_AMOUNT));
    }

    @Test
    public void vatAmountShouldNotChangeWhenCreditApplied() {
        OrderLine orderLine = processAnOrderWithPriceMovement();
        assertThat(orderLine.getAmount().vat(), is(VAT_AMOUNT));
    }

    private OrderLine processAnOrderWithNoPriceMovement() {
        return process(anOrderWithLineAtPriceBand1(), this.priceBand1);
    }

    private OrderLine processAnOrderWithPriceMovement() {
        return process(anOrderWithLineAtPriceBand1(), this.priceBand2);
    }

    private OrderLine processAnOrderWithPriceMovementOfTwoBands() {
        return process(anOrderWithLineAtPriceBand1(), this.priceBand3);
    }

    private OrderLine process(Order order, PriceBand newPriceBand) {
       // this.processor.process(Orders.over(order), this.product, newPriceBand);
        return order.getLines().iterator().next();
    }

    private Order anOrderWithLineAtPriceBand1() {
        return anOrderWithLine(anOrderLine()
                .id(randomOrderLineId())
                .currentPriceBand(priceBand1)
                .quantity(QUANTITY)
                .amount(fromNetAndVat(NET_AMOUNT, VAT_AMOUNT))
                .vatRate(VAT_RATE)
                .product(this.product));
    }

    private Order anOrderWithLine(OrderLine.Builder orderLine) {
        return anOrder().reference(OrderReference.create()).line(orderLine.build()).build();
    }
}