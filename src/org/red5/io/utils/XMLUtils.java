package org.red5.io.utils;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006-2007 by respective authors (see below). All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later 
 * version. 
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *Misc XML utils
 *
 * @author The Red5 Project (red5@osflash.org)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public class XMLUtils {
    /**
     * Logger
     */
	protected static Log log = LogFactory.getLog(XMLUtils.class.getName());

    /**
     * Converts string representation of XML into Document
     * @param str              String representation of XML
     * @return                 DOM object
     * @throws IOException     I/O exception
     */
    public static Document stringToDoc(String str) throws IOException {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			return db.parse(new InputSource(str));
		} catch (Exception ex) {
			throw new IOException("Error converting from string to doc "
					+ ex.getMessage());
		}
	}

    /**
     * Converts doc to String
     * @param dom            DOM object to convert
     * @return               XML as String
     */
    public static String docToString(Document dom) {
		return XMLUtils.docToString1(dom);
	}

	/**
	 * Convert a DOM tree into a String using Dom2Writer
     * @return               XML as String
     * @param dom            DOM object to convert
     */
	public static String docToString1(Document dom) {
		StringWriter sw = new StringWriter();
		DOM2Writer.serializeAsXML(dom, sw);
		return sw.toString();
	}

	/**
	 * Convert a DOM tree into a String using transform
     * @param domDoc                  DOM object
     * @throws java.io.IOException    I/O exception
     * @return                        XML as String
     */
	public static String docToString2(Document domDoc) throws IOException {
		try {
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "no");
			StringWriter sw = new StringWriter();
			Result result = new StreamResult(sw);
			trans.transform(new DOMSource(domDoc), result);
			return sw.toString();
		} catch (Exception ex) {
			throw new IOException("Error converting from doc to string "
					+ ex.getMessage());
		}
	}

}
