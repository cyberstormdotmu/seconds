package com.ishoal.core.orders;

import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.CreditType;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBandMovement;
import com.ishoal.core.domain.PriceMovement;
import com.ishoal.core.domain.Product;

import java.math.BigDecimal;

import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.PriceBandMovement.aPriceBandMovement;

public class PriceMovementProcessor {

    private final VatCalculator vatCalculator;

    public PriceMovementProcessor(VatCalculator vatCalculator) {
        this.vatCalculator = vatCalculator;
    }

    public void process(Orders orders, Product product, PriceBand offerPriceBand) {
        for (Order order : orders) {
            process(order, product, offerPriceBand);
        }
    }

    private void process(Order order, Product product, PriceBand offerPriceBand) {
        for (OrderLine affectedOrderLine : order.getLines()) {
            process(affectedOrderLine, product, offerPriceBand);
        }
    }

    private void process(OrderLine orderLine, Product product, PriceBand offerPriceBand) {
        if (shouldProcess(orderLine, product, offerPriceBand)) {
            PriceBand originalPriceBand = orderLine.getCurrentPriceBand();
            PriceBandMovement priceBandMovement = priceBandMovement(originalPriceBand, offerPriceBand);
            process(orderLine, priceBandMovement);
        }
    }

    private void process(OrderLine orderLine, PriceBandMovement priceBandMovement) {
        PriceMovement priceMovement = priceBandMovement.priceMovementForQuantity(orderLine.getQuantity());
        orderLine.addCredit(buyerCredit(orderLine, priceMovement));
        orderLine.updateCurrentPriceBand(priceBandMovement.getEndPriceBand());
    }

    private BuyerCredit buyerCredit(OrderLine orderLine, PriceMovement priceMovement) {
        BigDecimal netAmount = priceMovement.getCustomerCredit();
        BigDecimal vatAmount = vatCalculator.calculateVatAmount(netAmount, orderLine.getVatRate());

        return aBuyerCredit()
                .creditType(CreditType.PRICE_MOVEMENT)
                .amount(fromNetAndVat(netAmount, vatAmount))
                .reason(priceMovement.getReason())
                .build();
    }

    private PriceBandMovement priceBandMovement(PriceBand originalPriceBand, PriceBand offerPriceBand) {
        return aPriceBandMovement().startPriceBand(originalPriceBand).endPriceBand(offerPriceBand).build();
    }

    private boolean shouldProcess(OrderLine orderLine, Product product, PriceBand offerPriceBand) {
        return isLineForCorrectProduct(orderLine, product) && hasPriceBandMoved(orderLine, offerPriceBand);
    }

    private boolean hasPriceBandMoved(OrderLine affectedOrderLine, PriceBand offerPriceBand) {
        return !affectedOrderLine.getCurrentPriceBand().equals(offerPriceBand);
    }

    private boolean isLineForCorrectProduct(OrderLine affectedOrderLine, Product product) {
        return affectedOrderLine.getProduct().equals(product);
    }
}