package com.ishoal.app.integrationtest.admin.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.core.domain.OrderStatus;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_UNCONFIRMED;
import static com.ishoal.app.integrationtest.interactions.admin.AdminListOrders.adminListOrders;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ListOrdersByStatusIT extends AbstractIntegrationTest {

    @Test
    public void cannotListConfirmedOrdersWhenNotAuthenticated() {
        usingNoAuthentication();
        adminListOrders(OrderStatus.CONFIRMED)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void cannotListConfirmedOrdersWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        adminListOrders(OrderStatus.CONFIRMED)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void canListConfirmedOrders() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.CONFIRMED)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldHaveThreeConfirmedOrders() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.CONFIRMED)
                .then()
                .body("", hasSize(3));
    }

    @Test
    public void canListRequestedOrders() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.PROCESSING)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void requestedOrderShouldHaveCorrectOrderReference() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.PROCESSING)
                .then()
                .body("[0].reference", is(ORDER_REFERENCE_UNCONFIRMED));
    }

    @Test
    public void requestedOrderShouldHaveCorrectStatus() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.PROCESSING)
                .then()
                .body("[0].status", is(OrderStatus.PROCESSING.getDisplayName()));
    }

    @Test
    public void requestedOrderShouldHaveCorrectOrderTotal() {
        usingValidAdminAuthentication();
        adminListOrders(OrderStatus.PROCESSING)
                .then()
                .body("[0].orderTotal.net", is(new BigDecimal("154500.00")));
    }
}