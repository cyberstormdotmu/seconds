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

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.model.Lobby;

/**
 * 
 * Created on 2006-sep-26
 * @author Fredrik Johansson
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public class BotMain {

	private static Logger log = Logger.getLogger(BotMain.class);
	
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BotConfig config = new BotConfig();
            config.setSize(30);
            config.setHost(new InetSocketAddress("localhost", 4123));
            config.setStartid(1);
            config.setAiclass("com.cubeia.firebase.bot.ai.impl.tank.TankBot");
            
            Lobby.createPresetLobby();
            
            BotGroup group = new BotGroup(config);
            
            log.info("Created "+group.getSize()+" bots.");
            
            group.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
}

