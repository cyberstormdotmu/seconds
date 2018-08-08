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
package com.cubeia.events.event.achievement;

import com.cubeia.events.event.Event;

// @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public class BonusEvent extends Event  {

	public String game;
	public String player;
	public String name;
	public boolean broadcast = false;
	
	/** Can very well be null */
	public PlayerAchivementView achievement;
	
	/** Can very well be null */
	public PlayerItemView item;
	
    public BonusEvent(){    }

    @Override
    public String toString() {
    	return "BonusEvent, player["+player+"] game["+game+"] type["+type+"] name["+name+"] attributes["+attributes+"] achievement["+achievement+"] item["+item+"]";
    }
}