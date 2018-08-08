package com.ishoal.app.integrationtest.interactions.buyer;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_PROFILE;
import static com.jayway.restassured.RestAssured.given;

public class UpdateProfile {

    public static Response updateBuyerProfile(BuyerProfileDto.Builder profile) {
        return given()
            .filters(Filters.DEFAULT_FILTERS)
            .contentType(ContentType.JSON)
            .body(profile.build())
            .put(WS_PROFILE);
    }
}
