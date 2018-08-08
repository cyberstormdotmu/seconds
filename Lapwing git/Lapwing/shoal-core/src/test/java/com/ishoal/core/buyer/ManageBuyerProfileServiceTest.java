package com.ishoal.core.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.repository.BuyerProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_UNEDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.ORGANISATION_NAME;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildIncompleteProfile;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildUpdateBuyerProfileRequest;
import static com.ishoal.core.buyer.FetchBuyerProfileRequest.aFetchBuyerProfileRequest;
import static com.ishoal.core.matchers.GenericMatcher.lamdaMatch;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManageBuyerProfileServiceTest {

    private static final long USER_ID = 1L;
    public static final String INCOMPLETE_PROFILE = "incompleteProfile";
    public static final String COMPLETE_PROFILE = "completeProfile";

    @Mock
    private BuyerProfileEntityRepository buyerProfileEntityRepository;

    @Mock
    private BuyerProfileRepository buyerProfileRepository;

    private User anIncompleteProfileUser = User.aUser().username(INCOMPLETE_PROFILE).build();

    private ManageBuyerProfileService manageBuyerProfileService;

    private ArgumentCaptor<BuyerProfile> updatedProfile = ArgumentCaptor.forClass(BuyerProfile.class);

    @Before
    public void before() {

        BuyerProfile anIncompleteProfile = buildAnIncompleteBuyerProfile();
        BuyerProfile aCompleteProfile = buildACompleteBuyerProfile();

        manageBuyerProfileService = new ManageBuyerProfileService(buyerProfileRepository);

        when(buyerProfileRepository.updateBuyerProfile(userNameEquals(INCOMPLETE_PROFILE), updatedProfile.capture())).thenReturn(

            anIncompleteProfile);
        when(buyerProfileRepository.fetchBuyerProfile(userNameEquals(COMPLETE_PROFILE))).thenReturn(aCompleteProfile);
        when(buyerProfileRepository.fetchBuyerProfile(userNameEquals(INCOMPLETE_PROFILE))).thenReturn(
            anIncompleteProfile);
    }

    @Test
    public void shouldFetchBuyerProfile_whenFetchRequestIsMade() {

        FetchBuyerProfileRequest request = buildAFetchRequest();
        manageBuyerProfileService.fetchProfile(request);

        verify(buyerProfileRepository).fetchBuyerProfile(any(User.class));
    }
    
    @Test
    public void shouldReturnABuyerProfile_whenFetchRequestIsMade() {

        PayloadResult<BuyerProfile> response = manageBuyerProfileService.fetchProfile(buildAFetchRequest());

        assertServiceSuccessful(response);
        assertThat(response.getPayload(), is(notNullValue()));
    }

    @Test
    public void shouldFailFetchWhenBuyerProfileDoesNotExist() {

        PayloadResult<BuyerProfile> result = manageBuyerProfileService.fetchProfile(buildABadFetchRequest());

        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldUpdateBuyerProfile_whenUpdateRequestIsMade() {

        manageBuyerProfileService.updateProfile(buildUpdateBuyerProfileRequestForAnyUser());

        verify(buyerProfileRepository).updateBuyerProfile(any(User.class), any(BuyerProfile.class));
        assertThat(updatedProfile.getValue().getOrganisation().getName(), is(ORGANISATION_NAME));
        assertThat(updatedProfile.getValue().getContact(), is(notNullValue()));
        assertThat(updatedProfile.getValue().getBankAccount(), is(notNullValue()));
    }

    @Test
    public void shouldPersistProfileStatusComplete_whenUpdateRequestIsMade() {
        manageBuyerProfileService.updateProfile(buildUpdateBuyerProfileRequestForAnyUser());

        assertThat(updatedProfile.getValue().isCompleted(), is(true));
    }

    @Test
    public void shouldReturnSuccess_whenUpdateRequestIsMade() {

        PayloadResult<BuyerProfile> result = manageBuyerProfileService.updateProfile(
            buildUpdateBuyerProfileRequestForAUserWithAnUneditdedProfile());

        assertThat(result.isSuccess(), is(true));
    }

    @Test
    public void shouldReturnSavedEntityInPayload_whenRepositoryReturns() {

        PayloadResult<BuyerProfile> result = manageBuyerProfileService.updateProfile(
            buildUpdateBuyerProfileRequestForAUserWithAnUneditdedProfile());

        assertThat(result.getPayload(), is(notNullValue()));
    }

    @Test
    public void shouldFailUpdateWhenBuyerProfileDoesNotExist() {

        PayloadResult<BuyerProfile> result = manageBuyerProfileService.updateProfile(buildABadBuyerProfileRequest());

        assertThat(result.isError(), is(true));
    }


    private FetchBuyerProfileRequest buildAFetchRequest() {

        return aFetchBuyerProfileRequest().user(anIncompleteProfileUser).build();
    }

    private void assertServiceSuccessful(PayloadResult<BuyerProfile> response) {

        assertThat(response.getError(), response.isSuccess(), is(true));
    }

    private FetchBuyerProfileRequest buildABadFetchRequest() {

        return aFetchBuyerProfileRequest().user(aBadUser()).build();
    }

    private UpdateBuyerProfileRequest buildUpdateBuyerProfileRequestForAUserWithAnUneditdedProfile() {

        return buildUpdateBuyerProfileRequest().user(anIncompleteProfileUser).build();
    }

    private UpdateBuyerProfileRequest buildUpdateBuyerProfileRequestForAnyUser() {

        return buildUpdateBuyerProfileRequest().user(anIncompleteProfileUser).build();
    }

    private UpdateBuyerProfileRequest buildABadBuyerProfileRequest() {

        return buildUpdateBuyerProfileRequest().user(aBadUser()).build();
    }

    private User aBadUser() {

        return buildAUser().username("thisguydoesntexist").build();
    }

    private BuyerProfile buildAnIncompleteBuyerProfile() {

        return buildIncompleteProfile().user(User.aUser().id(new UserId(USER_ID)).username(AN_UNEDITED_USER_PROFILE).build())
            .isCompleted(false)
            .build();
    }

    private BuyerProfile buildACompleteBuyerProfile() {
        return buildIncompleteProfile().user(User.aUser().id(new UserId(USER_ID)).username(AN_UNEDITED_USER_PROFILE).build())
            .isCompleted(true)
            .build();
    }

    public User userNameEquals(String userName) {

        return (User) argThat(lamdaMatch(obj -> userName.equals(((User)obj).getUsername())));
    }

}