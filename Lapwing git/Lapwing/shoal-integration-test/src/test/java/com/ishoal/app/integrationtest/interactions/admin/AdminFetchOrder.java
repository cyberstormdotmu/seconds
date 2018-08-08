package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_ORDERS;
import static com.jayway.restassured.RestAssured.given;

public class AdminFetchOrder {

    public static Response adminFetchOrder(String orderReference) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .contentType(ContentType.JSON)
                .get(WS_ADMIN_ORDERS + orderReference);
    }

    public static AdminOrderDto adminFetchOrderAsDto(String orderReference) {
        return adminFetchOrder(orderReference).as(AdminOrderDto.class);
    }
}
