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

package com.cubeia.firebase.bot;

import java.util.regex.Pattern;

import com.cubeia.firebase.poker.pokerbots.server.BotServerDaemon;


/**
 * 
 * Created on 2006-sep-28
 * @author Fredrik Johansson
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public class BotServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	// default port = 8080
    	int port = 8080;
    	
    	if (args.length > 0) {
		    // If the first argument is a string of digits then we take that
		    // to be the port number to use
		    if (Pattern.matches("[0-9]+", args[0])) {
		        port = Integer.parseInt(args[0]);            
		    }
    	}
        BotServerDaemon.startDistServerHTTP(port);
    }

}

