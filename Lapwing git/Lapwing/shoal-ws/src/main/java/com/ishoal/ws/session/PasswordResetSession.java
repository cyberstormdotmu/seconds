package com.ishoal.ws.session;

import com.ishoal.core.domain.PasswordResetToken;

public interface PasswordResetSession {
    PasswordResetToken getPasswordResetToken();
    void setPasswordResetToken(PasswordResetToken token);
}
