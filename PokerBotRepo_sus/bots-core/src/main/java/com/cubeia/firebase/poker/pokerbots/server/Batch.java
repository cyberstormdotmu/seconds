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

/*
 * Created on 2006-jan-31
 *
 * $RCSFile: $
 * $Revision: #7 $
 * $Author: johlag $
 * $Date: 2006/08/30 $
 */
package com.cubeia.firebase.poker.pokerbots.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Value object for a batch of Bots.
 * A batch is defined as a number of bots sharing the same characteristics.
 *
 * @author Fredrik
 */
public class Batch implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -759454020470179304L;

    /**
     * Date and time when request was done 
     */    
    private String startTime = null;
    
    /**
     * IP address of the client or the last proxy that sent the request
     */
    private String requestersAddress = null;

    /**
     * An internal reference id
     */
    private String id;

    /**
     * Reference to the parent batch
     */
    private String parentid;

    /**
     * Starting id for the batch
     * I.e. testbot_<startingid>
     */
    private int startingid;

    /**
     * Game ID for the designated game.
     * Will be used for lobby.
     */
    private int gameId;
    
    /**
     * Assignee IP or ID.
     * Reference to the node/server/instance
     * that should handle this batch (if applicable)
     */
    private String assignee;

    /**
     * Url to connect to
     */
    private String url;

    /**
     * Port to connect to
     */
    private int port;

    private String connectorType;
    
    /**
     * Requested number of bots to be started
     */
    private int requested;

    /**
     * Actually started number of bots
     */
    private int started = 0;
    
    private String currency;
    
    private int loggedIn = 0;
    private int connected = 0;
    private int seated = 0;
    
    public static enum Status {
        OK,
        FAILED,
        RESUMED,
        TERMINATED
    }
    
    private Status status = Status.OK;

    /**
     * Holds all defined string properties.
     * Key is property name and Value is the value applied.
     *
     * I.e. botid, "I am a bot"
     */
    private Map<String,String> stringProperties = new HashMap<String,String>();

    /**
     * Holds all defined properties.
     * Key is property name and Value is the value applied.
     *
     * I.e. spamchat, 3000
     */
    private Map<String,Integer> properties = new HashMap<String,Integer>();

    /**
     * Holds all defined flags.
     * Key is flag name and Value is the flag value (true or false).
     *
     * I.e. modem, true
     */
    private Map<String,Boolean> flags = new HashMap<String,Boolean>();
    
    /**
     * Attributes that should match the lobby element's parameters.  
     */
    private Map<String,String> lobbyAttributes = new HashMap<String,String>();

    /**
     * The Batch can be divided into sub-batches.
     * The sum of all sub-batches should make up this Batch.
     *
     * The sub-batches could for instance denote the batch
     * spread over multiple servers.
     */
    private List<Batch> subBatches = new ArrayList<Batch>();

    /**
     * 
     */
    private int mode;
    
    private int mttInstanceId;
    
    // Should this batch be stopped
    private boolean toBeStopped = false;

    public Batch(String assignee, String id) {
        super();
        this.assignee = assignee;
        this.id = id;
        this.startTime = new Date().toString();
    }


    public Batch() {
        super();
        this.startTime = new Date().toString();
    }

    /**
     * Create a new batch object from another batch object.
     * Everything but the list of sub batches is cloned.
     * @param batch
     */
    public Batch(Batch batch){
        super();
        this.assignee = batch.assignee;
        this.id = batch.id;
        this.parentid = batch.parentid;
        this.url = batch.url;
        this.port = batch.port;
        this.requested = batch.requested;
        this.started = batch.started;
        this.startingid = batch.startingid;
        this.status = batch.status;
        this.properties = new HashMap<String,Integer>(batch.properties);
        this.flags = new HashMap<String,Boolean>(batch.flags);
        this.mode = batch.mode;
        this.startTime = batch.startTime;
        this.requestersAddress  = batch.startTime;
        this.toBeStopped = batch.isToBeStopped();
        this.connected = batch.connected;
        this.seated = batch.seated;
        this.loggedIn = batch.loggedIn;
        this.stringProperties = batch.stringProperties;
        this.gameId = batch.gameId;
    }


    @Override
    public String toString(){
        String text = "Batch - url:["+url+"] port:["+port+"] "+
                        "id:["+id+"] "+
                        "parent:["+parentid+"] "+
                        "assignee:["+assignee+"] "+
                        "gameId:["+gameId+"] "+
                        "requested:["+requested+"] started:["+started+"] " +
                        "subbatches:["+subBatches.size()+"] " +
                        "Status:["+status+"] " +
                        "To be stopped: [" + toBeStopped + "] " +
                        "StartTime: [" + startTime + "] "+
                        "Currency: [" + currency + "] ";
                        
        for( String key : properties.keySet() ){
            text += key+":["+properties.get(key)+"] ";
        }
        for( String key : flags.keySet() ){
            text += key+":["+flags.get(key)+"] ";
        }
        for ( String key : stringProperties.keySet() ) {
        	text += key+":["+stringProperties.get(key)+"] ";
        }
        text += "Lobby Attributes[";
        for ( String key : lobbyAttributes.keySet() ) {
        	text += key+":["+lobbyAttributes.get(key)+"] ";
        }
        text += "]";
        return text;
    }




    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }




    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }




    /**
     * @return Returns the subBatches.
     */
    public List<Batch> getSubBatches() {
        return subBatches;
    }




    /**
     * @param subBatches The subBatches to set.
     */
    public void setSubBatches(List<Batch> subBatches) {
        this.subBatches = subBatches;
    }

    /**
     * 
     * @return Returns the string properties
     */
    public Map<String,String> getStringProperties() {
    	return stringProperties;
    }

    public Map<String, String> getLobbyAttributes() {
		return lobbyAttributes;
	}

    /**
     * @return Returns the properties.
     */
    public Map<String, Integer> getProperties() {
        return properties;
    }


    /**
     * @param properties The properties to set.
     */
    public void setProperties(Map<String, Integer> properties) {
        this.properties = properties;
    }


    /**
     * @return Returns the requested.
     */
    public int getRequested() {
        return requested;
    }


    /**
     * @param requested The requested to set.
     */
    public void setRequested(int requested) {
        this.requested = requested;
    }


    public int getLoggedIn() {
		return loggedIn;
	}


	public void setLoggedIn(int loggedIn) {
		this.loggedIn = loggedIn;
	}


	/**
     * @return Returns the started.
     */
    public int getStarted() {
        return started;
    }


    /**
     * @param started The started to set.
     */
    public void setStarted(int started) {
        this.started = started;
    }




    public int getPort() {
        return port;
    }




    public void setPort(int port) {
        this.port = port;
    }




    public String getUrl() {
        return url;
    }




    public void setUrl(String url) {
        this.url = url;
    }




    public int getStartingid() {
        return startingid;
    }




    public int getConnected() {
		return connected;
	}


	public void setConnected(int connected) {
		this.connected = connected;
	}


	public int getSeated() {
		return seated;
	}


	public void setSeated(int seated) {
		this.seated = seated;
	}


	public void setStartingid(int startingid) {
        this.startingid = startingid;
    }


    public String getAssignee() {
        return assignee;
    }



    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }


    public Map<String, Boolean> getFlags() {
        return flags;
    }


    public void setFlags(Map<String, Boolean> flags) {
        this.flags = flags;
    }


    public String getParentid() {
        return parentid;
    }


    public void setParentid(String parentid) {
        this.parentid = parentid;
    }


    public int getMode() {
        return mode;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMttInstanceId(){
    	return mttInstanceId;
    }

    public void setMttInstanceId(int mttInstanceId){
    	this.mttInstanceId = mttInstanceId;
    	
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }


    public String getStartTime() {
        // TODO Auto-generated method stub
        return startTime;
    }


    public String getRequestersAddress() {
        return requestersAddress;
    }


    public void setRequestersAddress(String requestersAddress) {
        this.requestersAddress = requestersAddress;
    }


    public boolean isToBeStopped() {
        return toBeStopped;
    }


    public void setToBeStopped(boolean toBeStopped) {
        this.toBeStopped = toBeStopped;
    }


	public void setStringProperties(Map<String, String> stringProperties) {
		this.stringProperties = stringProperties;
	}


	public int getGameId() {
		return gameId;
	}


	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getConnectorType() {
		return connectorType;
	}
	
	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}

