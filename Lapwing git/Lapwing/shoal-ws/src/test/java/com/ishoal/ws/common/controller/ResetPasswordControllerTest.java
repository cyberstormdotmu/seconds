package com.ishoal.ws.common.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.SimpleResult;
import com.ishoal.core.domain.PasswordResetToken;
import com.ishoal.core.user.ResetPasswordService;
import com.ishoal.ws.common.dto.ResetPasswordRequestDto;
import com.ishoal.ws.session.PasswordResetSession;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordControllerTest {

    @Mock
    private ResetPasswordService mockResetPasswordService;

    @Mock
    private PasswordResetSession mockPasswordResetSession;

    private ResetPasswordController controller;

    @Before
    public void before() {
        this.controller = new ResetPasswordController(this.mockResetPasswordService);
    }

    /*@Test
    public void shouldReturnOkResponseWhenInitiatePasswordResetSucceeds() {
        whenInitiatePasswordResetSucceeds();
        assertThat(initiatePasswordReset().getStatusCode(), is(HttpStatus.OK));
    }*/

    @Test
    public void shouldReturnOkResponseWhenResetPasswordSucceeds() {
        whenResetPasswordSucceeds();
        assertThat(resetPassword().getStatusCode(), is(HttpStatus.OK));
    }

    /*private ResponseEntity<?> initiatePasswordReset() {
        return this.controller.initiatePasswordReset(randomUsername(), this.mockPasswordResetSession);
    }*/

    private ResponseEntity<?> resetPassword() {
        return this.controller.resetPassword(ResetPasswordRequestDto.aResetPasswordRequest().build(),
                this.mockPasswordResetSession);
    }

    private String randomUsername() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private void whenResetPasswordSucceeds() {
        when(this.mockResetPasswordService.resetPassword(any(PasswordResetToken.class), anyString(), anyString()))
                .thenReturn(SimpleResult.success());
    }

    private void whenInitiatePasswordResetSucceeds() {
        when(this.mockResetPasswordService.initiatePasswordReset(anyString())).thenAnswer(invocation -> {
            return PayloadResult.success(PasswordResetToken.forValidUser(invocation.getArgumentAt(0, String.class)));
        });
    }
}