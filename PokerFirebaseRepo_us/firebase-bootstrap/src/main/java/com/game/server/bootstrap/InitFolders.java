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
package com.game.server.bootstrap;

import java.io.File;

import com.cubeia.firebase.api.util.Arguments;

public class InitFolders {

	public final File config, game, work, lib;
	
	public InitFolders(File config, File game, File lib, File work) {
		Arguments.notNull(config, "config");
		Arguments.notNull(game, "game");
		Arguments.notNull(work, "work");
		Arguments.notNull(lib, "lib");
		this.config = config;
		this.game = game;
		this.work = work;
		this.lib = lib;
	}
}
