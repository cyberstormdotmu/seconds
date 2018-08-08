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

package com.cubeia.firebase.bot;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class GroupStats {
	
	private transient Logger log = Logger.getLogger(this.getClass());
	
	public AtomicInteger started = new AtomicInteger(0);
	public AtomicInteger connected = new AtomicInteger(0);
	public AtomicInteger loggedin = new AtomicInteger(0);
	public AtomicInteger seated = new AtomicInteger(0);
	public AtomicInteger deniedSeating = new AtomicInteger(0);
	
	
	public void printInfo(String owner) {
		log.debug(owner+" - "+this);
	}
	
	@Override
    public String toString() {
		String desc = "Started["+started+"] connected["+started+"] Loggedin["+loggedin+"] Seated["+seated+"]"+ "JoinDenied["+deniedSeating+"]";
		return desc;
	}
}
