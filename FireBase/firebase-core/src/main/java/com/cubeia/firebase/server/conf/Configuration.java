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
package com.cubeia.firebase.server.conf;

import java.util.Map;

import com.cubeia.firebase.api.server.conf.Namespace;
import com.cubeia.firebase.api.server.conf.PropertyKey;

public interface Configuration {
	
	public Map<PropertyKey, String> cloneProperties();

	public String getProperty(PropertyKey key);
	
	public String getProperty(Namespace ns, String prop);
	
	public boolean hasProperty(Namespace ns, String prop);

	public String[] getProperties(Namespace ns);
	
	public PropertyKey[] getKeys();
	
	public int size();
	
}
