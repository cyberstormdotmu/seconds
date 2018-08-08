package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Random;

import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.OrderPayment.anOrderPayment;
import static com.ishoal.core.domain.PriceBandTestData.firstPriceBand;
import static com.ishoal.core.domain.PriceBandTestData.lastPriceBand;
import static com.ishoal.core.domain.ProductTestData.productBuilder;

public class OrderTestData {


    public static final DateTime CREDIT_CREATED = DateTime.now();
    public static final DateTime DATE_PAYMENT_RECEIVED = DateTime.now().minusDays(2);
    public static final DateTime DATE_PAYMENT_CREATED = DateTime.now().minusMinutes(10);
    public static final BigDecimal PAYMENT_AMOUNT = new BigDecimal("600.00");
    public static final String PAYMENT_REFERENCE = "HMNPOSMZRNO";
    public static final String PAYMENT_USER_REFERENCE = "Part payment";
    public static final BigDecimal CREDIT_VAT_AMOUNT = new BigDecimal("4.00");
    public static final BigDecimal CREDIT_NET_AMOUNT = new BigDecimal("20.00");
    public static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    public static final BigDecimal LINE_WITH_CREDIT_VAT_AMOUNT = new BigDecimal("1000.00");
    public static final BigDecimal LINE_WITH_CREDIT_NET_AMOUNT = new BigDecimal("5000.00");
    public static final int QUANTITY = 5;
    public static final BigDecimal ORDER_LINE_NET_AMOUNT = new BigDecimal("50000.00");
    public static final BigDecimal ORDER_LINE_VAT_AMOUNT = new BigDecimal("10000.00");
    public static final String HPELITE840 = "HPELITE840";
    public static final String HPOFFICEJET4620 = "HPOFFICEJET4620";

    public static OrderLineId randomOrderLineId() {
        return OrderLineId.from(new Random().nextLong());
    }

    public static Order.Builder orderWithNoLines() {
		return anOrder()
                .invoiceAddress(AddressTestData.anAddress().build())
                .deliveryAddress(AddressTestData.anAddress().build());
	}

    public static Order.Builder orderWithOneLine() {
        return anOrder().line(orderLine().build());
    }

    public static OrderLine.Builder orderLine() {
        return anOrderLine()
                .product(hpElite840())
                .initialPriceBand(firstPriceBand())
                .currentPriceBand(firstPriceBand())
                .quantity(50)
                .amount(fromNetAndVat(ORDER_LINE_NET_AMOUNT, ORDER_LINE_VAT_AMOUNT))
                .vatRate(VAT_RATE);
    }

	public static OrderLine.Builder orderLineWithCredit() {
        return anOrderLine()
                .product(hpOfficeJet4620())
                .initialPriceBand(firstPriceBand())
                .currentPriceBand(lastPriceBand())
                .quantity(QUANTITY)
                .amount(fromNetAndVat(LINE_WITH_CREDIT_NET_AMOUNT, LINE_WITH_CREDIT_VAT_AMOUNT))
                .vatRate(VAT_RATE)
                .credit(credit());
    }

    private static BuyerCredit credit() {
        return aBuyerCredit()
                .creditType(CreditType.PRICE_MOVEMENT)
                .amount(fromNetAndVat(CREDIT_NET_AMOUNT, CREDIT_VAT_AMOUNT))
                .created(CREDIT_CREATED)
                .build();
    }

    public static OrderPayment orderPayment() {
        return anOrderPayment()
                .paymentReference(PaymentReference.from(PAYMENT_REFERENCE))
                .paymentType(PaymentType.BANK_TRANSFER)
                .dateReceived(DATE_PAYMENT_RECEIVED)
                .created(DATE_PAYMENT_CREATED)
                .amount(PAYMENT_AMOUNT)
                .userReference(PAYMENT_USER_REFERENCE)
                .build();
    }

    private static Product hpElite840() {
        return productBuilder(ProductCode.from(HPELITE840)).build();
    }

    private static Product hpOfficeJet4620() {
        return productBuilder(ProductCode.from(HPOFFICEJET4620)).build();
    }

}