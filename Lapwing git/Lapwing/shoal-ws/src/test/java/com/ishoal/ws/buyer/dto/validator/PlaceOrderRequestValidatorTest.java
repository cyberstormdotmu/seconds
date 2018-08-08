package com.ishoal.ws.buyer.dto.validator;

import com.ishoal.common.Result;
import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.ws.common.dto.AddressDto.anAddressDto;
import static com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto.anOrderLine;
import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.aShoppingBasket;
import static com.ishoal.ws.buyer.dto.ShoppingBasketItemDto.aShoppingBasketItem;
import static org.junit.Assert.assertTrue;

public class PlaceOrderRequestValidatorTest {

    public static final String HPELITE840 = "HPELITE840";
    public static final String HPOFFICEJET4620 = "HPOFFICEJET4620";
    public static final int LAPTOP_QTY = 3;
    public static final int PRINTER_QTY = 1;
   
    public static final BigDecimal LAPTOP_PRICE = new BigDecimal("1030.00");
    public static final BigDecimal PRINTER_PRICE = new BigDecimal("295.00");
    private static final BigDecimal CREDIT_TO_BE_APPLIED = new BigDecimal("100.00");

    private static final PlaceOrderRequestValidator validator = new PlaceOrderRequestValidator();

    @Test
    public void shouldNotBeValidIfRequestHasDifferentNumberOfProductsToBasket() {
        Result result = validate(requestWithLaptop(), basket());
        assertTrue(result.isError());
    }

    @Test
    public void shouldNotBeValidIfRequestHasDifferentProductsToBasket() {
        Result result = validate(requestWithLaptop().line(scanner() ), basket());
        assertTrue(result.isError());
    }

    @Test
    public void shouldNotBeValidIfRequestHasItemWithDifferentQuantityToBasket() {
        Result result = validate(requestWithLaptop().line(printer().quantity(10)), basket());
        assertTrue(result.isError());
    }

    @Test
    public void shouldNotBeValidIfRequestHasItemWithDifferentUnitPriceToBasket() {
        Result result = validate(requestWithLaptop().line(printer().unitPrice(BigDecimal.TEN)), basket());
        assertTrue(result.isError());
    }

    @Test
    public void shouldBeValidIfRequestMatchesBasket() {
        Result result = validate(validRequest(), basket());
        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldNotBeValidIfRequestHasNegativeCredit() {
        Result result = validate(validRequest().creditToBeApplied(new BigDecimal("-1")), basket());
        assertTrue(result.isError());
    }

    @Test
    public void shouldNotBeValidIfRequestHasNoInvoiceAddress() {
        Result result = validate(validRequest().invoiceAddress(null), basket());
        assertTrue(result.isError());
    }


    @Test
    public void shouldNotBeValidIfRequestHasNoDeliveryAddress() {
        Result result = validate(validRequest().deliveryAddress(null), basket());
        assertTrue(result.isError());
    }

    private Result validate(PlaceOrderRequestDto.Builder request, ShoppingBasketDto.Builder basket) {
        return validator.validate(request.build(), basket.build());
    }

    private PlaceOrderRequestDto.Builder validRequest() {
        return requestWithLaptop().line(printer());
    }

    private PlaceOrderRequestDto.Builder requestWithLaptop() {
        return PlaceOrderRequestDto.aPlaceOrderRequest()
                .line(laptop())
                .creditToBeApplied(CREDIT_TO_BE_APPLIED)
                .invoiceAddress(validAddress())
                .deliveryAddress(validAddress());
    }

    private AddressDto validAddress() {
        return anAddressDto()
                .build();
    }

    private PlaceOrderRequestOrderLineDto.Builder laptop() {
        return anOrderLine()
                    .productCode(HPELITE840)
                    .quantity(LAPTOP_QTY)
                    .unitPrice(LAPTOP_PRICE.setScale(0))
                    .stock(40000L);
                  
    }

    private PlaceOrderRequestOrderLineDto.Builder printer() {
        return anOrderLine()
                .productCode(HPOFFICEJET4620)
                .quantity(PRINTER_QTY)
                .unitPrice(PRINTER_PRICE.setScale(0))
                .stock(40000L);
               
    }

    private PlaceOrderRequestOrderLineDto.Builder scanner() {
        return anOrderLine()
                .productCode("HPSCANJET")
                .quantity(1)
                .unitPrice(BigDecimal.TEN)
                .stock(4000L);
        
              
              
    }

    private ShoppingBasketDto.Builder basket() {
        ShoppingBasketItemDto laptop = aShoppingBasketItem().productCode(HPELITE840).quantity(LAPTOP_QTY).unitPrice(LAPTOP_PRICE).stock(40000L).build();
        ShoppingBasketItemDto printer = aShoppingBasketItem().productCode(HPOFFICEJET4620).quantity(PRINTER_QTY).unitPrice(PRINTER_PRICE).stock(40000L).build();

        return aShoppingBasket()
                .item(laptop)
                .item(printer);
    }
}
