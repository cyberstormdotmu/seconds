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

package com.cubeia.network.shared.web.wicket.pages.login;

import com.cubeia.network.shared.web.wicket.BaseApplication;
import com.cubeia.network.shared.web.wicket.SecureWicketAuthenticatedWebSession;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.pages.SignOutPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutPage extends SignOutPage {

	private static final long serialVersionUID = 1L;
	
	Logger log = LoggerFactory.getLogger(getClass());

	public LogoutPage() {
        this(null);
    }
    
    public LogoutPage(PageParameters params) {
        super(params);
        logout();
        setResponsePage(BaseApplication.get().getHomePage());
    }

	private void logout() {
		SecureWicketAuthenticatedWebSession session = (SecureWicketAuthenticatedWebSession)Session.get();
		log.info("Logging out user: "+session.getId()+" "+session.getClientInfo().getProperties()+" "+session.getClientInfo());
        session.signOut();
	}
    
}
