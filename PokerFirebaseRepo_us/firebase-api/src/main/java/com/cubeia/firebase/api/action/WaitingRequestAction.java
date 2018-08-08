package com.cubeia.firebase.api.action;

import com.cubeia.firebase.api.action.visitor.GameActionVisitor;

public class WaitingRequestAction extends AbstractPlayerAction {

	private static final long serialVersionUID = 1L;

	private int id; 
	
	public WaitingRequestAction(int playerId, int tableId) {
		super(playerId, tableId);
	}

	@Override
	public void visit(GameActionVisitor visitor) {
		visitor.visit(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
