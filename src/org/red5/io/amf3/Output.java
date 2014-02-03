package org.red5.io.amf3;

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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.red5.io.amf.AMF;
import org.red5.io.object.RecordSet;
import org.red5.io.object.Serializer;
import org.red5.io.object.SerializerOption;

/**
 * AMF3 output writer
 *
 * @see  org.red5.io.amf3.AMF3
 * @see  org.red5.io.amf3.Input
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (jojo@struktur.de)
 */
public class Output extends org.red5.io.amf.Output implements org.red5.io.object.Output {

	protected static Log log = LogFactory.getLog(Output.class.getName());

	/**
	 * Set to a value above <tt>0</tt> to disable writing of the AMF3 object tag.
	 */
	private int amf3_mode;
	/**
	 * List of strings already written.
	 * */
	private List<String> stringReferences;

	/**
	 * Constructor of AMF3 output.
	 *
	 * @param buf
	 *            instance of ByteBuffer
	 * @see ByteBuffer
	 */
	public Output(ByteBuffer buf) {
		super(buf);
		amf3_mode = 0;
		stringReferences = new LinkedList<String>();
	}

	/**
	 * Provide access to raw data.
	 *
	 * @return ByteBuffer
	 */
	protected ByteBuffer getBuffer() {
		return buf;
	}

    /** {@inheritDoc} */
	@Override
	public boolean supportsDataType(byte type) {
		return true;
	}

	// Basic Data Types

	protected void writeAMF3() {
		if (amf3_mode == 0)
			buf.put(AMF.TYPE_AMF3_OBJECT);
	}

    /** {@inheritDoc} */
	@Override
	public void writeBoolean(Boolean bol) {
		writeAMF3();
		buf.put(bol ? AMF3.TYPE_BOOLEAN_TRUE : AMF3.TYPE_BOOLEAN_FALSE);
	}

    /** {@inheritDoc} */
	@Override
	public void writeNull() {
		writeAMF3();
		buf.put(AMF3.TYPE_NULL);
	}

    /** {@inheritDoc} */
	protected void putInteger(long value) {
		if (value < 0) {
			buf.put((byte) (0x80 | ((value >> 22) & 0xff)));
			buf.put((byte) (0x80 | ((value >> 15) & 0x7f)));
			buf.put((byte) (0x80 | ((value >> 8) & 0x7f)));
			buf.put((byte) (value & 0xff));
		} else if (value <= 0x7f) {
			buf.put((byte) value);
		} else if (value <= 0x3fff) {
			buf.put((byte) (0x80 | ((value >> 7) & 0x7f)));
			buf.put((byte) (value & 0x7f));
		} else if (value <= 0x1fffff) {
			buf.put((byte) (0x80 | ((value >> 14) & 0x7f)));
			buf.put((byte) (0x80 | ((value >> 7) & 0x7f)));
			buf.put((byte) (value & 0x7f));
		} else {
			buf.put((byte) (0x80 | ((value >> 22) & 0xff)));
			buf.put((byte) (0x80 | ((value >> 15) & 0x7f)));
			buf.put((byte) (0x80 | ((value >> 8) & 0x7f)));
			buf.put((byte) (value & 0xff));
		}
	}

	/** {@inheritDoc} */
	protected void putString(String str, java.nio.ByteBuffer string) {
		final int len = string.limit();
		int pos = stringReferences.indexOf(str);
		if (pos >= 0) {
			// Reference to existing string
			putInteger(pos << 1);
			return;
		}

		putInteger(len << 1 | 1);
		buf.put(string);
		stringReferences.add(str);
	}

    /** {@inheritDoc} */
	@Override
	public void putString(String string) {
		if ("".equals(string)) {
			// Empty string;
			putInteger(1);
			return;
		}

		final java.nio.ByteBuffer strBuf = AMF3.CHARSET.encode(string);
		putString(string, strBuf);
	}

    /** {@inheritDoc} */
	@Override
	public void writeNumber(Number num) {
		writeAMF3();
		if (num instanceof Long || num instanceof Integer || num instanceof Short || num instanceof Byte) {
			buf.put(AMF3.TYPE_INTEGER);
			putInteger(num.longValue());
		} else {
			buf.put(AMF3.TYPE_NUMBER);
			buf.putDouble(num.doubleValue());
		}
	}

    /** {@inheritDoc} */
	@Override
	public void writeString(String string) {
		writeAMF3();
		buf.put(AMF3.TYPE_STRING);
		if ("".equals(string)) {
			putInteger(1);
		} else {
			final java.nio.ByteBuffer strBuf = AMF3.CHARSET.encode(string);
			putString(string, strBuf);
		}
	}

