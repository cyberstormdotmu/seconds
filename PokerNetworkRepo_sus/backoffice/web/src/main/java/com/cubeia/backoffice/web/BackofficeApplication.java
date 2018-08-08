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

package com.cubeia.backoffice.web;

import com.cubeia.network.shared.web.wicket.BaseApplication;
import com.cubeia.network.shared.web.wicket.pages.login.LoginPage;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 *
 */
@Component("wicketApplication")
public class BackofficeApplication extends BaseApplication {
    private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Constructor
	 */
	public BackofficeApplication() {
		 
	}

    @Override
    public Class<? extends Page> getHomePage() {
        return Home.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }


    @Override
    public String getApplicationTitle() {
        return "Network Backoffice";
    }
}
