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

package com.cubeia.firebase.poker.pokerbots.server.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.cubeia.firebase.poker.pokerbots.server.controller.Controller;
import com.cubeia.firebase.poker.pokerbots.server.informer.Informer;


/**
 * A very simple and basic HTTP server to serve html pages
 * with forms and some basic servlets.
 *
 * If a fully fledged http interface is desired, it would probably
 * be beneficial to implement a full MVC solution instead
 * (I.e. use a framework).
 *
 * @author fredrik.johansson
 *
 */
public class BotHTTPServer {

    private static Logger log = Logger.getLogger(BotHTTPServer.class);
    
    private static Controller controller;
    private static Informer informer;

    /**
     * Starts a HTTP Server with all servlets using the supplied controller
     * as a Down-Handler.
     *
     */
    public static void start(Controller downHandler, Informer upHandler, int port) {
    	
    	controller = downHandler;
        informer = upHandler;
        
        
        try{
            Server server = new Server();
            
            HttpConfiguration https = new HttpConfiguration();
      		https.addCustomizer(new SecureRequestCustomizer());
      		
      		SslContextFactory sslContextFactory = new SslContextFactory();
      		sslContextFactory.setKeyStorePath("pokerkeystore.jks");
 	  	    sslContextFactory.setKeyStorePassword("password");
 	  	    sslContextFactory.setKeyManagerPassword("password");
 	  	    
            ServerConnector connector = new NetworkTrafficSelectChannelConnector(server,new HttpConnectionFactory(https),sslContextFactory);
            connector.setPort(port);
            //TODO: verify the neeed of the two next lines and the values
            // http://download.eclipse.org/jetty/stable-7/xref/org/eclipse/jetty/embedded/ManyConnectors.html
            connector.setIdleTimeout(30000);
//            connector.setRequestHeaderSize(8192);
            server.addConnector(connector);

           
            /*
             * The base resource will be associated with the location of the index.html
             * This is not an very flexible solution, but since we are making a minimal
             * version we accept this.
             *
             * Using index.html allows developers to run the webserver jar file or no
             * jar file, as long as the web resources are in the classpath.
             */
            URL url = Thread.currentThread().getContextClassLoader().getResource("index.html");
            String resource = url.toString().replaceAll("index.html", "");

            
            //TODO: this is a 1 to 1 port of the old code, with some small modifications
            //      most possibly, this can be coded much more elegant
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS) {
//            	
                //TODO: verify is neccessary in jetty 8
            	private static final long serialVersionUID = -6619143591339795284L;
//
				@Override
            	public Resource getResource(String pathInContext)  {
					//System.out.println("pathInContext: " + pathInContext);              
            		Resource r = null;
                    try {
                        r = super.getResource(pathInContext);
                        //System.out.println("Resource     : " + r.toString());
                    } catch (MalformedURLException ex) {
                        java.util.logging.Logger.getLogger(BotHTTPServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

            		if(r == null || !r.exists()) {
            			if(pathInContext.startsWith("/")) {
            				pathInContext = pathInContext.substring(1);
            			}
            			URL url = Thread.currentThread().getContextClassLoader().getResource(pathInContext);
            			if(url == null) {
                            //avoid null-pointer exception
                            if (getClassLoader() != null) {
            				    url = getClassLoader().getResource(pathInContext);                                
                            }                   
            			}
            			if(url != null) {
            				log.debug("Did not find resource '" + pathInContext + "' in root class path; Found in wider class path.");
                            //System.out.println("url: " + url);
                            try {
                                r = Resource.newResource(url);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(BotHTTPServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //getResourceMetaData not available in jetty 8
                            //most possibly this is not needed anymore
//            		        ResourceMetaData meta = getResourceMetaData(r);
//            		        r.setAssociate(meta);
            			} else {
            				log.debug("Did not find resource '" + pathInContext + "' in root class path nor in wider class path.");
            			}
            		}
            		return r;
            	}
            };                     
            context.setContextPath("/");

            context.setBaseResource(Resource.newResource(resource));
            //removed for jetty8 (most possibly replaced by DefaultServlet below)
            //context.setHandler(new ResourceHandler());
            server.setHandler(context);

            // this is needed additionally (in comparison to the jetty1.5)
            context.addServlet("org.eclipse.jetty.servlet.DefaultServlet", "/");
                      
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.MenuServlet", "/menu");
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.StartBatchServlet", "/batch/start");
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.ViewBatchesServlet", "/batch/view");        
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.StopBatchServlet","/batch/stop");       
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.CacheInfoServlet", "/cache/info");
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.BotControlViewBatchesServlet", "/botcontrol/batch/view");          
            context.addServlet("com.cubeia.firebase.poker.pokerbots.server.http.BotControlCacheInfoServlet", "/botcontrol/cache/info");
            
            server.start();

        }catch (Exception e) {
            log.error("Could not start HTTPServer (make sure index.html is in the classpath): "+e,e);
        }
    }


	/**
     * Get the set Controller service to use.
     *
     * @return
     */
    public static Controller getController(){
        return controller;
    }

    /**
     * Get the set Infomer service to use.
     *
     * @return
     */
    public static Informer getInformer(){
        return informer;
    }
}
