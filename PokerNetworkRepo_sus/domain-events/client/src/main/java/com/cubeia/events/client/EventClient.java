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

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.events.client.mq.Consumer;
import com.cubeia.events.client.mq.DomainEventProperties;
import com.cubeia.events.client.mq.Producer;
import com.cubeia.events.event.Event;
import com.cubeia.events.event.GameEvent;
import com.cubeia.events.event.OperatorEvent;
import com.cubeia.events.event.PlayerEvent;
import com.cubeia.events.event.SystemEvent;
import com.cubeia.events.event.achievement.BonusEvent;


public class EventClient {

	public static final String GAME_EVENTS = "gameEvents";
	public static final String PLAYER_EVENTS = "playerEvents";
	public static final String OPERATOR_EVENTS = "operatorEvents";
	public static final String SYSTEM_EVENTS = "systemEvents";
	public static final String BONUS_EVENTS = "bonusEvents";

	Logger log = LoggerFactory.getLogger(getClass());
	
	Producer producer = new Producer();
	
	ObjectMapper mapper = new ObjectMapper();

	EventListener eventListener;
	
	Consumer consumer = new Consumer();
	
	private DomainEventProperties props = new DomainEventProperties();
	
	public EventClient() {}
	
//	public EventClient(EventListener eventListener) {
//		this.eventListener = eventListener;
//		startListeningForEvent();
//	}
	
	/**
	 * @param eventListener
	 */
	public void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
		startListeningForEvent();
	}
	
	
	private void startListeningForEvent() {
		if (props.getEnabled()) {
			log.debug("Start consuming events");
			consumer.setEventListener(eventListener);
			consumer.startConsuming(EventClient.BONUS_EVENTS);
			// consumer.startConsuming(EventClient.GAME_EVENTS);
		} else {
			log.info("Event client is disabled by configuration and will not consume bonus events from MQ");
		}
	}

	public void send(GameEvent event) {
		marshalAndSend(event, GAME_EVENTS);
	}
	
	public void send(OperatorEvent event) {
		marshalAndSend(event, OPERATOR_EVENTS);
	}
	
	public void send(SystemEvent event) {
		marshalAndSend(event, SYSTEM_EVENTS);
	}
	
	public void send(PlayerEvent event) {
		marshalAndSend(event, PLAYER_EVENTS);
	}
	
	public void send(BonusEvent event) {
		marshalAndSend(event, BONUS_EVENTS);
	}
	
	private void marshalAndSend(Event event, String subject) {
		if (props.getEnabled()) {
			try {
				String json = mapper.writeValueAsString(event);
				log.debug("Sending event json: "+json);
				producer.send(json, subject);
			} catch (Exception e) {
				log.error("No running MQ found. Will drop this event.", e);
			}
		}
	}

	
}
