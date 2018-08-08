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

package com.cubeia.firebase.poker.pokerbots.server.cache;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.cache.Node;
import org.jboss.cache.PropertyConfigurator;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jgroups.stack.IpAddress;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.local.LocalServer;
import com.cubeia.firebase.util.NamedThreadFactory;



/**
 * Handles a tree cache.
 * Singleton (one cache per JVM).
 *
 * This class provides the underpinnings for the CacheController.
 */
public class CacheHandler{

	private static Logger log = Logger.getLogger(CacheHandler.class);
	
    public final static String CACHE_BATCH_ROOT = "/batch/";
    public final static String CACHE_GLOBAL_ROOT = "/global";
    
    public final static String KEY_STARTID = "startid";
    
    /** The default (first) id to use for starting id */
    public final static int DEFAULT_START_ID = 1000;
    
    /** Initial delay before starting to update the cache */
    public int UPDATER_INITIAL_DELAY = 5;
    
    /** Period (seconds) between cache updates */
    public int UPDATER_PERIOD = 5;

    TreeCache cache;

    private static CacheHandler handler = new CacheHandler();


    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("CacheUpdater"));

    /**
     * Empty constructor
     *
     */
    private CacheHandler(){
        try{
            cache = createCache("BotServerData");
            // Bind to JMX
            bindCacheToJMX();
            // start the updater thread
            scheduler.scheduleWithFixedDelay(new Updater(), UPDATER_INITIAL_DELAY, UPDATER_PERIOD, TimeUnit.SECONDS);
        }catch(Exception e){
            log.fatal("Could not start cache service. "+e,e);
        }
    }


    /**
     * Gets the singleton instance
     * @return
     */
    public static CacheHandler getInstance(){
        return handler;
    }

    /**
     * Create and configure the TreeCache.
     *
     * @param name
     * @return
     * @throws Exception
     */
    public TreeCache createCache(String name) throws Exception {
        TreeCache tree=new TreeCache();
        PropertyConfigurator config=new PropertyConfigurator();
        config.configure(tree, "replAsync-service.xml"); // read in generic replAsync xml
        tree.setClusterName(name);
        tree.createService();
        tree.startService();
        return tree;
     }


    public TreeCache getCache(){
        return cache;
    }

    /**
     * Adds a listener to the cache.     * @param listener
     */
    public void registerListener(TreeCacheListener listener){
        cache.addTreeCacheListener(listener);
    }

    /**
     * Gets the list of members connected to the cache.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<IpAddress> getMembers() {
        return cache.getMembers();
    }

    public IpAddress getLocalAddress() {
        return (IpAddress)cache.getLocalAddress();
    }
    
    
    /**
     * Gets all batches contained under a node structure.
     * The returned Batch is immutable.
     *
     * @param node
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Batch> getBatches(Node node) {
        List<Batch> result = new ArrayList<Batch>();

        // Iterate all batches in node
        Map<Object,Node> children = node.getChildren();

        if (children != null && children.size() > 0) {

            // There are batches in the cache.
            for (Node child : children.values()) {
                // Get the top level batch as an immutable object
                Batch batch = new Batch(getBatchInNode(child));

                // Recurse into node children
                Map<Object,Node> subChildren = child.getChildren();

                // Iterate sub nodes                
                if (subChildren != null && subChildren.size() > 0) {
                    boolean allTerminated = true;
                    for (Node subNode : subChildren.values()) {
                        Batch subbatch = getBatchInNode(subNode);
                        // Sum runtime statistics
                        batch.setStarted(batch.getStarted() + subbatch.getStarted());
                        batch.setLoggedIn(batch.getLoggedIn() + subbatch.getLoggedIn());
                        batch.setConnected(batch.getConnected() + subbatch.getConnected());
                        batch.setSeated(batch.getSeated() + subbatch.getSeated());
                        
                        batch.getSubBatches().add( subbatch );
                        if (subbatch.getStatus()!= Batch.Status.TERMINATED) {
                            allTerminated = false;
                        }
                    }
                    if (allTerminated) {
                        batch.setStatus(Batch.Status.TERMINATED);
                    }
                }
                result.add(batch);
                
            }
        }

        return result;
    }
    
    

    /**
     * Gets all batches contained in a node (should be just one).
     * The batch returned is immutable.
     *
     * @param node
     * @return
     */
    public Batch getBatchInNode(Node node) {
        Batch batch = new Batch((Batch)node.get(node.getName()));
        return batch;
    }
    
    
    /**
     * Get a list containing all top level batches
     * with their sub-batches included.
     * 
     * @return a List of all batches.
     */
    public List<Batch> getAllTopBatches() {
        List<Batch> result = new ArrayList<Batch>();
        try{
            // Get batch-root node
            Node root = cache.get(CacheHandler.CACHE_BATCH_ROOT);
            if (root != null) {
                // Get all first level batches
                result.addAll(getBatches(root));
            }

        }catch (Exception ex) {
            log.error("Error when inspecting cache: "+ex,ex);
        }
        
        return result;
    }
    
    
    
    /**
     * Get a List with sub-batches that matches supplied assignee.
     * 
     * @param assignee
     * @return
     */
    public List<Batch> getSubbatchesForAssignee(String assignee) {
        log.debug("Get all Sub-Batches for assignee: "+assignee);
        List<Batch> result = new ArrayList<Batch>();

        List<Batch> topBatches = getAllTopBatches();
        
        // Iterate all batches and subbatches to see if we have local matches
        for (Batch topBatch : topBatches) {
            List<Batch> subBatches = topBatch.getSubBatches();
            
            // Iterate Sub-Batches
            for (Batch batch : subBatches) {
                if (batch.getAssignee().equals(assignee)) {
                    result.add(batch);
                }
            }
        }
        
        return result;
    }
    
    
    /**
     * Checks if this batch is assigned to the current node.
     * 
     * @param batch
     * @return
     */
    public boolean isLocallyAssigned(Batch batch){
        String localIpAddress = CacheHandler.getInstance().getLocalAddress().getIpAddress().toString();
        String batchAssignee = batch.getAssignee();

        // Check if we have a match
        if (batchAssignee != null && localIpAddress.equals(batchAssignee)) {
            return true;
        }
        // No match, so return false
        return false;
    }
    
    
    /**
     * Bind the Tree-Cache to JMX
     */
    public void bindCacheToJMX() {
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName monitorName = new ObjectName("com.cubeia.bots.cache:type=TreeCache");
            mbs.registerMBean(cache, monitorName);
        }catch(Exception ex) {
            log.error("Could bind the cache to the JMX Server", ex);
        }
    }
    
    /**
     * Update a batch in the cache.
     * @param batch
     */
    public void updateBatch(Batch local) {
        log.trace("Update "+local.getId()+" With: "+local);
        try{
            Node node = cache.get(CACHE_BATCH_ROOT+local.getParentid()+"/"+local.getId());
            getCache().put(node.getFqn(), local.getId(), local);
                        
        } catch (Exception ex) {
            log.error("Could not update batch with runtime data: "+ex, ex);
        }
    }

    /**
     * Update a collection of batches to the cache.
     * @param batch
     */
    public void updateBatches(Collection<Batch> batches) {
        log.trace(" ---------- BATCH UPDATE NUMBER: "+batches.size());
        for (Batch batch : batches) {
            log.trace("BATCH UPDATE : "+batch);
            updateBatch(batch);
        }
    }

    
    /**
     * Returns a usable starting id for the bots and then increments 
     * and updates the cache with the number of bots.
     * 
     * @param numerOfBots
     * @return
     */
    public int getAndIncrementStartingID(int numberOfBots) {
        log.debug("Get StartingID and increment with: "+numberOfBots);
        int startid = -1;
        try{
            Node node = cache.get(CACHE_GLOBAL_ROOT);
            if (node == null) {
                node = createGlobalNode();
            }
            int numberOfMembers = cache.getMembers().size();
            startid = ((Integer)node.get(KEY_STARTID)).intValue();
            
            // Increment and update cache
            int nextStartId = startid + numberOfBots + numberOfMembers; // +1 for one watchingolem per member 
            System.out.println("old startID: "+startid+"; nextStartId: "+nextStartId+"; numBots: "+numberOfBots);
            getCache().put(node.getFqn(), KEY_STARTID, nextStartId);
            
        } catch (Exception ex) {
            log.error("Could not get and update starting id: "+ex, ex);
        }
        
        return startid;
    }
    
    private Node createGlobalNode() {
        log.info("Creating global node");
        try {
            // Set all defaults
            Map <String,Integer> values = new HashMap<String,Integer>();
            values.put(KEY_STARTID, DEFAULT_START_ID);
            
            cache.put(CACHE_GLOBAL_ROOT, values);
            Node node = cache.get(CACHE_GLOBAL_ROOT);
            return node;
            
        } catch(Exception ex) {
            log.error("Could not create the global node: "+ex, ex);
        }
        return null;
    }
    
    /**
     * The Updater is responsible for acquiring runtime data for
     * local batches and updating the cache.
     *
     * @author fredrik.johansson
     */
    private class Updater implements Runnable {

        public void run() {
            try {
                Map<String,Batch> batches = LocalServer.getInstance().getBatches();
                updateBatches(batches.values());
            } catch(Exception ex) {
                log.warn("BatchUpdater failed to update: "+ex);
            }
        }

    }
    
    
 

}
