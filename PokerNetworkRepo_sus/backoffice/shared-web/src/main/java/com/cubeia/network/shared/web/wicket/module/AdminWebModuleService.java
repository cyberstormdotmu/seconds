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
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@Component
public class AdminWebModuleService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    List<AdminWebModule> modules;

    Map<String,AdminWebModule.SearchItem> entities;


    private List<PageNode> pages;

    @PostConstruct
    public void mapSearchEntities() {
        entities = new HashMap<>();
        pages = new ArrayList<>();
        for(AdminWebModule m : modules) {
            pages.addAll(m.getPages());

            for(AdminWebModule.SearchItem item : m.getSearchItems()) {
                AdminWebModule.SearchItem previous = entities.put(item.getName(), item);
                if(previous!=null) {
                    log.warn("Multiple search entities with the same name: " + item.getName());
                }
            }
        }
        Collections.sort(pages, new Comparator<PageNode>() {
            @Override
            public int compare(PageNode o1, PageNode o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }

    public List<AdminWebModule> getModules() {
        return modules;
    }

    public SearchResultPanel<?> createResultPanel(String wicketId, SearchEntity hit) {
        for(AdminWebModule.SearchItem item : entities.values()) {
            if(hit.getClass().equals(item.getEntityClass())) {
                return item.getPanelCreator().createPanel(wicketId,hit);
            }
        }
        return null;
    }

    public SearchEntity createSearchEntity(String type, String sourceAsString) throws IOException {
        AdminWebModule.SearchItem searchItem = entities.get(type);
        if(searchItem!=null) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.readValue(sourceAsString, searchItem.getEntityClass());
        } else {
            log.warn("No mapping for typ {}", type);
            return null;
        }


    }

    public List<PageNode> getModulePages() {
        return pages;
    }
}
