package com.ishoal.ws.buyer.controller;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_EDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.CONTACT_EMAIL;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.ORGANISATION_NAME;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAnOrganisation;
import static com.ishoal.ws.buyer.dto.BankAccountDto.aBankAccount;
import static com.ishoal.ws.buyer.dto.BuyerProfileDto.aBuyerProfileDto;
import static com.ishoal.ws.buyer.dto.ContactDto.aContact;
import static com.ishoal.ws.buyer.dto.OrganisationDto.anOrganisationDto;
import static com.ishoal.ws.common.dto.AddressDto.anAddressDto;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.buyer.UpdateBuyerProfileRequest;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.session.BuyerSession;

@RunWith(MockitoJUnitRunner.class)
public class BuyerProfileControllerTest {

    private static final String USER_NAME = AN_EDITED_USER_PROFILE;
    @Mock
    private ManageBuyerProfileService manageBuyerProfileService;
    private BuyerProfileController controller;

    private ArgumentCaptor<FetchBuyerProfileRequest> fetchRequest = ArgumentCaptor.forClass(FetchBuyerProfileRequest.class);
    private ArgumentCaptor<UpdateBuyerProfileRequest> updateRequest = ArgumentCaptor.forClass(UpdateBuyerProfileRequest.class);


    private User user;


    @Before
    public void before() {

        BuyerProfile payload = aBuyerProfile().build();

        when(manageBuyerProfileService.fetchProfile(fetchRequest.capture()))
            .thenReturn(PayloadResult.success(payload));

        when(manageBuyerProfileService.updateProfile(updateRequest.capture()))
            .thenReturn(PayloadResult.success(payload));

        controller = new BuyerProfileController(manageBuyerProfileService);

        user = buildAUser().build();
    }

    private BuyerProfile.Builder aBuyerProfile() {

        return BuyerProfile.aBuyerProfile().id(1L)
            .user(buildAUser().build())
            .isCompleted(true)
            .organisation(buildAnOrganisation());
    }

