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

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.defined.Parameter;
import com.cubeia.firebase.api.util.ParameterUtil;
import com.cubeia.firebase.bot.model.Lobby;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.bot.model.TableSelector;
import com.cubeia.firebase.bot.model.TargetTable;
import com.cubeia.firebase.bot.model.Tournament;
import com.cubeia.firebase.io.protocol.Param;
import com.cubeia.firebase.io.protocol.TableRemovedPacket;
import com.cubeia.firebase.io.protocol.TableSnapshotPacket;
import com.cubeia.firebase.io.protocol.TableUpdatePacket;
import com.cubeia.firebase.io.protocol.TournamentRemovedPacket;
import com.cubeia.firebase.io.protocol.TournamentSnapshotPacket;
import com.cubeia.firebase.io.protocol.TournamentUpdatePacket;


public class BotGroup implements BotGroupMBean {
	
	private Logger log = Logger.getLogger(BotGroup.class);
	
	/** The lobby representation */
	private Lobby lobby = Lobby.getInstance();
	
	/** The bots in this group */
	private ConcurrentMap<Integer,Bot> bots = new ConcurrentHashMap<Integer,Bot>();
	
	
	/** the lobby bot config context */
	private BotConfig lobbyconfig = new BotConfig();
	
	/** The configuration / context */
	private BotConfig config;
	
	private BotGroupConfig groupConfig = new DefaultBotGroupConfig();
	
	private GroupStats stats = new GroupStats();
	
    /** Can be used by creator to keep track of the group */
	private String groupid;
    
