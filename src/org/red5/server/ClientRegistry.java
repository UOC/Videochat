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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.red5.server.api.IClient;
import org.red5.server.api.IClientRegistry;
import org.red5.server.exception.ClientNotFoundException;
import org.red5.server.exception.ClientRejectedException;
import org.red5.server.jmx.JMXAgent;

/**
 * Registry for clients. Associates client with it's id so it's possible to get client by id
 * from whenever we need
 */
public class ClientRegistry implements IClientRegistry, ClientRegistryMBean {
	/**
	 * Clients map
	 */
	private Map<String, IClient> clients = new ConcurrentHashMap<String, IClient>();

	/**
	 *  Next client id
	 */
	private AtomicInteger nextId = new AtomicInteger();

	{
		JMXAgent.registerMBean(this, this.getClass().getName(),
				ClientRegistryMBean.class);
	}

	/**
	 * Add client to registry
	 * @param client           Client to add
	 */
	protected void addClient(IClient client) {
		clients.put(client.getId(), client);
	}

	public Client getClient(String id) throws ClientNotFoundException {
		if (!hasClient(id)) {
			throw new ClientNotFoundException(id);
		}
		return (Client) clients.get(id);
	}

	/**
	 * Returns a list of Clients.
	 */
	public ClientList<Client> getClientList() {
		ClientList<Client> list = new ClientList<Client>();
		for (IClient c : clients.values()) {
			list.add((Client) c);
		}
		return list;
	}

	/**
	 * Check if client registry contains clients.
	 * 
	 * @return             <code>True</code> if clients exist, otherwise <code>False</code>
	 */
	protected boolean hasClients() {
		return !clients.isEmpty();
	}
	
	/**
	 * Return collection of clients
	 * @return             Collection of clients
	 */
	protected Collection<IClient> getClients() {
		if (!hasClients()) {
			// Avoid creating new Collection object if no clients exist.
			return Collections.EMPTY_SET;
		}
		
		return Collections.unmodifiableCollection(clients.values());
	}

	/**
	 * Check whether registry has client with given id
	 *
	 * @param id         Client id
	 * @return           true if client with given id was register with this registry, false otherwise
	 */
	public boolean hasClient(String id) {
		if (id == null) {
			// null ids are not supported
			return false;
		}
		return clients.containsKey(id);
	}

	/**
	 * Return client by id
	 *
	 * @param id          Client id
	 * @return            Client object associated with given id
	 * @throws ClientNotFoundException
	 */
	public IClient lookupClient(String id) throws ClientNotFoundException {
		if (!hasClient(id)) {
			throw new ClientNotFoundException(id);
		}

		return clients.get(id);
	}

	/**
	 * Return client from next id with given params
	 *
	 * @param params                         Client params
	 * @return                               Client object
	 * @throws ClientNotFoundException
	 * @throws ClientRejectedException
	 */
	public IClient newClient(Object[] params) throws ClientNotFoundException,
			ClientRejectedException {
		IClient client = new Client(nextId(), this);
		addClient(client);
		return client;
	}

	/**
	 * Return next client id
	 * @return         Next client id
	 */
	public String nextId() {
		return "" + nextId.getAndIncrement();
	}

	/**
	 * Return previous client id
	 * @return        Previous client id
	 */
	public String previousId() {
		return "" + nextId.get();
	}

	/**
	 * Removes client from registry
	 * @param client           Client to remove
	 */
	protected void removeClient(IClient client) {
		clients.remove(client.getId());
	}

}
