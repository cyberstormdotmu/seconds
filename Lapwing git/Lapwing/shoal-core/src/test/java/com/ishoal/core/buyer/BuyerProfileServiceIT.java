package com.ishoal.core.buyer;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.BuyerProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_EDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAFetchBuyerProfileRequest;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildRegisterBuyerRequest;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildUpdateBuyerProfileRequest;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = {ShoalCoreTestConfiguration.class})
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/user-test-data.sql")
@Sql("/sql/buyer-profile-test-data.sql")
@Transactional
public class BuyerProfileServiceIT {

    @Autowired
    private ManageBuyerProfileService manageBuyerProfileService;

    @Autowired
    private BuyerRegistrationService buyerRegistrationService;

    @Test
    public void shouldCreateUserProfile() {

        PayloadResult<BuyerProfile> result = createANewBuyerProfile();

        assertThat(result.isSuccess(), is(true));
        assertThat(result.getPayload().getCreatedDate(), is(notNullValue()));
        assertThat(result.getPayload().getModifiedDate(), is(notNullValue()));
    }

    @Test
    public void shouldFetchUserProfile() {

        createANewBuyerProfile();

        PayloadResult<BuyerProfile> response = manageBuyerProfileService.fetchProfile(
            buildAFetchBuyerProfileRequest());

        assertThat(response.isSuccess(), is(true));
        assertThat(response.getPayload().getUser().getUsername(), is(AN_EDITED_USER_PROFILE));
    }

    @Test
    public void shouldUpdateUserProfile() {

        createANewBuyerProfile();

        UpdateBuyerProfileRequest buyerProfileRequest = buildAnUpdateBuyerProfileRequest();

        PayloadResult<BuyerProfile> result = manageBuyerProfileService.updateProfile(buyerProfileRequest);

        assertThat(result.isSuccess(), is(true));

        PayloadResult<BuyerProfile> response = manageBuyerProfileService.fetchProfile(
            buildAFetchBuyerProfileRequest());

        assertThat(response.isSuccess(), is(true));

        BuyerProfile buyerProfile = response.getPayload();
        assertThat(buyerProfile.getOrganisation().getName(), is(buyerProfileRequest.getOrganisation().getName()));
    }

    private PayloadResult<BuyerProfile> createANewBuyerProfile() {

        return buyerRegistrationService.registerBuyer(buildACreateBuyerProfileRequest());
    }

    private RegisterBuyerRequest buildACreateBuyerProfileRequest() {

        return buildRegisterBuyerRequest().build();
    }

    private UpdateBuyerProfileRequest buildAnUpdateBuyerProfileRequest() {

        return buildUpdateBuyerProfileRequest().build();
    }
}
