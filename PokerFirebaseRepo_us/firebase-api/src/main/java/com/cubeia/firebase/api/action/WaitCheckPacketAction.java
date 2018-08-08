package com.cubeia.firebase.api.action;

import com.cubeia.firebase.api.action.visitor.GameActionVisitor;

public class WaitCheckPacketAction extends AbstractPlayerAction {

	private int tableid; 
	
	public WaitCheckPacketAction(int playerId, int tableId) {
		super(playerId, tableId);
		this.tableid = tableId;
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

}
