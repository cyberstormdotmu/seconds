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

package com.cubeia.firebase.bot.ai;

import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.io.protocol.CreateTableResponsePacket;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.MttTransportPacket;
import com.cubeia.firebase.io.protocol.ProbePacket;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;

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
public interface AI extends StateAI {
    
	/**
	 * Flag if the implementing AI wants to track the
	 * state of the table (expensive).
	 * 
	 * @return
	 */
	public boolean trackTableState();
	
	/** The table model so we can populate it with data. */
    public Table getTable();
    public void setTable(Table table);

    public void handleJoinDenied();
    
    public void handleLeaveDenied();
    
    public void handleTournamentRegistrationDenied(int tournamentId);
    
    public void handleGamePacket(GameTransportPacket packet);
    
    public void handleServiceTransportPacket(ServiceTransportPacket packet);
    
    public void handleTournamentPacket(MttTransportPacket packet); 
    
    public void handleProbePacket(ProbePacket packet);
    
    public void handleCreateTableResponsePacket(CreateTableResponsePacket packet);
    
    /**
     * Should we process incoming lobby changes?
     * @return
     */
    public boolean processLobbyPackets();

    /**
     * Halt gameplay
     */
	public void stop();
	
	/**
	 * Check if we are seated at the given table. If not an error 
	 * will be written to the log.
	 * 
	 * Use this to verify correct table id on all packets that
	 * require that you are seated at a table.
	 * 
	 * @param tableid
	 */
	public void verifyTablePacketId(int tableid);

	public void setReseating(boolean b);

	public boolean isReseating();

	public void handleDisconnected();

	/**
	 * Invoked when logging in, allowing the game specific bot to return custom credentials.
	 *
	 * If null is returned, default authentication will be used. BasicAI will return null.
	 *
	 * @return login credentials
	 */
	LoginCredentials getCredentials();

}

