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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StopBatchServlet extends HttpServlet{

	private static final long serialVersionUID = 3792155265186979959L;

    /**
     * Parses the request, builds a batch and sends it to the controller.
     * The Controller is fetched from the HTTPServer (TODO: not really good design there...).
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        // set header field first
        Set <String>set = req.getParameterMap().keySet();
        res.setContentType("text/html");
        System.out.println("-----------stopBatchServlet -----");
        System.out.println(set);
        //System.out.println(req.getParameterMap().values());
        System.out.println("-----------stopBatchServlet -----");
        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();
        out.println("<html><head><title> Bot HTTP Server</title>"+
                "<link rel='stylesheet' type='text/css' href='"+req.getContextPath()+"/style.css' />"+
                "</head><body>");
        out.println("<h2> Stopping batches...</h3>");
        out.println("</BODY>");
        out.close();
        for (String str : set) {
            if (str.equals("shutdownall")) {
                BotHTTPServer.getController().stopAllBatches();
                return;
            }
        }
        for (String str : set) {           
            BotHTTPServer.getController().stopBatch(str);                       
        }
        
        
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
