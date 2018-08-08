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
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MenuServlet extends HttpServlet {

    private static final transient Logger log = Logger.getLogger(MenuServlet.class);
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        Properties menuPages = new Properties();
        
        try { 
            InputStream customStream = this.getClass().getResourceAsStream("/menu.properties");
            if (customStream != null) {
                menuPages.load(customStream);
            } else {
                InputStream stream = this.getClass().getResourceAsStream("/default_menu.properties");
                if (stream != null) {
                    log.info("No custom menu found in classpath (menu.properties), using default.");
                    menuPages.load(stream);
                } else {
                    log.warn("Could not find default_menu.properties in classpath");
                }
            }
            
        } catch (Exception e) {
            log.warn("Failed to open menu properties file (menu.properties or default_menu.properties in classpath). Exception: "+e.getMessage());
            menuPages = new Properties();
        }
            
        
        // set header field first
        res.setContentType("text/html");


        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();
        out.println("<html><head><title> Bot HTTP Server</title>"+
                "<link rel='stylesheet' type='text/css' href='"+req.getContextPath()+"/style.css' />"+
        "</head><body>");


        out.println("<h3>The Bot Farm</h3>");

        out.println("AI Types:");
        out.println("<ul type=\"square\">");
        
        Object[] aiArray = menuPages.keySet().toArray();
        Arrays.sort(aiArray);
        
        for (Object key : aiArray) {
            out.println("<li><a href=\""+menuPages.getProperty(key.toString())+"\" target=\"main\">"+key+"</a></li>");
        }

        out.println("</ul>");

        out.println("<br/>");
        out.println("<a href=\"batch/view\" target=\"main\">View Batches</a><br/>");
        out.println("<br/>");
        out.println("<a href=\"cache/info\" target=\"main\">View Cluster Information</a><br/>");
        out.println("<br/>");
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
