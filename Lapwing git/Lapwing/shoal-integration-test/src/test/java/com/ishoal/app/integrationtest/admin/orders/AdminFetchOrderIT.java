package com.ishoal.app.integrationtest.admin.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_CONFIRMED_WITH_CREDIT;
import static com.ishoal.app.integrationtest.interactions.admin.AdminFetchOrder.adminFetchOrder;
import static org.hamcrest.Matchers.is;

public class AdminFetchOrderIT extends AbstractIntegrationTest {

    @Test
    public void cannotFetchOrderWhenNotAuthenticated() {
        usingNoAuthentication();
        adminFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void cannotFetchOrderWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        adminFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void canFetchOrder() {
        usingValidAdminAuthentication();
        adminFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void orderHasSummaryWithCorrectReference() {
        usingValidAdminAuthentication();
        adminFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .body("summary.reference", is(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT));
    }
}
