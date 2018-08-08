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

package com.cubeia.firebase.bot.packet;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;
import org.apache.mina.common.IoSession;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.BotState;
import com.cubeia.firebase.bot.ai.MttAI;
import com.cubeia.firebase.bot.mina.crypto.AESCryptoProvider;
import com.cubeia.firebase.bot.mina.crypto.SessionKey;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.io.protocol.EncryptedTransportPacket;
import com.cubeia.firebase.io.protocol.Enums;
import com.cubeia.firebase.io.protocol.Enums.TournamentRegisterResponseStatus;
import com.cubeia.firebase.io.protocol.Enums.WatchResponseStatus;
import com.cubeia.firebase.io.protocol.CreateTableResponsePacket;
import com.cubeia.firebase.io.protocol.FilteredJoinTableAvailablePacket;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.JoinResponsePacket;
import com.cubeia.firebase.io.protocol.LeaveResponsePacket;
import com.cubeia.firebase.io.protocol.LoginResponsePacket;
import com.cubeia.firebase.io.protocol.MttPickedUpPacket;
import com.cubeia.firebase.io.protocol.MttRegisterResponsePacket;
import com.cubeia.firebase.io.protocol.MttSeatedPacket;
import com.cubeia.firebase.io.protocol.MttTransportPacket;
import com.cubeia.firebase.io.protocol.NotifySeatedPacket;
import com.cubeia.firebase.io.protocol.PingPacket;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;
import com.cubeia.firebase.io.protocol.TableRemovedPacket;
import com.cubeia.firebase.io.protocol.TableSnapshotListPacket;
import com.cubeia.firebase.io.protocol.TableSnapshotPacket;
import com.cubeia.firebase.io.protocol.TableUpdateListPacket;
import com.cubeia.firebase.io.protocol.TableUpdatePacket;
import com.cubeia.firebase.io.protocol.TournamentRemovedPacket;
import com.cubeia.firebase.io.protocol.TournamentSnapshotListPacket;
import com.cubeia.firebase.io.protocol.TournamentSnapshotPacket;
import com.cubeia.firebase.io.protocol.TournamentUpdateListPacket;
import com.cubeia.firebase.io.protocol.TournamentUpdatePacket;
import com.cubeia.firebase.io.protocol.UnwatchResponsePacket;
import com.cubeia.firebase.io.protocol.WatchResponsePacket;

