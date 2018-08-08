package com.ishoal.ws.session;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.session.StandardSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.ishoal.core.domain.PasswordResetToken;

public class HttpPasswordResetSessionTest {

    private HttpPasswordResetSession session;
    
    @Before
    public void before() {
        StandardManager manager = new StandardManager();
        manager.setContext(new StandardContext());
        StandardSession httpSession = new StandardSession(manager);
        httpSession.setValid(true);
        this.session = new HttpPasswordResetSession(httpSession);
    }
    
    @Test
    public void shouldReturnTokenForInvalidUserByDefault() {
        assertThat(this.session.getPasswordResetToken().isValidUser(), is(false));
    }
    
    @Test
    public void shouldReturnTokenThatHasBeenSet() {
        PasswordResetToken token = PasswordResetToken.forValidUser(RandomStringUtils.randomAlphanumeric(10));
        this.session.setPasswordResetToken(token);
        assertThat(this.session.getPasswordResetToken(), is(token));
    }
}