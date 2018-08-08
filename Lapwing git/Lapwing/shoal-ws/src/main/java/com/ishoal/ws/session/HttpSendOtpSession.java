package com.ishoal.ws.session;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.ishoal.core.domain.RegistrationOtp;

public class HttpSendOtpSession implements SendSmsSession{

	private static final String PASSWORD_RESET_TOKEN = "passwordResetToken";

	private final HttpSession httpSession;

    public HttpSendOtpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
    

    @Override
    public RegistrationOtp getPasswordResetToken() {
        return Optional.ofNullable((RegistrationOtp)httpSession.getAttribute(PASSWORD_RESET_TOKEN))
                .orElse(RegistrationOtp.forInvalidUser(null));
    }

    @Override
    public void setPasswordResetToken(RegistrationOtp token) {
        httpSession.setAttribute(PASSWORD_RESET_TOKEN, token);
    }
}
