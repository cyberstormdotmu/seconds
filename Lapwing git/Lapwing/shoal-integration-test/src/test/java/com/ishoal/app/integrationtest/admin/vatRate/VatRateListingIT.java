package com.ishoal.app.integrationtest.admin.vatRate;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.interactions.admin.VatRateList.listVatRates;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;

public class VatRateListingIT extends AbstractIntegrationTest {


    @Test
    public void cannotListVatRatesWhenNotAuthenticated() {
        usingNoAuthentication();
        listVatRates()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canListVatRatesWhenAuthenticatedAsAdmin() {

        usingValidAuthentication();
        listVatRates()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnSomeData() {

        usingValidAuthentication();
        listVatRates()
            .then()
            .body("[0].code", is("STANDARD"))
            .body("[0].rate", is(new BigDecimal("20.00")))
            .body("[1].code", is("REDUCED"))
            .body("[1].rate", is(new BigDecimal("5.00")))
            .body("[2].code", is("ZERO"))
            .body("[2].rate", is(new BigDecimal("0.00")))
            .body("[3].code", is("EXEMPT"))
            .body("[3].rate", is(nullValue()));
    }

    private void usingValidAuthentication() {

        usingValidAdminAuthentication();
    }
}
