package com.ishoal.app.integrationtest.buyer.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.core.repository.OrderRepository;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_CONFIRMED_WITH_CREDIT;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_UNCONFIRMED;
import static com.ishoal.app.integrationtest.interactions.buyer.BuyerListOrders.buyerListOrders;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class ListOrdersForBuyerIT extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void cannotListOrdersWhenNotAuthenticated() {
        usingNoAuthentication();
        buyerListOrders()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canListOrdersForABuyer() {
        usingValidBuyerAuthentication();
        buyerListOrders()
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldGetOrdersForTheBuyer() {
        usingValidBuyerAuthentication();
        buyerListOrders()
                .then()
                .body("", hasSize(2));
    }

    @Test
    public void shouldHaveTheExpectedOrderReferences() {
        usingValidBuyerAuthentication();
        buyerListOrders()
                .then()
                .body("reference", hasItems(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT, ORDER_REFERENCE_UNCONFIRMED));
    }
}
