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

package com.cubeia.firebase.bot.ai.impl.lobby;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.ai.BasicAI;
import com.cubeia.firebase.bot.model.Lobby;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.LobbyUnsubscribePacket;
import com.cubeia.firebase.io.protocol.ProbePacket;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;
import com.cubeia.firebase.io.protocol.Enums.LobbyType;


/**
 * 
 * Created on 2006-sep-28
 * @author Fredrik Johansson
 *
 */
public class LobbyAI extends BasicAI {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(BasicAI.class);
	
	private LobbyType type = LobbyType.REGULAR;

	public LobbyAI(Bot bot) {
		super(bot);
		if (bot.getGroup().getConfig().isTournamentBot()) {
			type = LobbyType.MTT;
		}
		bot.logDebug("I am a Lobby bot. Type: "+type+". GameId: "+bot.getGroup().getConfig().getGameId());
	}

	public void stop() {
		super.stop();
		LobbyUnsubscribePacket packet = new LobbyUnsubscribePacket();
		packet.gameid = bot.getGroup().getConfig().getGameId();
		packet.address = "/";
		bot.sendPacket(packet);
	}

	/**
	 * The lobby bot does not need table state (will not play)
	 */
	public boolean trackTableState() {
		return false;
	}

	 /**
	  *  the lobby bot does not care about game transport packets
	  */ 
	 public void handleGamePacket(GameTransportPacket packet) { }
	 
	 /**
	  * the lobby bot does not care about service transport packets
	  */
	 public void handleServiceTransportPacket(ServiceTransportPacket packet) {}
	 
	 @Override
	protected void handleConnect() {
		 logInfo("Lobby bot connected, starting subscription.");
		 startLobbySubscription("/", type);
		 startKeepAlive();
	}

	protected void handleSeated() { }

	public void handleProbePacket(ProbePacket packet) {}

	public boolean processLobbyPackets() {
		return true;
	}

	@SuppressWarnings("unused")
	private class DumpInfo implements Runnable {
		public void run() {
			Lobby.getInstance().dumpLobbyToLog(bot.getScreenname());
		}
	}
}

