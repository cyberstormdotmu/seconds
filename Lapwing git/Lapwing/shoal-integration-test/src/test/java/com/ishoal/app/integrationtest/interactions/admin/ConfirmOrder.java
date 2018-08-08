package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_CONFIRM_ORDERS;
import static com.jayway.restassured.RestAssured.given;

public class ConfirmOrder {
    public static Response confirmOrder(String orderReference, long version) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .param("version", version)
                .put(String.format(WS_ADMIN_CONFIRM_ORDERS, orderReference));
    }


}
