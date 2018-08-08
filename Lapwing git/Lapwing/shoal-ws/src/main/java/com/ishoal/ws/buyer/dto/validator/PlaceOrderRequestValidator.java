package com.ishoal.ws.buyer.dto.validator;

import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;

import java.math.BigDecimal;
import java.util.Optional;

public class PlaceOrderRequestValidator {

    public Result validate(PlaceOrderRequestDto request, ShoppingBasketDto basket) {
        Result result;

        result = hasValidBasket(request, basket);
        if(result.isSuccess()) {
            result = hasValidCreditAmount(request);
        }
        if(result.isSuccess()) {
            result = hasValidInvoiceAddress(request);
        }
        if(result.isSuccess()) {
            result = hasValidDeliveryAddress(request);
        }

        return result;
    }

    private Result hasValidCreditAmount(PlaceOrderRequestDto request) {
        BigDecimal creditToBeApplied = Optional.ofNullable(request.getCreditToBeApplied()).orElse(BigDecimal.ZERO);

        if(creditToBeApplied.signum() < 0) {
            return SimpleResult.error("Cannot have a negative amount of Shoal Credits to be applied to the order");
        }

        return SimpleResult.success();
    }

    private Result hasValidInvoiceAddress(PlaceOrderRequestDto request) {
        if(request.getInvoiceAddress() == null) {
            return SimpleResult.error("Order request is missing an invoice address");
        }
        return SimpleResult.success();
    }

    private Result hasValidDeliveryAddress(PlaceOrderRequestDto request) {
        if(request.getDeliveryAddress() == null) {
            return SimpleResult.error("Order request is missing a delivery address");
        }
        return SimpleResult.success();
    }

    private Result hasValidBasket(PlaceOrderRequestDto request, ShoppingBasketDto basket) {
        Result result;
        result = hasSameNumberOfItems(request, basket);
        if(result.isSuccess()) {
            result = hasSameItems(request, basket);
        }
        return result;
    }

    private Result hasSameItems(PlaceOrderRequestDto request, ShoppingBasketDto basket) {
        for(PlaceOrderRequestOrderLineDto line : request.getLines()) {
            Result result;
            ShoppingBasketItemDto basketItem = basket.findItem(line.getProductCode());
            if(basketItem != null) {
                result = hasSameQuantityAndUnitPrice(line, basketItem);
            } else {
                result = SimpleResult.error("Product " + line.getProductCode() + " was not found in the basket");
            }

            if(result.isError()) {
                return result;
            }
        }

        return SimpleResult.success();
    }

    private Result hasSameQuantityAndUnitPrice(PlaceOrderRequestOrderLineDto line, ShoppingBasketItemDto basketItem) {
        boolean sameQuantity = basketItem.getQuantity() == line.getQuantity();
        boolean sameUnitPrice = basketItem.getUnitPrice().compareTo(line.getUnitPrice()) == 0;

        if(sameQuantity && sameUnitPrice) {
            return SimpleResult.success();
        }
        return SimpleResult.error("Order request and basket have differing quantity or unit price for product " + line.getProductCode());
    }

    private Result hasSameNumberOfItems(PlaceOrderRequestDto request, ShoppingBasketDto basket) {
        if(request.getLines().size() == basket.getItems().size()) {
            return SimpleResult.success();
        }
        return SimpleResult.error("Order request and basket do not have the same number of items");
    }
}
