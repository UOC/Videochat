package org.red5.io.mock;

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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.io.object.BaseInput;
import org.red5.io.object.Deserializer;
import org.w3c.dom.Document;

public class Input extends BaseInput implements org.red5.io.object.Input {

	protected static Log log = LogFactory.getLog(Input.class.getName());

	protected List list;

	protected int idx;

	public Input(List list) {
		super();
		this.list = list;
		this.idx = 0;
	}

	/**
     * Getter for property 'next'.
     *
     * @return Value for property 'next'.
     */
    protected Object getNext() {
		return list.get(idx++);
	}

	/** {@inheritDoc} */
    public byte readDataType() {
		Byte b = (Byte) getNext();
		return b.byteValue();
	}

	// Basic

	/** {@inheritDoc} */
    public Object readNull() {
		return null;
	}

	/** {@inheritDoc} */
    public Boolean readBoolean() {
		return (Boolean) getNext();
	}

	/** {@inheritDoc} */
    public Number readNumber() {
		return (Number) getNext();
	}
    /** {@inheritDoc} */
	public String getString() {
		return (String) getNext();
	}
    /** {@inheritDoc} */
	public String readString() {
		return (String) getNext();
	}

	/** {@inheritDoc} */
    public Date readDate() {
		return (Date) getNext();
	}

	// Array

	/** {@inheritDoc} */
    public Object readArray(Deserializer deserializer) {
    	return getNext();
    }
    
	/** {@inheritDoc} */
    public Object readMap(Deserializer deserializer) {
		return getNext();
	}
    
	/** {@inheritDoc} */
    public Map<String, Object> readKeyValues(Deserializer deserializer) {
		return (Map<String, Object>) getNext();
	}
    
	// Object

	/** {@inheritDoc} */
    public Object readObject(Deserializer deserializer) {
		return getNext();
	}

	/** {@inheritDoc} */
	public Document readXML() {
		return (Document) getNext();
	}

	/** {@inheritDoc} */
    public Object readCustom() {
		// Not supported
		return null;
	}

	/** {@inheritDoc} */
    public Object readReference() {
		final Short num = (Short) getNext();
		return getReference(num.shortValue());
	}

}
