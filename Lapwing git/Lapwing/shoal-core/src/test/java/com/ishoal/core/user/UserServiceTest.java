package com.ishoal.core.user;

import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.domain.User.aUser;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final String USER_NAME = "ABuyer@buyerland.com";

    @Mock
    private UserRepository userRepository;

    private ArgumentCaptor<User> userSavedToRepository = ArgumentCaptor.forClass(User.class);
    private UserService userService;


    @Before
    public void before() {

        User user = buildAUser().build();

        Users users = Users.someUsers().addUser(user).addUser(user).addUser(user).build();

        when(userRepository.findByUsernameIgnoreCase(USER_NAME)).thenReturn(user);
        when(userRepository.save(userSavedToRepository.capture())).thenReturn(user);
        when(userRepository.findAllUsersWithoutARole()).thenReturn(users);

        /*userService = new UserService(userRepository);*/
    }

    @Test
    public void shouldSaveAUserToTheRepository() {
        User user = aUser().username(USER_NAME).build();

        userRepository.save(user);

        verify(userRepository).save(user);
    }

    @Test
    public void shouldNotAddAnyRolesToANewUser() {
        User user = aUser().username(USER_NAME).build();

        userRepository.save(user);

        assertThat(user.getRoles().iterator().hasNext(), is(false));
    }

    @Test
    public void shouldFetchAllUsersWithoutRolesFromRepository() {
        userService.fetchAllUsersWithoutARole();

        verify(userRepository).findAllUsersWithoutARole();
    }

    @Test
    public void shouldReturnUsersToCaller() {

        Users users = userService.fetchAllUsersWithoutARole();

        assertThat(users.count(), is(3));
    }
}