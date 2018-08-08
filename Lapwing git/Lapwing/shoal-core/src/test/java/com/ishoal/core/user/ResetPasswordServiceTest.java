package com.ishoal.core.user;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.core.domain.User;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.sms.SmsService;

/**
 * These tests have been written around the implementation of the
 * ResetPasswordService as it existed. It is clearly a work in progress and has
 * not been finished. Hence the fact it sends sms to a fixed recipient, etc.
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private SmsService mockSmsService;

    private ResetPasswordService resetPasswordService;

    @Before
    public void before() {
        this.resetPasswordService = new ResetPasswordService(this.mockUserRepository, this.mockSmsService);
    }

    @Test
    public void initiatingPasswordResetForAValidUserShouldCauseSmsToBeSent() {
        whenUserRepositoryFindsAUser();
        this.resetPasswordService.initiatePasswordReset(randomUsername());
        verify(this.mockSmsService).sendSms(anyString(), anyString());
    }
    
    @Test
    public void initiatingPasswordResetForANonExistantUserShouldNotCauseSmsToBeSent() {
        whenUserRepositoryDoesNotFindAUser();
        this.resetPasswordService.initiatePasswordReset(randomUsername());
        verify(this.mockSmsService, never()).sendSms(anyString(), anyString());
    }
    
    private void whenUserRepositoryFindsAUser() {
        when(this.mockUserRepository.findByUsernameIgnoreCase(anyString())).thenReturn(User.aUser().build());
    }
    
    private void whenUserRepositoryDoesNotFindAUser() {
        when(this.mockUserRepository.findByUsernameIgnoreCase(anyString())).thenReturn(null);
    }

    private String randomUsername() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}