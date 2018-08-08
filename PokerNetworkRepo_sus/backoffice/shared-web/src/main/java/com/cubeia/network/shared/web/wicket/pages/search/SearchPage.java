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

import com.cubeia.network.shared.web.wicket.BasePage;
import com.cubeia.network.shared.web.wicket.SearchConfiguration;
import com.cubeia.network.shared.web.wicket.module.AdminWebModuleService;
import com.cubeia.network.shared.web.wicket.search.SearchEntity;
import com.cubeia.network.shared.web.wicket.search.SearchResultPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

@AuthorizeInstantiation({"ROLE_ADMIN"})
public class SearchPage extends BasePage {

    public static final String PARAM_QUERY = "q";

    private static final int HIT_LIMIT = 5;

    @SuppressWarnings("unused")
	private static final String APPLICATION_JSON = "application/json";

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(getClass());
    
    @SpringBean(name = "searchConfig")
    private SearchConfiguration config;


    @SpringBean
    private AdminWebModuleService moduleService;

	private HitProvider dataProvider;

    /**
     * Constructor that is invoked when page is invoked without a session.
     *
     * @param parameters Page parameters
     */
    @SuppressWarnings("serial")
	public SearchPage(PageParameters parameters) {
        super(parameters);
        
        final Model<String> searchInputModel = new Model<String>();
        
        dataProvider = createHitProvider();
        
        final Label resultsCount = new Label("resultsCount", new AbstractReadOnlyModel<String>() {
            @Override public String getObject() { 
                String str = "" + dataProvider.size() + " results. ";
                
                if (dataProvider.isSorting()) {
                    str += "Sorted by " + (dataProvider.isAscending() ? "ascending" : "descending") + " <i>" + dataProvider.getSortField() + "</i>.";
                } else {
                    str += "Sorted by rank.";
                }

                return str;
            }
        });
        resultsCount.setEscapeModelStrings(false);
        resultsCount.setOutputMarkupId(true);
        add(resultsCount);
        
        final WebMarkupContainer resultsContainer = new WebMarkupContainer("resultsContainer") {
            @Override protected void onConfigure() {
                super.onConfigure();
                setVisible(dataProvider.size() > 0);
            }
        };
        resultsContainer.setOutputMarkupPlaceholderTag(true);
        resultsContainer.setOutputMarkupId(true);
        add(resultsContainer);
        
        Form<String> form = new Form<String>("searchForm");
        add(form);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        
        form.add(new RequiredTextField<String>("searchInput", searchInputModel));
        form.add(new IndicatingAjaxButton("searchButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String query = searchInputModel.getObject();
                log.debug("search query: '{}'", query);
                
                sendQuery(query);
                
                target.add(feedback);
                target.add(resultsContainer);
                target.add(resultsCount);
            }
        });
        
        
        DataView<SearchEntity> dataView = new DataView<SearchEntity>("searchResult", dataProvider, HIT_LIMIT) {
			@Override
			protected void populateItem(Item<SearchEntity> item) {
                SearchEntity hit = item.getModelObject();

                SearchResultPanel<?> panel = moduleService.createResultPanel("value",hit);
                if(panel!=null){
                    item.add(panel);
                }  else {
                    item.add(new Label("value", hit.toString()));
                }
			}
			
		};
        resultsContainer.add(dataView);
        
        resultsContainer.add(new AjaxPagingNavigator("navigator", dataView));
        resultsContainer.add(new AjaxPagingNavigator("navigator2", dataView));
        
        
        if (!parameters.get(PARAM_QUERY).isNull()) {
            String query = parameters.get(PARAM_QUERY).toString();
            searchInputModel.setObject(query);
            sendQuery(query);
        }
    }
    

    private void sendQuery(String query) {
        try {
            dataProvider.setQuery(query);
        } catch (Exception e) {
            error("Query syntax error or couldn't contact search engine.");
        }
    }
    
    
	private HitProvider createHitProvider() {
		String clusterName = config.getSearchClusterName();
		URL searchUrl;
		
        try {
			searchUrl = new URL(config.getSearchUrl());
		} catch (MalformedURLException e1) {
			throw new RuntimeException("error getting/parsing search base url from config, found: " + config.getSearchUrl());
		}
        String indexName = searchUrl.getPath().replace("/", "");
        
        log.debug("search base url: {}, cluster name = {}, index name = {}", 
            new Object[] { searchUrl, clusterName, indexName });
        
        return new HitProvider(clusterName, searchUrl, indexName, HIT_LIMIT, moduleService);
	}

    @Override
    public String getPageTitle() {
        return "Search";
    }
    
    
}
