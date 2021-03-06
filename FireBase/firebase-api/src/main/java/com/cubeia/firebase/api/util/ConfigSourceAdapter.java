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
package com.cubeia.firebase.api.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>NB: </b> This class is within the public API because of build reasons,
 * it should only be used for testing! It will be moved shortly. See Trac issue
 * #417.
 * 
 * <p>This adapter class may contain a name, but returns null on the
 * input stream accessor.
 * 
 * @author Larsan
 */
//FIXME: Move to test, if you can get Maven to support it, see Trac issue #417
public class ConfigSourceAdapter implements ConfigSource {

	private String name;
	
	/**
	 * @param name Source name, may be null
	 */
	public ConfigSourceAdapter(String name) {
		this.name = name;
	}
	
	public ConfigSourceAdapter() { }
	
	public InputStream getInputStream() throws IOException {
		return null;
	}

	public String getName() {
		return name;
	}
}
