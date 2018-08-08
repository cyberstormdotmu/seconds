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
package com.cubeia.network.web.search;

import com.cubeia.network.shared.web.wicket.search.SearchResultPanel;
import com.cubeia.network.web.LinkFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class UserPanel extends SearchResultPanel<User> {

	public UserPanel(String id, IModel<User> model) {
		super(id, new CompoundPropertyModel<>(model.getObject()));
		
        add(LinkFactory.userDetailsLink("userLink", model.getObject().getUserId()));
        add(new Label("userId"));
		add(new Label("externalUserId"));
		add(new Label("operatorId"));
		add(new Label("userName"));
		add(new Label("status"));
		add(new Label("userType"));
		
		add(new Label("userInformation.firstName"));
		add(new Label("userInformation.lastName"));
		add(new Label("userInformation.email"));
		
        add(new AttributesPanel("attributes", Model.ofMap(model.getObject().getAttributes())));
	}

}
