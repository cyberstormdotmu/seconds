/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
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
package com.cubeia.firebase.api.game.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.action.JoinRequestAction;
import com.cubeia.firebase.api.action.LeaveAction;
import com.cubeia.firebase.api.action.LeaveResponseAction;
import com.cubeia.firebase.api.action.RemovePlayerAction;
import com.cubeia.firebase.api.action.ReserveSeatRequestAction;
import com.cubeia.firebase.api.action.SeatPlayersMttAction;
import com.cubeia.firebase.api.action.Status;
import com.cubeia.firebase.api.action.WaitCheckPacketAction;
import com.cubeia.firebase.api.action.WaitCheckResponsePacketAction;
import com.cubeia.firebase.api.action.WaitingRequestAction;
import com.cubeia.firebase.api.action.WaitingResponseAction;
import com.cubeia.firebase.api.game.lobby.DefaultLobbyMutator;
import com.cubeia.firebase.api.game.lobby.LobbyTableAccessor;
import com.cubeia.firebase.api.game.player.GenericPlayer;
import com.cubeia.firebase.api.game.player.PlayerStatus;
import com.cubeia.firebase.api.game.table.InterceptionResponse;
import com.cubeia.firebase.api.game.table.Table;
import com.cubeia.firebase.api.game.table.TableListener;
import com.cubeia.firebase.api.game.table.TableWaiting;
import com.cubeia.firebase.io.protocol.WaitingList;

/**
 * Table action handler used for standard tables, i.e. cashgame tables.
 * 
 * This handler will ignore mtt seating requests.
 *
 * @author Fredrik
 */
public class StandardTableActionHandler extends AbstractTableActionHandler {

	private static transient Logger log = Logger.getLogger(StandardTableActionHandler.class);
	
	private static List<TableWaiting> waitingListLobby = new ArrayList<TableWaiting>();
	
	/**
	 * Tatvasoft - Global waiting list to maintain waiting list for all tables
	 */
	private static Map<Integer, ArrayList<TableWaiting>> globalWaitingList = new HashMap<Integer, ArrayList<TableWaiting>>();
	
		
	/*------------------------------------------------
		 
		CONSTRUCTOR(S)
	
	 ------------------------------------------------*/
	
	
	public StandardTableActionHandler(Table table, LobbyTableAccessor acc, DefaultLobbyMutator mut) {
		super(table, acc, mut);		
	}

	/*------------------------------------------------
		 
		VISITOR METHODS
		
		Handles actions that are not covered in
		the abstract super class.
	
	 ------------------------------------------------*/
	@Override
	public void visit(WaitingRequestAction action) {
		System.out.println("Inside visit(WaitingRequestAction action) ");
		sendWaitingResponse(action.getPlayerId(),action.getTableId());

	}

