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

package com.cubeia.firebase.bot;

import java.net.ServerSocket;

/**
 * Run this to see how many sockets you can allocated
 * @author Fredrik
 *
 */
public class SocketTest {
	
	public static void main(String args[]) throws Exception	{
		for (int n = 0; ; n++) {
			if (n % 100 == 0) {
				System.out.println(n + "...");
			}
			new ServerSocket(30000 + n);
		}
	}



}
