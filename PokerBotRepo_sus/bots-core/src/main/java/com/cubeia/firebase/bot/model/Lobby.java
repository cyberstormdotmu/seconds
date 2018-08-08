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

package com.cubeia.firebase.bot.model;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.TableMutator;

/**
 * The Lobby is implemented as a Singleton since that allows for lobby information to be
 * shared across all bot groups within the same VM.
 * 
 * The Lobby holds two maps of table information. One is the lobby information as pushed by the 
 * server and the other is a more detailed data model of the known tables and their seat-information.
 * 
 * The underlying reason for this is concurrent access to non-full tables. We are using the lobby-information
 * based map for a fast lookup for non-full tables. This can be done concurrently.
 * 
 * @author Fredrik
 *
 */
public class Lobby implements LobbyMBean {

    private transient Logger log = Logger.getLogger(this.getClass());
    
    private static final String MTT_TABLE_IDENTIFIER = "/mtt/";

    private static Lobby instance = new Lobby(); 

    // private ConcurrentMap<Integer, TableSnapshot> lobbyData = new ConcurrentHashMap<Integer, TableSnapshot>();

    /** The lobby for this group */
    private ConcurrentMap<Integer,Table> tables = new ConcurrentHashMap<Integer,Table>();

    private ConcurrentMap<Integer,Tournament> tournaments = new ConcurrentHashMap<Integer,Tournament>();

    private Set<Integer> mttTables = new HashSet<Integer>();
    
    /** Will check 1 -> (excluding) MAX LOBBY ID */
    private static final int MAX_LOBBY_ID = 50;

    /** 
     * Holds a list of the last n tables that returned denied join requests.
     * TODO: Maybe change this to a timeout map instead?
     */
    private LinkedBlockingQueue<Integer> blacklist = new LinkedBlockingQueue<Integer>(6);

    /**
     * Hiding the constructor
     */
    protected Lobby() {
        initJmx();
    }

    /**
     * Get the singelton instance
     *
     */
    public static Lobby getInstance() {
        return instance;
    }


    /**
     * Add MBean info to JMX.
     * Will be called from the constructor.
     *
     */
    private void initJmx() {
        try {
            log.info("Binding 'Lobby' to JMX");
            MBeanServer mbs = getMBeanServer();
            ObjectName monitorName = new ObjectName("com.cubeia.bot:type=Lobby");
            mbs.registerMBean(this, monitorName);
        } catch(Exception e) {
            log.error("failed to start JMX for: Lobby", e);
        }
    }

    private MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * Add table to the lobby data model
     * 
     * @param table
     */
    public void addTable(Table table) {
        tables.put(table.getId(), table);
    }

    public void removeTable(int id) {
        tables.remove(id);
    }

    /**
     * Update or Add table.
     * 
     * 
     * @param table
     */
    public void updateTable(Table table) {
        if (isMttTable(table)) {
            /* 
             * Add the table to the set of mtt tables, so that we can ignore it if
             * this table is updated. This is because the TableUpdatePacket does not
             * contain the address.
             */
            mttTables.add(table.getId());
            return;
        }
        
        Table table2 = tables.get(table.getId());
        if (table2 == null) {
            synchronized (tables) {
                // double check for race condition
                if (tables.get(table.getId()) == null) {
                    addTable(table);
                }
            }
        } else {
            // Update runtime attributes
            table2.setSeated(table.getSeated());
        }
    }
    
    public void updateTable(int tableid, TableMutator tableMutator) {
    	synchronized (tables) {
    		Table table = tables.get(tableid);
    		if(table != null) {
    			tableMutator.mutate(table);
    		}
    	}
	}

    public Table getTable(int id) {
        return tables.get(id);
    }
    
    public Table getTable(String name) {
    	synchronized (tables) {
            for (Table table : tables.values()) {
            	if(table.getName().equalsIgnoreCase(name)) {
            		return table;
            	}
            }
    	}
    	return null;
	}

    public ConcurrentMap<Integer,Table> getTables() {
        return tables;
    }

    public void setTables(ConcurrentMap<Integer,Table> tables) {
        this.tables = tables;
    }


    public Tournament getTournament(int id) {
        return tournaments.get(id);
    }

    public void updateTournament(Tournament tournament) {
        tournaments.put(tournament.getId(), tournament);
    }

    public Map<Integer, Tournament> getTournaments() {
        return tournaments;
    }

    public void removeTournament(int id) {
        tournaments.remove(id);
    }

    /**
     * A player was seated so log the seat as taken.
     * This can be called before the lobby has been received if the
     * player was reseated after a reconnect to the server.
     * 
     * @param tableId
     * @param playerId
     * @param seat
     */
    public void playerSeated(int tableId, int playerId) {
        if (!tables.containsKey(tableId)) {
            tables.put(tableId, new Table(tableId, "n/a", -1, null));
        }

        Table table = tables.get(tableId);
        // One at a time boys
        synchronized (table) {
            table.addLocalPlayer(new Player(playerId));
        }
    }

