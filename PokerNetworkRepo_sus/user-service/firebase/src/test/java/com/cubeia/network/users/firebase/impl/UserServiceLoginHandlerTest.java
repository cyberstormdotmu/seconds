/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.network.users.firebase.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cubeia.backoffice.users.api.dto.AuthenticationResponse;
import com.cubeia.backoffice.users.client.UserServiceClient;
import com.cubeia.firebase.api.action.local.LoginRequestAction;
import com.cubeia.firebase.api.action.local.LoginResponseAction;

public class UserServiceLoginHandlerTest {

    @Mock
    private UserServiceClient client;
    private UserServiceLoginHandler loginHandler;
    
    @Before
    public void setup() {
        initMocks(this);
        loginHandler = new UserServiceLoginHandler(client);
    }
    
    @Test
    public void testAuthenticateWithUserNamePasswordOk() {
        LoginRequestAction req = new LoginRequestAction("user", "password", 1);
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthenticated(true);
        authResponse.setSessionToken("token");
        authResponse.setScreenname("screenname");
        authResponse.setUserId(392L);
        when(client.authenticate(1L, "user", "password")).thenReturn(authResponse);
        
        LoginResponseAction loginResponse = loginHandler.handle(req);
        
        assertThat(loginResponse.isAccepted(), is(true));
        assertThat(loginResponse.getData(), is("token".getBytes()));
        assertThat(loginResponse.getErrorCode(), is(0));
        assertThat(loginResponse.getPlayerid(), is(392));
        assertThat(loginResponse.getScreenname(), is("screenname"));
    }

    @Test
    public void testAuthenticateWithUserNamePasswordFail() {
        LoginRequestAction req = new LoginRequestAction("user", "password", 1);
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthenticated(false);
        when(client.authenticate(1L, "user", "password")).thenReturn(authResponse);
        
        LoginResponseAction loginResponse = loginHandler.handle(req);
        
        verify(client, never()).authenticateSessionToken(Mockito.anyString());
        
        assertThat(loginResponse.isAccepted(), is(false));
        assertThat(loginResponse.getErrorCode(), is(0));
        assertThat(loginResponse.getPlayerid(), is(-1));
    }
    
    @Test
    public void testAuthenticateWithSessionToken() throws UnsupportedEncodingException {
        LoginRequestAction req = new LoginRequestAction(null, null, -1);
        req.setData("token".getBytes("utf-8"));
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthenticated(true);
        authResponse.setSessionToken("token");
        authResponse.setScreenname("screenname");
        authResponse.setUserId(392L);
        when(client.authenticateSessionToken("token")).thenReturn(authResponse);
        
        LoginResponseAction loginResponse = loginHandler.handle(req);
        
        assertThat(loginResponse.isAccepted(), is(true));
        assertThat(loginResponse.getData(), is("token".getBytes()));
        assertThat(loginResponse.getErrorCode(), is(0));
        assertThat(loginResponse.getPlayerid(), is(392));
        assertThat(loginResponse.getScreenname(), is("screenname"));
    }
    
    
    @Test
    public void testAuthenticateWithSessionTokenFail() {
        LoginRequestAction req = new LoginRequestAction(null, null, -1);
        req.setData("token".getBytes());
        
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthenticated(false);
        when(client.authenticateSessionToken("token")).thenReturn(authResponse);
        
        LoginResponseAction loginResponse = loginHandler.handle(req);
        
        verify(client, never()).authenticate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        
        assertThat(loginResponse.isAccepted(), is(false));
        assertThat(loginResponse.getErrorCode(), is(0));
        assertThat(loginResponse.getPlayerid(), is(-1));
    }    
    
    @Test
    public void testShouldUseSessionBasedAuthentication() {
        assertThat(loginHandler.shouldUseSessionAuth(createRequest(null, null, 0, "data")), is(true));
        assertThat(loginHandler.shouldUseSessionAuth(createRequest("", "", 0, "data")), is(true));
        assertThat(loginHandler.shouldUseSessionAuth(createRequest("", null, 0, "data")), is(true));
        assertThat(loginHandler.shouldUseSessionAuth(createRequest(null, "", 0, "data")), is(true));
        
        assertThat(loginHandler.shouldUseSessionAuth(createRequest(null, null, 0, null)), is(false));
        assertThat(loginHandler.shouldUseSessionAuth(createRequest(null, null, 0, "")), is(false));
    }

    private LoginRequestAction createRequest(String user, String pwd, int opId, String data) {
        LoginRequestAction request = new LoginRequestAction(user, pwd, opId);
        request.setData(data != null ? data.getBytes() : null);
        return request;
    }
}
