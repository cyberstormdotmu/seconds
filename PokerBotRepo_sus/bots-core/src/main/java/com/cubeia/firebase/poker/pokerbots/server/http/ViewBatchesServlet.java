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
import com.cubeia.firebase.poker.pokerbots.server.translation.ConfigTranslator;


public class ViewBatchesServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = -3745448875428380424L;


    /**
     * Parses the request, builds a batch and sends it to the controller.
     * The Controller is fetched from the HTTPServer (TODO: not really good design there...).
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        // set header field first
        res.setContentType("text/html");

        // Get all Batches
        Collection<Batch> batches = BotHTTPServer.getInformer().getAllBatches();


        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();
        out.println("<html><head><title> Bot HTTP Server</title>"+
                "<link rel='stylesheet' type='text/css' href='"+req.getContextPath()+"/style.css' />"+
                "</head><body>");
        out.println("<h1> View All Batches</h1>");
        out.println("<h3>Batches found: "+batches.size()+"</h3><br/>");
        out.println("<br/><p>");
        
        out.println("<form action=\"/batch/stop\" method=\"get\">");
        String color = "#EEEEEE";
        for (Batch batch : batches) {
            if (batch.getStatus() == Batch.Status.TERMINATED) {
                color = "red";
            } else {
                color = "#EEEEEE";
            }
            out.println("<table cellspacing='10'><tr><td bgcolor='"+color+"' rowspan='"+batch.getSubBatches().size()+"'>");
            printBatchInformation(out, batch);
            out.println("</td>");

            for (Batch subBatch : batch.getSubBatches()) {
                out.println("<td valign='top' style='font-size=60%'>");
                printBatchInformation(out, subBatch);
                out.println("</td>");

            }
            
            if ( batch.getStatus() == Batch.Status.OK ) {
                out.println("<td><label for=\"shutdown\">Shutdown batch</label></td>");
                out.println("<td><input type=\"checkbox\" name=\""+batch.getId()+"\" id=\""+batch.getId()+"\"/></td>");
            }
            out.println("</tr>");
            out.println("</table>");
            
            
        }

        out.println("</p>");
       
        
        out.println("<td><label for=\"shutdown\">Shutdown All batches</label></td>");
        out.println("<td><input type=\"checkbox\" name=\"shutdownall\" id=\"shutdownall\"/></td>");
        out.println("<input type=\"submit\" value=\"shutdown\"/>");
        
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



    private void printBatchInformation(PrintWriter out, Batch batch) {
        out.println("<b>"+batch.getId()+"</b><br/>");
        out.println("Start time: <i>"+batch.getStartTime()+"</i><br/>");
        out.println("Requester: <i>"+batch.getRequestersAddress()+"</i><br/>");
        out.println("Parent: <i>"+batch.getParentid()+"</i><br/>");
        out.println("URL: <i>"+batch.getUrl()+"</i><br/>");
        out.println("Port: <i>"+batch.getPort()+"</i><br/>");
        out.println("Game ID: <i>"+batch.getGameId()+"</i><br/>");
        out.println("Tournament AI: <i>"+batch.getProperties().containsKey(ConfigTranslator.TOURNAMENT_PROPERTY_FLAG)+"</i><br/>");
        out.println("Currency: <i>"+batch.getCurrency()+"</i><br/>");
        
        out.println("Starting ID: <i>"+batch.getStartingid()+"</i><br/>");
        out.println("Requested: <i>"+batch.getRequested()+"</i><br/>");
        
        out.println("Started: <i>"+batch.getStarted()+"</i><br/>");
        out.println("Connected: <i>"+batch.getConnected()+"</i><br/>");
        out.println("Logged in: <i>"+batch.getLoggedIn()+"</i><br/>");
        
        out.println("Seated: <i>"+batch.getSeated()+"</i><br/>");
        out.println("Sub-Batches: <i>"+batch.getSubBatches().size()+"</i><br/>");
        out.println("Reconnect: <i>"+batch.getProperties().containsKey("reconnect")+"</i><br/>");
        out.println("To be stopped: <i>"+batch.isToBeStopped()+"</i><br/>");
        
        // Different color depending on status
        String color = "green";
        if (batch.getStatus().equals(Batch.Status.RESUMED)){
            color = "yellow";
        }else if (batch.getStatus().equals(Batch.Status.FAILED) ){
            color = "red";
        }else if (batch.getStatus() == Batch.Status.TERMINATED ) {
            color = "black";
        }
            
            
        out.println("Status: <i><font color='"+color+"'/>"+batch.getStatus().toString()+"</font></i><br/>");
        
        out.println("<br/><b>Flags</b><br/>");
        for( String key : batch.getFlags().keySet() ){
            out.println(key+":["+batch.getFlags().get(key)+"] <br/>");
        }
        
        out.println("<br/><b>Properties</b><br/>");
        for( String key : batch.getProperties().keySet() ){
            out.println(key+":["+batch.getProperties().get(key)+"] <br/>");
        }

    }

}

