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

package com.cubeia.firebase.bot.automation;

import java.util.LinkedList;
import java.util.List;

/**
 * Bot automation scenario
 * Contains batches to run
 * 
 * @author Peter Lundh
 *
 */
public class AutomationScenario {

	/**
	 * Scenario name
	 */
	private String name;
	
	/**
	 * Number of seconds to run scenario
	 */
	private int	runtime;
	
	/**
	 * list of batches to execute for this scenario
	 */
	private List<AutomationBatch> batches = new LinkedList<AutomationBatch>();

	
	/**
	 * Return list of batches
	 * @return returns batch list
	 */
	public List<AutomationBatch> getBatches() {
		return batches;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getRuntime() {
		return runtime;
	}


	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	
	
}
