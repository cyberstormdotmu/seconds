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

import java.util.ArrayList;
import java.util.List;

/**
 * List Wrapper
 *
 * @author fredrik
 */
public class PlayerItemList {

	public List<PlayerItemView> items = new ArrayList<>();

	public PlayerItemList() {
	}

	@Override
	public String toString() {
		return "PlayerItemList items[" + items + "]";
	}

}
