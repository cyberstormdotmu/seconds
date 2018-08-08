package com.ishoal.app.integrationtest.buyer.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.interactions.admin.AdminFetchOrder;
import com.ishoal.app.integrationtest.interactions.buyer.UpdateBasket;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.PRODUCT_CODE_HPELITE840;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ORDERS;
import static com.ishoal.app.integrationtest.interactions.buyer.PlaceOrder.anOrderFor;
import static com.ishoal.app.integrationtest.interactions.buyer.PlaceOrder.placeOrder;
import static com.ishoal.app.integrationtest.interactions.buyer.PlaceOrder.placeOrderAndReturnReference;
import static com.ishoal.app.integrationtest.matchers.AccessDeniedMatcher.accessDenied;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

@DirtiesDb
public class OrderPlacementIT extends AbstractIntegrationTest {

    public static final String PAYMENT_INVOICE = "On Invoice";

    @Test
    public void shouldNotBeAbleToPlaceOrderWhenNotAuthenticated() {
        usingNoAuthentication();

        placeOrder(anOrderFor(PRODUCT_CODE_HPELITE840, 10, BigDecimal.TEN,400L))
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void shouldBeAbleToPlaceOrder() {
        usingValidBuyerAuthentication();

        ShoppingBasketDto basket = setupBasket();
        placeOrder(anOrderFor(basket)).then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void successfulPlacementShouldReturnLocationOfOrder() {
        usingValidBuyerAuthentication();
        ShoppingBasketDto basket = setupBasket();
        placeOrder(anOrderFor(basket)).then().header("Location", startsWith(WS_ORDERS));
    }

    @Test
    public void successfulPlacementShouldReturnOrderReference() {
        usingValidBuyerAuthentication();
        ShoppingBasketDto basket = setupBasket();
        assertThat(placeOrderAndReturnReference(anOrderFor(basket)), is(notNullValue()));
    }

    @Test
    public void orderShouldBePersistedInDatabase() {
        usingValidBuyerAuthentication();
        ShoppingBasketDto basket = setupBasket();
        String reference = placeOrderAndReturnReference(anOrderFor(basket));
        assertThat(order(reference), is(notNullValue()));
    }
    
    @Test
    public void orderShouldBePersistedWithLine() {
        usingValidBuyerAuthentication();
        assertThat(placeOrderAndReturnOrder().getLines(), is(not(empty())));
    }

    @Test
    public void orderLineShouldBePersistedWithInitialPriceBand() {
        usingValidBuyerAuthentication();
        assertThat(placeOrderAndReturnLine().getInitialUnitPrice(), is(notNullValue()));
    }
    
    @Test
    public void orderLineShouldBePersistedWithCurrentPriceBand() {
        usingValidBuyerAuthentication();
        assertThat(placeOrderAndReturnLine().getCurrentUnitPrice(), is(notNullValue()));
    }

    @Test
    public void shouldBeAbleToPlaceOrderWithoutPaymentDetails() {
        usingValidBuyerAuthentication();

        ShoppingBasketDto basket = setupBasket();
        placeOrder(anOrderFor(basket).paymentMethod(PAYMENT_INVOICE)).then().statusCode(HttpStatus.SC_CREATED);
    }

    private OrderLineDto placeOrderAndReturnLine() {
        return placeOrderAndReturnOrder().getLines().get(0);
    }

    private AdminOrderDto placeOrderAndReturnOrder() {
        ShoppingBasketDto basket = setupBasket();
        String reference = placeOrderAndReturnReference(anOrderFor(basket));
        return order(reference);
    }

    private AdminOrderDto order(String reference) {
        usingValidAdminAuthentication();
        return AdminFetchOrder.adminFetchOrderAsDto(reference);
    }

    private ShoppingBasketDto setupBasket() {
        return UpdateBasket.updateAndReturnBasket(PRODUCT_CODE_HPELITE840, 10);
    }
}