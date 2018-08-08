package com.ishoal.app.integrationtest.buyer.credits;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.buyer.FetchCredits.fetchCredits;

public class BuyerFetchCreditsIT extends AbstractIntegrationTest {

    @Test
    public void cannotFetchCreditsWhenNotAuthenticated() {
        usingNoAuthentication();
        fetchCredits()
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canFetchCreditsWhenAuthenticated() {
        usingValidBuyerAuthentication();
        fetchCredits()
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

}
