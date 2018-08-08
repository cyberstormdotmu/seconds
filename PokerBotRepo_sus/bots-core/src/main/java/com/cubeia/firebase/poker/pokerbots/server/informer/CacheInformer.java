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

package com.cubeia.firebase.poker.pokerbots.server.informer;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.cache.TreeCache;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.cache.CacheHandler;


/**
 * 
 */
public class CacheInformer implements Informer {
 
	@SuppressWarnings("unused")
    private Logger log = Logger.getLogger(this.getClass());

    protected Informer upHandler;

    protected TreeCache cache;

    /**
     * Empty constructor.
     * Gets the Cache and stores a reference.
     *
     */
    public CacheInformer(){
        cache = CacheHandler.getInstance().getCache();
    }

    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.informer.Informer#getBatch(String)
     */
    public Batch getBatch(String id) {
        return upHandler.getBatch(id);
    }

    /**
     * Get the whole tree
     *
     * @see com.cubeia.firebase.poker.pokerbots.server.informer.Informer#getAllBatches()
     */
    public List<Batch> getAllBatches() {
        return CacheHandler.getInstance().getAllTopBatches();
    }

    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.informer.Informer#attachInformation(Informer)
     */
    public void attachInformation(Informer upHandler) {
        this.upHandler = upHandler;
    }

    public int getNextStartingid(int numberOfBots) {
        return CacheHandler.getInstance().getAndIncrementStartingID(numberOfBots);
    }


}
