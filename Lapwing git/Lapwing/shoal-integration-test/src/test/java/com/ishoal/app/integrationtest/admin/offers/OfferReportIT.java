package com.ishoal.app.integrationtest.admin.offers;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.ws.admin.dto.AdminOfferReportDto;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.admin.FetchOfferReport.fetchOfferReport;
import static com.ishoal.app.integrationtest.interactions.admin.FetchOfferReport.fetchOfferReportAsDto;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OfferReportIT extends AbstractIntegrationTest {
    private static final String OFFER_REFERENCE = "c3eeeb64-cd6c-11df-a41f-1b8d709f3333";

    @Test
    public void cannotFetchReportWhenNotAuthenticated() {
        usingNoAuthentication();
        fetchOfferReport(OFFER_REFERENCE)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void cannotFetchReportWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        fetchOfferReport(OFFER_REFERENCE)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void canFetchReportWhenAuthenticatedAsAdmin() {
        usingValidAdminAuthentication();
        fetchOfferReport(OFFER_REFERENCE)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void reportShouldHaveProductCode() {
        usingValidAdminAuthentication();
        AdminOfferReportDto report = fetchOfferReportAsDto(OFFER_REFERENCE);
        assertThat(report.getProductCode(), is("HPSPIRIT"));
    }

    @Test
    public void reportShouldHaveProductName() {
        usingValidAdminAuthentication();
        AdminOfferReportDto report = fetchOfferReportAsDto(OFFER_REFERENCE);
        assertThat(report.getProductName(), is("HP SPIRIT 13-4109na x360 Convertible Laptop"));
    }

    @Test
    public void reportShouldHaveOfferStartDate() {
        usingValidAdminAuthentication();
        AdminOfferReportDto report = fetchOfferReportAsDto(OFFER_REFERENCE);
        assertThat(report.getVendorName(), is("HP"));
    }
}
