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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.BotState;
import com.cubeia.firebase.bot.action.Action;
import com.cubeia.firebase.bot.model.Tournament;
import com.cubeia.firebase.io.protocol.Enums.LobbyType;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.MttRegisterRequestPacket;
import com.cubeia.firebase.io.protocol.Param;
import com.cubeia.firebase.io.protocol.ProbePacket;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;

/**
 * Base for Tournament Bots.
 *
 * @author Fredrik
 */
public class MttAI extends AbstractAI {

	private static final int REGISTER_DELAY_SECONDS = 2;
	
	private static final int REJOIN_AGAIN_DELAY_SECONDS = 30;

	private static transient Logger log = Logger.getLogger(MttAI.class);
	
	/**
	 * The tournament instance to join.
	 */
	private int mttId = -1;
	
	private boolean rejoin = false;
	
	private String tournamentName;
	private String lobbyAddress;
	
	public MttAI(Bot bot) {
		super(bot);
		log.debug("Created MTT AI - "+bot.getPid());
	}
	
	/**
	 * Game AI should handle this.
	 */
	public void handleGamePacket(GameTransportPacket packet) {
		log.debug("Handle : "+packet);
	}
	
	/**
	 * Game AI should handle this.
	 */
	public void handleServiceTransportPacket(ServiceTransportPacket packet) {
		log.debug("Handle : "+packet);
	}	

	public void handleJoinDenied() {
        log.warn("Unexpected event: handleJoinDenied");
    }
	public void handleLeaveDenied() {log.warn("Unexpected event: handleLeaveDenied");}
	public void handleProbePacket(ProbePacket packet) {}
	
	public int getMttId() {
		return mttId;
	}

	public void setMttId(int mttId) {
		this.mttId = mttId;
	}

	
	public boolean trackTableState() {
		return false;
	}

	public boolean isRejoin() {
        return rejoin;
    }

    public void setRejoin(boolean rejoin) {
        this.rejoin = rejoin;
    }

    public void stop() {
    	super.stop();
	}
	
	public void verifyTablePacketId(int tableid) {
      if (table.getId() != tableid) {
            logFatal("I received wrong table id! I am seated at: "+table.getId()+". I got packet from: "+tableid);
       } 
	}

	public void handleStateChange(BotState state) {
		switch (state) {
	        
	        case CONNECTED:
	        	log.debug("MTT AI Connected- "+bot.getPid());
	            handleConnect();
	            break;
	            
	        case LOGGED_IN:
	            handleLoggedin();
	            break;
	            
	        case MTT_REGGED:
	        	handleRegistered();
	        	break;
	        	
	        case MTT_OUT:
	            handleOut();
	            break;
	            
			case FREE:
				break;
				
			case IDLE:
				break;
				
			case SEATED:
				break;
				
			default:
				break;
	        
	    }
	}

	
    /**
	 * I was registered with the tournament.
	 * Just report and wait...
	 *
	 */
	private void handleRegistered() {
		startLobbyObjectSubscription(lobbyAddress, LobbyType.MTT);
		// bot.logDebug("I was successfully registred at the tournament: "+mttId+". Rejoin later: "+rejoin);
	}
	
	
	private void handleOut() {
		stopLobbyObjectSubscription(lobbyAddress, LobbyType.MTT);
	    // bot.logDebug("I was out of the tournament: "+mttId+". Will rejoin new tournament: "+rejoin);
        if (rejoin) {
            rejoinTournament();
        }
    }

    private void rejoinTournament() {
        logInfo("Will try to rejoin the same tournament: "+tournamentName);
        boolean found = false;
        for (Tournament t : bot.getGroup().getLobby().getTournaments().values()) {
            if (t.isOpenForRegistration() && t.getName().equals(tournamentName)) {
                // Found a match. Register for this tournament
                mttId = t.getId();
                scheduleJoinTournament();
                found = true;
            }
        }
        if (!found) {
            logInfo("Could not find an open tournament with name: "+tournamentName+". Will reschedule rejoin in "+REJOIN_AGAIN_DELAY_SECONDS+" s.");

            Action action = new Action(bot) {
                @Override
                public void run() {
                    handleOut();
                }
            };
            executor.schedule(action, REJOIN_AGAIN_DELAY_SECONDS, TimeUnit.SECONDS);
        }
    }


    public void handleTournamentRegistrationDenied(int tournamentId) {
	    if (mttId > 0) {
            if(rejoin) {
                logInfo("Registration denied, trying to join tournament with same name in "+REJOIN_AGAIN_DELAY_SECONDS+" sec");
                Action action = new Action(bot) {
                    @Override
                    public void run() {
                        rejoinTournament();
                    }
                };
                executor.schedule(action, REJOIN_AGAIN_DELAY_SECONDS, TimeUnit.SECONDS);
            }
	    } else {
	       // nfo += "I will reschedule new random join.";
	        scheduleJoinTournament();
	    }
    }

	/**
     * The bot was logged in successfully.
     * Try and register with the given tournament.
     */
    protected void handleLoggedin() {
    	bot.getGroup().getStats().loggedin.incrementAndGet();
    	scheduleJoinTournament();
    }
    
    /**
    * join a tournament by sending a register packet
    * 
    * @param targetId
    */
    private void joinTournament(int targetId) {
        MttRegisterRequestPacket request = new MttRegisterRequestPacket();
        request.mttid = targetId;
        request.params = new ArrayList<Param>();
        bot.sendPacket(request);

        Tournament tournament = bot.getGroup().getLobby().getTournament(targetId);
        lobbyAddress = tournament.getAddress() + "/" + targetId;
        
        logInfo("Joining tournament: "+tournament+", with lobby address: "+lobbyAddress);
        
        // If rejoin then store name for future reference
        if (rejoin) {
            tournamentName = tournament.getName();
        }        
    }

    private int reserveTournamentSeat() {
        int targetId = -1;
        if (mttId <= 0) {
            targetId = bot.getGroup().getLobby().getTargetTournament(bot);
            if (targetId <= 0) {
                bot.logDebug("I did not get a tournament to join. I will reschedule");
            }
        } else {
            if (bot.getGroup().getLobby().isTournamentSeatable(mttId, bot.getLeaveEmptySeats())) {
                targetId = mttId;
            } else {
                bot.logDebug("Leaving " + bot.getLeaveEmptySeats() + " seats empty. Not joining tournament " + mttId);
            }
        }
        return targetId;
    }
                
    private void scheduleJoinTournament() {
        Action action = new Action(bot) {

            public void run() {
            	try {
	                int targetId = reserveTournamentSeat();
	                
	                if (targetId > 0) {
	                    joinTournament(targetId);
	                }else{
	                    scheduleJoinTournament();
	                }
            	} catch (Exception e) {
            		log.error("Failed to join tournament ", e);
            	}
            }
        };
        
        executor.schedule(action, REGISTER_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    protected void startLobbyObjectSubscription(String path, LobbyType type) {
//        LobbyObjectSubscribePacket packet = new LobbyObjectSubscribePacket();
//        packet.gameid = bot.getGroup().getConfig().getGameId();
//        packet.address = path;
//        packet.type = type;
//        logDebug("Subscribe to lobby object: "+packet);
//        bot.sendPacket(packet);
    }
    
    protected void stopLobbyObjectSubscription(String path, LobbyType type) {
//        LobbyObjectUnsubscribePacket packet = new LobbyObjectUnsubscribePacket();
//        packet.gameid = bot.getGroup().getConfig().getGameId();
//        packet.address = path;
//        packet.type = type;
//        logDebug("Unsubscribe to lobby object: "+packet);
//        bot.sendPacket(packet);
    }

}
