package org.red5.server;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IBWControllable;
import org.red5.server.api.IBandwidthConfigure;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.jmx.JMXAgent;
import org.red5.server.jmx.JMXFactory;
import org.uoc.red5.videoconference.SingletonMeetings;

/**
 * Client is an abstraction representing user connected to Red5 application.
 * Clients are tied to connections and registred in ClientRegistry
 */
public class Client extends AttributeStore implements IClient, ClientMBean {
	/**
	 *  Logger
	 */
	protected static Log log = LogFactory.getLog(Client.class.getName());

	/**
	 *  Scopes this client connected to
	 */
	protected Map<IConnection, IScope> connToScope = new ConcurrentHashMap<IConnection, IScope>();

	/**
	 *  Creation time as Timestamp
	 */
	protected long creationTime;

	/**
	 *  Clients identificator
	 */
	protected String id;

	/**
	 * MBean object name used for de/registration purposes.
	 */
	private ObjectName oName;

	/**
	 *  Client registry where Client is registred
	 */
	protected ClientRegistry registry;

	public Client() {
		//here for jmx reference only
	}

	/**
	 * Creates client, sets creation time and registers it in ClientRegistry
	 *
	 * @param id             Client id
	 * @param registry       ClientRegistry
	 */
	public Client(String id, ClientRegistry registry) {
		this.id = id;
		this.registry = registry;
		this.creationTime = System.currentTimeMillis();
		//create a new mbean for this instance
		oName = JMXFactory.createObjectName("type", "Client", "id", id);
		JMXAgent.registerMBean(this, this.getClass().getName(),
				ClientMBean.class, oName);
		//JMXFactory.createMBean("org.red5.server.Client", "id=" + id);
	}

	/**
	 *  Disconnects client from Red5 application
	 */
	public void disconnect() {
		if (log.isDebugEnabled()) {
			log.debug("Disconnect, closing " + getConnections().size()
					+ " connections");
		}

		// Close all associated connections
		for (IConnection con : getConnections()) {
			con.close();
		}
	}

	/**
	 * Check clients equality by id
	 *
	 * @param obj        Object to check against
	 * @return           true if clients ids are the same, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Client)) {
			return false;
		}
		return ((Client) obj).getId().equals(id);
	}

	/**
	 * Return bandwidth configuration context, that is, broadcasting bandwidth and quality settings for this client
	 * @return      Bandwidth configuration context
	 */
	public IBandwidthConfigure getBandwidthConfigure() {
		// TODO implement it
		return null;
	}

	/**
	 * Return set of connections for this client
	 *
	 * @return           Set of connections
	 */
	public Set<IConnection> getConnections() {
		return connToScope.keySet();
	}

	/**
	 * Return client connections to given scope
	 *
	 * @param scope           Scope
	 * @return                Set of connections for that scope
	 */
	public Set<IConnection> getConnections(IScope scope) {
		if (scope == null) {
			return getConnections();
		}

		Set<IConnection> result = new HashSet<IConnection>();
		for (Entry<IConnection, IScope> entry : connToScope.entrySet()) {
			if (scope.equals(entry.getValue())) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	/**
	 *
	 * @return
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Parent flow controllable object, that is, parent object that is used to determine client broadcast bandwidth
	 * settings. In case of base Client class parent is host.
	 *
	 * @return     IFlowControllable instance
	 */
	public IBWControllable getParentBWControllable() {
		// parent is host
		return null;
	}

	/**
	 *
	 * @return
	 */
	public Collection<IScope> getScopes() {
		return connToScope.values();
	}

	/**
	 * if overriding equals then also do hashCode
	 * @return
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * Iterate through the scopes and their attributes.
	 * Used by JMX
	 *
	 * @return list of scope attributes
	 */
	public List<String> iterateScopeNameList() {
		log.debug("iterateScopeNameList called");
		List<String> scopeNames = new ArrayList<String>();
		log.debug("Scopes: " + connToScope.values().size());
		for (IScope scope : connToScope.values()) {
			log.debug("Client scope: " + scope);
			for (Map.Entry entry : scope.getAttributes().entrySet()) {
				log.debug("Client scope attr: " + entry.getKey() + " = "
						+ entry.getValue());
			}
		}
		return scopeNames;
	}

	/**
	 * Associate connection with client
	 * @param conn         Connection object
	 */
	protected void register(IConnection conn) {
		log.debug("Registering connection for this client");
		
		connToScope.put(conn, conn.getScope());
	}

	/**
	 * Set new bandwidth configuration context
	 * @param config             Bandwidth configuration context
	 */
	public void setBandwidthConfigure(IBandwidthConfigure config) {
		// TODO implement it
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Client: " + id;
	}

	/**
	 * Removes client-connection association for given connection
	 * @param conn         Connection object
	 */
	protected void unregister(IConnection conn) {
		// Remove connection from connected scopes list
		
		connToScope.remove(conn);
		// If client is not connected to any scope any longer then remove
		if (connToScope.isEmpty()) {
			// This client is not connected to any scopes, remove from registry.
			registry.removeClient(this);
			// deregister with jmx
			JMXAgent.unregisterMBean(oName);
		}
	}
}
