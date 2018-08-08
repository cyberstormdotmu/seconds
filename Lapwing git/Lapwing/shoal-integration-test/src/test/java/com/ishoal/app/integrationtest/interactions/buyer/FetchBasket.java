package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_BASKET;
import static com.jayway.restassured.RestAssured.given;

public class FetchBasket {
    public static Response fetchBasket() {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .when()
                .get(WS_BASKET);
    }
}
