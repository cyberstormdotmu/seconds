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

package com.cubeia.firebase.bot.model;

/**
 * Used as dto for bots to give them a target table and 
 * seat to join.
 * 
 * @author Fredrik
 *
 */
public class TargetTable {
	
	private Table table;
	private int seat;

	public TargetTable(Table table, int seat) {
		super();
		this.table = table;
		this.seat = seat;
	}

	public String toString() {
		return "Table["+table+"] Seat["+seat+"]";
	}
	
	/**
	 * @return the seat
	 */
	public int getSeat() {
		return seat;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	
	
}
