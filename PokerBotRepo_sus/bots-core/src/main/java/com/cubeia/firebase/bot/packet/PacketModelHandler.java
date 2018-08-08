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

package com.cubeia.firebase.bot.packet;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.io.protocol.Enums;
import com.cubeia.firebase.io.protocol.JoinResponsePacket;
import com.cubeia.firebase.io.protocol.LeaveResponsePacket;
import com.cubeia.firebase.io.protocol.NotifyJoinPacket;
import com.cubeia.firebase.io.protocol.NotifyLeavePacket;
import com.cubeia.firebase.io.protocol.ProbePacket;

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
public class PacketModelHandler extends DefaultPacketHandler {

	
    @SuppressWarnings("unused")
	private Logger log = Logger.getLogger(PacketModelHandler.class);
	
    public static AtomicInteger numjoins = new AtomicInteger(0);
    
	private final Bot bot;
	
    public PacketModelHandler(Bot bot) {
		this.bot = bot;
    }
    
    @Override
	public void visit(JoinResponsePacket packet) {
   		if (packet.status == Enums.JoinResponseStatus.OK) {
   			bot.getGroup().getLobby().playerSeated(packet.tableid, bot.getPid());
   		} else {
   		    bot.getGroup().getLobby().playerLeft(packet.tableid, bot.getPid());
   		    bot.getGroup().getLobby().blacklistTable(packet.tableid);
   		}
	}

    /**
     * TODO: If you are denied leave then the seat is actually not available yet.
     */
    @Override
	public void visit(LeaveResponsePacket packet) {
        bot.getGroup().getLobby().playerLeft(packet.tableid, bot.getPid());
	}

    @Override
	public void visit(NotifyJoinPacket packet) {
	 	bot.getGroup().getLobby().playerSeated(packet.tableid, packet.pid);
    	if (packet.pid == bot.getPid()) {
    		Table table = bot.getGroup().getLobby().getTable(packet.tableid);
    		bot.getBotAI().setTable(table);
    	}
	}

    @Override
	public void visit(NotifyLeavePacket packet) {
		bot.getGroup().getLobby().playerLeft(packet.tableid, bot.getPid());
	}

    @Override
	public void visit(ProbePacket packet) {
		bot.getBotAI().handleProbePacket(packet);
		
	}

}

