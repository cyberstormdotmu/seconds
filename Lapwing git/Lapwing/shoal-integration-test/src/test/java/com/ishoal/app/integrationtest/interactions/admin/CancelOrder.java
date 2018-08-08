package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_CANCEL_ORDERS;
import static com.jayway.restassured.RestAssured.given;

public class CancelOrder {
    public static Response cancelOrder(String orderReference, long version) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .contentType(ContentType.JSON)
                .param("version", version)
                .put(String.format(WS_ADMIN_CANCEL_ORDERS, orderReference));
    }
}
