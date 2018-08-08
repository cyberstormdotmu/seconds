package com.ishoal.core.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

// This class must be Serializable as it is stored in the session state.
public class PasswordResetToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_VALIDITY_MINUTES = 10;

    private String username;
    private boolean validUser;
    private String verificationCode;
    private DateTime expiryDateTime;
    private int failedVerificationCount = 0;

    private PasswordResetToken() {
        super();
    }

    private PasswordResetToken(String username, boolean validUser) {
        this();
        this.username = username;
        this.validUser = validUser;
    }

    public static PasswordResetToken forValidUser(String username) {
        PasswordResetToken token = new PasswordResetToken(username, true);
        token.verificationCode = RandomStringUtils.randomNumeric(VERIFICATION_CODE_LENGTH);
        token.expiryDateTime = DateTime.now().plusMinutes(VERIFICATION_CODE_VALIDITY_MINUTES);
        return token;
    }

    public static PasswordResetToken forInvalidUser(String username) {
        return new PasswordResetToken(username, false);
    }

    public String getUsername() {
        return username;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public DateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public int getFailedVerificationCount() {
        return failedVerificationCount;
    }

    public void incrementFailedVerificationCount() {
        this.failedVerificationCount++;
    }
}
