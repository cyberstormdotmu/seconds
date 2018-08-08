package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_PRODUCT_CATEGORIES;
import static com.jayway.restassured.RestAssured.given;

public class FetchCategories {
    public static Response fetchCategories() {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .when()
                .get(WS_PRODUCT_CATEGORIES);
    }
}
