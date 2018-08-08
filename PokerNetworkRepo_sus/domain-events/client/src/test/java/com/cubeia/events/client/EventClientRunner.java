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
package com.cubeia.events.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.events.event.GameEvent;
import com.cubeia.events.event.OperatorEvent;
import com.cubeia.events.event.PlayerEvent;
import com.cubeia.events.event.achievement.BonusEvent;
import com.cubeia.events.event.operator.BonusEvents;
import com.cubeia.events.event.operator.OperatorEvents;
import com.cubeia.events.event.operator.PlayerEvents;

public class EventClientRunner implements EventListener {

	static Logger log = LoggerFactory.getLogger(EventClientRunner.class);
	
	public static void main(String[] args) {
		try {
			
			EventClient client = new EventClient();
//			client.setEventListener(new EventClientRunner());
			
			Thread.sleep(1000);
			
			//sendBonusEvent(client);
			// sendOperatorAuthenticationError(client);
			// sendPokerRoundEndEvent(client);
			//sendTournamentPayoutEvent(client);
			// sendUserLoggedInEvent(client);
			
//			while (true) {
			    sendPaymentInfoAsBonusEvent(client);
//			    Thread.sleep(3000);
//			}
			
			
//			sendBonusEvent(client);
			
//			while (true) {
//				Thread.sleep(100);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
		
	}
	
	private static void sendPaymentInfoAsBonusEvent(EventClient client) {
        BonusEvent event = new BonusEvent();
        event.player = "1003";
        event.type = "payment";
        event.name = "deposit";
        event.subType = "confirmed";
        
        event.attributes.put("ADDRESS", "x84398498");
        
        event.attributes.put("ORIGINAL_AMOUNT", "500000");
        event.attributes.put("ORIGINAL_CURRENCY", "SAT");
        
        event.attributes.put("CONVERTED_AMOUNT", "5.00");
        event.attributes.put("CONVERTED_CURRENCY", "XMB");
        
        client.send(event);
    }

    @SuppressWarnings("unused")
	private static void sendUserLoggedInEvent(EventClient client) {
		PlayerEvent event = PlayerEvents.createLoggedIn(22L, 0L, "whatever");
		client.send(event);
	}

	@SuppressWarnings("unused")
	private static void sendOperatorAuthenticationError(EventClient client) {
		OperatorEvent event = OperatorEvents.createAuthenticationError(1L, "test", "http://localhost/foo");
		client.send(event);
	}
	
	private static void sendBonusEvent(EventClient client) {
		BonusEvent event = BonusEvents.createBonusEvent("player", "game", "name of achievement");
		log.info(" >>>>> SEND Bonus Event");
		client.send(event);
	}
	
	private static void sendPokerRoundEndEvent(EventClient client) {
		GameEvent event = new GameEvent();
		event.game = "poker";
		event.type = "roundEnd";
		event.player = "222";
		event.operator = "10";
		event.attributes.put("stake", "2");
		event.attributes.put("winAmount", "4");
		event.attributes.put("win", "true");
		event.attributes.put("accountCurrency", "XCC");
		log.info(" >>>>> SEND Game Event");
		client.send(event);
	}
	
	private static void sendTournamentPayoutEvent(EventClient client) {
		GameEvent event = new GameEvent();
		event.game = "poker";
		event.type = "tournamentPayout";
		event.player = "11122";
		event.operator = "10";
		event.attributes.put("tournamentId", "11");
		event.attributes.put("tournamentName", "Evening Tourny");
		event.attributes.put("tournamentPosition", "1");
		event.attributes.put("stake", "1");
		event.attributes.put("winAmount", "0.4");
		event.attributes.put("accountBalance", "1200");
		event.attributes.put("accountCurrency", "XCC");
		event.attributes.put("screenname", "BanarneDLX");
		log.info(" >>>>> SEND Tournament Payout Event");
		client.send(event);
	}

	public void onEvent(GameEvent event) {
		log.info(" *** On Game Event: "+event);
	}

	@Override
	public void onEvent(BonusEvent event) {
		log.info(" *** On Bonus Event: "+event);
	}

}
