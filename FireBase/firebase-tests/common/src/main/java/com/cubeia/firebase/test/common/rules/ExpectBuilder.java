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
package com.cubeia.firebase.test.common.rules;

import com.cubeia.firebase.test.common.util.Serializer;

/**
 * <b>This is an experimental fluid style builder interface, please refer
 * to {@link FluidBuilder}.</b>
 * 
 * @author Lars J. Nilsson
 */
public interface ExpectBuilder extends Expect {

	public AssertBuilder expect(Class<?> cl);
	
	public AssertBuilder expect(Class<?> cl, Serializer ser);
	
	public AssertBuilder expect(Expect e);
	
}
