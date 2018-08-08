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
package com.cubeia.events.client.mq;

import java.io.IOException;

import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.transport.TransportListener;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.events.client.EventClient;
import com.cubeia.events.client.EventListener;
import com.cubeia.events.event.GameEvent;
import com.cubeia.events.event.achievement.BonusEvent;

public class Consumer implements ExceptionListener, TransportListener {

	Logger log = LoggerFactory.getLogger(getClass());

	MqConnectionFactory connections = new MqConnectionFactory();

	EventListener eventListener;

	ObjectMapper mapper = new ObjectMapper();

	public void startConsuming(String topic) {
		try {
			log.debug("Start consuming topic "+topic);
			
			ActiveMQConnection connection = connections.createConsumerConnection();
			
			connection.addTransportListener(this);  
			
			// JMS messages are sent and received using a Session. We will
			// create here a non-transactional session object. If you want
			// to use transactions you should set the first parameter to 'true'
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Getting the topic 
			Destination destination = session.createTopic(topic);

			MessageConsumer consumer = session.createConsumer(destination);
			connection.setExceptionListener(this);

			if (topic.equals(EventClient.BONUS_EVENTS)) {
				consumer.setMessageListener(new BonusMessageListener());
			} else if (topic.equals(EventClient.GAME_EVENTS)) {
				consumer.setMessageListener(new GameMessageListener());
			} else {
				log.warn("No message parser defined for event topic: "+topic+". Events received for this topic will not be reported to any listener.");
			}

		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}


	public void onException(JMSException e) {
		log.error("Event MQ Consumer reported an exception: " +e);
	}

	public void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}


	private class BonusMessageListener implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					BonusEvent event = mapper.readValue(textMessage.getText(), BonusEvent.class);
					log.debug("Event client consumer received Event: "+event+". Will route to listener: "+eventListener.getClass().getSimpleName());
					eventListener.onEvent(event);
				}
			} catch (Exception e) {
				log.error("Error when handling Bonus Event: " +e);
			}
		}
	}

	private class GameMessageListener implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					GameEvent event = mapper.readValue(textMessage.getText(), GameEvent.class);
					log.debug("Event client consumer received Event: "+event+". Will route to listener: "+eventListener.getClass().getSimpleName());
					eventListener.onEvent(event);
				}
			} catch (Exception e) {
				log.error("Error when handling Game Event: " +e);
			}
		}
	}

	@Override
	public void onCommand(Object command) {}


	@Override
	public void onException(IOException e) {
		log.error("Event MQ Consumer reported an IO Exception: " +e);
	}


	@Override
	public void transportInterupted() {
		log.error(" >>>>>>>>>>>>>>>>>>>>>>>>>>>> Event MQ Consumer transport interrupted.");
	}


	@Override
	public void transportResumed() {
		log.error(" >>>>>>>>>>>>>>>>>>>>>>>>>>>> Event MQ Consumer transport resumed.");
	}

}