	/**
	 * Create a group of bots.
	 * 
	 * 
	 */
	public BotGroup(BotConfig config) {
		
		initJmx();
		
		if(config.getGroupConfigClass() != null) {
			createGroupConfig(config.getGroupConfigClass());
		}

		groupConfig.setCurrency(config.getCurrency());
		groupConfig.setLobbyAttributes(config.getLobbyAttributes());
		
		/* create lobby bot */
		this.config = config;
		lobbyconfig.setGroupid(config.getGroupid());
		lobbyconfig.setHost(config.getHost());
		lobbyconfig.setSize(config.getSize());
		lobbyconfig.setStartid(config.getStartid());
		lobbyconfig.setAiclass("com.cubeia.firebase.bot.ai.impl.lobby.LobbyAI");
		lobbyconfig.setGameId(config.getGameId());
		lobbyconfig.setReconnect(config.isReconnect());
		lobbyconfig.setBatchProperties(config.getBatchProperties());
		lobbyconfig.setTournamentBot(config.isTournamentBot());
		lobbyconfig.setConnectorType(config.getConnectorType());
		lobbyconfig.setCurrency(config.getCurrency());
		
		Bot lobbyBot = createBot(lobbyconfig, lobbyconfig.getStartid());
		lobbyBot.setScreenname(groupConfig.createLobbyBotScreenName(lobbyBot.getId()));
		addBot(lobbyBot);
		
		config.setStartid(config.getStartid()+1);
		this.config = config;
        
//		lobby = Lobby.createPresetLobby();
        
        log.info("Starting a group of bots.");
        log.info("\t AI: "+config.getAiclass());
        log.info("\t Config: "+groupConfig.getClass().getName());
        log.info("\t Count: "+config.getSize());
        log.info("\t Start id: "+config.getStartid());
        log.info("\t Tournament Bots: "+config.isTournamentBot());
        log.info("\t Currency: "+config.getCurrency());
        
		createBots();
	}
	
	
	private void createGroupConfig(String clazz) {
		try {
			Class<?> cl = getClass().getClassLoader().loadClass(clazz);
			if(BotGroupConfig.class.isAssignableFrom(cl)) {
				groupConfig = (BotGroupConfig) cl.newInstance();
			} else {
				throw new IllegalStateException("Class " + cl.getName() + " not instance of BotGroupConfig");
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void createBots() {
		for (int i = 0; i < config.getSize(); i++) {
			Bot bot = createBot(config, config.getStartid()+i);
			addBot(bot);
		}

	}
	
	private Bot createBot(BotConfig config, int id) {
		Bot bot = new Bot(this, id, config);
		bot.setScreenname(groupConfig.createBotScreenName(id));
		getStats().started.incrementAndGet();
		return bot;
	}
	

	/**
	 * Add a bot to the group.
	 * 
	 * @param bot
	 */
	public void addBot(Bot bot) {
		bots.put(bot.getId(), bot);
	}
	
	/**
	 * Add a list of bots to the group.
	 * 
	 * @param botList
	 */
	public void addBots(Collection<Bot> botList) {
		for (Bot bot : botList) {
			addBot(bot);
		}
	}
	
	public void start() {
		log.info("Starting BotGroup with "+getSize()+" bots.");
		for (Bot bot : bots.values()) {
			bot.connect();
			try {
				Thread.sleep(0, 500*1000);
			} catch (InterruptedException e) {}
		}
	}


	/**
	 * Logout and disconnect all bots.
	 *
	 */
	public void stop() {
		// Logout and disconnect all bots
		for (Bot bot : bots.values()) {
			bot.logout();
		}
		bots = null;
	}
	
	
	public int getSize() {
		return bots.size();
	}
	
	public Lobby getLobby() {
		return lobby;
	}

	
	/**
	 * @return the stats
	 */
	public GroupStats getStats() {
		return stats;
	}


	public BotConfig getConfig() {
		return config;
	}


	public void setConfig(BotConfig config) {
		this.config = config;
	}


    public String getGroupid() {
        return groupid;
    }


    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }


    public void handleTableSnapshot(TableSnapshotPacket packet) {
    	Table table = new Table(packet.tableid, packet.name, packet.capacity, packet.address);
    	table.setSeated(packet.seated);
    	for (Param p : packet.params) {
    		table.getAttributes().put(p.key, p);
    		if (p.key.equalsIgnoreCase("CURRENCY_CODE")) {
    			Parameter<String> stringParam = ParameterUtil.convertAsString(p);
    			table.setCurrency(stringParam.getValue());
    		}
    	}
    	lobby.updateTable(table);
    }
    
    public void handleTableRemoved(TableRemovedPacket packet) {
    	lobby.removeTable(packet.tableid);
    }
    
    public TargetTable getRandomTargetTable(int playerId, int leaveEmptySeats) {
        return lobby.getRandomTargetTable(playerId, leaveEmptySeats, new TableSelector() {
			
			@Override
			public boolean isTableElegible(Table table) {
				return groupConfig.isTableJoinable(table);
			}
		});
    }

	public void handleTableUpdated(final TableUpdatePacket packet) {
		lobby.updateTable(packet.tableid, new TableMutator() {
			
			@Override
			public void mutate(Table table) {
				removeParameters(table, packet.removedParams);
				updateParameters(table, packet.params);
			}

			private void updateParameters(Table table, List<Param> params) {
				for (Param p : params) {
					table.getAttributes().put(p.key, p);
				}
			}

			private void removeParameters(Table table, String[] removedParams) {
				for (String key : removedParams) {
					table.getAttributes().remove(key);
				}
			}
		});
	}

	public void handleTournamentSnapshot(TournamentSnapshotPacket packet) {
        Tournament tournament = new Tournament(packet.mttid);
        tournament.setAddress(packet.address);
        tournament.updateFromParameters(packet.params);
        lobby.updateTournament(tournament);
    }


    public void handleTournamentUpdated(TournamentUpdatePacket packet) {
        Tournament tournament = lobby.getTournament(packet.mttid);
        tournament.updateFromParameters(packet.params);
        lobby.updateTournament(tournament);
    }


    public void handleTournamentRemoved(TournamentRemovedPacket packet) {
        lobby.removeTournament(packet.mttid);
    }
    
    private void initJmx() {
        try {
            log.info("Binding BotGroup '" + groupid + "' to JMX");
            MBeanServer mbs = getMBeanServer();
            String id = groupid;
            if (id == null) {
            	id = ""+System.currentTimeMillis();
            }
            
            ObjectName monitorName = new ObjectName("com.cubeia.bot:type=BotGroup,id="+groupid);
            mbs.registerMBean(this, monitorName);
        } catch(Exception e) {
            log.error("failed to start JMX for: BotGroup", e);
        }
    }

    private MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }

	@Override
	public int countAllBots() {
		return bots.size();
	}

	@Override
	public int countStartedBots() {
		return stats.started.get();
	}

	@Override
	public int countConnectedBots() {
		return stats.connected.get();
	}

	@Override
	public int countSeatedBots() {
		return stats.seated.get();
	}

	@Override
	public int countBotsDeniedSeat() {
		return stats.deniedSeating.get();
	}

	@Override
	public boolean isTableSeatable(String name) {
		Table table = lobby.getTable(name);
		return (table != null && lobby.isTableSeatable(name) && groupConfig.isTableJoinable(table));
	}
}
