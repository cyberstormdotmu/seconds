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

package com.cubeia.firebase.poker.pokerbots.server.distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jboss.cache.TreeCache;
import org.jgroups.stack.IpAddress;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.cache.CacheHandler;


/**
 * A TreeCache distribution calculator.
 *
 * This implementation implements the simplest possible
 * distribution algorithm. To provide your own enhanced
 * treecache-version you can subclass this class.
 *
 *
 */
public class CacheCalculator implements Calculator{

    protected TreeCache cache;

    /**
     * Create calculator and inject the cache.
     * @param cache
     */
    public CacheCalculator (TreeCache cache) {
        this.cache = cache;
    }


    /**
     * Distribute a batch into separate Batches.
     *
     */
    public List<Batch> distribute(Batch batch) {
        List<Batch> result = new ArrayList<Batch>();

        // Get the base-starting ID
        int startId = batch.getStartingid();
        
        List<IpAddress> members = CacheHandler.getInstance().getMembers();

        // Distribute the bots over the members only if the requested number is more than 10.
        if (batch.getRequested()>10) {
        
            // Divide the total requested with the number of nodes
            // (It may not be evenly divided)
            int approxSubBatchNumber = batch.getRequested() / members.size();

            // Create sub-batches
            for (IpAddress member : members) {
                Batch sub = new Batch(batch);
                sub.setParentid(batch.getId());
                // Set ID to id + assigned IpAddress
                sub.setId(batch.getId()+"-"+member.toString());
                // assign to this member
                sub.setAssignee(member.getIpAddress().toString());
                sub.setRequested(approxSubBatchNumber);
                // we need to set different starting IDs.
                sub.setStartingid(startId);
                
                result.add(sub);
                // Increment startId
                startId += approxSubBatchNumber+1; // Add one extra for lobby bot
            }

        } else {
            Batch sub = new Batch(batch);
            
            // Choose randomly from members
            IpAddress member = members.get(new Random().nextInt(members.size()));
            sub.setParentid(batch.getId());
            // Set ID to id + assigned IpAddress
            sub.setId(batch.getId()+"-"+member.toString());
            // assign to this member
            sub.setAssignee(member.getIpAddress().toString());
            sub.setRequested(batch.getRequested());
            // we need to set different starting IDs.
            sub.setStartingid(startId);
            
            result.add(sub);
        }
        

        return result;
    }

}
