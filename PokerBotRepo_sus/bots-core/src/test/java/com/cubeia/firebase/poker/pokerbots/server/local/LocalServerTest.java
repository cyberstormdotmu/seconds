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

import junit.framework.TestCase;

import com.cubeia.firebase.poker.pokerbots.server.Batch;
import com.cubeia.firebase.poker.pokerbots.server.local.LocalServer;
/**
 * Created on 2005-nov-30
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public class LocalServerTest extends TestCase {

    public static void main(String[] args) {
    }

    /*
     * Test method 
     */
    public void testIsWithinRange() {
        Batch batch = new Batch();
        batch.setStartingid(1000);
        batch.setRequested(100);
        
        LocalServer server = LocalServer.getInstance();
        
        assertEquals(true, server.isWithinRange(1050, batch));
        assertEquals(true, server.isWithinRange(1000, batch));
        assertEquals(true, server.isWithinRange(1099, batch));
        
        assertEquals(false, server.isWithinRange(999, batch));
        assertEquals(false, server.isWithinRange(1100, batch));
        
    }

}

/*
$Log: $
*/