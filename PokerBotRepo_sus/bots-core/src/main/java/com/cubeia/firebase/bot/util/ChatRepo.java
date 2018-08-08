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

package com.cubeia.firebase.bot.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

/**
 * Loads a file 'chat.txt' from classpath and parses it.
 * ':' will be used to parse messages.
 * 
 * I.e. "user: message" will be parsed and only message
 * will be used. If no ':' are present, the whole string will be used.
 * 
 *
 * @author Fredrik Johansson, Cubeia Ltd
 */
public class ChatRepo {

	private static final String CHAT_FILE = "/chat.txt";

	private static String FALLBACK = "Hi. I am a bot...";

	private static transient Logger log = Logger.getLogger(ChatRepo.class);
	
	private static ChatRepo instance = new ChatRepo();

	private ConcurrentMap<Integer, String> messages = new ConcurrentHashMap<Integer, String>();
	
	private Random rng = new Random();
	
	private ChatRepo() {
		loadChatRepo();
	};


	public static ChatRepo getInstance() {
		return instance;
	}

	private void loadChatRepo() {
		try {
			// InputStream in = ClassLoader.getSystemResourceAsStream(CHAT_FILE);
			InputStream in = ChatRepo.class.getResourceAsStream(CHAT_FILE);
			BufferedReader bufRead = new BufferedReader(new InputStreamReader(in));

			String line;    // String that holds current file line
			int count = 0;  // Line number of count 

			line = bufRead.readLine();
			count++;

			// Read through file one line at time. Print line # and line
			while (line != null){
				line = parseLine(line);
				messages.put(count, line);
				line = bufRead.readLine();
				count++;
			}

			bufRead.close();

		}catch (Exception e){
			log.warn("Chatfile not found or badly formatted. (Looking for file: "+CHAT_FILE+"). Error: "+e);
		}
	}
	
	private String parseLine(String line) {
		String[] parts = line.split(":");
		if (parts.length > 1) {
			return parts[1].trim();
		} else {
			return line;
		}
	}


	public String getRandom() {
		try {
			String msg = null;
			if (messages.size() > 0) {
				int key = rng.nextInt(messages.size());
				msg = messages.get(key);
				if (msg.length() > 127) {
					msg = msg.substring(0, 127);
				}
			}
			
			if (msg == null) {
				msg = FALLBACK;
			}
			return msg;
		} catch(NullPointerException e) {
            return FALLBACK;
        }catch(Exception e) {
			log.warn("Exception when getting random message: "+e);
			return FALLBACK;
		}
	}
	
}
