package org.red5.server.net.remoting;

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

/**
 * Remoting header to be sent to a server.
 * 
 * Informations about predefined headers can be found at
 * http://www.osflash.org/amf/envelopes/remoting/headers
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (jojo@struktur.de)
 *
 */
public class RemotingHeader {

	/** Name of header specifying string to add to gateway url. */
	public static String APPEND_TO_GATEWAY_URL = "AppendToGatewayUrl";

	/** Name of header specifying new gateway url to use. */
	public static String REPLACE_GATEWAY_URL = "ReplaceGatewayUrl";

	/** Name of header specifying new header to send. */
	public static String PERSISTENT_HEADER = "RequestPersistentHeader";

	/** Name of header containing authentication data. */
	public static String CREDENTIALS = "Credentials";

	/** Name of header to request debug informations from the server. */
	public static String DEBUG_SERVER = "amf_server_debug";

	/**
	 * The name of the header.
	 */
	protected String name;

	/**
	 * Is this header required?
	 */
	protected boolean required;

	/**
	 * The actual data of the header.
	 */
	protected Object data;

	/**
	 * Create a new header to be sent through remoting.
	 * 
	 * @param name            Header name
	 * @param required        Header required?
	 * @param data            Header data
	 */
	public RemotingHeader(String name, boolean required, Object data) {
		this.name = name;
		this.required = required;
		this.data = data;
	}
}
