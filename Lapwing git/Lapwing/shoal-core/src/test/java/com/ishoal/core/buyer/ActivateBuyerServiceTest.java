package com.ishoal.core.buyer;

import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.email.EmailMessage;
import com.ishoal.email.EmailService;
import com.ishoal.email.TemplatePropertiesFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.domain.User.aUser;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivateBuyerServiceTest {
    public static final String USER_NAME = "ABuyer@buyerland.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    private TemplatePropertiesFactory templatePropertiesFactory;

    @Captor
    private ArgumentCaptor<EmailMessage> emailMessageSentToBuyer;

    private ArgumentCaptor<User> userSavedToRepository = ArgumentCaptor.forClass(User.class);
    private ActivateBuyerService activateBuyerService;


    @Before
    public void before() {

        User user = buildAUser().build();

        Users users = Users.someUsers().addUser(user).addUser(user).addUser(user).build();

        templatePropertiesFactory = new TemplatePropertiesFactory("http://domain");

        when(userRepository.findByUsernameIgnoreCase(USER_NAME)).thenReturn(user);
        when(userRepository.save(userSavedToRepository.capture())).thenReturn(user);
        when(userRepository.findAllUsersWithoutARole()).thenReturn(users);

        activateBuyerService = new ActivateBuyerService(emailService, templatePropertiesFactory, userRepository);
    }

    @Test
    public void shouldBeAbleToMakeUserIntoABuyer() {
        User user = aUser().username(USER_NAME).build();
        activateBuyerService.activateBuyer(user);

        verify(userRepository).save(any(User.class));
        assertThat(userSavedToRepository.getValue().getRoles().iterator().hasNext(), is(true));
        assertThat(userSavedToRepository.getValue().getRoles().iterator().next()
            .getName(), is("BUYER"));
    }

    @Test
    public void shouldSendAConfirmationMessageToTheBuyer() {

        User user = aUser().username(USER_NAME).build();
        activateBuyerService.activateBuyer(user);

        verify(emailService).sendMessage(emailMessageSentToBuyer.capture());
        assertThat(emailMessageSentToBuyer.getValue().getRecipient().getAddress(), is(user.getEmailAddress()));
        assertThat(emailMessageSentToBuyer.getValue().getSubject(), is(notNullValue()));
        assertThat(emailMessageSentToBuyer.getValue().getText(), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseErrorIfTryingToactivateBuyer_whenUserDoesNotExist() {
        User user = aUser().username("noSuchUser").build();
        activateBuyerService.activateBuyer(user);
    }
}