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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cubeia.firebase.poker.pokerbots.server.Batch;


/**
 * Dummy implementation of the Information interface
 * 
 */
public class MockInformer implements Informer{
 
    private Logger log = Logger.getLogger(this.getClass());
    
    /**
     * Create a dummy-Batch and return it.
     * 
     */
    public Batch getBatch(String id) {
        return createBatch(1);
    }

    public void attachInformation(Informer upHandler) {
        log.info("Attach Information was called.");
    }

    /**
     * Returns a random number of batches (between 0-10)
     * 
     */
    public List<Batch> getAllBatches() {
        int number = (int)(Math.random()*10);
        
        List<Batch> output = new ArrayList<Batch>();
        
        for( int i = 0; i < number; i++ ){
            output.add(createBatch(i));
        }
         
        return output;
    }
    
    
    /**
     * Create a dummy-Batch
     * 
     * @param seq, an increasing counter used to create unique id's
     */
    private Batch createBatch(int seq){
        Batch batch = new Batch();
        batch.setId("Mock-Batch-"+seq);
        batch.setUrl("0.0.0.0");
        batch.setPort(6996);
        batch.setRequested(100);
        batch.setStarted( (int)(Math.random() * 100) );
        
        return batch;
    }

    public int getNextStartingid(int numberOfBots) {
        return 1;
    }
}
