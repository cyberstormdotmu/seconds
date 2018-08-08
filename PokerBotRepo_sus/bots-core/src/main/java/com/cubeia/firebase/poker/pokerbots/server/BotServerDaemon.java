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

package com.cubeia.firebase.poker.pokerbots.server;

import org.apache.log4j.Logger;

import com.cubeia.firebase.poker.pokerbots.server.controller.CacheController;
import com.cubeia.firebase.poker.pokerbots.server.controller.Controller;
import com.cubeia.firebase.poker.pokerbots.server.controller.LocalController;
import com.cubeia.firebase.poker.pokerbots.server.controller.MockController;
import com.cubeia.firebase.poker.pokerbots.server.http.BotHTTPServer;
import com.cubeia.firebase.poker.pokerbots.server.informer.CacheInformer;
import com.cubeia.firebase.poker.pokerbots.server.informer.Informer;
import com.cubeia.firebase.poker.pokerbots.server.informer.LocalInformer;
import com.cubeia.firebase.poker.pokerbots.server.informer.MockInformer;


/**
 * The server daemon starts up a bot server with the appropiate 
 * service stack.
 *  
 * @author Fredrik
 */
public class BotServerDaemon {

    private static Logger log = Logger.getLogger(BotHTTPServer.class);
    
    /**
     * Starts a local HTTP Server that uses a Mocked controller only.
     *
     * Starts a bot server with the following service-stack:
     * 
     * [      Jetty       ]
     * [ Mock C ][ Mock I ]
     * [     Bot Group    ]
     * 
     */
    public static void startMockHTTP(){
        log.info("Start Mock HTTP Server");
        BotHTTPServer.start(new MockController(), new MockInformer(), 8080);
    }
    
    /**
     * Starts a local HTTP server that communicates directly
     * with the Bot groups through a Local Controller.
     *
     * Starts a bot server with the following service-stack:
     * 
     * [    Jetty     ]
     * [  Local C/I   ]
     * [  Bot Group   ]
     * 
     */
    public static void startLocalHTTP(int port, int startid){
        log.info("Start Local HTTP Server; port=" + port + ", startid=" + startid);
        BotHTTPServer.start(new LocalController(startid), new LocalInformer(), port);
    }
    
   
    /**
     * Starts a Distributed HTTP server solution.
     *
     * Starts a bot server with the following service-stack:
     * 
     * [      Jetty       ]
     * [ Cache Controller ]  < ----- > [ Cache Controller ]
     * [ Local Controller ]
     * [    Bot Group     ]
     * 
     */
    public static void startDistServerHTTP(int port){
        log.info("Start Distributed HTTP");
        
        // --- CONTROLLER STACK ---
        // Create a Cache Controller using a Mock Controller for now
        Controller topController = new CacheController();
        Controller downController = new LocalController();
        
        // Attach the Mock controller as down handler
        topController.attachController(downController);
        
        
        // --- INFORMER STACK ---
        Informer cacheInformer = new CacheInformer();
        Informer localInformer = new LocalInformer();
        
        // Attach the mock informer as uphandler
        cacheInformer.attachInformation(localInformer);
        
        
        
        // Start a HTTP server with the topController
        BotHTTPServer.start(topController, cacheInformer, port);

    }
    
}
