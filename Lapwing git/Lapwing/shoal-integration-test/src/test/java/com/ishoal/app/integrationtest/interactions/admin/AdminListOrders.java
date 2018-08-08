package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.core.domain.OrderStatus;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_ORDERS;
import static com.jayway.restassured.RestAssured.given;

public class AdminListOrders {
    public static Response adminListOrders(OrderStatus orderStatus) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .contentType(ContentType.JSON)
                .param("status", orderStatus)
                .get(WS_ADMIN_ORDERS);
    }

}
