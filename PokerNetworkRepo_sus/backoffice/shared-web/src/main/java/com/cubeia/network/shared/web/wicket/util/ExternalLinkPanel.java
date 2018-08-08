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

package com.cubeia.network.shared.web.wicket.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Renders an external link including the tag.
 * 
 * The difference from {@link ExternalLink} is that this panel
 * also renders the tag instead of just attaching link behaviour
 * to the markup element. 
 * 
 * @author w
 */
public class ExternalLinkPanel extends Panel {
    
    private static final long serialVersionUID = 1L;

    public ExternalLinkPanel(String id, String label, String href) {
        this(id, label, null, href);
    }

    public ExternalLinkPanel(String id, IModel<String> label, IModel<String> href) {
        this(id, label, null, href);
    }
    
    public ExternalLinkPanel(String id, String label, String title, String href) {
        this(id, Model.of(label), Model.of(title), Model.of(href));
    }
    
    public ExternalLinkPanel(String id, IModel<String> label, IModel<String> title, IModel<String> href) {
        super(id);
        
        ExternalLink link = new ExternalLink("link", href);
        link.add(new Label("label", label));
        
        if (title != null) {
            link.add(new AttributeModifier("title", title));
        }
        
        add(link);
    }
    
}
