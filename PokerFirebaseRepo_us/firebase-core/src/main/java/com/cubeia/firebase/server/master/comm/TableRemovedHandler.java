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
package com.cubeia.firebase.server.master.comm;

import java.lang.reflect.InvocationTargetException;

import com.cubeia.firebase.api.command.CommandException;
import com.cubeia.firebase.api.util.SocketAddress;
import com.cubeia.firebase.server.commands.TableRemoved;

public class TableRemovedHandler extends BaseHandler<TableRemoved> {

	public Object handle(SocketAddress sender, TableRemoved com) throws InvocationTargetException, CommandException {
		con.getNode().tableRemoved(new int[] { com.getAttachment().getId() }, com.getAttachment().getMttId());
		return null;
	}
}
