package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_VENDOR;
import static com.jayway.restassured.RestAssured.given;

public class VendorList {
    private VendorList() {}
    public static Response listVendors() {
        return given()
            .filters(Filters.DEFAULT_FILTERS)
            .contentType(ContentType.JSON)
            .get(WS_VENDOR);
    }
}
