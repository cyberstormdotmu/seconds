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

import org.apache.log4j.Logger;

import com.cubeia.backoffice.users.api.dto.AuthenticationResponse;
import com.cubeia.backoffice.users.client.UserServiceClient;
import com.cubeia.firebase.api.action.local.LoginRequestAction;
import com.cubeia.firebase.api.action.local.LoginResponseAction;
import com.cubeia.firebase.api.login.LoginHandler;

public class UserServiceLoginHandler implements LoginHandler {

	private Logger log = Logger.getLogger(this.getClass());
	
	private final UserServiceClient client;

	public UserServiceLoginHandler(UserServiceClient client) {
		this.client = client;
	}

	/**
	 * Authenticate the user agains the user service.
	 * If the {@link LoginRequestAction#getData()} and no username or password is set the bytes will be converted
	 * to a utf-8 string and the {@link UserServiceClient#authenticateSessionToken(String)} will be called.
	 * 
	 * If no credentials data is given username/password authentication will be used instead.
	 */
	@Override
	public LoginResponseAction handle(LoginRequestAction request) {
		ClassLoader originalClassLoader = UserServiceImpl.switchClassloader();
		try {
			Long operatorId = new Long(request.getOperatorid());
			
			AuthenticationResponse authenticate;
			if (shouldUseSessionAuth(request)) {
			    String tokenString = new String(request.getData(), "utf-8");
			    log.debug("found credentials data, using session based login: token = " + tokenString);
                authenticate = client.authenticateSessionToken(tokenString);
			} else {
                log.debug("found no credentials data, using password based login");
                authenticate = client.authenticate(operatorId, request.getUser(), request.getPassword());
			}
			    
			if (log.isDebugEnabled()) {
				log.debug("Authentication result from user-service for request["+request.getOperatorid()+","+request.getUser()+"] : "+authenticate);
			}
			
			LoginResponseAction response;
			if (authenticate.getAuthenticated()) {
				response = new LoginResponseAction(authenticate.getAuthenticated(), authenticate.getUserId().intValue());
				response.setScreenname(authenticate.getScreenname());
				
				// Set session token as credentials
				if (authenticate.getSessionToken() != null) {
					response.setAuthToken(authenticate.getAuthToken());
					response.setData(authenticate.getSessionToken().getBytes("utf-8"));
				}
				
			} else {
				log.debug("Login not authenticated for request " + request);
				response = new LoginResponseAction(authenticate.getAuthenticated(), -1);
			}
			
			return response;
		} catch (Exception e) {
			log.error("login failed", e);
			LoginResponseAction lra = new LoginResponseAction(false, -1);
			lra.setErrorMessage(e.getMessage());
			return lra;
		} finally {
			UserServiceImpl.restoreClassloader(originalClassLoader);
		}
	}

    protected boolean shouldUseSessionAuth(LoginRequestAction request) {
        return request.getData() != null  &&  request.getData().length > 0  
            &&  (request.getPassword() == null  ||  request.getPassword().isEmpty())  
            &&  (request.getUser() == null  ||  request.getUser().isEmpty());
    }

}
