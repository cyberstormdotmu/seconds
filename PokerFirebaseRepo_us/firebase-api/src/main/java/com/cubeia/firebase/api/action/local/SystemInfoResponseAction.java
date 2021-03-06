/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
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
package com.cubeia.firebase.api.action.local;

import java.util.ArrayList;
import java.util.List;

import com.cubeia.firebase.api.action.visitor.LocalActionVisitor;
import com.cubeia.firebase.api.defined.Parameter;

public class SystemInfoResponseAction implements LocalAction {

    private int players; 
    
    private List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
    
    public SystemInfoResponseAction(int players) {
        this.players = players;
    }
    
    public void visit(LocalActionVisitor visitor) {
        visitor.handle(this);        
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter<?>> parameters) {
        this.parameters = parameters;
    }
    
    
}
