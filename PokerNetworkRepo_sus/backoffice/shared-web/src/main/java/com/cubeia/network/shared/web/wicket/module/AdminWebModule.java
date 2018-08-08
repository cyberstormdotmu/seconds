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
package com.cubeia.network.shared.web.wicket.module;


import com.cubeia.network.shared.web.wicket.navigation.PageNode;
import com.cubeia.network.shared.web.wicket.search.SearchEntity;
import com.cubeia.network.shared.web.wicket.search.SearchResultPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * An admin web module is a self contained set of pages & services.
 *
 * When available in the spring context it will be picked up by the <code>BaseApplication</code>
 * and all it's menu items will be added to the applications menu
 *
 *
 * All spring configuration for a module should be put in a admin-web-module.xml
 */
public abstract class AdminWebModule {

    private List<SearchItem> searchItems = new ArrayList<>();

    public void registerSearchType(String name, Class<? extends SearchEntity> entityClass, PanelCreator creator){
        searchItems.add(new SearchItem(name,entityClass,creator));
    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    abstract public String getName();

    abstract public List<PageNode> getPages();

    public static class SearchItem {

        private String name;
        private Class<? extends  SearchEntity> entityClass;
        private PanelCreator panelCreator;

        public SearchItem(String name, Class<? extends SearchEntity> entityClass, PanelCreator panelCreator) {
            this.name = name;
            this.entityClass = entityClass;
            this.panelCreator = panelCreator;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<? extends SearchEntity> getEntityClass() {
            return entityClass;
        }

        public void setEntityClass(Class<? extends SearchEntity> entityClass) {
            this.entityClass = entityClass;
        }

        public PanelCreator getPanelCreator() {
            return panelCreator;
        }

        public void setPanelCreator(PanelCreator panelCreator) {
            this.panelCreator = panelCreator;
        }

    }
}
