package com.ishoal.core.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.repository.BuyerProfileRepository;
import com.ishoal.core.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_EDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_UNEDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildRegisterBuyerRequest;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildIncompleteProfile;
import static com.ishoal.core.matchers.GenericMatcher.lamdaMatch;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuyerRegistrationServiceTest {

    private static final long USER_ID = 1L;
    public static final String INCOMPLETE_PROFILE = "incompleteProfile";

    @Mock
    private BuyerProfileEntityRepository buyerProfileEntityRepository;

    @Mock
    private BuyerProfileRepository buyerProfileRepository;

    @Mock
    private UserRepository userRepository;

    private User anIncompleteProfileUser = User.aUser().username(INCOMPLETE_PROFILE).build();

    private BuyerRegistrationService buyerRegistrationService;

    private ArgumentCaptor<BuyerProfile> createdProfile = ArgumentCaptor.forClass(BuyerProfile.class);

    private ArgumentCaptor<User> userSavedToRepository = ArgumentCaptor.forClass(User.class);

    @Before
    public void before() {

        BuyerProfile anIncompleteProfile = buildAnIncompleteBuyerProfile();

        buyerRegistrationService = new BuyerRegistrationService(buyerProfileRepository, userRepository);

        when(userRepository.save(userSavedToRepository.capture())).thenReturn(anIncompleteProfileUser);

        when(buyerProfileRepository.createBuyerProfile(userNameEquals(INCOMPLETE_PROFILE), createdProfile.capture())).thenReturn(

            anIncompleteProfile);
    }

    @Test
    public void shouldSaveUserEntity_whenRegisterBuyerRequestIsMade() {

        buyerRegistrationService.registerBuyer(buildARegisterBuyerRequestRequest());

        verify(userRepository).save(any(User.class));
        assertThat(userSavedToRepository.getValue().getUsername(), is(AN_EDITED_USER_PROFILE));
    }

    @Test
    public void shouldSaveBuyerProfileEntity_whenRegisterBuyerRequestIsMade() {

        buyerRegistrationService.registerBuyer(buildARegisterBuyerRequestRequest());

        verify(buyerProfileRepository).createBuyerProfile(any(User.class), any(BuyerProfile.class));
        assertThat(createdProfile.getValue().getOrganisation().getName(), is(notNullValue()));
    }

    @Test
    public void shouldReturnEntityInPayload() {

        PayloadResult<BuyerProfile> result = buyerRegistrationService.registerBuyer(
            buildARegisterBuyerRequestRequest());

        assertThat(result.getPayload(), is(notNullValue()));
    }


    private RegisterBuyerRequest buildARegisterBuyerRequestRequest() {

        return buildRegisterBuyerRequest().build();
    }

    private BuyerProfile buildAnIncompleteBuyerProfile() {

        return buildIncompleteProfile().user(User.aUser().id(new UserId(USER_ID)).username(AN_UNEDITED_USER_PROFILE).build())
            .isCompleted(false)
            .build();
    }

    public User userNameEquals(String userName) {

        return (User) argThat(lamdaMatch(obj -> userName.equals(((User)obj).getUsername())));
    }
}