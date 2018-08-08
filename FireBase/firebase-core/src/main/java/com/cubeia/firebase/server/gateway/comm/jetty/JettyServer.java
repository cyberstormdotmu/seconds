/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
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
package com.cubeia.firebase.server.gateway.comm.jetty;

import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.cubeia.firebase.api.server.Initializable;
import com.cubeia.firebase.api.server.SystemException;
import com.cubeia.firebase.api.util.SocketAddress;
import com.cubeia.firebase.server.gateway.GatewayConfig;
import com.cubeia.firebase.server.gateway.ServerConfig;
import com.cubeia.firebase.server.instance.SystemCoreException;
import com.cubeia.firebase.server.node.ClientNodeContext;
import com.cubeia.firebase.server.service.crypto.SystemCryptoProvider;

/**
 * This is a server for a Jetty instance. It starts and stop the embedded
 * Jetty on init/destroy. It mounts two contexts:
 * 
 * <ul>
 * 	<li>/cometd - for CometD style Bayeux communication</li>
 *  <li>/socket - for Web Socket communication</li>
 * </ul>
 * 
 * @author Lars J. Nilsson
 */
public class JettyServer implements Initializable<ClientNodeContext> {

	private static final String WS_PATH_SPEC = "/socket";

    private static final String COMETD_PATH_SPEC = "/cometd/*";

    public static final String HANDSHAKE_HTTP_HEADER = "X-Cubeia-Firebase-Handshake";
	
	private Server server;
	private ClientNodeContext con;
	
	private final Logger log = Logger.getLogger(getClass());
	private final ServerConfig serverConfig;
	private final SystemCryptoProvider cr;
	private final GatewayConfig nodeConf;
	private final CrossOriginConfig crossConfig;
	
	public JettyServer(GatewayConfig conf, ServerConfig config, CrossOriginConfig crossConfig, SystemCryptoProvider cr) {
		this.nodeConf = conf;
		this.serverConfig = config;
		this.crossConfig = crossConfig;
		this.cr = cr;
	}
	
