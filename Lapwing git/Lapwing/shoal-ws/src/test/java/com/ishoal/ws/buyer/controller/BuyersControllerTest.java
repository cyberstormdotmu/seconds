package com.ishoal.ws.buyer.controller;

import static com.ishoal.core.domain.BuyerProfile.aBuyerProfile;
import static com.ishoal.core.domain.User.aUser;
import static com.ishoal.core.domain.Users.someUsers;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.ActivateBuyerService;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.user.UserService;
import com.ishoal.ws.admin.controller.BuyersController;
import com.ishoal.ws.buyer.dto.RegistrationSummariesDto;

@RunWith(MockitoJUnitRunner.class)
public class BuyersControllerTest {

    private static final String USER_NAME = "rogerwatkins@gmail.com";
    private static final String INACTIVE = "INACTIVE";

    @Mock
    private UserService userService;
    @Mock
    private ManageBuyerProfileService manageBuyerProfileService;
    @Mock
    private ActivateBuyerService activateBuyerService;

    private ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    private ArgumentCaptor<FetchBuyerProfileRequest> fetchProfileRequestCaptor = ArgumentCaptor.forClass(FetchBuyerProfileRequest.class);

    private BuyersController buyersController;

    @Before
    public void before() {

        User user = aUser().username(USER_NAME).build();
        Users users = someUsers().addUser(aUser().username("1").build())
            .addUser(aUser().username("2").build()).addUser(aUser().username("3").build()).build();

       // buyersController = new BuyersController(userService, manageBuyerProfileService, activateBuyerService);
        when(userService.findByUsernameIgnoreCase(USER_NAME)).thenReturn(user);
        when(userService.fetchAllUsersWithoutARole()).thenReturn(users);
    }

    @Test
    public void shouldInvokeUserService_whenActivateBuyerCalled() {

        buyersController.activateBuyer(USER_NAME);

        verify(activateBuyerService).activateBuyer(any(User.class));
    }

    @Test
    public void shouldGetTheUserFromTheService() {

        buyersController.activateBuyer(USER_NAME);

        verify(userService).findByUsernameIgnoreCase(USER_NAME);
    }

    @Test
    public void shouldPassUserIntoService() {

        buyersController.activateBuyer(USER_NAME);

        verify(activateBuyerService).activateBuyer(userCaptor.capture());

        assertThat(userCaptor.getValue().getUsername(), is(USER_NAME));
    }

    @Test
    public void shouldReturnSuccess_whenUserUpdated() {

        ResponseEntity<?> responseEntity = buyersController.activateBuyer(USER_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldGiveErrorIfTheUserDoesNotExist() {

        ResponseEntity<?> responseEntity = buyersController.activateBuyer("nouserfound");

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturnSuccess_whenUsersFetched() {

        ResponseEntity<?> responseEntity = buyersController.fetchInactiveBuyers(INACTIVE);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldGetUsersFromService_whenUsersFetched() {
        buyersController.fetchInactiveBuyers(INACTIVE);

        verify(userService).fetchAllUsersWithoutARole();
    }

    @Test
    public void shouldReturnListOfUsersToClient_whenUsersFetched() {

        String noRole = INACTIVE;
        ResponseEntity<RegistrationSummariesDto> responseEntity = fetchUsersWithRole(noRole);

        assertThat(responseEntity.getBody().count(), is(3));
    }

    private ResponseEntity<RegistrationSummariesDto> fetchUsersWithRole(String noRole) {

        return (ResponseEntity<RegistrationSummariesDto>) buyersController.fetchInactiveBuyers(noRole);
    }


    @Before
    public void beforeFetchBuyerProfile() {
        when(manageBuyerProfileService.fetchProfile(fetchProfileRequestCaptor.capture()))
            .thenReturn(PayloadResult.success(aBuyerProfile()
                .user(aUser().build())
                .organisation(Organisation.anOrganisation().build())
                .createdDate(DateTime.now())
                .build()));
    }

    @Test
    public void shouldFetchTheUserSummaryDataFromTheBuyerProfileOnceForEveryUser() {

        buyersController.fetchInactiveBuyers(INACTIVE);

        List<FetchBuyerProfileRequest> allFetchRequests = fetchProfileRequestCaptor.getAllValues();

        assertThat(allFetchRequests.size(), is(3));
        Iterator<FetchBuyerProfileRequest> iterator = allFetchRequests.iterator();
        assertThat(iterator.next().getUser().getUsername(), is("1"));
        assertThat(iterator.next().getUser().getUsername(), is("2"));
        assertThat(iterator.next().getUser().getUsername(), is("3"));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void shouldPopulateUserSummary() {
        ResponseEntity<RegistrationSummariesDto> responseEntity = fetchUsersWithRole(INACTIVE);

        assertThat(responseEntity.getBody().getRegistrations().get(0).getBuyer(), is(notNullValue()));
        assertThat(responseEntity.getBody().getRegistrations().get(0).getOrganisation(), is(notNullValue()));
        assertThat(responseEntity.getBody().getRegistrations().get(0).getRegisteredDate(), is(notNullValue()));
    }

}