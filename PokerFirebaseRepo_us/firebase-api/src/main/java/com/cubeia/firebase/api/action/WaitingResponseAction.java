package com.cubeia.firebase.api.action;

import java.util.List;

import com.cubeia.firebase.api.action.visitor.GameActionVisitor;
import com.cubeia.firebase.io.protocol.WaitingList;

public class WaitingResponseAction extends AbstractPlayerAction {

	private static final long serialVersionUID = 1L;
	private int tableid;
	private int playerid;
	
	//public String[] waitingplayers;
	public List<WaitingList> waitingplayers;
	
	public WaitingResponseAction(int playerId, int tableId) {
		super(playerId, tableId);
	}

	public WaitingResponseAction(int playerId, int tableId,List<WaitingList> waitingplayers) {
		super(playerId, tableId);
		this.tableid=tableId;
		this.playerid=playerId;
		this.waitingplayers=waitingplayers;
	}
	
	@Override
	public void visit(GameActionVisitor visitor) {
		visitor.visit(this);	
	}

	public int getTableid() {
		return tableid;
	}

	public void setTableid(int tableid) {
		this.tableid = tableid;
	}
	
	public int getPlayerid() {
		return playerid;
	}

	public void setPlayerid(int playerid) {
		this.playerid = playerid;
	}
	
	public List<WaitingList> getWaitingplayers() {
		return waitingplayers;
	}

	public void setWaitingplayers(List<WaitingList> waitingplayers) {
		this.waitingplayers = waitingplayers;
	}
}
