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

import java.io.File;
import java.net.InetAddress;
import java.net.URL;

import com.cubeia.firebase.api.server.conf.Configurable;
import com.cubeia.firebase.api.util.StringList;

public interface TestIFace extends Configurable {
	
	public static enum MyEnum { ONE, TWO }

	
	/// --- STRING LIST --- ///
	
	public StringList getStrings();
	
	
	/// --- INET ADDRESS --- //
	
	public InetAddress getInetAddress();
	
	
	/// --- ENUM --- ///
	
	public MyEnum getEnum();
	
	
	/// --- PRIMITIVES --- ///
	
	public byte getByte();
	
	public boolean getBoolean();
	
	public short getShort();
	
	public int getInt();
	
	public long getLong();
	
	public float getFloat();
	
	public double getDouble();
	
	public char getChar();
	
	
	/// --- PRIMITIVE OBJECTS --- ///
	
	public Byte getByteO();
	
	public Boolean getBooleanO();
	
	public Short getShortO();
	
	public Integer getIntO();
	
	public Long getLongO();
	
	public Float getFloatO();
	
	public Double getDoubleO();
	
	public Character getCharO();
	
	
	/// --- SOME OBJECTS --- ///
	
	public String getString();
	
	public File getFile();
	
	public URL getUrl();

}