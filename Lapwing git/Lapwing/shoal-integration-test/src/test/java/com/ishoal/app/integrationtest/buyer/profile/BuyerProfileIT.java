package com.ishoal.app.integrationtest.buyer.profile;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.interactions.buyer.BuyerTestHelper;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingNewlyRegisteredAndAuthorisedBuyerAuthentication;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_BUILDING_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_LOCALITY;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_NUMBER;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_POSTCODE;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_POSTTOWN;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_SORTCODE;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_ACCOUNT_STREET_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.BANK_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.CONTACT_EMAIL_ADDRESS;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.CONTACT_FIRST_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.CONTACT_PHONE_NUMBER;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.CONTACT_SURNAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.CONTACT_TITLE;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_BUILDING;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_DEPARTMENT;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_LOCALITY;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_POSTCODE;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_POSTTOWN;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.DELIVERY_ADDRESS_STREET;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.ORGANISATION_NAME;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.ORGANISATION_REG;
import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.buildABuyerProfile;
import static com.ishoal.app.integrationtest.interactions.buyer.FetchProfile.fetchProfile;
import static com.ishoal.app.integrationtest.interactions.buyer.UpdateProfile.updateBuyerProfile;
import static org.hamcrest.core.Is.is;

@DirtiesDb
public class BuyerProfileIT extends AbstractIntegrationTest {

    private BuyerTestHelper helper;

    @Before
    public void before() {
        helper = new BuyerTestHelper(authenticationHelper);
    }

    @Test
    public void unauthorisedUserCannotFetchProfile() {

        usingNoAuthentication();

        fetchProfile().then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void authorisedUserCanFetchProfile() {

        helper.createANewBuyer();

        usingNewlyRegisteredAndAuthorisedBuyerAuthentication();
        fetchProfile().then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void registeredUserCanUpdateProfile() {

        helper.createANewBuyer();

        usingNewlyRegisteredAndAuthorisedBuyerAuthentication();
        updateBuyerProfile(buildABuyerProfile())
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldRetrieveAllProfileData() {

        helper.createANewBuyer();

        usingNewlyRegisteredAndAuthorisedBuyerAuthentication();
        updateBuyerProfile(buildABuyerProfile())
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .body("organisation.name", is(ORGANISATION_NAME))
            .body("organisation.registrationNumber", is(ORGANISATION_REG))
            .body("contact.title", is(CONTACT_TITLE))
            .body("contact.firstName", is(CONTACT_FIRST_NAME))
            .body("contact.surname", is(CONTACT_SURNAME))
            .body("contact.emailAddress", is(CONTACT_EMAIL_ADDRESS))
            .body("contact.phoneNumber", is(CONTACT_PHONE_NUMBER))
            .body("deliveryAddress.departmentName", is(DELIVERY_ADDRESS_DEPARTMENT))
            .body("deliveryAddress.buildingName", is(DELIVERY_ADDRESS_BUILDING))
            .body("deliveryAddress.streetAddress", is(DELIVERY_ADDRESS_STREET))
            .body("deliveryAddress.locality", is(DELIVERY_ADDRESS_LOCALITY))
            .body("deliveryAddress.postTown", is(DELIVERY_ADDRESS_POSTTOWN))
            .body("deliveryAddress.postcode", is(DELIVERY_ADDRESS_POSTCODE))
            .body("bankAccount.accountName", is(BANK_ACCOUNT_NAME))
            .body("bankAccount.sortCode", is(BANK_ACCOUNT_SORTCODE))
            .body("bankAccount.accountNumber", is(BANK_ACCOUNT_NUMBER))
            .body("bankAccount.bankName", is(BANK_NAME))
            .body("bankAccount.buildingName", is(BANK_ACCOUNT_BUILDING_NAME))
            .body("bankAccount.streetAddress", is(BANK_ACCOUNT_STREET_NAME))
            .body("bankAccount.locality", is(BANK_ACCOUNT_LOCALITY))
            .body("bankAccount.postTown", is(BANK_ACCOUNT_POSTTOWN))
            .body("bankAccount.postcode", is(BANK_ACCOUNT_POSTCODE))
            .body("isCompleted", is(true));
    }
}
