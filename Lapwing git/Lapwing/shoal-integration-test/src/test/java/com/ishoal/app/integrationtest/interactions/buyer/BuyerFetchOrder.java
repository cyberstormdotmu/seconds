package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ORDERS;
import static com.jayway.restassured.RestAssured.given;

public class BuyerFetchOrder {
    public static Response buyerFetchOrder(String orderReference) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .get(WS_ORDERS + orderReference);
    }

}
