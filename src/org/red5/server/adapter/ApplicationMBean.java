package org.red5.server.adapter;

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

import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;

/**
 * JMX mbean for Application.
 *
 * @author Paul Gregoire (mondain@gmail.com)
 */
public interface ApplicationMBean {

	public boolean appStart(IScope app);

	public boolean appConnect(IConnection conn, Object[] params);

	public boolean appJoin(IClient client, IScope app);

	public void appDisconnect(IConnection conn);

	public void appLeave(IClient client, IScope app);

	public void appStop(IScope app);

	public boolean roomStart(IScope room);

	public boolean roomConnect(IConnection conn, Object[] params);

	public boolean roomJoin(IClient client, IScope room);

	public void roomDisconnect(IConnection conn);

	public void roomLeave(IClient client, IScope room);

	public void roomStop(IScope room);

}
