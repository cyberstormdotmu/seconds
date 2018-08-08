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

import com.cubeia.firebase.bot.util.Arithmetic;

import junit.framework.TestCase;

public class ArithmeticTest extends TestCase {

	public void testLinearMaxAverage() {
		for (int i = 0; i < 10000; i++) {
			int rnd = Arithmetic.LinearMaxAverage(100);
			assertTrue(rnd > 49);
			assertTrue(rnd < 151);
		}
	}

	public void testGaussianAverage() {
		for (int i = 0; i < 10000; i++) {
			int rnd = Arithmetic.GaussianAverage(100);
//			System.out.println("Gaussian : "+rnd);
			assertTrue(rnd > 99);
		}
	}
	
	
//	public void testGaussian() {
//        for (int i = 0; i < 20; i++) {
//            int rnd = Arithmetic.GaussianAverage(2000, 2000);
//            System.out.println("Gaussian : "+rnd);
//            // assertTrue(rnd > 99);
//        }
//    }

}
