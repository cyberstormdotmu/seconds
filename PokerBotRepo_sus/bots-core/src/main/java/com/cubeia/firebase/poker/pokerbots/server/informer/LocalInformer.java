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

import java.util.Collection;

import org.apache.log4j.Logger;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.local.LocalServer;


/**
 * Informer interface that gets the information from the
 * local bot group.
 *
 */
public class LocalInformer implements Informer {
 
    private Logger log = Logger.getLogger(this.getClass());
    
    public Batch getBatch(String id) {
        return LocalServer.getInstance().getBatch(id);
    }

    public Collection<Batch> getAllBatches() {
        return LocalServer.getInstance().getBatches().values();
    }


    /**
     * A Local Informer cannot use another UpHandler,
     * so print a warning to the logs.
     *
     * @param upHandler, another informer to draw information from.
     */
    public void attachInformation(Informer upHandler) {
        log.warn("A Local Informer cannot use another UpHandler.");
    }

    
    public int getNextStartingid(int numberOfBots) {
        int startid = LocalServer.getInstance().getStartid().get();
        LocalServer.getInstance().getStartid().set(startid + numberOfBots + 1);
        return startid;
    }


}
