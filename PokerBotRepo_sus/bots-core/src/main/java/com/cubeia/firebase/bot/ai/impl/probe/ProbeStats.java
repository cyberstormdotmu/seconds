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

package com.cubeia.firebase.bot.ai.impl.probe;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.Logger;

/**
 * Dynamic mbean for probe statistics
 * @author peter
 *
 */
public class ProbeStats implements ProbeStatsMBean {

	private Logger log = Logger.getLogger(ProbeStats.class);

	/** instance */
	private static ProbeStats instance = null;

	/** return singleton instance, create if needed */
	public static ProbeStats getInstance() {
		if (instance == null) {
			instance = new ProbeStats();
			instance.initJmx();
		}
		return instance;
	}

	/** storage for attribute/value pairs */
	private Map<String, Integer> stats = new ConcurrentHashMap<String, Integer>();

	/**
	 * Register bean in bean server
	 * @return 
	 */
	public void initJmx() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName monitorName;
		try {
			monitorName = new ObjectName(
					"com.game.bot.ai.impl.probe:type=ProbeStats");
			mbs.registerMBean(getInstance(), monitorName);
		} catch (Exception e) {
			log.error("failed to register MBean", e);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve value
	 * @param string
	 * @return 
	 */
	private synchronized long getValue(String string) {
		Integer value = stats.get(string);
		if (value != null) {
			return value.longValue();
		} else {
			System.out.println("Missing attribute: " + string);
			return -1;
		}
	}

	/**
	 * Put value
	 * @param string
	 * @param value
	 */
	public synchronized void putValue(String string, int value) {
		stats.put(string, Integer.valueOf(value));
	}

	/**
	 * Retrieve attribute 
	 */
	public synchronized Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		return getValue(attribute);
	}

	/**
	 * get the list of all known attributes
	 */
	public synchronized AttributeList getAttributes(String[] attributes) {
		AttributeList list = new AttributeList();
		for (String name : stats.keySet()) {
			long value = getValue(name);
			list.add(new Attribute(name, value));
		}
		return list;

	}

	/**
	 * Return MBean info structure
	 */
	public synchronized MBeanInfo getMBeanInfo() {
		SortedSet<String> names = new TreeSet<String>();
		for (Object name : stats.keySet())
			names.add((String) name);
		MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[names.size()];
		Iterator<String> it = names.iterator();
		for (int i = 0; i < attrs.length; i++) {
			String name = it.next();
			attrs[i] = new MBeanAttributeInfo(name, "java.lang.long",
					"Property " + name, true, // isReadable
					true, // isWritable
					false); // isIs
		}
		return new MBeanInfo(this.getClass().getName(), "Probe Stats MBean",
				attrs, null, // constructors
				null, // operations
				null); // notifications
	}

	/**
	 * invoke method is not supported in this bean - return null
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		return null;
	}

	/**
	 * set attribute does nothing as the attributes should be read only 
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
	}

	/**
	 * set attribute list is not supported, dummy implementation
	 */
	public AttributeList setAttributes(AttributeList attributes) {
		return null;
	}
}
