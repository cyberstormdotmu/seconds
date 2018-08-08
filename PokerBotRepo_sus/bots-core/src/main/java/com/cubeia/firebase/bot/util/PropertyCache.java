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

package com.cubeia.firebase.bot.util;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches setters for String, Boolean, int
 * 
 * @author peter
 * 
 */
public class PropertyCache {

	private Map<String, Method> properties = new ConcurrentHashMap<String, Method>();

	@SuppressWarnings({ "rawtypes" })
    public PropertyCache(Object object) {
		
		Class c = object.getClass();
		Method methods[] = c.getMethods(); 
		
		for ( Method method : methods) {
			properties.put(method.getName(), method);
		}
	}
	
	public Method getMethod(String methodName) {
		return properties.get(methodName);
	}
	
}
