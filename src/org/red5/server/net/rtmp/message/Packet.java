package org.red5.server.net.rtmp.message;

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

import org.apache.mina.common.ByteBuffer;
import org.red5.server.net.rtmp.event.IRTMPEvent;

/**
 * RTMP packet. Consists of packet header, data and event context.
 */
public class Packet {
    /**
     * Header
     */
	protected Header header;
    /**
     * RTMP event
     */
	protected IRTMPEvent message;
    /**
     * Packet data
     */
	protected ByteBuffer data;

    /**
     * Create packet with given header
     * @param header       Packet header
     */
    public Packet(Header header) {
		this.header = header;
		data = ByteBuffer.allocate(header.getSize()
				+ (header.getTimer() == 0xffffff ? 4 : 0));
	}

    /**
     * Create packet with given header and event context
     * @param header     RTMP header
     * @param event      RTMP message
     */
    public Packet(Header header, IRTMPEvent event) {
		this.header = header;
		this.message = event;
	}

	/**
     * Getter for header
     *
     * @return  Packet header
     */
    public Header getHeader() {
		return header;
	}

	/**
     * Setter for event context
     *
     * @param message  RTMP event context
     */
    public void setMessage(IRTMPEvent message) {
		this.message = message;
	}

	/**
     * Getter for event context
     *
     * @return RTMP event context
     */
    public IRTMPEvent getMessage() {
		return message;
	}

	/**
     * Setter for data
     *
     * @param data Packet data
     */
    public void setData(ByteBuffer data) {
		this.data = data;
	}

	/**
     * Getter for data
     *
     * @return Packet data
     */
    public ByteBuffer getData() {
		return data;
	}

}