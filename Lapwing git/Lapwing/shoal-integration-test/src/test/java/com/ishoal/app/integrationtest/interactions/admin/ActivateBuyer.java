package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_BUYER;
import static com.jayway.restassured.RestAssured.given;

public class ActivateBuyer {
    public static Response activateBuyer(String username) {
        return given()
            .filters(Filters.DEFAULT_FILTERS)
            .put(WS_BUYER + username + "/activateBuyer");
    }
}
