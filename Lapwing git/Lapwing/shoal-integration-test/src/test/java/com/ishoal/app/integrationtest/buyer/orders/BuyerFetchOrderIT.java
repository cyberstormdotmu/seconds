package com.ishoal.app.integrationtest.buyer.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_CONFIRMED_WITH_CREDIT;
import static com.ishoal.app.integrationtest.interactions.buyer.BuyerFetchOrder.buyerFetchOrder;
import static org.hamcrest.Matchers.is;

public class BuyerFetchOrderIT extends AbstractIntegrationTest {

    @Test
    public void cannotFetchOrderWhenNotAuthenticated() {
        usingNoAuthentication();
        buyerFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canFetchOrder() {
        usingValidBuyerAuthentication();
        buyerFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void orderHasSummaryWithCorrectReference() {
        usingValidBuyerAuthentication();
        buyerFetchOrder(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT)
                .then()
                .body("summary.reference", is(ORDER_REFERENCE_CONFIRMED_WITH_CREDIT));
    }

}
