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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.automation.AutomationBatch;
import com.cubeia.firebase.bot.automation.AutomationConfig;
import com.cubeia.firebase.bot.automation.AutomationScenario;
import com.cubeia.firebase.bot.automation.LogHarvester;
import com.cubeia.firebase.poker.pokerbots.server.BotServerDaemon;
import com.cubeia.firebase.poker.pokerbots.server.http.BotHTTPServer;


public class AutoLoadServer {
	
	private static Logger log = Logger.getLogger(BotHTTPServer.class);
	
	private static String serverLogFile = "logs/test.log";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	   	int port = 8080;
		String scenarioName = null;
		String configFileName = "config/autoLoadTestConfig.xml";
		
		for ( int i = 0; i < args.length; i ++ ) {
			if ( args[i].equals("-p") ) {
				port = Integer.parseInt(args[i+1]);
				i ++;
			}
			if ( args[i].equals("-s") ) {
				scenarioName = args[i+1];
				i++;
			}
			if ( args[i].equals("-c") ) {
				configFileName = args[i+1];
				i++;
			}
			
			if ( args[i].equals("-l") ) {
				serverLogFile = args[i+1];
				i++;
			}
			

		}
		
		if ( scenarioName == null ) {
			System.out.println("Name of scenario to run must be set (-s <scenarioName>)");
			System.exit(1);
		}
		
		log.info("AutoLoadServer started");
		log.info("Config file      : " + configFileName);
		log.info("Scenarion to run : " + scenarioName);
		log.info("Server log file  : " + serverLogFile);
		
		AutomationConfig config = new AutomationConfig();
		
		try {
			config.readConfig(configFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		startLocalServer(port);

		// find scenario
		Map<String, AutomationScenario> scenarios = config.getScenarios();
		AutomationScenario automationScenario = scenarios.get(scenarioName);
		if ( automationScenario == null ) {
			System.out.println("Can't find scenario: "+scenarioName);
			System.exit(1);
		}	
		
		startScenario(port, automationScenario);
	}

	
	/**
	 * Start a scenario 
	 * @param port
	 * @param automationScenario
	 */
	private static void startScenario(int port, AutomationScenario automationScenario) {
		
		// keep track of scenario start time
		long startTime = System.currentTimeMillis();
		
		// get batches to run in this scenario
		List<AutomationBatch> batchList = automationScenario.getBatches();
		ListIterator<AutomationBatch> batchListIterator = batchList.listIterator();
		while ( batchListIterator.hasNext() ) {
			AutomationBatch batch = batchListIterator.next(); 
			// start batch
			startBatch(batch, port);
		}
		// Log scenarion run time
		log.info("Scenario will run for "+automationScenario.getRuntime()/60+" minute(s)");
		
		// Wait for scenario run time to expire
		try {
			Thread.sleep(automationScenario.getRuntime() * 1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		LogHarvester logHarvester = new LogHarvester();
		
		String outFileName ="logs/"+automationScenario.getName() + "-";
		Date date = new Date(startTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		
		outFileName += dateFormat.format(date) + ".csv";
		
		log.info("AutoLoadServer stopped");
		
		try {
			logHarvester.processLogFile(serverLogFile, outFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Start local http server (botfarm)
	 * @param port - tcp port number
	 */
	private static void startLocalServer(int port) {
		
		BotServerDaemon.startLocalHTTP(port, 1);
		// Give the server som time to settle down
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Start a batch of Bots
	 * @param batch
	 * @param port
	 */
	private static void startBatch(AutomationBatch batch, int port) {
		
		try {

			Map<String, String> properties = batch.getProperties();

			// create url, base first
            String urlString = "http://localhost:"+port+"/batch/start?";
            
 
			// append all found properties to url
			for (String key : properties.keySet()) {
				urlString += key +"=" + properties.get(key) + "&";
				
			}

			// connect to http server
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			
			// trigger 
            connection.getContentType();
                        
		} catch(Exception e) {
			 e.printStackTrace();
		}
	}
	
}