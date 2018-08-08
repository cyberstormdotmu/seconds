
package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestDto;
import com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import com.ishoal.ws.common.dto.AddressDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import java.math.BigDecimal;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ORDERS;
import static com.ishoal.ws.buyer.dto.PlaceOrderRequestDto.aPlaceOrderRequest;
import static com.ishoal.ws.buyer.dto.PlaceOrderRequestOrderLineDto.anOrderLine;
import static com.jayway.restassured.RestAssured.given;

public class PlaceOrder {
    public static Response placeOrder(PlaceOrderRequestDto.Builder order) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .contentType(ContentType.JSON)
                .body(order.build())
                .post(WS_ORDERS);
    }

    public static String placeOrderAndReturnReference(PlaceOrderRequestDto.Builder order) {
        Response response = placeOrder(order);
        return response.getHeader("Location").substring(WS_ORDERS.length());
    }

    public static PlaceOrderRequestDto.Builder anOrderFor(ShoppingBasketDto basket) {
        PlaceOrderRequestDto.Builder orderBuilder = anOrderWithInvoiceAndDeliveryAddress();
        for(ShoppingBasketItemDto item : basket.getItems()) {
            PlaceOrderRequestOrderLineDto.Builder line = anOrderLine()
                    .productCode(item.getProductCode())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                  	.stock(item.getStock());

            orderBuilder.line(line);
        }

        return orderBuilder;
    }


    public static PlaceOrderRequestDto.Builder anOrderFor(String productCode, int quantity, BigDecimal unitPrice,Long stock) {
        return anOrderWithInvoiceAndDeliveryAddress().line(anOrderLine().productCode(productCode).quantity(quantity).unitPrice(unitPrice).stock(stock));
    }

    private static PlaceOrderRequestDto.Builder anOrderWithInvoiceAndDeliveryAddress() {
        return aPlaceOrderRequest()
                .paymentMethod("On Invoice")
                .creditToBeApplied(BigDecimal.ZERO)
                .invoiceAddress(defaultAddress())
                .deliveryAddress(defaultAddress());
    }

    private static AddressDto defaultAddress() {
        return AddressDto.anAddressDto()
                .organisationName("Shoal Ltd")
                .departmentName("Development")
                .buildingName("The Old Hall")
                .streetAddress("Back Lane")
                .locality("Bramham")
                .postTown("Wetherby")
                .postcode("LS23 6QR")
                .build();
    }
}