    /**
     * A player left the table
     * 
     * @param tableId
     * @param playerId
     * @param seat
     */
    public void playerLeft(int tableId, int playerId) {
        Table table = tables.get(tableId);
        if (table != null) {
            // One at a time boys
            synchronized (table) {
                table.removeLocalPlayer(playerId);
            }
        }
    }

    /**
     * Test fixture factory method.
     * 
     * @return
     */
    public static Lobby createPresetLobby() {
        Lobby lobby = getInstance();
        for (int i = 1; i < MAX_LOBBY_ID; i++) {
            Table table = new Table(i, "test_"+i, 10, null);
            lobby.addTable(table);
        }
        return lobby;
    }

    /**
     * Get a random table id
     * 
     * @return table id
     */
    public int getRandomTableId() {
        int index = new Random().nextInt(tables.size());
        Table table = tables.get(index);
        if ( table != null ) {
            return table.getId();
        }
        return -1;
    }

    /**
     * Get a random target for seating from the lobby.
     * A Target contains a table and a seat.
     * 
     * The lobby will reserve the returned seat (i.e. set the player to the seat).
     * 
     * @param playerId the playerId who is looking for a table
     * @param leaveEmptySeats defines how many empty seats to leave. Should be >= 0.
     * @return table
     */
    public TargetTable getRandomTargetTable(int playerId, int leaveEmptySeats, TableSelector selector) {
        // We must process this one-at-a-time.
        // If this proves to be a bottleneck we need a rewrite here
        synchronized (tables) {
            for (Table table : tables.values()) {
                if (!isBlacklisted(table.getId()) && table.isJoinable(leaveEmptySeats) && selector.isTableElegible(table)) {
                    // log.debug("Seating player["+playerId+"] at table: "+table);
                    playerSeated(table.getId(), playerId);
                    TargetTable target = new TargetTable(table, -1);
                    return target;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean isTableSeatable(String name) {
    	synchronized (tables) {
            for (Table table : tables.values()) {
            	if(table.getName().equalsIgnoreCase(name)) {
            		return !isBlacklisted(table.getId()) 
            				&& table.isJoinable(0);
            	}
            }
        }
    	return false;
    }


    /**
     * Check if a tournament has available seats to join
     * 
     * @param mttID
     * @param leaveEmptySeats 
     * @return true if seats available
     */
    public boolean isTournamentSeatable(int mttID, int leaveEmptySeats) {
        Tournament t = this.getTournament(mttID);
        int emptySeats = t.getCapacity() - t.getRegistered();
        log.info("seats: " + emptySeats + " CAP: " + t.getCapacity() + " REG: " + t.getRegistered() );
        
        return (emptySeats > leaveEmptySeats);
    }    
    
    
    /**
     * Will return a preferred tournament that you register to (we will try to group
     * all request on the same tournament(s)).
     * 
     * @param bot
     * @return id or -1 if no match found.
     */
    public int getTargetTournament(Bot bot) {
        int id = -1;
        synchronized (tournaments) {
            for (Tournament t : tournaments.values()) {
                // if bot group cannot fill tournament, continue
                if (bot.getGroup().getSize() < t.getCapacity()) {
                    continue;
                }
                if ( isTournamentSeatable(t.getId(), bot.getLeaveEmptySeats())) {
                    t.incrementRegisteredPlayers();
                    return t.getId();
                }
            }
        }
        return id;
    }


    public void dumpLobbyToLog(String owner) {
        String lobby = "Bot Lobby for owner: "+owner+"\n";
        lobby += "\n--- Tables ------------ \n";
        for (Table table : tables.values()) {
            lobby += table + "\n";
        }
        lobby += "\n--- Tournaments ------------ \n";
        for (Tournament mtt : tournaments.values()) {
            lobby += mtt + "\n";
        }
        log.fatal(lobby);
    }

    public int getTableCount() {
        return tables.size();
    }

    public String[] getTableNames() {
        Collection<Table> tableList = tables.values();
        String[] names = new String[tableList.size()];
        int index = 0;
        for (Table table : tableList) {
            names[index] = table.getName();
            index++;
        }
        return names;
    }

    public void printToLog() {
        dumpLobbyToLog("JMX");
    }

    /**
     * Ignore this table for a while.
     * 
     * @param tableid
     */
    public void blacklistTable(int tableid) {
        synchronized (blacklist) {
            if (blacklist.remainingCapacity() == 0) {
                blacklist.poll();
            }
            try {
                blacklist.put(tableid);
            } catch (InterruptedException e) {
            }
        }
    }

    private boolean isMttTable(Table table) {
        return (table.getAddress() != null && table.getAddress().contains(MTT_TABLE_IDENTIFIER)) || mttTables.contains(table.getId());
    }
    
    private boolean isBlacklisted(int tableid) {
        return blacklist.contains(tableid);
    }

    public Integer[] getBlacklist() {
        return blacklist.toArray(new Integer[blacklist.size()]);
    }
}
