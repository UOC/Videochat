package org.red5.server.net.rtmp;

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

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.red5.server.api.IScope;
import org.red5.server.jmx.JMXAgent;
import org.red5.server.jmx.JMXFactory;
import org.red5.server.net.rtmp.message.Packet;

public class RTMPMinaConnection extends RTMPConnection implements
		RTMPMinaConnectionMBean {
	/**
	 * Logger
	 */
	protected static Log log = LogFactory.getLog(RTMPMinaConnection.class
			.getName());

	/**
	 * MINA I/O session, connection between two endpoints
	 */
	private IoSession ioSession;

	/** Constructs a new RTMPMinaConnection. */
	public RTMPMinaConnection() {
		super(PERSISTENT);
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		super.close();
		if (ioSession != null) {
			ioSession.close();
		}
		// deregister with jmx
		JMXAgent.unregisterMBean(oName);
	}

	@Override
	public boolean connect(IScope newScope, Object[] params) {
		boolean success = super.connect(newScope, params);
		if (!success) {
			return false;
		}
		try {
			String hostStr = host;
			int port = 1935;
			if (host != null && host.indexOf(":") > -1) {
				int idx = host.indexOf(":");
				hostStr = host.substring(0, idx);
				port = Integer.parseInt(host.substring(idx + 1));
			}
			//create a new mbean for this instance
			oName = JMXFactory.createObjectName("type", "RTMPMinaConnection",
					"connectionType", type, "host", hostStr, "port", port + "",
					"clientId", client.getId());
			JMXAgent.registerMBean(this, this.getClass().getName(),
					RTMPMinaConnectionMBean.class, oName);
		} catch (Exception e) {
			log.warn("Exception registering mbean", e);
		}
		return success;
	}

	/**
	 * Return MINA I/O session
	 *
	 * @return MINA O/I session, connection between two endpoints
	 */
	public IoSession getIoSession() {
		return ioSession;
	}

	/** {@inheritDoc} */
	@Override
	public long getPendingMessages() {
		if (ioSession == null)
			return 0;
		
		return ioSession.getScheduledWriteRequests();
	}

	/** {@inheritDoc} */
	@Override
	public long getReadBytes() {
		if (ioSession == null)
			return 0;
		
		return ioSession.getReadBytes();
	}

	/** {@inheritDoc} */
	@Override
	public long getWrittenBytes() {
		if (ioSession == null)
			return 0;
		
		return ioSession.getWrittenBytes();
	}

	public void invokeMethod(String method) {
		invoke(method);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isConnected() {
		return super.isConnected() && (ioSession != null) && ioSession.isConnected();
	}

	/** {@inheritDoc} */
	@Override
	protected void onInactive() {
		this.close();
	}

	/** {@inheritDoc} */
	@Override
	public void rawWrite(ByteBuffer out) {
		if (ioSession != null) {
			ioSession.write(out);
		}
	}

	/**
	 * Setter for MINA I/O session (connection)
	 *
	 * @param protocolSession  Protocol session
	 */
	void setIoSession(IoSession protocolSession) {
		SocketAddress remote = protocolSession.getRemoteAddress();
		if (remote instanceof InetSocketAddress) {
			remoteAddress = ((InetSocketAddress) remote).getAddress()
					.getHostAddress();
			remotePort = ((InetSocketAddress) remote).getPort();
		} else {
			remoteAddress = remote.toString();
			remotePort = -1;
		}
		remoteAddresses = new ArrayList<String>();
		remoteAddresses.add(remoteAddress);
		remoteAddresses = Collections.unmodifiableList(remoteAddresses);
		this.ioSession = protocolSession;
	}

	/** {@inheritDoc} */
	@Override
	public void write(Packet out) {
		if (ioSession != null) {
			writingMessage(out);
			ioSession.write(out);
		}
	}
}
