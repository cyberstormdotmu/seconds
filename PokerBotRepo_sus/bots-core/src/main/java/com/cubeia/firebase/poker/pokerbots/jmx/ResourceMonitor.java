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

package com.cubeia.firebase.poker.pokerbots.jmx;

import com.cubeia.firebase.api.util.SecondCounter;



/**
 * Implementation of the resource monitor mbean interface.
 * 
 */
public class ResourceMonitor implements ResourceMonitorMBean{
    
	public static SecondCounter gamePacketCounter = new SecondCounter();
	
    public int getConnected() {
    	return -1;
    }
    
    public int getLoggedin() {
    	return -1;
    }
    
    public int getAvgGamePackets() {
		return gamePacketCounter.current();
	}
    
}
