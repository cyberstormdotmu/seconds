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

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.BotState;
import com.cubeia.firebase.bot.action.Action;
import com.cubeia.firebase.bot.util.Arithmetic;
import com.cubeia.firebase.io.protocol.LobbySubscribePacket;
import com.cubeia.firebase.io.protocol.Enums.LobbyType;

/**
 * Implements some basic state handling.
 * Extend this class to implement the game AI without
 * having to implement state logic and execution handling.
 * 
 * Created on 2006-sep-28
 * @author Fredrik Johansson
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public abstract class BasicAI extends AbstractAI {
    
	/** The chat intervall will be ised as base for randomized chat */
	private static final int CHAT_INTERVALL = 30;

	protected boolean chat = false;
	
	/** Subscribe to lobby while playing? */
	protected boolean subscribeToLobby = false;
	
	/** Join tables through waiting list instead of regular join? */
	protected boolean waitinglist = false;
	
    /** How do the bot stay at the table? Configurable through web interface */
    protected int leaveDelaySeconds = Delays.LEAVE_DELAY_SECONDS;

	private ScheduledFuture<?> chatTask;
	
	private int deniedSeatCounter = 0;
    
    public BasicAI(Bot bot) {
        super(bot);
    }
    
    public enum StatusType {
        JOIN,
        LEAVE
    }
    
    /**
     * Take actions according to the new state
     */
    public void handleStateChange(BotState state) {
       switch (state) {
           
           case CONNECTED:
               handleConnect();
               break;
               
           case LOGGED_IN:
               handleLoggedin();
               break;
           
           case SEATED:
               handleSeated();
               break;
               
           case FREE:
        	   handleFree();
               break;
               
			default:
				break;
	               
       }
    }
    

    /**
     * Handle a server denial message.
     */
    public void handleJoinDenied() {
    	bot.getGroup().getStats().deniedSeating.incrementAndGet();
    	deniedSeatCounter++;
    	if (deniedSeatCounter > 20) {
    		bot.logWarn("I was denied my seat request ("+deniedSeatCounter+" times). Rescheduling");
    	} else {
    		bot.logInfo("I was denied my seat request ("+deniedSeatCounter+" times). Rescheduling");
    	}
        // Seating was denied, reschedule
        scheduleJoinRandomTable(Delays.LOGIN_DELAY_SECONDS);
    }
    
    
    /**
     * Handle a server denial message.
     */
    public void handleLeaveDenied() {
        bot.logInfo("My leave seat request was denied. I will request a new table.");
//        // Leave was deneid, reschedule leave
//    	scheduleLeaveTable(Delays.LOGIN_DELAY_SECONDS);
    	
    	// Hmmm maybe we should ignore this and request a new seat anyway
        scheduleJoinRandomTable(Delays.LOGIN_DELAY_SECONDS);
    }
    
    
   /**
    * I am free and not seated.
    * Schedule a sit request in a while.
    */
   protected void handleFree() {
       scheduleJoinRandomTable(Delays.LOGIN_DELAY_SECONDS);
   }
    

   public void handleTournamentRegistrationDenied(int tournamentId) {
       bot.logWarn("Unexpected tournament registration denied for tournament: "+tournamentId);
   }

    /**
     * The bot was seated at a table
     *
     */
    protected void handleSeated() {
    	logInfo("Joined table: "+table.getId());
    	deniedSeatCounter = 0;
    	if (leaveDelaySeconds > 0) {
    		int i = new Random().nextInt(leaveDelaySeconds) + leaveDelaySeconds/2;
    		scheduleLeaveTable(i);
    	}
    	if (chat) {
    		scheduleChatAtTable(Arithmetic.GaussianAverage(CHAT_INTERVALL));
    	}
    }
    
    /**
     * The bot was logged in successfully.
     * Take a seat at a random table after a certain 
     * delay. This delay needs to accommodate time for 
     * a possible reseating to occur.
     */
    protected void handleLoggedin() {
    	bot.getGroup().getStats().loggedin.incrementAndGet();
    	logInfo("Logged in");
    	if (subscribeToLobby) {
    	    startLobbySubscription("/");
    	}
    	if (waitinglist) {
    	    scheduleJoinWaitingList(Delays.SEAT_DELAY_SECONDS);
    	} else {
    	    scheduleJoinRandomTable(Delays.SEAT_DELAY_SECONDS);
    	}
    }

   
    protected void startLobbySubscription(String path) {
        startLobbySubscription(path, LobbyType.REGULAR);
    }
 
    protected void startLobbySubscription(String path, LobbyType type) {
        LobbySubscribePacket packet = new LobbySubscribePacket();
        packet.gameid = bot.getGroup().getConfig().getGameId();
        packet.address = path;
        packet.type = type;
        logDebug("Subscribe to Lobby: "+packet);
        bot.sendPacket(packet);
    }


    private void scheduleJoinWaitingList(int seconds) {
        Action action = new Action(bot) {
            public void run() {
                if (!reseating) {
                    bot.joinWaitingList();
                }
            }
        };
        executor.schedule(action, seconds, TimeUnit.SECONDS);
    }
    
    /**
     * Join random table in LOGIN_DELAY_SECONDS seconds
     * if we have not been reseated.
     */
    private void scheduleJoinRandomTable(long seconds) {
        Action action = new Action(bot) {
            public void run() {
            	try {
	            	if (!reseating) {
		            	if (!bot.joinRandomTable()) {
		            		scheduleJoinRandomTable(Delays.RE_SEAT_DELAY_SECONDS);
		            		return;
		            	}
	            	}
            	} catch (Throwable th) {
            		logError("Scheduled join failed", th);
            	}
            }
        };
        executor.schedule(action, seconds, TimeUnit.SECONDS);
    }

	public void setLeaveDelaySeconds(int leaveDelaySeconds) {
	    logDebug("will leave table in "+leaveDelaySeconds+"s");
		this.leaveDelaySeconds = leaveDelaySeconds;
	};
     
	public void verifyTablePacketId(int tableid) {
    	// verify table
    	if (table.getId() != tableid) {
    		logFatal("I received wrong table id! I am seated at: "+table.getId()+". I got packet from: "+tableid);
    	} 
	}
	
	public boolean isChat() {
		return chat;
	}

	public void setChat(boolean chat) {
		this.chat = chat;
	}
	
	 public boolean isLobby() {
        return subscribeToLobby;
    }
    
    public void setLobby(boolean subscribeToLobby) {
        this.subscribeToLobby = subscribeToLobby;
    }
    
	
	public boolean isWaitinglist() {
        return waitinglist;
    }


    public void setWaitinglist(boolean waitinglist) {
        this.waitinglist = waitinglist;
    }


    @Override
	public void handleDisconnected() {
    	super.handleDisconnected();
		if (chatTask != null) {
			chatTask.cancel(false);
		}
	}
	
	/**
     * Stand up from seat in n seconds.
     * 
     * @param seconds
     */
	private void scheduleLeaveTable(long seconds) {
        // Schedule to leave table
        Action action = new Action(bot) {
            public void run() {
                stop();
                bot.leaveTable();
                if (chatTask != null) {
        			chatTask.cancel(false);
        		}
            }
        };
        executor.schedule(action, seconds, TimeUnit.SECONDS);
    }
	
	private void scheduleChatAtTable(long seconds) {
		Action action = new Action(bot) {
            public void run() {
                bot.chat();
                scheduleChatAtTable(Arithmetic.GaussianAverage(CHAT_INTERVALL));
            }
        };
        if (bot.getState() == BotState.SEATED) {
        	chatTask = executor.schedule(action, seconds, TimeUnit.SECONDS);
        }
	}
	
}

