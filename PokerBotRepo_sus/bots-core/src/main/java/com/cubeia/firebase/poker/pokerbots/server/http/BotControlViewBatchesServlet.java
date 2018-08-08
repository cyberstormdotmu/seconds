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
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cubeia.firebase.poker.pokerbots.server.Batch;


public class BotControlViewBatchesServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = -1847385024522350229L;
//    private Logger log = Logger.getLogger(this.getClass());


    /**
     * Parses the request, builds a batch and sends it to the controller.
     * The Controller is fetched from the HTTPServer (TODO: not really good design there...).
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        // set header field first
        res.setContentType("text/xml");

        // Get all Batches
        Collection<Batch> batches = BotHTTPServer.getInformer().getAllBatches();


        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();
        out.println("<batchinfo>");
        out.println("\t<NumberOfBatches>" + batches.size() + "</NumberOfBatches>");

        if ( batches.size() > 0 ) 
        {
	        for (Batch batch : batches) {
	        	out.println("\t<batch id=\"" + batch.getId() + "\">");
	            printBatchInformation(out, batch, false);
	
	            if ( batch.getSubBatches().size() > 0 ) {
	            	
	            	for (Batch subBatch : batch.getSubBatches()) {
	            		out.println("\t\t<subbatch id=\"" + subBatch.getId() + "\">");
	            		printBatchInformation(out, subBatch, true);
	            		out.println("\t\t</subbatch>");
	            	}
	            }
	            out.println("\t</batch>");
	        }
        }
        out.println("</batchinfo>");
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



    private void printBatchInformation(PrintWriter out, Batch batch, boolean issubbatch) {

        
        if ( issubbatch ) {
        	out.println("\t\t\t<parentbatchid>" + batch.getParentid()+"</parentbatchid>" );
        } else {
        	out.println("\t\t<numberofsubbatches>" + batch.getSubBatches().size()+ "</numberofsubbatches>");
        }
/*
        out.println("URL=" + batch.getUrl());
        out.println("Port=" + batch.getPort());
        out.println("Mode=" + batch.getMode());
        Integer type = batch.getProperties().get("allowedtabletypes");
        if (type != null) {
	        if (type==1) {
	            out.println("Tabletype=RNG/STT");    
	        } else if (type==2) {
	        		out.println("Tabletype=RNG");    
	        } else if (type==3) {
	            out.println("Tabletype=STT");    
	        } else  {
	            out.println("Tabletype=" + type);
	        }
        }
        out.println("StartingID=" + batch.getStartingid());
        out.println("Requested=" + batch.getRequested());
        out.println("Started=" + batch.getStarted());
            
        out.println("Status=" + batch.getStatus().toString());
       

        for( String key : batch.getFlags().keySet() ){
            out.println("Flag:"+key+ "="+batch.getFlags().get(key) );
        }
        
        for( String key : batch.getProperties().keySet() ){
            out.println("Property:"+ key+"="+batch.getProperties().get(key));
        }
*/

    }

}
