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
package com.cubeia.network.shared.web.wicket.pages.search;

import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cubeia.network.shared.web.wicket.module.AdminWebModuleService;
import com.cubeia.network.shared.web.wicket.search.SearchEntity;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial") 
public class HitProvider implements IDataProvider<SearchEntity> {
    Logger log = LoggerFactory.getLogger(getClass());


    class Sort implements Serializable {
        String field;
        boolean ascending;
        public Sort(String field, boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }
        
        public String getField() {
            return field;
        }
        
        public boolean isAscending() {
            return ascending;
        }
        
        public boolean isNonEmpty() {
            return field != null;
        }
    }
    
    private String indexName;
    private URL searchUrl;
    private String clusterName;
    private AdminWebModuleService moduleService;
    private List<SearchEntity> hits = new ArrayList<>();
    private String queryString;
    private long offset;
    private long totalHits;
    private int limit;

    private Sort sort = new Sort(null, true);
    
    HitProvider(String clusterName, URL searchUrl, String indexName, int limit, AdminWebModuleService moduleService) {
        this.moduleService = moduleService;
        this.clusterName = clusterName;
        this.searchUrl = searchUrl;
        this.indexName = indexName;
        this.limit = limit;
    }
    
    public void setQuery(String queryString) {
        this.queryString = queryString;

        sort = parseSort(queryString);
        
        offset = 0;
        totalHits = 0;
        hits = null;
        
        doSearch((int) offset, (int) limit);
    }
    
    protected Sort parseSort(String query) {
        Pattern datePatt = Pattern.compile(".* +_sort\\:([a-zA-Z]+),?(asc|desc)? +.*");
        Matcher m = datePatt.matcher(" " + query + " ");
        
        String sortField = null;
        boolean asc = true;
        
        if (m.matches()) {
            sortField = m.group(1);
            if ("desc".equals(m.group(2))) {
                asc = false;
            }
        }
        
        return new Sort(sortField, asc);
    }

    private Client createClient() {
        Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        settingsBuilder.put("cluster.name", clusterName);
        
        Client client = new TransportClient(settingsBuilder.build()).addTransportAddress(new InetSocketTransportAddress(searchUrl.getHost(), searchUrl.getPort()));
        return client;
    }
    
    private void doSearch(int offset, int limit) {
        Client client = createClient();

        log.debug("search: offset = {}, limit = {}", offset, limit);
        
        QueryStringQueryBuilder root = QueryBuilders.queryString(queryString);

        SearchResponse resp;
        try {
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setQuery(root).setFrom(offset).setSize(limit);
            
            if (sort.isNonEmpty()) {
                searchRequestBuilder.addSort(sort.getField(), sort.isAscending() ? ASC : DESC);
            }
            
            log.debug("ES query: {}", searchRequestBuilder);
            
            resp = searchRequestBuilder.execute().get();
            
            totalHits = resp.getHits().getTotalHits();
            
            List<SearchEntity> hits = new ArrayList<>();
            
            for (SearchHit h : resp.getHits().getHits()) {
                log.trace(">>>>>>>>> ");
                log.trace(h.sourceAsString());
                log.trace(">>>>>>>>> ");


                SearchEntity searchEntity = moduleService.createSearchEntity(h.getType(), h.getSourceAsString());
                if(searchEntity!=null) {
                    hits.add(searchEntity);
                }


            }  
            
            this.hits = hits;
        } catch (Exception e) {
            log.error("error searching", e);
            this.hits = null;
            this.totalHits = 0;
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
        
    }
    
    @Override
    public void detach() {
    }

    @Override
    public Iterator<SearchEntity> iterator(long first, long count) {
        if (hits == null  ||  first != offset) {
            offset = first;
            doSearch((int) offset, (int) count);
        }             
        return hits.iterator();
    }

    @Override
    public long size() {
        return totalHits;
    }

    @Override
    public IModel<SearchEntity> model(SearchEntity object) {
        return Model.of(object);
    }

    public boolean isSorting() {
        return sort.isNonEmpty();
    }

    public String getSortField() {
        return sort.getField();
    }

    public boolean isAscending() {
        return sort.isAscending();
    }
    
}