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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

@SuppressWarnings("serial")
public class AttributesPanel extends Panel {
    
	public AttributesPanel(String id, final IModel<Map<String, Attribute>> model) {
		super(id, new CompoundPropertyModel<>(model.getObject()));
		
		LoadableDetachableModel<List<Attribute>> valuesModel = new LoadableDetachableModel<List<Attribute>>() {
		    @Override protected List<Attribute> load() {
		        return new ArrayList<Attribute>(model.getObject().values());
		    }
        };
		
        ListView<Attribute> attributes = new ListView<Attribute>("attributes", valuesModel) {
            @Override
            protected void populateItem(ListItem<Attribute> item) {
                item.setModel(new CompoundPropertyModel<>(item.getModel()));
                item.add(new Label("key"));
                item.add(new Label("value"));
            }
        };
		add(attributes);
	}

}
