package com.ishoal.ws.common.dto;

public class ResetPasswordRequestDto {
    private String verificationCode;
    private String newPassword;

    private ResetPasswordRequestDto() {
        super();
    }

    private ResetPasswordRequestDto(Builder builder) {
        this();
        verificationCode = builder.verificationCode;
        newPassword = builder.newPassword;
    }

    public static Builder aResetPasswordRequest() {
        return new Builder();
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public static final class Builder {
        private String verificationCode;
        private String newPassword;

        private Builder() {
        }

        public Builder verificationCode(String val) {
            verificationCode = val;
            return this;
        }

        public Builder newPassword(String val) {
            newPassword = val;
            return this;
        }

        public ResetPasswordRequestDto build() {
            return new ResetPasswordRequestDto(this);
        }
    }
}
