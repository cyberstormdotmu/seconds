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
import com.cubeia.network.shared.web.wicket.util.DateDisplayModel;
import com.cubeia.network.web.LinkFactory;
import com.cubeia.network.web.search.Transaction.Entry;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class TransactionPanel extends SearchResultPanel<Transaction> {

	public TransactionPanel(String id, IModel<Transaction> model) {
		super(id, new CompoundPropertyModel<>(model.getObject()));
		
		add(LinkFactory.transactionDetailsLink("transactionLink", model.getObject().getTransactionId()));
		
		add(new Label("transactionId"));
		add(new Label("externalId"));
		add(new Label("timestamp", new DateDisplayModel(model, "timestamp")));
		add(new Label("comment"));
		
		ListView<Entry> entries = new ListView<Entry>("entries", model.getObject().getEntries()) {
			@Override
			protected void populateItem(ListItem<Entry> item) {
				CompoundPropertyModel<Entry> em = new CompoundPropertyModel<Entry>(item.getModel());
				item.setModel(em);
				
				item.add(LinkFactory.accountDetailsLink("accountLink", em.getObject().getAccount().getAccountId())
				    .add(new Label("account.accountId")));
				
				item.add(LinkFactory.userDetailsLink("userLink", em.getObject().getAccount().getUserId())
				    .add(new Label("account.userId")));
				
				item.add(new Label("amount"));
			}
		};
		add(entries);
		
		add(new AttributesPanel("attributes", Model.ofMap(model.getObject().getAttributes())));
		
	}

}
