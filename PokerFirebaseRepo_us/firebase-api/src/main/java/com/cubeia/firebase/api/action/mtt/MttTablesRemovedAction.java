/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
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
package com.cubeia.firebase.api.action.mtt;

import java.util.Set;

import com.cubeia.firebase.api.action.visitor.MttActionVisitor;

/**
 * Action that notifies the tournament that the given tables 
 * have been removed.
 *
 * @author Fredrik
 */
public class MttTablesRemovedAction extends AbstractMttAction {

	/** Version id */
	private static final long serialVersionUID = 5419336877343398894L;

	private Set<Integer> tables;
	
	public MttTablesRemovedAction(int mttId) {
		super(mttId);
	}
	
	public Set<Integer> getTables() {
		return tables;
	}

	public void setTables(Set<Integer> tables) {
		this.tables = tables;
	}
	
	public void addTable(Integer tableId) {
		tables.add(tableId);
	}
	
	public void accept(MttActionVisitor visitor) {
		visitor.visit(this);
	}
}