/**
 * 
 * Created on 2006-sep-26
 * @author Fredrik Johansson
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public class PacketHandler extends DefaultPacketHandler {

	private static final Logger log = Logger.getLogger(PacketHandler.class);
	
    /** the bot that owns the handler */
    Bot bot;
    
    private AtomicBoolean reseating = new AtomicBoolean(false);
    
    public PacketHandler(Bot bot) {
        this.bot = bot;
    }
    
    @Override
	public void visit(LoginResponsePacket packet) {
        if (packet.status == Enums.ResponseStatus.OK) {
            bot.logDebug("Logged in with pid: "+packet.pid);
            bot.setPid(packet.pid);
    		bot.setState(BotState.LOGGED_IN);
        } else {
            bot.logDebug("Login failed");
        }
	}

	@Override
	public void visit(JoinResponsePacket packet) {
		if (bot.getRequestedTableId() != packet.tableid && !(bot.getBotAI() instanceof MttAI)) {
			bot.logWarn("I got a join response for a table I did not request. Requested: "+bot.getRequestedTableId()+" Packet: "+packet);
		} else {
			
			if (packet.status == Enums.JoinResponseStatus.OK) {
				handleSeated(packet.tableid);	
				reseating.set(false);
			} else {
				bot.getBotAI().handleJoinDenied();
			}
		}
	}
	
	public void visit(PingPacket packet) {
		bot.sendPacket(packet);
	}
	
	@Override
	public void visit(FilteredJoinTableAvailablePacket packet) {
	    bot.joinTable(packet.tableid, packet.seat);
	}
	
	@Override
	public void visit(LeaveResponsePacket packet) {
		if (packet.status == Enums.ResponseStatus.OK) {
		    bot.getGroup().getStats().seated.decrementAndGet();
		    if (bot.getBotAI().getTable().getId() == packet.tableid) {
		        bot.logDebug("I successfully left a table: "+packet.tableid);
		        
		        if (!reseating.get()) {
		        	bot.getGroup().getLobby().blacklistTable(packet.tableid);
		            bot.setState(BotState.FREE);
		            reseating.set(true);
		        }
		    } else {
		        // The server has cleaned up a previous session.
		        bot.logDebug("An old session was cleaned up at table: "+packet.tableid);
		    }
	        
		} else {
		    if (!reseating.get()) {
		    	bot.getGroup().getLobby().blacklistTable(packet.tableid);
		        bot.getBotAI().handleLeaveDenied();
                reseating.set(true);
            }
		}
	}
	
	@Override
	public void visit(TableSnapshotListPacket packet) {
		for (TableSnapshotPacket p : packet.snapshots) {
			visit(p);
		}
	}
	
	@Override
	public void visit(TableUpdateListPacket packet) {
		for (TableUpdatePacket p : packet.updates) {
			visit(p);
		}
	}
    
    @Override
    public void visit(TableSnapshotPacket packet) {
        if (bot.getBotAI().processLobbyPackets()) {
            bot.getGroup().handleTableSnapshot(packet);
        }
    }

    @Override
	public void visit(TableUpdatePacket packet) {
        if (bot.getBotAI().processLobbyPackets()) {
            bot.getGroup().handleTableUpdated(packet);
        }
	}
    
    public void visit(TournamentSnapshotListPacket packet) {
    	for (TournamentSnapshotPacket p : packet.snapshots) {
    		visit(p);
    	}
    }
    
    public void visit(TournamentUpdateListPacket packet) {
    	for (TournamentUpdatePacket p : packet.updates) {
    		visit(p);
    	}
    }
    
	@Override
    public void visit(TableRemovedPacket packet) {
    	bot.getGroup().handleTableRemoved(packet);
    }
	
    @Override
    public void visit(TournamentSnapshotPacket packet) {
        if (bot.getBotAI().processLobbyPackets()) {
            bot.getGroup().handleTournamentSnapshot(packet);
        }
    }

    @Override
    public void visit(TournamentUpdatePacket packet) {
        if (bot.getBotAI().processLobbyPackets()) {
            bot.getGroup().handleTournamentUpdated(packet);
        }
    }
    
    @Override
    public void visit(TournamentRemovedPacket packet) {
        bot.getGroup().handleTournamentRemoved(packet);
    }
    
	@Override
    public void visit(GameTransportPacket packet) {
		bot.getBotAI().verifyTablePacketId(packet.tableid);
		bot.getBotAI().handleGamePacket(packet);
	}
	
	@Override
	public void visit(CreateTableResponsePacket packet) {
		bot.getBotAI().handleCreateTableResponsePacket(packet);
	}
	
	public void visit(ServiceTransportPacket packet) {
		bot.getBotAI().handleServiceTransportPacket(packet);		
	}
	
	@Override
	public void visit(MttTransportPacket packet) {
	    bot.getBotAI().handleTournamentPacket(packet);
	}

	@Override
	public void visit(NotifySeatedPacket packet) {
		bot.logDebug("I am already seated at: "+packet.tableid+" seat: "+packet.seat);
		if (!bot.getBotAI().isReseating()) {
			// Only one table per bot
			bot.getBotAI().setReseating(true);
			bot.joinTable(packet.tableid, packet.seat);
		} else {
			bot.logDebug("Ignoring previous seat: table["+packet.tableid+"] seat["+packet.seat+"]");
		}
	}
	
	
	@Override
    public void visit(MttRegisterResponsePacket packet) {
       if (packet.status != TournamentRegisterResponseStatus.OK) {
           bot.getBotAI().handleTournamentRegistrationDenied(packet.mttid);
           bot.logDebug("I was denied registering on tournament: "+packet.mttid+" Reason: "+packet.status);
           
       } else {
           if (bot.getBotAI() instanceof MttAI) {
               bot.setState(BotState.MTT_REGGED);
           }
       }
    }
	
	@Override
    public void visit(MttSeatedPacket packet) {
	    bot.joinTable(packet.tableid, packet.seat);
    }

	private void handleSeated(int tableId) {
		Table t = new Table();
		t.setId(tableId);
		bot.getBotAI().setTable(t);
		bot.setState(BotState.SEATED);
		bot.getGroup().getStats().seated.incrementAndGet();
	}
    
    @Override
    public void visit(MttPickedUpPacket packet) {
        if (packet.keepWatching) {
            bot.unwatchTable();
        }
    }
    
    /**
     * We can get watch responses without sending out watch requests
     * if the game picks up players; i.e. transforms a player from
     * joined to watcher. But we dont care about watching tables 
     * since that interferes with the seated table vs actions from 
     * table check so we will unwatch it directly. 
     */
    @Override
    public void visit(WatchResponsePacket packet) {
    	if (packet.status.equals(WatchResponseStatus.OK)) {
    		log.debug("Will unwatch table: "+packet.tableid);
    		bot.unwatchTable(packet.tableid);
    	}
    }
    
    @Override
    public void visit(UnwatchResponsePacket packet) {
       // bot.logDebug("I stopped watching table: "+packet.tableid+" : "+packet.status.name());
    }
    
    @Override
    public void visit(EncryptedTransportPacket packet) {
    	if (packet.func == Bot.SESSION_KEY_RESPONSE) {
    		System.err.println(bot.getId() + " received key.");
    		IoSession session = bot.getClient().getSession();
			AESCryptoProvider provider = (AESCryptoProvider) session.getAttribute(Bot.CRYPTO_PROVIDER);
			session.setAttribute(Bot.USE_CRYPTO);
    		provider.setSessionKey(decryptKey(packet));
    		bot.setState(BotState.CONNECTED);
    	}
    }

    private SessionKey decryptKey(EncryptedTransportPacket packet) {
    	log.info("received session key: " + new BigInteger(packet.payload));
    	
    	Cipher cipher;
    	try {
    		cipher = Cipher.getInstance("RSA");
    		cipher.init(Cipher.DECRYPT_MODE, bot.getPrivateKey());
    		byte[] sessionKeyBytes = cipher.doFinal(packet.payload);
    		
    		SessionKey sessionKey = new SessionKey(sessionKeyBytes);
    		return sessionKey;
    	} catch (Exception e) {
    		throw new RuntimeException("Failed decrypting key.", e);
    	}
    }
    
	
}

