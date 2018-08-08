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

package com.cubeia.firebase.poker.pokerbots.server.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.BotConfig;
import com.cubeia.firebase.bot.BotGroup;
import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.translation.ConfigTranslator;
import com.cubeia.firebase.util.NamedThreadFactory;


/**
 * This is a Singleton that wraps the local bot server. 
 * 
 * This implementation is a bit more transparent towards the 
 * current implementation.
 * 
 * 
 */
public class LocalServer {
    
    // The instance
    private static LocalServer instance = new LocalServer();
    
    /** Holds the batches created on this node. */
    private Map<String,Batch> batches = new ConcurrentHashMap<String,Batch>();
    
    private Logger log = Logger.getLogger(this.getClass());
    
    /** Starting id (only used when local informer is polled for startid */
    private AtomicInteger startid = new AtomicInteger(1);
    
    private Map<String, BotGroup> botGroups = new ConcurrentHashMap<String, BotGroup>();
    
    /** 
     * Use a threadpool for starting local batches.
     * This is needed for gui-liveliness since slogin (for instance) blocks the 
     * calling thread when starting bots.
     */
    private Executor executor = Executors.newFixedThreadPool(2, new NamedThreadFactory("LocalServer-Start")); 
    
    /**
     * Singleton accessor.
     * 
     */
    public static LocalServer getInstance() {
        return instance;
    }
    
    public void setInitialStartId(int id) {
    	startid.set(id);
    }
    
    /**
     * Stop a batch
     * @param id
     */
    public void stopBatch(String batchId) {
        BotGroup group = botGroups.get(batchId);
        group.stop();
        botGroups.remove(batchId);
        batches.remove(batchId);
    }
    
    /**
     * 
     * Stop all batches
     */
    public void stopAllBatches() {
        for (BotGroup group : botGroups.values()) {
        	group.stop();
        }
        botGroups = new ConcurrentHashMap<String, BotGroup>();
        batches = new ConcurrentHashMap<String,Batch>();
    }
    
   
    /**
     * Add a batch for reference.
     * @param batch
     */
    public void startBatch(final Batch batch) {
        log.info("Start Local Batch: "+batch);
        // Store a reference with parent + local id
        batches.put(batch.getId(), batch);
        
        // Get a Bot Config
        System.out.println(batch.getId());
        final BotConfig config = ConfigTranslator.translate(batch);
        // Set number of bots
        config.setSize(batch.getRequested());
        
        // Create a job that will be handed off to an executor
        Runnable startJob = new Runnable() {
            public void run(){
                BotGroup group = new BotGroup(config);
                // Add reference
                botGroups.put(batch.getId(), group);
                group.start();
            }   
        };
        
        executor.execute(startJob);
        
    }
    
    /**
     * Get a map with all batches.
     * The information is updated first.
     * 
     * @return
     */
    public Map<String,Batch> getBatches() {
        updateAllBatches();
        return batches;
    }
    
    /**
     * Get a single batch.
     * @param id
     * @return
     */
    public Batch getBatch(String id) {
        return batches.get(id);
    }
    
    
    /**
     * Update all batches
     *
     */
    private void updateAllBatches() {
        for (String key : batches.keySet()) {
            updateBatchInformation(key);
        }
    }
    
    /**
     * Update all batches with runtime information.
     *
     */
    private void updateBatchInformation(String id){
        Batch batch = batches.get(id);
        
        // Reset counter(s)
        batch.setStarted(0);
        
        if (batch != null) {
            BotGroup group = botGroups.get(id);
            batch.setStarted(group.getStats().started.get());
            batch.setLoggedIn(group.getStats().loggedin.get());
            batch.setConnected(group.getStats().connected.get());
            batch.setSeated(group.getStats().seated.get());
        }
    }
    
    
    /**
     * Is the Bot-ID within the batch range?
     * 
     * @param id
     * @param batch
     * @return
     */
    public boolean isWithinRange(int id, Batch batch) {
        return id >= batch.getStartingid() && id < batch.getStartingid()+batch.getRequested();
    }

    public AtomicInteger getStartid() {
        return startid;
    }

    public void setStartid(AtomicInteger startid) {
        this.startid = startid;
    }
}
