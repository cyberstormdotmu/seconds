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

import java.util.Random;

public class Arithmetic {
	
	private static Random rng = new Random();
	
	/**
	 * Returns a random value equally distributed
	 * between 0.5*value and 1.5*value.
	 * 
	 * @param value
	 * @return
	 */
	public static int LinearMaxAverage(int value) {
		int i = rng.nextInt(value);
		return value/2 + i; 
	}
	
	
	/**
	 * Returns a gaussian average using the value
	 * as the standard deviation.
	 * 
	 * The resulting value will be at least the given value.
	 * Maximum value is not applicable.
	 * 
	 * @param value
	 * @return
	 */
	public static int GaussianAverage(int value) {
		float g = Math.abs((float)rng.nextGaussian());
		return (int)(value + ((double)value)* g);
	}
	
	/**
     * Returns a gaussian average for the given mean and deviation.
     * 
     * 
     * @param mean
     * @param deviation
     * @return result, may be negative
     */
    public static int GaussianAverage(int mean, int deviation) {
        float g = (float)rng.nextGaussian();
        g = g*(float)deviation;
        return mean + (int)g;
    }
	
}
