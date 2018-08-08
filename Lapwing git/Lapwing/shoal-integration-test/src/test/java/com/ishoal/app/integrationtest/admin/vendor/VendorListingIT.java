package com.ishoal.app.integrationtest.admin.vendor;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.interactions.admin.VendorList.listVendors;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class VendorListingIT extends AbstractIntegrationTest {


    @Test
    public void cannotListVendorsWhenNotAuthenticated() {
        usingNoAuthentication();
        listVendors()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canListVendorsWhenAuthenticatedAsAdmin() {

        usingValidAuthentication();
        listVendors()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnSomeData() {

        usingValidAuthentication();
        listVendors()
            .then()
            .body("[0]", is("HP"));
    }

    private void usingValidAuthentication() {

        usingValidAdminAuthentication();
    }
}
