package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_BASKET;
import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.aShoppingBasket;
import static com.ishoal.ws.buyer.dto.ShoppingBasketItemDto.aShoppingBasketItem;
import static com.jayway.restassured.RestAssured.given;

public class UpdateBasket {
    public static Response updateBasket(ShoppingBasketDto basket) {
        return given()
                .contentType(ContentType.JSON)
                .filters(Filters.DEFAULT_FILTERS)
                .body(basket)
                .when()
                .put(WS_BASKET);
    }

    public static ShoppingBasketDto updateAndReturnBasket(ShoppingBasketDto basket) {
        return updateBasket(basket).as(ShoppingBasketDto.class);
    }

    public static ShoppingBasketDto updateAndReturnBasket(String productCode, long quantity) {
        ShoppingBasketItemDto item = basketItem(productCode, quantity);
        return UpdateBasket.updateAndReturnBasket(aShoppingBasket().item(item).build());
    }

    public static ShoppingBasketDto updateAndReturnBasket(ShoppingBasketItemDto... basketItems) {
        ShoppingBasketDto.Builder shoppingBasket = aShoppingBasket();
        for(ShoppingBasketItemDto basketItem : basketItems) {
            shoppingBasket.item(basketItem);
        }
        return UpdateBasket.updateAndReturnBasket(shoppingBasket.build());
    }

    public static ShoppingBasketItemDto basketItem(String productCode, long quantity) {
        return aShoppingBasketItem().productCode(productCode).quantity(quantity).build();
    }
}