    /** {@inheritDoc} */
	@Override
	public void writeDate(Date date) {
		writeAMF3();
		buf.put(AMF3.TYPE_DATE);
		if (hasReference(date)) {
			putInteger(getReferenceId(date) << 1);
			return;
		}

		storeReference(date);
		putInteger(1);
		buf.putDouble(date.getTime());
	}

    /** {@inheritDoc} */
    public void writeArray(Collection array, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_ARRAY);
    	if (hasReference(array)) {
    		putInteger(getReferenceId(array) << 1);
    		return;
    	}

    	storeReference(array);
		amf3_mode += 1;
		int count = array.size();
		putInteger(count << 1 | 1);
		putString("");
		for (Object item: array) {
			serializer.serialize(this, item);
		}
		amf3_mode -= 1;
    }

    /** {@inheritDoc} */
    public void writeArray(Object[] array, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_ARRAY);
    	if (hasReference(array)) {
    		putInteger(getReferenceId(array) << 1);
    		return;
    	}

    	storeReference(array);
		amf3_mode += 1;
		int count = array.length;
		putInteger(count << 1 | 1);
		putString("");
		for (Object item: array) {
			serializer.serialize(this, item);
		}
		amf3_mode -= 1;
    }

    /** {@inheritDoc} */
    public void writeArray(Object array, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_ARRAY);
    	if (hasReference(array)) {
    		putInteger(getReferenceId(array) << 1);
    		return;
    	}

    	storeReference(array);
		amf3_mode += 1;
		int count = Array.getLength(array);
		putInteger(count << 1 | 1);
		putString("");
		for (int i=0; i<count; i++) {
			serializer.serialize(this, Array.get(array, i));
		}
		amf3_mode -= 1;
    }

    /** {@inheritDoc} */
    public void writeMap(Map<Object, Object> map, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_ARRAY);
    	if (hasReference(map)) {
    		putInteger(getReferenceId(map) << 1);
    		return;
    	}

    	storeReference(map);
		// Search number of starting integer keys
		int count = 0;
		for (int i=0; i<map.size(); i++) {
			try {
				if (!map.containsKey(i))
					break;
			} catch (ClassCastException err) {
				// Map has non-number keys.
				break;
			}
			count++;
		}

		amf3_mode += 1;
		if (count == map.size()) {
			// All integer keys starting from zero: serialize as regular array
			putInteger(count << 1 | 1);
			putString("");
			for (int i=0; i<count; i++) {
				serializer.serialize(this, map.get(i));
			}
			amf3_mode -= 1;
			return;
		}

		putInteger(count << 1 | 1);
		// Serialize key-value pairs first
		for (Map.Entry<Object, Object> entry: map.entrySet()) {
			Object key = entry.getKey();
			if ((key instanceof Number) && !(key instanceof Float) && !(key instanceof Double) &&
					((Number) key).longValue() >= 0 && ((Number) key).longValue() < count) {
				// Entry will be serialized later
				continue;
			}
			putString(key.toString());
			serializer.serialize(this, entry.getValue());
		}
		putString("");
		// Now serialize integer keys starting from zero
		for (int i=0; i<count; i++) {
			serializer.serialize(this, map.get(i));
		}
		amf3_mode -= 1;
    }

    /** {@inheritDoc} */
    public void writeMap(Collection array, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_ARRAY);
    	if (hasReference(array)) {
    		putInteger(getReferenceId(array) << 1);
    		return;
    	}

    	storeReference(array);
    	// TODO: we could optimize this by storing the first integer
    	//       keys after the key-value pairs
		amf3_mode += 1;
		putInteger(1);
		int idx = 0;
    	for (Object item: array) {
    		if (item != null) {
    			putString(String.valueOf(idx));
    			serializer.serialize(this, item);
    		}
    		idx++;
    	}
    	amf3_mode -= 1;
		putString("");
    }

    /** {@inheritDoc} */
	@Override
	protected void writeArbitraryObject(Object object, Serializer serializer) {
        // If we need to serialize class information...
		Class objectClass = object.getClass();
        if (serializer.isOptEnabled(object, SerializerOption.SerializeClassName)) {
			putString(objectClass.getName());
		} else {
			putString("");
		}

    	// Store key/value pairs
    	amf3_mode += 1;
		// Get public field values
		Map<String, Object> values = new HashMap<String, Object>();
        // Iterate thru fields of an object to build "name-value" map from it
        for (Field field : objectClass.getFields()) {
			int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers)) {
				if (log.isDebugEnabled()) {
					log.debug("Skipping " + field.getName() + " because its transient");
				}
				continue;
			}

			Object value;
			try {
                // Get field value
                value = field.get(object);
			} catch (IllegalAccessException err) {
                // Swallow on private and protected properties access exception
                continue;
			}
            // Put field to the map of "name-value" pairs
            values.put(field.getName(), value);
		}

		// Output public values
		Iterator<Map.Entry<String, Object>> it = values.entrySet().iterator();
        // Iterate thru map and write out properties with separators
        while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
            // Write out prop name
			putString(entry.getKey());
            // Write out
            serializer.serialize(this, entry.getValue());
		}
    	amf3_mode -= 1;
        // Write out end of object marker
		putString("");
	}

    /** {@inheritDoc} */
    public void writeObject(Object object, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_OBJECT);
    	if (hasReference(object)) {
    		putInteger(getReferenceId(object) << 1);
    		return;
    	}

    	storeReference(object);
    	if (object instanceof IExternalizable) {
    		// The object knows how to serialize itself.
        	int type = AMF3.TYPE_OBJECT_EXTERNALIZABLE << 2 | 1 << 1 | 1;
        	putInteger(type);
        	putString(object.getClass().getName());
        	amf3_mode += 1;
        	((IExternalizable) object).writeExternal(new DataOutput(this, serializer));
        	amf3_mode -= 1;
        	return;
    	}

    	// We have an inline class that is not a reference.
    	// We store the properties using key/value pairs
    	int type = AMF3.TYPE_OBJECT_VALUE << 2 | 1 << 1 | 1;
    	putInteger(type);

        // Create new map out of bean properties
        BeanMap beanMap = new BeanMap(object);
        // Set of bean attributes
        Set set = beanMap.entrySet();
		if ((set.size() == 0) || (set.size() == 1 && beanMap.containsKey("class"))) {
			// BeanMap is empty or can only access "class" attribute, skip it
			writeArbitraryObject(object, serializer);
			return;
		}

        // Write out either start of object marker for class name or "empty" start of object marker
		Class objectClass = object.getClass();
        if (serializer.isOptEnabled(object, SerializerOption.SerializeClassName)) {
        	// classname
        	putString(objectClass.getName());
		} else {
			putString("");
		}

    	// Store key/value pairs
    	amf3_mode += 1;
		Iterator it = set.iterator();
        while (it.hasNext()) {
			BeanMap.Entry entry = (BeanMap.Entry) it.next();
			String keyName = entry.getKey().toString();
			if ("class".equals(keyName)) {
				continue;
			}

			// Check if the Field corresponding to the getter/setter pair is transient
			try {
				Field field = objectClass.getDeclaredField(keyName);
				int modifiers = field.getModifiers();

				if (Modifier.isTransient(modifiers)) {
					if (log.isDebugEnabled()) {
						log.debug("Skipping " + field.getName() + " because its transient");
					}
					continue;
				}
			} catch (NoSuchFieldException nfe) {
				// Ignore this exception and use the default behaviour
				log.debug("writeObject caught NoSuchFieldException");
			}

			putString(keyName);
			serializer.serialize(this, entry.getValue());
		}
    	amf3_mode -= 1;

    	// End of object marker
    	putString("");
    }

    /** {@inheritDoc} */
	@Override
    public void writeObject(Map<Object, Object> map, Serializer serializer) {
		writeAMF3();
		buf.put(AMF3.TYPE_OBJECT);
    	if (hasReference(map)) {
    		putInteger(getReferenceId(map) << 1);
    		return;
    	}

    	storeReference(map);
    	// We have an inline class that is not a reference.
    	// We store the properties using key/value pairs
    	int type = AMF3.TYPE_OBJECT_VALUE << 2 | 1 << 1 | 1;
    	putInteger(type);

    	// No classname
    	putString("");

    	// Store key/value pairs
    	amf3_mode += 1;
    	for (Map.Entry<Object, Object> entry: map.entrySet()) {
    		putString(entry.getKey().toString());
    		serializer.serialize(this, entry.getValue());
    	}
    	amf3_mode -= 1;

    	// End of object marker
    	putString("");
    }

    /** {@inheritDoc} */
	@Override
    public void writeRecordSet(RecordSet recordset, Serializer serializer) {
    	writeString("Not implemented.");
    }

    /** {@inheritDoc} */
	@Override
	public void writeXML(String xml) {
		writeAMF3();
		buf.put(AMF3.TYPE_XML);
		putString(xml);
	}
}
