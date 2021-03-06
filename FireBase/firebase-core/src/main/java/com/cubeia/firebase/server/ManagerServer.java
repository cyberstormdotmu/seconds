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
package com.cubeia.firebase.server;

import java.io.File;
import java.security.SecureRandom;

import com.cubeia.firebase.api.server.SystemException;
import com.cubeia.firebase.server.instance.ServerInstance;
import com.game.server.bootstrap.BootstrapNode;
import com.game.server.bootstrap.InitFolders;
import com.game.server.bootstrap.SharedClassLoader;

/**
 * Simple test class for running a single manager node service with
 * id "man1" within a pre-setup environment, such as eclipse.
 * 
 * @author lars.nilsson
 */
public class ManagerServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	try {
	    	System.setProperty("eclipse", "true");
	    	final ServerInstance inst = new ServerInstance();
	    	inst.addTrustedSarLocation(new File(new File(Constants.FIREBASE_HOME), "dist/"));
	    	inst.init(checkId(args), checkInitFolder(args), new SharedClassLoader(ManagerServer.class.getClassLoader()), new BootstrapNode[] { new BootstrapNode("manager", "man1") });
	    	inst.start();
	    	Runtime.getRuntime().addShutdownHook(new Thread() {
	    		@Override
	    		public void run() {
	    			inst.stop();
	    			inst.destroy();
	    		}
	    	});
    	} catch (SystemException e) {
			e.printStackTrace();
		}
    }
    
	private static InitFolders checkInitFolder(String[] args) {
		return new InitFolders(new File("src/config"), new File("game/"), new File("lib/"), new File("work/"));
	}
	
	private static String checkId(String[] args) {
		if(args.length > 0) return args[0];
		return String.valueOf(new SecureRandom().nextInt());
	}
}

