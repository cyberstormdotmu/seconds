package com.cubeia.firebase.api.game.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cubeia.firebase.api.game.lobby.DefaultLobbyMutator;
import com.cubeia.firebase.api.game.lobby.LobbyTableAccessor;
import com.cubeia.firebase.api.game.player.GenericPlayer;

/**
 * The Waiting instanse for table
 * 
 * @author Tatvasoft
 *
 */
public class TableWaiting implements Serializable 
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4140802048166268530L;

	// The revision id
	private int revisionId = -1;
	// The table id
	private int tableId = -1;
	// The tourname id
	private int mttId = -1;
	// The Generic player
	private GenericPlayer player;
	// The table
	private Table table;
	// The LobbyTableAccessor
	LobbyTableAccessor acc; 
	// The DefaultLobbyMutator 
	DefaultLobbyMutator mut;
	// The lastActionTimeStamp
	long lastActionTimeStamp;
	// The Waiting Flag
	boolean waitingFlag = true;
	
	/*
	 * Getter and Setters
	 */
	public int getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(int revisionId) {
		this.revisionId = revisionId;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public int getMttId() {
		return mttId;
	}
	public void setMttId(int mttId) {
		this.mttId = mttId;
	}
	public GenericPlayer getPlayer() {
		return player;
	}
	public void setPlayer(GenericPlayer player) {
		this.player = player;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public LobbyTableAccessor getAcc() {
		return acc;
	}

	public void setAcc(LobbyTableAccessor acc) {
		this.acc = acc;
	}

	public DefaultLobbyMutator getMut() {
		return mut;
	}

	public void setMut(DefaultLobbyMutator mut) {
		this.mut = mut;
	}

	public long getLastActionTimeStamp() {
		return lastActionTimeStamp;
	}

	public void setLastActionTimeStamp(long lastActionTimeStamp) {
		this.lastActionTimeStamp = lastActionTimeStamp;
	}

	public boolean isWaitingFlag() {
		return waitingFlag;
	}

	public void setWaitingFlag(boolean waitingFlag) {
		this.waitingFlag = waitingFlag;
	}
}
