package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.admin.dto.AdminProductDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_MANAGE_PRODUCTS;
import static com.jayway.restassured.RestAssured.given;

public class CreateAProduct {
    public static Response createAProduct(AdminProductDto product) {
        return given()
                .contentType(ContentType.JSON)
                .filters(Filters.DEFAULT_FILTERS)
                .body(product)
                .when()
                .post(WS_ADMIN_MANAGE_PRODUCTS);
    }


}
