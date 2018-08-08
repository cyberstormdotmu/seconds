package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_PRODUCTS;
import static com.jayway.restassured.RestAssured.given;

public class ListProducts {

    public static Response listProducts(String category) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .param("category", category)
                .get(WS_PRODUCTS);
    }

}
