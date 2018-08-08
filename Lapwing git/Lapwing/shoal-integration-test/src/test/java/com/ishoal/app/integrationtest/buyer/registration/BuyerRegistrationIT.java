package com.ishoal.app.integrationtest.buyer.registration;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.buildABuyerRegistrationDto;
import static com.ishoal.app.integrationtest.interactions.buyer.RegisterBuyer.registerBuyer;

public class BuyerRegistrationIT extends AbstractIntegrationTest {

    @Test
    public void canRegisterWithoutLoggingIn() {
        usingNoAuthentication();
        registerBuyer(buildABuyerRegistrationDto())
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void tryingToRegisterTheSameUserTwiceShouldFail() {
        usingNoAuthentication();
        registerBuyer(buildABuyerRegistrationDto())
            .thenReturn();

        registerBuyer(buildABuyerRegistrationDto())
            .then()
            .statusCode(HttpStatus.SC_CONFLICT);
    }
}
