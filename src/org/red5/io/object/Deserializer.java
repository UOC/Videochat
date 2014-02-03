package org.red5.io.object;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Deserializer class reads data input and handles the data 
 * according to the core data types 
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public class Deserializer {

	// Initialize Logging
	protected static Log log = LogFactory.getLog(Deserializer.class.getName());

	/**
	 * Deserializes the input parameter and returns an Object
	 * which must then be cast to a core data type
	 * @param in
	 * @return Object
	 */
	public Object deserialize(Input in) {

		byte type = in.readDataType();

		while (type == DataTypes.CORE_SKIP) {
			type = in.readDataType();
		}

		if (log.isDebugEnabled()) {
			log.debug("Datatype: " + DataTypes.toStringValue(type));
		}

		Object result;

		switch (type) {
			case DataTypes.CORE_NULL:
				result = in.readNull();
				break;
			case DataTypes.CORE_BOOLEAN:
				result = in.readBoolean();
				break;
			case DataTypes.CORE_NUMBER:
				result = in.readNumber();
				break;
			case DataTypes.CORE_STRING:
				result = in.readString();
				break;
			case DataTypes.CORE_DATE:
				result = in.readDate();
				break;
			case DataTypes.CORE_ARRAY:
				result = in.readArray(this);
				break;
			case DataTypes.CORE_MAP:
				result = in.readMap(this);
				break;
			case DataTypes.CORE_XML:
				result = in.readXML();
				break;
			case DataTypes.CORE_OBJECT:
				result = in.readObject(this);
				break;
			case DataTypes.OPT_REFERENCE:
				result = in.readReference();
				break;
			default:
				result = in.readCustom();
				break;
		}

		if (type >= DataTypes.CORE_OBJECT) {
			result = postProcessExtension(result);
		}

		return result;
	}

	/**
	 * Post processes the result
	 * TODO Extension Point
     * @param result
     * @return
     */
	protected Object postProcessExtension(Object result) {
		// does nothing at the moment, but will later!
		return result;
	}

}
