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

import com.cubeia.firebase.poker.pokerbots.server.Batch;

/**
 * Bot Controller interface.
 * 
 * The controller routes a request to the underlying bot server(s).
 * A controller may use another controller as a down handler, then the request
 * or a tempered request will be passed on to the downhandler. This is
 * used to create middle layers (e.g. distributed calls).
 * 
 */
public interface Controller {
    
    /**
     * Request a batch of bots to be started.
     * 
     * @param batch
     */
    public void startBatch(Batch batch);

    /**
     * Request a batch of bots to be stopped.
     * 
     * @param batch
     */
    public void stopBatch(String batchId);
    
    
    /**
     * Request all batches to be stopped
     */
    
    public void stopAllBatches();
    

    /**
     * Attach a down handling controller.
     */
    public void attachController(Controller downHandler);
    
}
