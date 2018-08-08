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

import com.cubeia.network.shared.web.wicket.module.AdminWebModule;
import com.cubeia.network.shared.web.wicket.navigation.PageNode;
import com.cubeia.network.shared.web.wicket.pages.login.LoginPage;
import com.cubeia.network.shared.web.wicket.pages.login.LogoutPage;
import org.apache.wicket.*;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;


/**
 * Base application class for Cubeia relatated backoffice web applications
 *
 * Handles/sets up authentication and loads all AdminWebModule's that are available
 * in the spring application context
 *
 * @see  AdminWebModule
 *
 */
public abstract class BaseApplication extends AuthenticatedWebApplication {

    @Inject
    List<AdminWebModule> modules;


    @Override
    protected void init() {
        getMarkupSettings().setStripWicketTags(true);
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener() {

            @Override
            public void onUnauthorizedInstantiation(org.apache.wicket.Component component) {
                if (component instanceof Page) {
                    throw new RestartResponseAtInterceptPageException(getSignInPageClass());
                } else {
                    throw new UnauthorizedInstantiationException(component.getClass());
                }

            }
        });
        RuntimeConfigurationType configurationType = getConfigurationType();
        if(RuntimeConfigurationType.DEVELOPMENT == configurationType)   {
            getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        }
        // Initialize Spring
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        mountPages();
    }

    private void mountPages() {
        mountPage("/login", LoginPage.class);
        mountPage("/logout", LogoutPage.class);


        for(AdminWebModule module : modules) {
            List<PageNode> pages = module.getPages();
            mountPages("", pages);
        }
    }

    private void mountPages(String basePath, List<PageNode> pages) {
        for(PageNode p : pages) {
            if(p.getMountPoint()==null) {
                continue;
            }
            if(p.getMountPoint().length()>0) {
                String path = basePath + "/" + p.getMountPoint();
                mountPage(path,p.getPageClass());
                if(p.hasChildren()) {
                    mountPages(path, p.getChildren());
                }
            } else {
                if(p.hasChildren()) {
                    mountPages("", p.getChildren());
                }
            }



        }
    }


    public abstract String getApplicationTitle();

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator converterLocator = new ConverterLocator();
        converterLocator.set(BigDecimal.class, new CustomBigDecimalConverter());
        return converterLocator;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SecureWicketAuthenticatedWebSession.class;
    }
}
