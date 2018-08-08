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



/**
 * Mock implementation to test and develop the Controller interface
 *
 */
public class MockController implements Controller{

    private static Logger log = Logger.getLogger(MockController.class);
    
    /**
     *  @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#startBatch(Batch)
     */
    public void startBatch(Batch batch) {
       log.info("startBatch called for : "+batch);
    }

    /**
     * @see com.cubeia.firebase.poker.pokerbots.server.controller.Controller#attachController(com.cubeia.firebase.poker.pokerbots.server.controller.Controller)
     */
    public void attachController(Controller downHandler) {
        log.info("DownHandler attached");
    }

    public void stopBatch(String batchId) {
        log.info("stopBatch called for : "+batchId);
    }
    
    public void stopAllBatches() {
        log.info("stopAllBatches called");
    }
   
    
}
