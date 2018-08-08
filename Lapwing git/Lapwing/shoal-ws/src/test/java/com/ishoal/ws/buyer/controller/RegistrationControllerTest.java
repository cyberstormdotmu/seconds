package com.ishoal.ws.buyer.controller;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.buyer.BuyerProfileServiceTestData;
import com.ishoal.core.buyer.RegisterBuyerRequest;
import com.ishoal.core.buyer.BuyerRegistrationService;
import com.ishoal.ws.buyer.dto.BuyerRegistrationDto;
import com.ishoal.ws.buyer.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.ishoal.core.domain.BuyerProfile.aBuyerProfile;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_EDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.ORGANISATION_NAME;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAnOrganisation;
import static com.ishoal.core.matchers.GenericMatcher.lamdaMatch;
import static com.ishoal.ws.buyer.dto.BuyerRegistrationDto.aBuyerRegistrationDto;
import static com.ishoal.ws.buyer.dto.OrganisationDto.anOrganisationDto;
import static com.ishoal.ws.buyer.dto.UserDto.aNewUserDto;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @Mock
    private BuyerRegistrationService buyerRegistrationService;

    private RegistrationController controller;

    private ArgumentCaptor<RegisterBuyerRequest> createRequest = ArgumentCaptor.forClass(RegisterBuyerRequest.class);


    private BuyerProfile goodProfile;


    @Before
    public void before() {

        goodProfile = aBuyerProfile().id(1L)
            .user(BuyerProfileServiceTestData.buildAUser().build())
            .organisation(buildAnOrganisation())
            .build();

        when(buyerRegistrationService.registerBuyer(createRequest.capture()))
            .thenReturn(PayloadResult.success(goodProfile));

        when(buyerRegistrationService.registerBuyer(userNameEquals("nosuchuser")))
            .thenReturn(PayloadResult.error(ErrorType.CONFLICT, "an error occured"));

        when(buyerRegistrationService.registerBuyer(userNameEquals("invalidUser")))
            .thenReturn(PayloadResult.error("something went wrong"));

        controller = new RegistrationController(buyerRegistrationService);
    }

    @Test
    public void shouldGiveACreatedResponseWhenCreateProfileSuccessful() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();
        ResponseEntity<?> responseEntity = controller.register(BuyerRegistrationDto);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldCallBuyerProfileService_whenCreateProfile() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();

        controller.register(BuyerRegistrationDto);

        verify(buyerRegistrationService).registerBuyer(any(RegisterBuyerRequest.class));
    }

    @Test
    public void shouldRaiseErrorWhenServiceReportsUnsuccessful() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();

        controller.register(BuyerRegistrationDto);
    }

    @Test
    public void shouldSendRequestToService_whenCreateProfile() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();

        controller.register(BuyerRegistrationDto);

        assertThat(createRequestPassedToService().getUser().getUsername(), is(AN_EDITED_USER_PROFILE));
        assertThat(createRequestPassedToService().getOrganisation().getName(), is(ORGANISATION_NAME));
    }

    @Test
    public void shouldRaiseConflictError_WhenServiceReportsUserExists() {

        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto()
            .buyer(buildAUser().emailAddress("nosuchuser").build()).build();

        ResponseEntity<?> result = controller.register(BuyerRegistrationDto);

        assertThat(result.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldRaiseGenericError_WhenServiceReportsAnyOtherError() {

        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto()
            .buyer(buildAUser().emailAddress("invalidUser").build()).build();

        ResponseEntity<?> result = controller.register(BuyerRegistrationDto);

        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldClearOutThePasswordInTheDto() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();

        controller.register(BuyerRegistrationDto);

        assertThat(BuyerRegistrationDto.getBuyer().getPassword(), is(""));
    }

    @Test
    public void shouldHashTheUserPassword() {
        BuyerRegistrationDto BuyerRegistrationDto = buildABuyerRegistrationDto().build();

        controller.register(BuyerRegistrationDto);

        assertThat(createRequest.getValue().getUser().getHashedPassword().getValue(), startsWith("$2a$10$"));
    }

    public static BuyerRegistrationDto.Builder buildABuyerRegistrationDto() {

        return aBuyerRegistrationDto().buyer(buildAUser()
            .build())
            .organisation(anOrganisationDto().name(ORGANISATION_NAME)
                .registrationNumber("07496791")
                .build());
    }

    private static UserDto.Builder buildAUser() {

        return aNewUserDto().emailAddress(AN_EDITED_USER_PROFILE)
            .firstName("roger").surname("watkins")
            .password("PASSWORD_SHOULD_NEVER_BE_VISIBLE");
    }

    private RegisterBuyerRequest createRequestPassedToService() {

        return createRequest.getValue();
    }

    public RegisterBuyerRequest userNameEquals(String userName) {

        return (RegisterBuyerRequest) argThat(lamdaMatch(obj -> userName.equals(((RegisterBuyerRequest)obj).getUser().getUsername())));
    }
}