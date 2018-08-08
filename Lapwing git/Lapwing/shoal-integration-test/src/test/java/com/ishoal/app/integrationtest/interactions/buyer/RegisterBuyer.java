package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.buyer.dto.BuyerRegistrationDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_REGISTRATION;
import static com.jayway.restassured.RestAssured.given;

public class RegisterBuyer {
    public static Response registerBuyer(BuyerRegistrationDto.Builder user) {
        return given()
            .filters(Filters.DEFAULT_FILTERS)
            .contentType(ContentType.JSON)
            .body(user.build())
            .post(WS_REGISTRATION);
    }

}
