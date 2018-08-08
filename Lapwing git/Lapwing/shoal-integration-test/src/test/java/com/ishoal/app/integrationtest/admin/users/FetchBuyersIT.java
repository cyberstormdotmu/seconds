package com.ishoal.app.integrationtest.admin.users;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.core.domain.Role;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.buildABuyerRegistrationDto;
import static com.ishoal.app.integrationtest.interactions.admin.FetchBuyers.fetchBuyers;
import static com.ishoal.app.integrationtest.interactions.buyer.RegisterBuyer.registerBuyer;
import static com.ishoal.app.integrationtest.matchers.AccessDeniedMatcher.accessDenied;
import static com.ishoal.core.matchers.RegexMatcher.matchesRegex;
import static org.hamcrest.core.Is.is;

public class FetchBuyersIT extends AbstractIntegrationTest {

    private static final String INACTIVE = "INACTIVE";

    @Before
    public void before() {

        usingNoAuthentication();
        registerBuyer(buildABuyerRegistrationDto());
    }

    @Test
    public void cannotFetchUsersWhenNotAuthenticated() {
        usingNoAuthentication();
        fetchBuyers(INACTIVE)
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void cannotFetchUsersWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        fetchBuyers(INACTIVE)
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void shouldFetchAllUsersWithoutARole() {

        DateTime today = DateTime.now();

        usingValidAdminAuthentication();
        fetchBuyers(INACTIVE)
                .then()
                .statusCode(HttpStatus.SC_OK)
            .and()
            .body("registrations[0].buyer.firstName", is("Roger"))
            .body("registrations[0].buyer.surname", is("Watkins"))
            .body("registrations[0].buyer.emailAddress", is("rogerwatkins@hotmail.co.uk"))
            .body("registrations[0].organisation.name", is("HP Limited"))
            .body("registrations[0].organisation.registrationNumber", is("07496791"))
            .body("registrations[0].registeredDate", matchesRegex(String.format("%04d-%02d-%02d.*", today.year().get(), today.monthOfYear().get(), today.dayOfMonth().get())));
    }
    @Test
    public void shouldGiveNotImplementedResponse_whenTryingAnythingElse() {
        usingValidAdminAuthentication();
        fetchBuyers(Role.BUYER.getName())
            .then()
            .statusCode(HttpStatus.SC_NOT_IMPLEMENTED);
    }
}
