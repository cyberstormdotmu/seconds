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

package com.cubeia.firebase.poker.pokerbots.server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.cache.CacheHandler;
import com.cubeia.firebase.poker.pokerbots.server.distribution.CacheCalculator;
import com.cubeia.firebase.poker.pokerbots.server.distribution.Calculator;



/**
 * The Cache controller takes incoming requests and places them on
 * a distributed cache (JBoss TreeCache).
 * 
 * The cache controller also listens to events on the cache. 
 * If an event that concerns this node, then event will generate
 * a request that will be routed to the attached downhandler.
 * 
 * This allows for distributed asynchronous calls.
 * 
 */
public class CacheController implements Controller, TreeCacheListener{
    
    private static Logger log = Logger.getLogger(CacheController.class);
    
    private Controller downHandler;
    
    private TreeCache cache;
    
    private Calculator calculator;
    
    /** Caches the ID of started batches */
    private Set<String> started = new HashSet<String>();
    private Set<String> stopped = new HashSet<String>();
    
    private Set<IpAddress> oldMembers = new HashSet<IpAddress>();

    /**
     * Empty constructor.
     * Gets the Cache and stores a reference.
     *
     */
    public CacheController(){
        cache = CacheHandler.getInstance().getCache();
        CacheHandler.getInstance().registerListener(this);
        
        // Initialize the calculator
        calculator = new CacheCalculator(cache);
        
    }
    
    
    
    /**
     * When starting up we need to inspect the cache to see 
     * if we have work assigned (this can happen if this node
     * is recovering from a crash).
     *
     */
    private void inspectCache() {
        log.info("Inspecting Cache for assigned work");
        List<Batch> result = new ArrayList<Batch>();
        try{
            // Get batch-root node
            Node root = cache.get(CacheHandler.CACHE_BATCH_ROOT);
            if (root != null) {
                // Get all first level batches
                result.addAll(CacheHandler.getInstance().getBatches(root));
            }

        }catch (Exception ex) {
            log.error("Error when inspecting cache: "+ex,ex);
        }

        // Iterate all batches and subbatches to see if we have local matches
        for (Batch topBatch : result) {
            List<Batch> subBatches = topBatch.getSubBatches();
            
            // Iterate Sub-Batches
            for (Batch batch : subBatches) {
                // Check the sub-batch if we are assigned for it
                if (CacheHandler.getInstance().isLocallyAssigned(batch)) {
                    // This is our batch!
                    log.debug("Locally assigned Batch found (will start this) : "+batch);
                    // Reset batch
                    batch.setStarted(0);
                    batch.setStatus(Batch.Status.RESUMED);
                    startLocalBatch(batch);
                }
            }
        }
    }
    
    
    
    /**
     * The CacheController will add the batch to the cache together with children batches
     * for distribution.
     * 
     *  @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#startBatch(Batch)
     */
    public void startBatch(Batch batch) {
        log.info("Start Batch called for : "+batch);
        
        try {
            // Get distribution batches
            List<Batch> subBatches = calculator.distribute(batch);

            // Add the parent batch to the treecache
            cache.put(CacheHandler.CACHE_BATCH_ROOT+batch.getId(), batch.getId(), batch);
            
            // Add all sub-batches as children 
            if (validDownHandler()){
                for (Batch sub : subBatches) {
                    log.debug("Add Sub-Batch: "+sub);
                    cache.put(CacheHandler.CACHE_BATCH_ROOT+batch.getId()+"/"+sub.getId(), sub.getId(), sub);
                }
            }
            
        } catch (CacheException e){
            log.error("Could not add batch to cache: "+e,e);
        }
 
    }

