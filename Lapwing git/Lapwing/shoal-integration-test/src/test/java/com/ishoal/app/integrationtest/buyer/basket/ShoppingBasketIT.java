package com.ishoal.app.integrationtest.buyer.basket;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.buyer.FetchBasket.fetchBasket;
import static com.ishoal.app.integrationtest.interactions.buyer.UpdateBasket.updateBasket;
import static com.ishoal.app.integrationtest.matchers.AccessDeniedMatcher.accessDenied;
import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.aShoppingBasket;
import static com.ishoal.ws.buyer.dto.ShoppingBasketItemDto.aShoppingBasketItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class ShoppingBasketIT extends AbstractIntegrationTest {

    public static final String HPELITE840 = "HPELITE840";
    public static final String HPSPECTRE = "HPSPECTRE";

    @Test
    public void cannotGetBasketWhenNotAuthenticated() {
        usingNoAuthentication();
        fetchBasket()
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void cannotUpdateBasketWhenNotAuthenticated() {
        usingNoAuthentication();
        updateBasket(ShoppingBasketDto.anEmptyShoppingBasket())
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void fetchBasketReturnsAnEmptyBasketWhenNoBasketPreviouslySet() {
        usingValidBuyerAuthentication();
        fetchBasket()
                .then()
                .body("items", hasSize(0));
    }

    @Test
    public void updateBasketReturnsBasketWithTheSameProducts() {
        usingValidBuyerAuthentication();
        updateBasket(basketWithTwoProducts())
                .then()
                .body("items.productCode", hasItems(HPELITE840, HPSPECTRE));
    }

    @Test
    public void fetchBasketThatHasBeenUpdatedReturnsTheBasket() {
        usingValidBuyerAuthentication();

        updateBasket(basketWithTwoProducts());

        fetchBasket().then().body("items.productCode", hasItems(HPELITE840, HPSPECTRE));

    }

    @Test
    public void theBasketCanBeUpdatedMultipleTimes() {
        usingValidBuyerAuthentication();

        updateBasket(basketWithOneProduct());
        fetchBasket().then().body("items.productCode", hasItems(HPELITE840));

        updateBasket(basketWithTwoProducts());
        fetchBasket().then().body("items.productCode", hasItems(HPELITE840, HPSPECTRE));
    }



    private ShoppingBasketDto basketWithOneProduct() {
        return aShoppingBasket()
                .item(item(HPELITE840, 10))
                .build();
    }

    private ShoppingBasketDto basketWithTwoProducts() {
        return aShoppingBasket()
                .item(item(HPELITE840, 10))
                .item(item(HPSPECTRE, 5))
                .build();
    }

    private ShoppingBasketItemDto item(String productCode, long quantity) {
        return aShoppingBasketItem().productCode(productCode).quantity(quantity).build();
    }
}