    @Test
    public void shouldGiveAnOKResponse_WhenFetchProfileSuccessful(BuyerSession buyerSession, boolean useSessionUser) {

        ResponseEntity<?> response = controller.fetchProfile(buyerSession, buildAUser().build(), useSessionUser);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldCallBuyerProfileService_whenFetchProfile(BuyerSession buyerSession, boolean useSessionUser) {
        controller.fetchProfile(buyerSession, buildAUser().build(), useSessionUser);

        verify(manageBuyerProfileService).fetchProfile(any(FetchBuyerProfileRequest.class));
    }

    @Test
    public void shouldSendRequestToTheService_whenFetchProfile(BuyerSession buyerSession, boolean useSessionUser) {

        controller.fetchProfile(buyerSession,user, useSessionUser);

        assertThat(fetchRequestPasedToService().getUser(), is(user));
    }

    @Test
    public void shouldReturnProfileStatusToClient_whenFetchProfile(BuyerSession buyerSession, boolean useSessionUser) {

        testControllerReturnsProfileStatusWhenFetched(false, buyerSession, useSessionUser);
        testControllerReturnsProfileStatusWhenFetched(true, buyerSession, useSessionUser);

        testControllerReturnsProfileStatusWhenUpdated(false);
        testControllerReturnsProfileStatusWhenUpdated(true);
    }

    @Test
    public void shouldReturnPayloadToClient_whenFetchProfileSuccessful(BuyerSession buyerSession, boolean useSessionUser) {

        ResponseEntity<BuyerProfileDto> response = (ResponseEntity<BuyerProfileDto>)controller.fetchProfile(buyerSession, buildAUser().build(), useSessionUser);

        assertThat(response.getBody().getOrganisation().getName(), is(ORGANISATION_NAME));
    }

    @Test
    public void shouldReturnServerError_whenFetchProfileFails() {
        when(manageBuyerProfileService.updateProfile(updateRequest.capture()))
            .thenReturn(PayloadResult.error("Could not have the buyer profile"));

        ResponseEntity<?> responseEntity = controller.updateProfile(user, buildABuyerProfile());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldGiveAnOKResponse_whenUpdatedProfileSuccessful() {

        ResponseEntity<?> response = controller.updateProfile(user, buildABuyerProfile());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldCallBuyerProfileService_whenUpdateProfile() {

        controller.updateProfile(user, buildABuyerProfile());

        verify(manageBuyerProfileService).updateProfile(any(UpdateBuyerProfileRequest.class));
    }

    @Test
    public void shouldSendRequestToTheService_whenUpdateProfile() {

        controller.updateProfile(user, buildABuyerProfile());

        assertThat(updateRequestPassedToService().getUser().getUsername(), is(USER_NAME));
        assertThat(updateRequestPassedToService().getOrganisation().getName(), is(ORGANISATION_NAME));
        assertThat(updateRequestPassedToService().getContact().getEmailAddress(), is(CONTACT_EMAIL));
        assertThat(updateRequestPassedToService().getBankAccount(), is(notNullValue()));
    }

    @Test
    public void shouldReturnServerError_whenUpdateProfileFails() {

        when(manageBuyerProfileService.updateProfile(updateRequest.capture()))
            .thenReturn(PayloadResult.error("Could not have the buyer profile"));

        ResponseEntity<?> responseEntity = controller.updateProfile(user, buildABuyerProfile());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    private void testControllerReturnsProfileStatusWhenFetched(boolean isCompleted,BuyerSession buyerSession, boolean useSessionUser) {

        when(manageBuyerProfileService.fetchProfile(any(FetchBuyerProfileRequest.class)))
            .thenReturn(PayloadResult.success(aBuyerProfile().isCompleted(isCompleted).build()));

        ResponseEntity<BuyerProfileDto> response = (ResponseEntity<BuyerProfileDto>)controller.fetchProfile(buyerSession, buildAUser().build(), useSessionUser);

        assertThat(response.getBody().isIsCompleted(), is(isCompleted));
    }

    private void testControllerReturnsProfileStatusWhenUpdated(boolean isCompleted) {
        when(manageBuyerProfileService.updateProfile(any(UpdateBuyerProfileRequest.class)))
            .thenReturn(PayloadResult.success(aBuyerProfile().isCompleted(isCompleted).build()));

        ResponseEntity<BuyerProfileDto> response =
            (ResponseEntity<BuyerProfileDto>)controller.updateProfile(buildAUser().build(), buildABuyerProfile());

        assertThat(response.getBody().isIsCompleted(), is(isCompleted));
    }

    private static BuyerProfileDto buildABuyerProfile() {

        return aBuyerProfileDto()
            .organisation(anOrganisationDto().name(ORGANISATION_NAME).registrationNumber("07496791").build())
            .contact(aContact().title("Mr").firstName("Roger").surname("Watkins")
                .emailAddress(CONTACT_EMAIL).password("password").phoneNumber("07493845094").build())
            .bankAccount(aBankAccount().accountName("MR R E WATKINS").sortCode("88-88-88")
                .accountNumber("88888888").bankName("Barclays").buildingName("1").streetAddress("Churchill place")
                .locality("Canary Wharf").postTown("LONDON").postcode("E145HP").build())
            .addresses(expectedaddresses())
            .build();
    }

    private static List<AddressDto> expectedaddresses() {
        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(anAddressDto().departmentName("Sales").buildingName("BOSL House").streetAddress("High Street").locality("Bramham").postTown("Wetherby").postcode("LS23 6QR").build());
        addresses.add(anAddressDto().departmentName("Marketing").buildingName("The Hive").streetAddress("High Street").locality("Pateley Bridge").postTown("Harrogate").postcode("HG3 5QF").build());
        return addresses;
    }

    
    private FetchBuyerProfileRequest fetchRequestPasedToService() {

        return fetchRequest.getValue();
    }

    private UpdateBuyerProfileRequest updateRequestPassedToService() {

        return updateRequest.getValue();
    }
}