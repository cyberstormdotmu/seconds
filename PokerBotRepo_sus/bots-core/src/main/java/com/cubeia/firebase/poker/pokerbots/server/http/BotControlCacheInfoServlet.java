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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jgroups.stack.IpAddress;

import com.cubeia.firebase.poker.pokerbots.server.cache.CacheHandler;


public class BotControlCacheInfoServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = -1438761437458473294L;
//    private Logger log = Logger.getLogger(this.getClass());


    /**
     * Parses the request, builds a batch and sends it to the controller.
     * The Controller is fetched from the HTTPServer (TODO: not really good design there...).
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        // set header field first
        res.setContentType("text/plain");
        
        List<IpAddress> members = CacheHandler.getInstance().getMembers();

        // Finally get the writer and write some response
        PrintWriter out = res.getWriter();

        out.println("ClusterSize=" + members.size());
				
        for (IpAddress member : members) {
            out.println("Member=" + member);
        }

                                 
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
