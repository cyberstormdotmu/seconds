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
 * This is a state-model for the AI Bots.
 * @author Fredrik
 *
 */
public class Player {
	
	private int id;
	private String name;
	private long stack;
	
	public Player(int id) {
		this.id = id;
	}
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "["+id+"]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStack() {
		return stack;
	}
	public void setStack(long stack) {
		this.stack = stack;
	}
	
	
	
}
