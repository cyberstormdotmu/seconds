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
package com.cubeia.firebase.api.mtt.lobby;

import com.cubeia.firebase.api.lobby.AttributeMapper;
import com.cubeia.firebase.api.mtt.MTTState;

/**
 * This object is responsible for mapping a table to a set of 
 * attributes. It is used by the game lobby classes to transform
 * a table into serializable attributes values.
 * 
 * @author lars.j.nilsson
 */
public interface MttAttributeMapper extends AttributeMapper<MTTState> { }
