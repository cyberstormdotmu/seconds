package com.ishoal.app.integrationtest.interactions.admin;

import com.ishoal.app.integrationtest.Filters;
import com.ishoal.ws.admin.dto.AdminOfferReportDto;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.ishoal.app.integrationtest.IntegrationTestConstants.WS_ADMIN_OFFER_REPORT;
import static com.jayway.restassured.RestAssured.given;

public class FetchOfferReport {

    public static Response fetchOfferReport(String offerReference) {
        return given()
                .filters(Filters.DEFAULT_FILTERS)
                .contentType(ContentType.JSON)
                .get(String.format(WS_ADMIN_OFFER_REPORT, offerReference));
    }

    public static AdminOfferReportDto fetchOfferReportAsDto(String offerReference) {
        return fetchOfferReport(offerReference).as(AdminOfferReportDto.class);
    }
}