	@Override
	public void visit(WaitCheckPacketAction action) {
		System.out.println("Inside visit(WaitcheckrequestAction action) ");
		//sendWaitingResponse(action.getPlayerId(),action.getTableId());
		List<TableWaiting> tableWaitingList = StandardTableActionHandler.globalWaitingList.get(action.getTableid());
		
		if(tableWaitingList != null && tableWaitingList.size()>0){
			int playerId = tableWaitingList.get(0).getPlayer().getPlayerId();
			WaitCheckResponsePacketAction response = new WaitCheckResponsePacketAction(playerId, action.getTableId());
		    getNotifier().sendToClient(playerId, response);
		}
	}

	
	/**
	 * Join a player at a regular table.
	 * 
     */
    @Override
    public void visit(JoinRequestAction action) 
    { 
    	String allowedBySeatingRules = seatingRules.actionAllowed(action, table);
   
    	boolean reservedFlag = action.isReservedFlag();
    	
    	
    	// If request is for Waiting
    	if(!reservedFlag && allowedBySeatingRules.equals("R"))
    	{
    		try 
    		{
    			//joinWaitingList(action, table, acc, mut);
    			
				if(joinWaitingList(action, table, acc, mut))
				{
					InterceptionResponse allowedByInterceptor = new InterceptionResponse(false, -1);
					allowedByInterceptor = checkJoinInterceptor(table, action.getSeatId(), action.getPlayerId(), action.getParameters());   
					try {
						// Put 5 seconds gap before making another request
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Send join response of Waiting
					 sendJoinResponse(action.getPlayerId(), action.getTableId(), action.getSeatId(), allowedBySeatingRules, allowedByInterceptor.getResponseCode());
					 
					if (mut != null && acc != null) {
						mut.updateTable(acc, table);
					}
				}
			} 
    		catch (Exception e) 
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		else
		{
			// default response is DENIED (2)
    		InterceptionResponse allowedByInterceptor = new InterceptionResponse(false, -1);
	
	    	if (allowedBySeatingRules.equals("J")) 
	    	{
	    		// Get the seatId, or find the id of the first empty seat if the seatId is -1.
	    		int seatId = action.getSeatId() == -1 ? getFirstVacantSeatId() : action.getSeatId();
	    		action.setSeatId(seatId);
	
	    		// If we have an interceptor, we should consult it
	    		allowedByInterceptor = checkJoinInterceptor(table, action.getSeatId(), action.getPlayerId(), action.getParameters());            
	    	}
	
	    	boolean allowed = allowedBySeatingRules.equals("J") && allowedByInterceptor.isAllowed();
	
	    	// Send the response before we tell the game that a player has joined.
	    	// sendJoinResponse(action.getPlayerId(), action.getTableId(), action.getSeatId(), allowed, allowedByInterceptor.getResponseCode());        
	
	    	// If there is a place vacant on table, add the player.
	    	if (allowed) 
	    	{
	    		TableWaiting waiting = null;
	    		
	    		// If waiting list contains player for this table
	    		if(globalWaitingList.get(action.getTableId()) != null && globalWaitingList.get(action.getTableId()).size() > 0)
	    		{
	    			ArrayList<TableWaiting> waitList = globalWaitingList.get(action.getTableId());
	    			
	    			// Get current timestamp
	    			//long currentTimeStamp = System.currentTimeMillis();
	    			
	    			//for(int i = 0; i < waitList.size(); i++)
	    			//{
	    				TableWaiting wait = waitList.get(0);
	    			
	    				// If there is not any join request from last 15 seconds, remove player from waiting list
	    				//if(currentTimeStamp - wait.getLastActionTimeStamp() > 15000)
	    				//{
	    				//	waitList.remove(i);
	    				//}
	    				//else
	    				//{
	    					waiting = wait;
	    					int waitingPlayerId = waiting.getPlayer().getPlayerId();
	        				
	        				if(action.getPlayerId() == waitingPlayerId)
	        				{
	        					waitList.remove(0);
	        				}
	    					//break;
	    				//}
	    				
	    			//}
	    			
	    			globalWaitingList.put(action.getTableId(), waitList);
	    			WaitingResponseAction responseAction = notifyWaitingResponse(action.getPlayerId(), action.getTableId());
	    			getNotifier().notifyAllPlayers(responseAction);
	    			
	    			if (mut != null && acc != null) {
						mut.updateTable(acc, table);
					}
	    			
	    			if(waitList == null || waitList.size() == 0)
    				{
    					globalWaitingList.remove(action.getTableId());
    					WaitingResponseAction waitingResponseAction = notifyWaitingResponse(action.getPlayerId(), action.getTableId());
    	    			getNotifier().notifyAllPlayers(waitingResponseAction);
    	    			
    	    			if (mut != null && acc != null) {
    						mut.updateTable(acc, table);
    					}
    				}
	    		}
	    		
	    		// If valid waiting player found
    			if(waiting != null)
    			{
    				int waitingPlayerId = waiting.getPlayer().getPlayerId();
    				
    				if(action.getPlayerId() == waitingPlayerId)
    				{
    					// Send joining response
    					sendJoinResponse(action.getPlayerId(), action.getTableId(), action.getSeatId(), allowedBySeatingRules, allowedByInterceptor.getResponseCode());

    					// Create player and seat him
    					GenericPlayer player = seatPlayer(action.getSeatId(), action.getPlayerId(), action.getNick(), null);

    					// Report to the client registry (See Trac ticket #143 for the reason why we do this here) 
    					registerPlayerToTable(table.getId(), action.getPlayerId(), action.getSeatId(), table.getMetaData().getMttId(), false);

    					getNotifier().notifyAllPlayersExceptOne(action, action.getPlayerId());

    					// Notify listener.
    					TableListener listener = table.getListener();
    					if (listener != null) {
    						listener.playerJoined(table, player);
    					}
    					
    				}
    				else
    				{
    					// Send waiting response
    					//sendJoinResponse(action.getPlayerId(), action.getTableId(), action.getSeatId(), "R", allowedByInterceptor.getResponseCode());
    				}
    			}
    			else
    			{
    				sendJoinResponse(action.getPlayerId(), action.getTableId(), action.getSeatId(), allowedBySeatingRules, allowedByInterceptor.getResponseCode());
    				
	    			// Create player and seat him
	    			GenericPlayer player = seatPlayer(action.getSeatId(), action.getPlayerId(), action.getNick(), null);
	
	    			// Report to the client registry (See Trac ticket #143 for the reason why we do this here) 
	    			registerPlayerToTable(table.getId(), action.getPlayerId(), action.getSeatId(), table.getMetaData().getMttId(), false);
	
	    			getNotifier().notifyAllPlayersExceptOne(action, action.getPlayerId());
	
	    			// Notify listener.
	    			TableListener listener = table.getListener();
	    			
	    			if (listener != null) 
	    			{
	    				listener.playerJoined(table, player);
	    			}
	
    			}
    			// Update lobby if requested.
				if (mut != null && acc != null) {
					mut.updateTable(acc, table);
				}
	
	    	}
	    }
    }
    

    /**
     * <p>Request for leaving a table.</p>
     * 
     * <p>If the request is denied by the interceptor then we will set the status of the
     * player to LEAVING.</p>
     * 
     * 
     * @see com.cubeia.firebase.api.action.visitor.DefaultActionVisitor#visit(com.cubeia.firebase.api.action.LeaveAction)
     */
    @Override
    public void visit(LeaveAction action) {
        InterceptionResponse allowed = new InterceptionResponse(true, -1);
        Status status = Status.FAILED;
        
        int pid = action.getPlayerId();
        
        // Check if player is seated at table before anything else (Ticket #526)
        if (table.getPlayerSet().getPlayer(pid) != null) {
        	if (seatingRules.actionAllowed(action, table)) {
                allowed = handleRemovePlayer(pid);
        	} 
        	status = allowed.isAllowed() ? Status.OK : Status.DENIED;
        	 
        } else {
            // We have received a leave action for a player that is not seated at the table.
            // Set the response status to denied.
            allowed = new InterceptionResponse(false, -1);
        }
    	
    	// Send the response
    	LeaveResponseAction response = new LeaveResponseAction(action.getPlayerId(), action.getTableId(), status.ordinal());
    	response.setResponseCode(allowed.getResponseCode());
        getNotifier().sendToClient(action.getPlayerId(), response);
    }


    /**
     * The system requested a player to be removed from the table.
     * 
     * We will not send a response here since the client is most likely
     * not connected anymore.
     * `
     */
    @Override
    public void visit(RemovePlayerAction action) {
        int pid = action.getPlayerId();
    	if (seatingRules.actionAllowed(action, table)) {
            handleRemovePlayer(pid);
    	} 
    }
    
    
    @Override
    public void visit(SeatPlayersMttAction action) {
    	log.error("Error: You are trying to seat tournament players at a normal table. Tableid: " + table.getId());
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void visit(ReserveSeatRequestAction action) {
    	boolean allowedBySeatingRules = seatingRules.actionAllowed(action, table);
        
    	// default response is DENIED (2)
        InterceptionResponse allowedByInterceptor = new InterceptionResponse(false, -1);
        
        if (allowedBySeatingRules) {
            // Get the seatId, or find the id of the first empty seat if the seatId is -1.
            int seatId = action.getSeatId() == -1 ? getFirstVacantSeatId() : action.getSeatId();
            action.setSeatId(seatId);
            
            // If we have an interceptor, we should consult it
            // Reserve seat will use the same interceptor call as for a join request
            allowedByInterceptor = checkReservationInterceptor(table, action.getSeatId(), action.getPlayerId(), Collections.EMPTY_LIST);            
        }
        
        boolean allowed = allowedBySeatingRules && allowedByInterceptor.isAllowed();
        
        // Send the response before we tell the game that a player has reserved a seat (if applicable).
        sendReserveSeatResponse(action, allowed, allowedByInterceptor.getResponseCode());
        
        
        // Add the player.
        if (allowed) {
            // Create player and seat him (no nick for reservations)
            // GenericPlayer player = reserveSeat(action.getSeatId(), action.getPlayerId());
            
            // Report to the client registry (See Trac ticket #143 for the reason why we do this here) 
            // registerPlayerToTable(table.getId(), action.getPlayerId(), action.getSeatId(), table.getMetaData().getMttId(), false);
            
            // scheduleStatusTimeout(player, PlayerStatus.RESERVATION);
            
            // Notify listener.
            /*TableListener listener = table.getListener();
            if (listener != null) {
            	listener.seatReserved(table, player);
            }*/
            
            // Update lobby if requested.
            if (mut != null && acc != null) {
            	mut.updateTable(acc, table);
            }
            
            GenericPlayer player = new GenericPlayer(action.getPlayerId(), "");
            player.setSeatId(action.getSeatId());
            
            TableWaiting waitList = new TableWaiting();
           // waitList.setGameId(table.getMetaData().getGameId());
            waitList.setMttId(table.getMetaData().getMttId());
            waitList.setPlayer(player);
            waitList.setTableId(table.getId());
            waitList.setTable(table);
            waitingListLobby.add(waitList);
            
            // Create message bus event
    		/*GameEvent event = new GameEvent();
    		event.setAction(reserve);
    		event.setPlayerId(request.getPlayerId());
    		event.setTableId(tableId);
    		
    		try {
    			sender.dispatch(event);
    		} catch (ChannelNotFoundException e) {
    			log.error("No channel defined for the request: "+request, e);
    		}*/
        }

    }


    
	/*------------------------------------------------
		 
		PRIVATE METHODS
		
	 ------------------------------------------------*/
    /**
     * Remove player and notify players if applicable by the table interceptor.
     * 
     * @param pid
     * @return the interceptor response
     */
	private InterceptionResponse handleRemovePlayer(int pid) {
		// If we have an interceptor, we should consult it
		InterceptionResponse allowed = checkLeaveInterceptor(table, pid);
		
		if (allowed.isAllowed()) {
			
		    // Trac #439: moved to top of block
		    // Notify listener
		    TableListener listener = table.getListener();
		    if (listener != null) {
		    	listener.playerLeft(table, pid);
		    }
			
			// Inform everyone.
			getNotifier().notifyAllPlayersExceptOne(new LeaveAction(pid, table.getId()), pid);  
			// Then remove player
			removePlayer(pid);
		    // Report to the client registry (See Trac ticket #143 for the reason why we do this here) 
			// Seat id does not matter
		    registerPlayerToTable(table.getId(), pid, -1, table.getMetaData().getMttId(), true);
			
		    /* Trac #439: moved to top of block
		     * Notify listener
		    TableListener listener = table.getListener();
		    if (listener != null) {
		    	listener.playerLeft(table, pid);
		    }*/
		    
		    // Update lobby if requested
		    if (mut != null && acc != null) {
		    	mut.updateTable(acc, table);
		    }
		    
		} else {
    		// Player was not allowed to leave so we need to set his status
    		// to Leaving and notify listeners.
    		GenericPlayer player = playerSet.getPlayer(pid);
    		if (player != null) {
    			player.setStatus(PlayerStatus.LEAVING);

                // Notify listener.
    			TableListener listener = table.getListener();
                if (listener != null) {
                	listener.playerStatusChanged(table, pid, player.getStatus());
                }
    		}
    	}
		
		return allowed;
	}
	
	/**
	 * Tatvasoft - Try to join waiting list
	 * 
	 * @param action the JoinRequestAction
	 * @param table the Table
	 * @param acc the LobbyTableAccessor
	 * @param mut the DefaultLobbyMutator
	 * @return true - if joinable
	 * 		   false - if not
	 * @throws Exception
	 */
	private boolean joinWaitingList(JoinRequestAction action,
								 Table table,
								 LobbyTableAccessor acc, 
								 DefaultLobbyMutator mut) throws Exception
	{
		boolean isJoinable = true;
		
		int tableId = action.getTableId();
		GenericPlayer player = new GenericPlayer(action.getPlayerId(),action.getNick());
        player.setSeatId(action.getSeatId());
        
        TableWaiting waitList = new TableWaiting();
        waitList.setMttId(table.getMetaData().getMttId());
        waitList.setPlayer(player);
        waitList.setTableId(tableId);
        waitList.setTable(table);
        waitList.setAcc(acc);
        waitList.setMut(mut);
        waitList.setLastActionTimeStamp(System.currentTimeMillis());
        
        ArrayList<TableWaiting> waitingList = globalWaitingList.get(tableId);
        
        // if Waiting list does not exist for current table, create new.
        if(waitingList == null || waitingList.size() == 0)
        {
        	waitingList = new ArrayList<TableWaiting>();
        	waitingList.add(waitList);
        	
        	globalWaitingList.put(tableId, waitingList);
        	
        	WaitingResponseAction responseAction = notifyWaitingResponse(action.getPlayerId(), action.getTableId());
    		getNotifier().notifyAllPlayers(responseAction);
        }
        else
        {
        	// Check if waiting player already exists in Waiting list
        	Map<String, Object> map = checkPlayerExists(waitingList, action.getPlayerId());
        	
        	// If not exists, add the player
        	if(map == null)
        	{
        		waitingList.add(waitList);
        	}
        	else
        	{
        		boolean isWaitingFlag = (Boolean)map.get("waitingFlag");
        		int index = (Integer)map.get("index");
        		
        		// If player still waiting, set the latest timestamp of request in waiting list
        		if(isWaitingFlag)
        		{
        			waitingList.set(index, waitList);
        		}
        		else
        		{
        			// If player is not waiting, remove it from waiting list
        			waitingList.remove(index);
        			isJoinable = false;
        		}
        	}
        	globalWaitingList.put(tableId, waitingList);
        	
        	WaitingResponseAction responseAction = notifyWaitingResponse(action.getPlayerId(), action.getTableId());
    		getNotifier().notifyAllPlayers(responseAction);
        }
        
        if (mut != null && acc != null) {
        	mut.updateTable(acc, table);
        }
        
        // Return if player joinable or not.
        return isJoinable;
	}
	
	// Gets Global Waiting List
	public static Map<Integer, ArrayList<TableWaiting>> getGlobalWaitingList() {
		return globalWaitingList;
	}

	// Sets Global Waiting List
	public static void setGlobalWaitingList(
			Map<Integer, ArrayList<TableWaiting>> globalWaitingList) {
		StandardTableActionHandler.globalWaitingList = globalWaitingList;
	}

	/**
	 * Check if Player exists in waiting list
	 * 
	 * @param list the waiting list
	 * @param playerId the player id
	 * 
	 * @return map of player if player found  
	 */
	private Map<String, Object> checkPlayerExists(List<TableWaiting> list, int playerId)
	{
		Map<String, Object> map = null;
		
		for(int i = 0; i < list.size(); i++)
		{
			TableWaiting tw = list.get(i);
			
			if(tw.getPlayer().getPlayerId() == playerId)
			{
				map = new HashMap<String, Object>();
				map.put("index", i);
				map.put("waitingFlag", tw.isWaitingFlag());
				return map;
			}
		}
		return map;
	}
}
