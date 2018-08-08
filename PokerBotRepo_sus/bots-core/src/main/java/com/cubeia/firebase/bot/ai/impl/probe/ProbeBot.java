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

package com.cubeia.firebase.bot.ai.impl.probe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.ai.BasicAI;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.ProbePacket;
import com.cubeia.firebase.io.protocol.ProbeStamp;
import com.cubeia.firebase.io.protocol.ServiceTransportPacket;


public class ProbeBot extends BasicAI {

    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(ProbeBot.class);
    
    static ProbeStats probeStats = ProbeStats.getInstance();
    
    /** Milliseconds between each probe packet, default = 5 seconds */
    private long sendPeriod = 5000;

	private ScheduledFuture<?> task; 
    
    
	public ProbeBot(Bot bot) {
        super(bot);
    }
	
	public void stop() {
		task.cancel(false);
		stopKeepAlive();
	}
    
    /**
     * The bot was seated at a table
     * I.e. logged in to an arena.
     *
     */
    @Override
    protected void handleSeated() {
        logInfo("TestGameBot Joined table: "+table.getId());

        Map<String,Integer> props = bot.getGroup().getConfig().getBatchProperties();
        Integer probeInterval = props.get("probeInterval");
        
        if ( probeInterval != null ) {
        	sendPeriod = probeInterval.intValue();
        }
        
       	Logic logic = new Logic();
       	task = executor.scheduleAtFixedRate(logic, 0, sendPeriod, TimeUnit.MILLISECONDS);
        	
    }
    
    
    
    
    
    public class Logic implements Runnable {
        
        public void run() {	 
            ProbePacket probePacket = new ProbePacket();
            // probePacket.tableid = bot.getGroup().getLobby().getRandomTableId();
            probePacket.tableid = table.getId();
            ProbeStamp stamp = new ProbeStamp();
        	stamp.clazz = getClass().toString();
        	stamp.timestamp = System.currentTimeMillis();
            probePacket.stamps = new ArrayList<ProbeStamp>();
            probePacket.stamps.add(stamp);
            logInfo("Sending probe: "+probePacket);
            bot.sendPacket(probePacket);
        }
        
    }
    
   
	public void handleProbePacket(ProbePacket packet) {
	    try {
    		ProbeStamp stampThis = new ProbeStamp();
        	stampThis.clazz = getClass().toString();
        	stampThis.timestamp = System.currentTimeMillis();
        	
        	packet.stamps.add(stampThis);
            bot.logInfo("[Table-"+packet.tableid+"] Probe recieved: " + packet);
            List<ProbeStamp> stamps = packet.stamps;
            long base = -1;
            for (ProbeStamp stamp : stamps) {
    			if(base == -1) base = stamp.timestamp;
    			long curr = stamp.timestamp;
    			String name = stamp.clazz.substring(stamp.clazz.lastIndexOf('.') + 1);
    			bot.logInfo(" - " + name + " - " + curr + " - " + (curr - base));
    			
    			 ProbeStats.getInstance().putValue(name, (int) (curr-base));
    		}
            
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	public void handleGamePacket(GameTransportPacket arg0) {}

	public void handleServiceTransportPacket(ServiceTransportPacket packet) {}


}
