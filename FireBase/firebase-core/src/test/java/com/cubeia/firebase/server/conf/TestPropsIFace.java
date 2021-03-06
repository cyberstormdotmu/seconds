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

import com.cubeia.firebase.api.server.conf.Configurable;
import com.cubeia.firebase.api.server.conf.Inheritance;
import com.cubeia.firebase.api.server.conf.Property;

public interface TestPropsIFace extends Configurable {

	@Property(property="string-one")
	public String getStringThree();
	
	@Property(property="string-two",defaultValue="kalle3")
	public String getStringFour();
	
	public String getStringFive();
	
	@Property(property="boolean-one",defaultValue="true")
	public boolean getBooleanOne();	
	
	@Property(fallback="string-five", inheritance=Inheritance.ALLOW)
	public String getOverrideOne();	
	
}
