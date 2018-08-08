package com.ishoal.core.domain;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class PasswordResetTokenTest {

    @Test
    public void tokenForValidUserShouldReportItIsForValidUser() {
        assertThat(tokenForValidUser().isValidUser(), is(true));
    }

    @Test
    public void tokenForInvalidUserShouldReportItIsForInvalidUser() {
        assertThat(tokenForInvalidUser().isValidUser(), is(false));
    }

    @Test
    public void tokenShouldBeSerialisable() {
        assertThat(tokenForValidUser(), instanceOf(Serializable.class));
    }

    @Test
    public void tokenForValidUserShouldHoldUsername() {
        String username = randomUsername();
        assertThat(PasswordResetToken.forValidUser(username).getUsername(), is(username));
    }

    @Test
    public void tokenForInvalidUserShouldHoldUsername() {
        String username = randomUsername();
        assertThat(PasswordResetToken.forInvalidUser(username).getUsername(), is(username));
    }

    @Test
    public void tokenForValidUserShouldHaveVerificationCode() {
        assertThat(tokenForValidUser().getVerificationCode(), not((isEmptyOrNullString())));
    }
    
    @Test
    public void tokenForInvalidUserShouldNotHaveVerificationCode() {
        assertThat(tokenForInvalidUser().getVerificationCode(), is(nullValue()));
    }
    
    @Test
    public void tokenForValidUserShouldHaveExpiryDateTime() {
        assertThat(tokenForValidUser().getExpiryDateTime(), not((nullValue())));
    }
    
    @Test
    public void tokenForInvalidUserShouldNotHaveExpiryDateTime() {
        assertThat(tokenForInvalidUser().getExpiryDateTime(), is(nullValue()));
    }
    
    @Test
    public void failedVerificationCountShouldStartAtZero() {
        assertThat(tokenForValidUser().getFailedVerificationCount(), is(0));
    }
    
    @Test
    public void canIncrementFailedVerificationCount() {
        PasswordResetToken token = tokenForValidUser();
        token.incrementFailedVerificationCount();
        assertThat(token.getFailedVerificationCount(), is(1));
    }

    private PasswordResetToken tokenForValidUser() {
        return PasswordResetToken.forValidUser(randomUsername());
    }

    private PasswordResetToken tokenForInvalidUser() {
        return PasswordResetToken.forInvalidUser(randomUsername());
    }

    private String randomUsername() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}