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
package com.cubeia.network.shared.web.wicket;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class SecureWicketAuthenticatedWebSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 3355101222374558750L;

	private static final Logger log = Logger.getLogger(SecureWicketAuthenticatedWebSession.class);

	@SpringBean(name = "authenticationManager")
	private AuthenticationManager authenticationManager;

	private String username;

	private Collection<? extends GrantedAuthority> authorities;

	public SecureWicketAuthenticatedWebSession(Request request) {
		super(request);
		injectDependencies();
		ensureDependenciesNotNull();
	}

	private void ensureDependenciesNotNull() {
		if (authenticationManager == null) {
			throw new IllegalStateException("An authenticationManager is required.");
		}
	}

	private void injectDependencies() {
		Injector.get().inject(this);
	}
	
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean authenticate(String username, String password) {
		this.username = username;
		boolean authenticated = false;
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			authenticated = authentication.isAuthenticated();
			authorities = authentication.getAuthorities();
		} catch (AuthenticationException e) {
			log.warn(String.format("User '%s' failed to login. Reason: %s", username, e.getMessage()));
			authenticated = false;
		}
		return authenticated;
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		getRolesIfSignedIn(roles);
		return roles;
	}

	private void getRolesIfSignedIn(Roles roles) {
		if (isSignedIn()) {
			for (GrantedAuthority authority : authorities) {
				roles.add(authority.getAuthority());
			}
		}
	}

}
