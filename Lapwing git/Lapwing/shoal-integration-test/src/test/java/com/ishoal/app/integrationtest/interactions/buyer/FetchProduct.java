package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_PRODUCTS;
import static com.jayway.restassured.RestAssured.given;

public class FetchProduct {
    public static Response fetchProduct(String productCode) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .get(WS_PRODUCTS + productCode);
    }

    public static ProductDto fetchProductAsDto(String productCode) {
        return fetchProduct(productCode).as(ProductDto.class);
    }
}
