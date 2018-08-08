package com.ishoal.ws.session;

import com.ishoal.core.domain.RegistrationOtp;

public interface SendSmsSession {
	RegistrationOtp getPasswordResetToken();
    void setPasswordResetToken(RegistrationOtp token);
}
