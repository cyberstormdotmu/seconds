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

package com.cubeia.firebase.bot.ai;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.action.Action;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.io.protocol.CreateTableResponsePacket;
import com.cubeia.firebase.io.protocol.MttTransportPacket;
import com.cubeia.firebase.io.protocol.ProbePacket;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;
import com.cubeia.firebase.io.protocol.VersionPacket;
import com.cubeia.firebase.poker.pokerbots.thread.JmxScheduledExecutor;

/**
 * Functionality common to all abstract AI implementation.
 * The Basic AI and MTT AI both provides game specific bots
 * with abstract and common functionality, but they both
 * share some logic which is contained in this class.
 *
 * @author Fredrik
 */
public abstract class AbstractAI implements AI {
	
	private static transient Logger log = Logger.getLogger(AbstractAI.class);
	
	private static final int KEEP_ALIVE_DELAY_S = 10;
	
    protected final Bot bot;
    
    /** Executor for actions */
    public static JmxScheduledExecutor executor = new JmxScheduledExecutor(4, "BotAI");
    
    /** The table is the state model that holds current information */
    protected Table table = new Table();
    
	/** Only true if we have taken a seat from a previous session */
    protected boolean reseating = false;
    
    private ScheduledFuture<?> keepAliveFuture;
    
	public AbstractAI(Bot bot) {
		this.bot = bot;
	}
	
	public String createScreenName(int botId) {
		 return "Bot_" + botId;
	 }


    /**
     * @return Returns the bot.
     */
    public Bot getBot() {
        return bot;
    }
    
    public boolean trackTableState() {
        return true;
    }
    
    /**
     * The bot was connected. Take actions accordingly, i.e. login
     *
     */
    protected void handleConnect() {
        Action action = new Action(bot) {
            public void run() {
                bot.login();
            }
        };
        
        executor.schedule(action, Delays.LOGIN_DELAY_SECONDS, TimeUnit.SECONDS);
        startKeepAlive();
    }
    
    /**
     * Override this to provide your own handling.
     */
    public void handleDisconnected() {
    	stopKeepAlive();
    }
    
    @Override
    public void stop() {
    	stopKeepAlive();
    }
    
    public boolean processLobbyPackets() {
        return false;
    }
    
    protected void logDebug(String msg) {
    	log.debug(bot.getScreenname()+" - "+msg);
    }
    
    protected void logInfo(String msg) {
    	log.info(bot.getScreenname()+" - "+msg);
    }
    
    protected void logError(String msg) {
    	log.error(bot.getScreenname()+" - "+msg);
    }
    
    protected void logError(String msg, Throwable th) {
    	log.error(bot.getScreenname()+" - "+msg, th);
    }
    
    protected void logFatal(String msg) {
    	log.fatal(bot.getScreenname()+" - "+msg);
    }
    
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
	public void handleProbe(ProbePacket packet) {}

	public void setReseating(boolean b) {
		reseating = true;
	}
	
	public boolean isReseating() {
		return reseating;
	}
	
	public void handleTournamentPacket(MttTransportPacket packet) {
		bot.logDebug("Unhandled packet (override handleTournamentPacket()): "+packet);
	}

	public void handleServiceTransportPacket(ServiceTransportPacket packet) {
		bot.logDebug("Unhandled packet (override handleServiceTransportPacket()): "+packet);
	}

	@Override
	public LoginCredentials getCredentials() {
		return null;
	}
	
	/**
	 * Override this to handle create table responses
	 */
	@Override
	public void handleCreateTableResponsePacket(CreateTableResponsePacket packet) {
		bot.logDebug("Unhandled packet (override handleCreateTableResponsePacket()): "+packet);
	}   
	
	protected void startKeepAlive() {
    	KeepAlive keepAlive = new KeepAlive();
    	keepAliveFuture = executor.scheduleWithFixedDelay(keepAlive, 1, KEEP_ALIVE_DELAY_S, TimeUnit.SECONDS);
  	}
    
    protected void stopKeepAlive() {
    	if (keepAliveFuture != null) {
    		keepAliveFuture.cancel(false);
    	}
  	}
	
	private class KeepAlive implements Runnable {
		@Override
		public void run() {
			// VersionPacket version = new VersionPacket(1, 1, new ProtocolObjectFactory().version());
			VersionPacket version = new VersionPacket(1, 1, 8559);
	  		bot.sendPacketAsync(version);
		}
    }
	
}