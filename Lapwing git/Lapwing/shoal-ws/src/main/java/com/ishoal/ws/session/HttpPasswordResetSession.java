package com.ishoal.ws.session;

import com.ishoal.core.domain.PasswordResetToken;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class HttpPasswordResetSession implements PasswordResetSession {
    private static final String PASSWORD_RESET_TOKEN = "passwordResetToken";

    private final HttpSession httpSession;

    public HttpPasswordResetSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }


    @Override
    public PasswordResetToken getPasswordResetToken() {
        return Optional.ofNullable((PasswordResetToken)httpSession.getAttribute(PASSWORD_RESET_TOKEN))
                .orElse(PasswordResetToken.forInvalidUser(null));
    }

    @Override
    public void setPasswordResetToken(PasswordResetToken token) {
        httpSession.setAttribute(PASSWORD_RESET_TOKEN, token);
    }
}
