package com.ishoal.app.integrationtest;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;

import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.NEW_USER_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.NEW_USER_PASSWORD;

public class AuthenticationHelper {

    private final ResetOptions resetOptions;

    public AuthenticationHelper(ResetOptions resetOptions) {

        this.resetOptions = resetOptions;
    }

    public static void usingValidAdminAuthentication() {
        authenticate("oliver.squires@ishoal.com", "password");
    }

    public static void usingValidBuyerAuthentication() {
        authenticate("tom@bosl.co.uk", "password");
    }
   

    public static void usingNewlyRegisteredAndAuthorisedBuyerAuthentication() {
        authenticate(NEW_USER_NAME, NEW_USER_PASSWORD);
    }

    public static void authenticate(String username, String password) {
        Filters.XSRF_FILTER.reset();
        RestAssured.given()
            .filter(Filters.XSRF_FILTER)
            .auth().basic(username, password)
            .when().get(IntegrationTestConstants.WS_LOGIN)
            .then().statusCode(HttpStatus.SC_OK);
    }

    public void usingNoAuthentication() {
        Filters.XSRF_FILTER.reset();
        RestAssured.reset();
        RestAssured.port = resetOptions.getPort();
        RestAssured.config = resetOptions.getConfig();
    }
}
