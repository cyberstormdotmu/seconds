package com.cubeia.firebase.api.action;

import com.cubeia.firebase.api.action.visitor.GameActionVisitor;

public class WaitCheckResponsePacketAction extends AbstractPlayerAction {

	private int tableid;
	private int playerid;
	
	
	public WaitCheckResponsePacketAction(int playerId, int tableId) {
		super(playerId, tableId);
		this.tableid=tableId;
		this.playerid=playerId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	
}