	@Override
	public void init(ClientNodeContext con) throws SystemException {
		this.con = con;
		try {
			SocketAddress sa = serverConfig.getWebClientBindAddress();
			server = new Server(sa.toInetSocketAddress());
			
			Connector connector = new ServerConnector(server);
			server.addConnector(connector);
			
			String sslConfig = "Not Configured";
            if (cr.getSystemKeyStore() != null) {
                SocketAddress sslSa = serverConfig.getWebClientSslBindAddress();
                
                SslContextFactory contextFactory = new SslContextFactory();
                contextFactory.setSslContext(cr.getSystemKeyStore().createSSLContext());
                SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory, org.eclipse.jetty.http.HttpVersion.HTTP_1_1.toString());

                HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();

                ServerConnector sslConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
                sslConnector.setPort(serverConfig.getWebClientSslBindAddress().getPort());
                sslConnector.setHost(serverConfig.getWebClientSslBindAddress().getHost().getHostAddress());
                server.addConnector(sslConnector);                
                
                sslConfig = sslSa.getHost() + ":" + sslSa.getPort();
            }
			
			// Setup Jetty JMX
			//MBeanContainer mbContainer=new MBeanContainer(con.getMBeanServer());
			//server.getContainer().addEventListener(mbContainer);
			//server.addBean(mbContainer);
			
            
			// TODO More configuration
			setupServlets();
			server.start();
			
			log.info("HTTP Server starting at: " + sa.getHost() + ":" + sa.getPort() + "; SSL configuration: " + sslConfig);
			
		} catch (Exception e) {
			throw new SystemCoreException("Failed to start jetty", e);
		}
	}

	@Override
	public void destroy() {
		try {
			server.stop();
		} catch (Exception e) {
			log.error("Failed to stop Jetty", e);
		}
	}

	
	// --- PRIVATE METHODS --- //
	
	private void setupServlets() {
		ServletContextHandler context1 = createWsContext();
		if(this.nodeConf.disableStaticHttpContent()) {
			log.debug("Static content is disabled.");
			// only non-static content
			server.setHandler(context1);
		} else {
			log.info("Static content is served from: " + nodeConf.getStaticWebDirectory().getAbsolutePath());
			// add static handler
			ServletContextHandler context2 = createStaticContext();
			// combine and add to server
			ContextHandlerCollection contexts = new ContextHandlerCollection();
			contexts.setHandlers(new Handler[] { context1, context2 });
	        server.setHandler(contexts);
		}
	}

	private ServletContextHandler createStaticContext() {
		ServletContextHandler context = new ServletContextHandler();
		context.setClassLoader(getClass().getClassLoader());
		context.setContextPath("/static");
		ServletHolder holder = new ServletHolder(new DefaultServlet());
		holder.setInitParameter("resourceBase", nodeConf.getStaticWebDirectory().getAbsolutePath());
		holder.setInitParameter("dirAllowed", String.valueOf(nodeConf.allowStaticWebDirectoryListing()));
		context.addServlet(holder, "/*");
		return context;
	}

	private ServletContextHandler createWsContext() {
		ServletContextHandler context = new ServletContextHandler(SESSIONS);
		context.setClassLoader(getClass().getClassLoader());
		context.setContextPath("/");
		
		// add web socket
		if (nodeConf.isWebSocketEnabled()) {
    		ServletHolder wsServletHolder = new ServletHolder(new SocketServlet(this.con));
    		wsServletHolder.setInitParameter("maxTextMessageSize", String.valueOf(nodeConf.getJsonMaxTextMessageSize()));
    		wsServletHolder.setInitParameter("maxIdleTime", String.valueOf(nodeConf.getWebSocketMaxIdleTimeout()));
            context.addServlet(wsServletHolder, WS_PATH_SPEC);
            log.info("websockets are enabled at: " + WS_PATH_SPEC);
		} else {
		    log.info("websockets are disabled");
		}
        
		// add cometd
		if (nodeConf.isCometdEnabled()) {
            ServletHolder cometdServletHolder = new ServletHolder(new BayeuxServlet(this.con, nodeConf));
            cometdServletHolder.setInitParameter("logLevel", "3");
            cometdServletHolder.setInitParameter("maxInterval", String.valueOf(nodeConf.getCometIdleTimeout()));
            cometdServletHolder.setInitParameter("timeout", String.valueOf(nodeConf.getCometPollTimeout()));
            cometdServletHolder.setInitParameter("jsonContext", CometdJsonContext.class.getName());
            cometdServletHolder.setInitParameter("maxSessionsPerBrowser", "1");
            context.addServlet(cometdServletHolder, COMETD_PATH_SPEC);
            log.info("cometd is enabled at: " + COMETD_PATH_SPEC);
		} else {
		    log.info("cometd is disabled");
		}
        
        if (nodeConf.enableHttpCrossOriginFilter()) {
	        // add cross-origin filter
	        FilterHolder filter = new FilterHolder(CrossOriginFilter.class);
	        filter.setInitParameter("allowedOrigins", crossConfig.getAllowedOrigins().toString());
	        filter.setInitParameter("allowedMethods", crossConfig.getAllowedMethods().toString());
	        filter.setInitParameter("allowedHeaders", crossConfig.getAllowedHeaders().toString());
	        filter.setInitParameter("preflightMaxAge", String.valueOf(crossConfig.getPreflightMaxAge()));
	        filter.setInitParameter("allowCredentials", String.valueOf(crossConfig.getAllowCredentials()));
	        log.debug("Cross-Origin filter is enabled; " +
	        		"allowed origins: " + crossConfig.getAllowedOrigins().toString() + ";" +
	        		"allowed methods: " + crossConfig.getAllowedMethods().toString() + ";" +
	        		"allowed headers: " + crossConfig.getAllowedHeaders().toString());
	        context.addFilter(filter, "/*", null);
        }
        
        return context;
	}
}

