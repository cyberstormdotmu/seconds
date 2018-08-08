package com.ishoal.core.domain;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

public class RegistrationOtp implements Serializable {

	private static final long serialVersionUID = 1L;
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_VALIDITY_MINUTES = 10;

    private String username;
    private boolean validUser;
    private String verificationCode;
    private DateTime expiryDateTime;
    private int failedVerificationCount = 0;

    private RegistrationOtp() {
        super();
    }

    private RegistrationOtp(String username, boolean validUser) {
        this();
        this.username = username;
        this.validUser = validUser;
    }

    public static RegistrationOtp forValidUser(String username) {
    	RegistrationOtp token = new RegistrationOtp(username, true);
        token.verificationCode = RandomStringUtils.randomNumeric(VERIFICATION_CODE_LENGTH);
        token.expiryDateTime = DateTime.now().plusMinutes(VERIFICATION_CODE_VALIDITY_MINUTES);
        return token;
    }

    public static RegistrationOtp forInvalidUser(String username) {
        return new RegistrationOtp(username, false);
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
