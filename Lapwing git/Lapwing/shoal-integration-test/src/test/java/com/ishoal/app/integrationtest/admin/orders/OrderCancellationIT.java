package com.ishoal.app.integrationtest.admin.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.interactions.admin.AdminFetchOrder;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_UNCONFIRMED;
import static com.ishoal.app.integrationtest.interactions.admin.CancelOrder.cancelOrder;
import static com.ishoal.app.integrationtest.matchers.AccessDeniedMatcher.accessDenied;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@DirtiesDb
public class OrderCancellationIT extends AbstractIntegrationTest {

    @Test
    public void cannotCancelOrderWhenNotAuthenticated() {
        usingNoAuthentication();
        cancel()
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void cannotCancelOrderWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        cancel()
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldBeAbleToCancelOrder() {
        usingValidAdminAuthentication();
        cancel().then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void badRequestReturnedWhenCancelWithWrongVersionNumber() {
        usingValidAdminAuthentication();
        cancelOrder(ORDER_REFERENCE_UNCONFIRMED, 1L)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void cancellationShouldChangeOrderStatus() {
        usingValidAdminAuthentication();
        cancel();
        assertThat(order().getSummary().getStatus(), is(OrderStatus.CANCELLED.getDisplayName()));
    }

    private Response cancel() {
        return cancelOrder(ORDER_REFERENCE_UNCONFIRMED, 0L);
    }

    private AdminOrderDto order() {
        return AdminFetchOrder.adminFetchOrderAsDto(ORDER_REFERENCE_UNCONFIRMED);
    }
}