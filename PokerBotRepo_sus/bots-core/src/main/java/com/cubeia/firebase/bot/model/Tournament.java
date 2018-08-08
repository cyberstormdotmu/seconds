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

package com.cubeia.firebase.bot.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cubeia.firebase.api.defined.Parameter;
import com.cubeia.firebase.api.util.ParameterUtil;
import com.cubeia.firebase.bot.io.BinaryUtil;
import com.cubeia.firebase.io.protocol.Enums.ParameterType;
import com.cubeia.firebase.io.protocol.Param;

public class Tournament {
    
    private int id;
    
    private String name = "n/a";
    private int capacity = -1;
    private int registered = -1;
    private String address = "";
    private boolean registering = false;

    private Map<String,Param> parameters = new ConcurrentHashMap<String,Param>();
    
   
    
    public Tournament(int id) {
        this.id = id;
    }
    
    public String toString() {
        String info = "Tournament id["+id+"] fqn["+address+"] cap["+capacity+"] reg["+registered+"] open["+registering+"] - Params: ";
        
        for (Param p : parameters.values()) {
            
            if (p.type == ParameterType.STRING.ordinal()) {
                Parameter<String> stringParameter = ParameterUtil.convertAsString(p);
                String paramVal = null;
                if(stringParameter!=null) {
                    paramVal = stringParameter.getValue();
                }
                info += p.key+"["+ paramVal+"] ";
                
            } else if (p.type == ParameterType.INT.ordinal())  {
                info += p.key+"["+BinaryUtil.bytesToInt(p.value)+"] ";
                
            } 
//            else if (p.type == ParameterType.DATE.ordinal())  {
//                info += p.key+"["+new Date(1000*BinaryData.byteArrayToInt(p.value))+"] ";
//            }
        }
        return info;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public int getRegistered() {
        return registered;
    }
    
    public void incrementRegisteredPlayers() {
        registered++;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
    
    public void updateFromParameters(List<Param> params) {
        for (Param p: params) {
            parameters.put(p.key, p);
        }
        
        // Set explicit variables
        if (parameters.containsKey("CAPACITY")) {
            capacity = BinaryUtil.bytesToInt(parameters.get("CAPACITY").value);
        }
        
        if (parameters.containsKey("REGISTERED")) {
            registered = BinaryUtil.bytesToInt(parameters.get("REGISTERED").value);
        }
        
        if (parameters.containsKey("STATUS")) {
            Parameter<String> status = ParameterUtil.convertAsString(parameters.get("STATUS"));
            registering = status.getValue().equals("REGISTERING");
        }
        
        if (parameters.containsKey("NAME")) {
            name = ParameterUtil.convertAsString(parameters.get("NAME")).getValue();
        }
    }

    public boolean isOpenForRegistration() {
        return registering;
    }
    
    public boolean isFull() {
        boolean full = false;
        if (registered >= 0 && capacity > 0) {
            full = registered >= capacity;
        }
        return full;
    }
    
}