    /**
     * Starts a local batch (route to downhandler/controller).
     * Synchronized to avoid race conditions for starting batches.
     */
    private void startLocalBatch(Batch batch) {
        synchronized (started) {
            if (validDownHandler() && !started.contains(batch.getId())) {
                // Route batch to downhandler
                downHandler.startBatch(batch);
                started.add(batch.getId());
            }
        }
    }
    
    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#attachController(com.cubeia.firebase.poker.pokerbots.server.controller.Controller)
     */
    public void stopBatch(String batchId) {
        if (validDownHandler()){
            try {
                Batch batch = (Batch) cache.get(CacheHandler.CACHE_BATCH_ROOT+batchId, batchId);
                batch.setToBeStopped(true);
                cache.put(CacheHandler.CACHE_BATCH_ROOT+batchId, batchId, batch);
            } catch (CacheException e) {
                log.error("stopBatch: "+e,e);
            }
        }
    }
    
    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#attachController(com.cubeia.firebase.poker.pokerbots.server.controller.Controller)
     */
    @SuppressWarnings("unchecked")
    public void stopAllBatches() {
        // Route straight to the down handler for now
        if (validDownHandler()){
            try {
                Node node = CacheHandler.getInstance().getCache().get(CacheHandler.CACHE_BATCH_ROOT);
                Map<Object,Node> children = node.getChildren();
                if (children != null) {
                    for (Node child : children.values()) {
                        Batch batch = new Batch((Batch)child.get(child.getName()));
                        batch.setToBeStopped(true);
                        cache.put(CacheHandler.CACHE_BATCH_ROOT+batch.getId(), batch.getId(), batch);
                    }
                }
            } catch (CacheException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Attach a downhandler.
     * Inspect the cache to see if we need to take actions.
     */
    public void attachController(Controller downHandler) {
        this.downHandler = downHandler;

        // Inspect the cache
        inspectCache();
    }
    
    
    /**
     * If we are missing an attached downhandler then we will 
     * log an error and return false.
     *
     */
    private boolean validDownHandler(){
        if (downHandler == null){
            log.error("Missing downhandler, call terminated!");
            return false;
        }else{
            return true;
        }
    }
    
    
    
    /**
     * A batch that is associated with this node was updated.
     * 
     * @param batch
     */
    private void handleModified(Batch batch) {
        log.trace("Handle modified batch handled by this node: "+batch);
    }
    
    
    /**
     * Handle a node crash if we are the coordinator.
     * @param node
     */
    private void handleNodeCrash(IpAddress node) {
       //if (cache.isCoordinator()) {
           List<Batch> batches = CacheHandler.getInstance().getSubbatchesForAssignee(node.getIpAddress().toString());
           log.warn("Will fail batches: "+batches.size());
           for (Batch batch : batches) {
               log.warn("Setting batch as failed: "+batch);
               batch.setStarted(0);
               batch.setStatus(Batch.Status.FAILED);
               CacheHandler.getInstance().updateBatch(batch);
           }
       //}
    }
    
    
    //--------------------------
    //
    // Cache Listener Methods
    //
    //--------------------------
    
    /**
     * A node was created in the cache.
     * We really don't care about it since the Node 
     * will not contain a Batch item yet (null).
     */
    public void nodeCreated(Fqn arg0) {
        log.debug("Node Created: "+arg0);
    }

    public void nodeRemoved(Fqn arg0) {
        // TODO Auto-generated method stub
        log.debug("Node Removed: "+arg0);
    }

    public void nodeLoaded(Fqn arg0) {
        // TODO Auto-generated method stub
        log.debug("Node Loaded: "+arg0);
    }

    public void nodeEvicted(Fqn arg0) {
        // TODO Auto-generated method stub
        log.debug("Node Evicted: "+arg0);
    }

    /**
     * A node was modified.
     * We need to find out if we should take actions.
     */
    public void nodeModified(Fqn arg0) {
        try{
            Node node = cache.get(arg0);
            
            if (node.get(node.getName()) instanceof Batch) {
                // Get the created Batch
                Batch batch = (Batch)node.get(node.getName());
                // Check if we should take action on the modified batch
                
                if (batch.isToBeStopped()) {
                    List <Batch> batches  = CacheHandler.getInstance().getBatches(node);
                    for (int i = 0; i< batches.size();i++){
                        if (CacheHandler.getInstance().isLocallyAssigned(batches.get(i))) {
                            stopLocalBatch(batches.get(i));
                        }
                    }                                       
                }
                if (CacheHandler.getInstance().isLocallyAssigned(batch)) {
                    if (!started.contains(batch.getId())) {
                        startLocalBatch(batch);
                        
                    }else{                        
                        handleModified(batch);
                    }
                }
            }
            
        } catch (CacheException e){
            log.error("Could not handle modified node: "+e, e);
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    private void stopLocalBatch(Batch batch) {
        synchronized (stopped) {
            if (validDownHandler() && !stopped.contains(batch.getId())) {
                // Route batch to downhandler
                downHandler.stopBatch(batch.getId());
                stopped.add(batch.getId());
            }
        }        
    }



    public void nodeVisited(Fqn arg0) {
        // TODO Auto-generated method stub
        log.debug("Node Visited: "+arg0);
    }

    public void cacheStarted(TreeCache arg0) {
        // TODO Auto-generated method stub
        log.debug("Cache Started: "+arg0);
    }

    public void cacheStopped(TreeCache arg0) {
        // TODO Auto-generated method stub
        log.debug("Cache Stopped: "+arg0);
    }

    
    /**
     * A server node has either joined or left the 
     * cluster. Find out which and take actions accordingly.
     * 
     * @see org.jboss.cache.TreeCacheListener#viewChange(org.jgroups.View)
     */
    @SuppressWarnings("unchecked")
    public void viewChange(View view) {
        log.info("View Change: "+view);
        // Check if any nodes has left us first
        for (IpAddress node : oldMembers) {
            if (!view.containsMember(node)) {
                // We have lost a member!
                log.warn("A node has left the cluster: "+node);
                handleNodeCrash(node);
            }
        }
        
        // Finally set the old members view to the new view
        oldMembers.clear();
        oldMembers.addAll(view.getMembers());
    }
}
