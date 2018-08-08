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

package com.cubeia.network.shared.web.wicket;

import com.cubeia.network.shared.web.wicket.module.AdminWebModuleService;
import com.cubeia.network.shared.web.wicket.navigation.Breadcrumbs;
import com.cubeia.network.shared.web.wicket.navigation.MenuPanel;
import com.cubeia.network.shared.web.wicket.navigation.PageNode;
import com.cubeia.network.shared.web.wicket.pages.search.SearchPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePage extends WebPage {

    private static final long serialVersionUID = -913606276144395037L;

    @SpringBean(required = false)
    AdminWebModuleService moduleService;

    @SpringBean
    AdminConfig adminConfig;



    public BasePage() {
        this(null);
    }
    
    @SuppressWarnings("serial")
    public BasePage(PageParameters p) {
        List<PageNode> pages = new ArrayList<>();
        pages.add(new PageNode("Home",WebApplication.get().getHomePage(),"icon-home",true,"home"));
        loadPages(pages);

        add(new MenuPanel("menuPanel", pages, this.getClass()));
        add(new Breadcrumbs("breadcrumb", pages, this.getClass()));
        add(new Label("applicationTitle", getTitle()));
        // defer setting the title model object as the title may not be generated now
        add(new Label("title", new Model<String>()));
        setLoggedInUsername();

        final TextField<String> searchField = new TextField<String>("globalSearchInput", new Model<String>());
        Form<Void> searchForm = new Form<Void>("globalSearchForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                setResponsePage(SearchPage.class, new PageParameters().add(SearchPage.PARAM_QUERY, searchField.getModelObject()));
            }
        };
        add(searchForm);
        searchForm.add(searchField);
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer("skinCss");
        if(adminConfig!=null && adminConfig.getSkinCssUrl()!=null) {
            webMarkupContainer.add(new AttributeModifier("href",adminConfig.getSkinCssUrl()));
        }
        add(webMarkupContainer);
    }

    private String getTitle() {
        WebApplication webApplication = BaseApplication.get();
        if(webApplication instanceof BaseApplication) {
            return ((BaseApplication) webApplication).getApplicationTitle();
        } else {
            return "";
        }
    }

    public void loadPages(List<PageNode> pages) {
        if(moduleService !=null) {
            pages.addAll(moduleService.getModulePages());
        }
    }

    private void setLoggedInUsername() {
		if (isSignedIn()) {
        	add(new Label("username", getSignedInUsername()));
        } else {
        	add(new Label("username", "Not logged in"));
        }
	}

	/**
	 * 
	 * @return SecureWicketAuthenticatedWebSession or null if not applicable
	 */
	public SecureWicketAuthenticatedWebSession getSecureWebSession() {
		WebSession session = (WebSession)Session.get();
        if (session instanceof SecureWicketAuthenticatedWebSession) {
			return (SecureWicketAuthenticatedWebSession) session;
		} else {
        	return null;
        }
	}
	
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        get("title").setDefaultModelObject(getPageTitle());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
    }

    public abstract String getPageTitle();
    
    public boolean isSignedIn() {
		SecureWicketAuthenticatedWebSession session = getSecureWebSession();
		if (session != null && session.isSignedIn()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return Signed in username or null if not signed in.
	 */
	public String getSignedInUsername() {
		if (isSignedIn()) {
        	return getSecureWebSession().getUsername();
        } else {
        	return null;
        }
    }
	
	/**
	 * @return Signed in remote ip address as String or null if not signed in.
	 */
	public String getSignedInRemoteIPAddress() {
		if (isSignedIn()) {
        	return getSecureWebSession().getClientInfo().getProperties().getRemoteAddress();
        } else {
        	return null;
        }
	}
	
	/**
	 * @return Signed in remote ip address as String or null if not signed in.
	 */
	public String getSignedInUserAgent() {
		if (isSignedIn()) {
        	return getSecureWebSession().getClientInfo().getUserAgent();
        } else {
        	return null;
        }
	}
}
