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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.cubeia.firebase.poker.pokerbots.server.BotServerDaemon;

public class LocalBotServer {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
    	
		Options o = new Options();
		
		o.addOption(OptionBuilder.
				withArgName("PORT").
				hasArg().
				withDescription("port to open at, defaults to 8080").
				create("p"));
		
		o.addOption(OptionBuilder.
				withArgName("ID").
				hasArg().
				withDescription("start id of all default batches, defaults to 1").
				create("i"));
		
		o.addOption("h", "help", false, "print help");
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(o, args);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		if(cmd.hasOption("h")) {
			new HelpFormatter().printHelp("./run.sh", o);
			return;
		}
    	
		int startid = get(cmd, "i", 1);
		int port = get(cmd, "p", 8080);
		
		BotServerDaemon.startLocalHTTP(port, startid);
	}

	private static int get(CommandLine cmd, String option, int defVal) {
		if(cmd.hasOption(option)) {
			return Integer.parseInt(cmd.getOptionValue(option));
		} else {
			return defVal;
		}
	}

}
