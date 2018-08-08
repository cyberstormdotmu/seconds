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

import com.cubeia.backoffice.users.client.UserServiceClient;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;
import com.cubeia.network.shared.web.wicket.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

@AuthorizeInstantiation({"ROLE_ADMIN"})
public class Home extends BasePage {
	private static final long serialVersionUID = 1L;

    @SpringBean
    private UserServiceClient userService;
    
    @SpringBean
    private WalletServiceClient walletService;
	
	public Home() {
		
		add(new Label("walletBaseUrl", new PropertyModel<String>(walletService, "baseUrl")));
        add(new Label("userBaseUrl", new PropertyModel<String>(userService, "baseUrl")));
	}

    public Home(PageParameters parameters) {
        super();
    }
    
    public void renderHead(IHeaderResponse resp) {
        resp.render(CssHeaderItem.forReference(new PackageResourceReference(Home.class, "home.css")));

    }
    
    @Override
    public String getPageTitle() {
        return "Home";
    }
}