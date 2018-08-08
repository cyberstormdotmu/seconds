package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.AuthenticationHelper;
import com.ishoal.app.integrationtest.interactions.admin.ActivateBuyer;
import com.jayway.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.NEW_USER_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.buildABuyerRegistrationDto;
import static com.ishoal.app.integrationtest.interactions.buyer.RegisterBuyer.registerBuyer;

public class BuyerTestHelper {

    private final AuthenticationHelper auth;

    public BuyerTestHelper(AuthenticationHelper auth) {

        this.auth = auth;
    }

    public void createANewBuyer() {

        registerANewBuyer();
        activateBuyer();
    }

    public ValidatableResponse registerANewBuyer() {

        auth.usingNoAuthentication();
        return registerBuyer(buildABuyerRegistrationDto())
            .then().statusCode(HttpStatus.SC_OK);
    }

    public ValidatableResponse activateBuyer() {
        usingValidAdminAuthentication();
        return ActivateBuyer.activateBuyer(NEW_USER_NAME).then().statusCode(HttpStatus.SC_OK);
    }
}
