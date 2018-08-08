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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cubeia.firebase.poker.pokerbots.server.Batch;


public class StartBatchServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = 6817142356291079207L;
    private Logger log = Logger.getLogger(this.getClass());


    /**
     * Parses the request, builds a batch and sends it to the controller.
     * The Controller is fetched from the HTTPServer (TODO: not really good design there...).
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        // set header field first
        res.setContentType("text/html");

        log.info("A Batch was requested to be started");
        
        // create a batch
        Batch batch = new Batch();
        
        // Set arbitrary id
        batch.setId("B-"+System.currentTimeMillis());

        //set requesters address
        batch.setRequestersAddress(req.getRemoteAddr());
        
        Map<String,String[]> params = new HashMap<String, String[]>( req.getParameterMap() );


        // URL (needed)
        try{
            batch.setUrl(params.remove("url")[0]);
        }catch(Exception e){
            log.warn("Could not parse url", e);
        }

        // Port (needed)
        try{
            int requested = Integer.parseInt(params.remove("port")[0]);
            batch.setPort(requested);
        }catch(Exception e){
            log.warn("Could not parse port", e);
        }

        // Requested (needed)
        try{
            int requested = Integer.parseInt( params.remove("requested")[0] );
            batch.setRequested(requested);
        }catch(Exception e){
            log.warn("Could not parse requested", e);
        }
        
        // Game Id (needed)
        try{
            int gameId = Integer.parseInt( params.remove("gameId")[0] );
            batch.setGameId(gameId);
        }catch(Exception e){
            log.warn("Could not parse game id: "+e);
        }
        
        try{
            String connectorType = params.remove("connectorType")[0];
            batch.setConnectorType(connectorType);
        }catch(Exception e){
            log.warn("Could not parse connector type: "+e);
        }
        
        try{
            String currency = params.remove("currency")[0];
            batch.setCurrency(currency);
        }catch(Exception e){
            log.warn("Could not parse connector type: "+e);
        }

        /*
         *  Get all lobby attributes and store them separately
         *  
         *  Lobby attributes are special cases that needs to be prefixed with lobby_key and
         *  have corresponding lobby_value field. Example of valid form elements for lobby attributes:
         *  
         *  <tr>
         *      <td><label for="lobby_key1">Lobby Attribute 1: </label></td>
         *      <td><input type="text" name="lobby_key1" id="lobby_key1"/></td>
         *      <td><input type="text" name="lobby_value1" id="lobby_value1"/></td>
         *  </tr>
         *  
         */
        Set<String> lobbyKeys = new HashSet<String>(params.keySet());
        for( String key : lobbyKeys ){
            if (key.startsWith("lobby_key")) {
            	String lobbyKey = params.remove(key)[0];
            	String lobbyValue = params.remove(key.replace("key", "value"))[0];
            	if (lobbyKey != null && !lobbyKey.equals("") && lobbyValue != null && !lobbyValue.equals("")) {
            		// log.info("LOBBY ATTRIBUTE "+lobbyKey+" -> "+lobbyValue);
            		batch.getLobbyAttributes().put(lobbyKey, lobbyValue);
            	}
            }
        }
        
        
        // Set the remaining parameters as properties
        Set<String> keys = params.keySet();
        for( String key : keys ){
            String param = params.get(key)[0];
            if (param.matches("[0-9].*")) {
                int value = Integer.parseInt(param);
                batch.getProperties().put(key, value);
                log.debug("int param: " + key + " = " + value);
            } else if (param.matches("on")) {
                batch.getFlags().put(key, true);
                log.debug("bool param: " + key + " = true");
            } else if (param.matches("")) {
               // Ignore
               log.debug("skipping empty parameter: "+key+" - "+param);
            } else {
            	batch.getStringProperties().put(key, param);
                log.debug("string param: " + key + " = " + param);
            }
        }
        
        int startingId = -1;
        
        try{
            String[] startingIdStr = params.remove("startingid");
            if (startingIdStr != null ) {                
                startingId = Integer.parseInt(startingIdStr[0]);
            }
        }catch(Exception e){
            // Could not parse startingid but that should be ok, dynamical startingid used
        }
        
        // Get dynamic starting id if not specific id is wanted
        if (startingId == -1) {            
            startingId = BotHTTPServer.getInformer().getNextStartingid(batch.getRequested());
        }
        batch.setStartingid(startingId);
        
        // Start the batch
        BotHTTPServer.getController().startBatch(batch);
               

        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();
        out.println("<html><head><title> Bot HTTP Server</title>"+
                "<link rel='stylesheet' type='text/css' href='"+req.getContextPath()+"/style.css' />"+
                "</head><body>");
        out.println("<h1> Start Batch</h1>");
        out.println("<P>The batch was requested to be started.</P>");
        out.println("<BR>");
        out.println("Flag values:");
        out.println("<ul>");
        Iterator<?> i = batch.getFlags().keySet().iterator();
        while (i.hasNext()) {
            out.println("<li>" + i.next() + "</li>");
        }
        out.println("</ul>");
        out.println("Properties:");
        out.println("<ul>");
        Iterator<?> j = batch.getProperties().keySet().iterator();
        while (j.hasNext()) {
            out.println("<li>" + j.next() + "</li>");
        }
        out.println("</ul>");
        out.println("</body></html>");
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        doGet(req, res);
    }

    @Override
    public String getServletInfo() {
        return "A simple servlet";
    }


}