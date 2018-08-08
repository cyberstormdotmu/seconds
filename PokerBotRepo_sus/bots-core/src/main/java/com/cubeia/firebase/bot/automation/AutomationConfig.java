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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cubeia.firebase.bot.util.Xml;

/**
 * Automation configuration
 * @author peter
 *
 */public class AutomationConfig {

	/**
	 * Map of scenarios
	 */
	private Map<String, AutomationScenario> scenarios = new ConcurrentHashMap<String, AutomationScenario>();
	
	/**
	 * Map of batches
	 */
	private Map<String,AutomationBatch> batches = new ConcurrentHashMap<String, AutomationBatch>();

	/**
	 * @return returns batch list
	 */
	public Map<String, AutomationBatch> getBatches() {
		return batches;
	}
	
	/**
	 * @return returns list of scenarios
	 */
	public Map<String, AutomationScenario> getScenarios() {
		return scenarios;
	}
	

	/**
	 * Read automation configuration from file and create scenarios & batches
	 * 
	 * @param configFileName - XML configuration file name 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public void readConfig(String configFileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {

		FileInputStream in = new FileInputStream(configFileName);

		Document doc = Xml.read(in);
		
		setBatches(doc);
		setScenarios(doc);

	}

	/**
	 * Get batch information from XML document 
	 * @param doc
	 * @throws XPathExpressionException
	 */
	private void setBatches(Document doc) throws XPathExpressionException {
		NodeList batch = Xml.selectNodes((Node) doc, "configuration/batch");
		for (int i = 0; i < batch.getLength(); i++) {
			Node node = batch.item(i);
			NamedNodeMap attributes = node.getAttributes();
			Node batchId = attributes.getNamedItem("id");
			Node batchName = attributes.getNamedItem("name");

			AutomationBatch automationBatch = new AutomationBatch();
			automationBatch.setName(batchName.getNodeValue());
			
			NodeList batchParamNodes = node.getChildNodes();
			for (int j = 0; j < batchParamNodes.getLength(); j++) {
				Node paramNode = batchParamNodes.item(j);
				NamedNodeMap paramAttributes = paramNode.getAttributes();
				if (paramAttributes != null) {
					
					Node paramName = paramAttributes.getNamedItem("name");
					Node paramValue = paramAttributes.getNamedItem("value");
					automationBatch.getProperties().put(paramName.getNodeValue(), paramValue.getNodeValue());
				}
			}
			batches.put(batchId.getNodeValue(), automationBatch);
		}
	}

	/**
	 * Get all defined scenarios from XML document
	 * @param doc
	 * @throws XPathExpressionException
	 */
	private void setScenarios(Document doc) throws XPathExpressionException {
		NodeList batch = Xml.selectNodes((Node) doc, "configuration/scenario");
		for (int i = 0; i < batch.getLength(); i++) {
			Node node = batch.item(i);
			NamedNodeMap attributes = node.getAttributes();
			Node scenarioId = attributes.getNamedItem("name");
			Node scenarioRuntime = attributes.getNamedItem("runtime");

			AutomationScenario automationScenario = new AutomationScenario();
			// set scenario name
			automationScenario.setName(scenarioId.getNodeValue());
			
			// set scenario runtime
			if ( scenarioRuntime != null ) {
				automationScenario.setRuntime(Integer.parseInt(scenarioRuntime.getNodeValue()));
			} else {
				// default, 1 hour (3600 seconds)
				automationScenario.setRuntime(3600);
			}
			
			NodeList batchParamNodes = node.getChildNodes();
			for (int j = 0; j < batchParamNodes.getLength(); j++) {
				Node paramNode = batchParamNodes.item(j);
				NamedNodeMap paramAttributes = paramNode.getAttributes();
				
				if (paramAttributes != null) {
					
					Node paramId = paramAttributes.getNamedItem("id");
					automationScenario.getBatches().add(batches.get(paramId.getNodeValue()));
				}
			}
			scenarios.put(scenarioId.getNodeValue(), automationScenario);
		}
	}
}
