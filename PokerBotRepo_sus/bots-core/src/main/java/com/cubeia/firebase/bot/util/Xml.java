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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Xml {

	private Xml() { }
	
	public static Document read(InputStream in) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		return fact.newDocumentBuilder().parse(in);
	
	}
	
	public static String selectString(Node root, String xpath) throws XPathExpressionException {
		XPath path = XPathFactory.newInstance().newXPath();
		return path.evaluate(xpath, root);
	}

	public static NodeList selectNodes(Node root, String xpath) throws XPathExpressionException {
		XPath path = XPathFactory.newInstance().newXPath();
		return (NodeList)path.evaluate(xpath, root, XPathConstants.NODESET);
	}
}
