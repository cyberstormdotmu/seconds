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

import org.apache.log4j.Logger;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.local.LocalServer;


/**
 * The Local Controller implements the controller logic
 * towards a local Bot handling.
 *
 * A Local Controller cannot use another down handler.
 */
public class LocalController implements Controller{

    private static Logger log = Logger.getLogger(LocalController.class);
    
    public LocalController(int startid) {
    	LocalServer.getInstance().setInitialStartId(startid);
    }
    
    public LocalController() {
    	this(1);
    }

    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#attachController(com.cubeia.firebase.poker.pokerbots.server.controller.Controller)
     */
    public void attachController(Controller downHandler) {
        log.warn("The Local Controller cannot use a DownHandler");
    }


    /**
     *  @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#startBatch(Batch)
     */
    public void startBatch(Batch batch) {
        LocalServer.getInstance().startBatch(batch);
    }

    /**
     *  @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#stopBatch(Batch)
     */
    public void stopBatch(String batchId) {
        LocalServer.getInstance().stopBatch(batchId);
    }
    
    /**
     *  @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#stopBatch(Batch)
     */
    public void stopAllBatches() {
        LocalServer.getInstance().stopAllBatches();
    }

}
