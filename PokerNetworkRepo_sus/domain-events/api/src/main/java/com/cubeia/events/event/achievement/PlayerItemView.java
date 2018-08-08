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

public class PlayerItemView {
	/** Unique id for this player-item */
	public String id;
	/** Unique id for the item references */
	public String nameId;
	public String player;
	/** Milliseconds from now */
	public Long expires;
	public String name;
	public String description;
	public String imageUrl;
	public String category;
	public Integer value;
	public Boolean equipped;
	
	
	@Override
	public String toString() {
		return "PlayerItemView [id=" + id + ", nameId=" + nameId + ", player=" + player + ", expires=" + expires + ", name=" + name + ", description="
				+ description + ", imageUrl=" + imageUrl + ", category=" + category + ", value=" + value + ", equipped=" + equipped + "]";
	}
	
	
}
