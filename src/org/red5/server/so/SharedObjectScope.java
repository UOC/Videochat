package org.red5.server.so;

/*
 * File RED5 modified by UOC
 *	
 * dcarrascogi@uoc.edu - Daniel Carrasco	 
 * marcgener@uoc.edu - Marc Gener
 *
 *
 */

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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.BaseConnection;
import org.red5.server.BasicScope;
import org.red5.server.api.IAttributeStore;
import org.red5.server.api.IContext;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventListener;
import org.red5.server.api.persistence.IPersistenceStore;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.red5.server.api.so.ISharedObjectSecurity;
import org.red5.server.api.so.ISharedObjectSecurityService;
import org.red5.server.api.statistics.ISharedObjectStatistics;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.service.ServiceUtils;

import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.utils.Utils;

/**
 * Special scope for shared objects 
 */
public class SharedObjectScope extends BasicScope implements ISharedObject, StatusCodes {
    /**
     * Logger
     */
	private Log log = LogFactory.getLog(SharedObjectScope.class.getName());
    /**
     * Lock to synchronize shared object updates from multiple threads
     */
	private final ReentrantLock lock = new ReentrantLock();
    /**
     * Server-side listeners
     */
	private Set<ISharedObjectListener> serverListeners = new CopyOnWriteArraySet<ISharedObjectListener>();
    /**
     * Event handlers
     */
	private Map<String, Object> handlers = new ConcurrentHashMap<String, Object>();
	/**
	 * Security handlers
	 */
	private Set<ISharedObjectSecurity> securityHandlers = new CopyOnWriteArraySet<ISharedObjectSecurity>();
    /**
     * Scoped shared object
     */
	protected SharedObject so;
	
	
	

    /**
     * Creates shared object with given parent scope, name, persistence flag state and store object
     * @param parent                    Parent scope
     * @param name                      Name
     * @param persistent                Persistence flag state
     * @param store                     Persistence store
     */
	public SharedObjectScope(IScope parent, String name, boolean persistent,
			IPersistenceStore store) {
		super(parent, TYPE, name, persistent);

		// Create shared object wrapper around the attributes
		String path = parent.getContextPath();
		if ("".equals(path) || path.charAt(0) != '/') {
			path = '/' + path;
		}
        // Load SO
        so = (SharedObject) store.load(TYPE + path + '/' + name);
		// Create if it doesn't exist
        if (so == null) {
			so = new SharedObject(attributes, name, path, persistent, store);
            // Save
			store.save(so);
		} else {
            // Rename and set path
            so.setName(name);
			so.setPath(path);
		}
        //UOC 
        String id_meeting = Utils.getMeetingId(Red5.getConnectionLocal());
        SingletonMeetings.getSingletonMeetings().setSo(id_meeting,so); 
        //
	}

	/** {@inheritDoc} */
	public void registerSharedObjectSecurity(ISharedObjectSecurity handler) {
		securityHandlers.add(handler);
	}
	
	/** {@inheritDoc} */
	public void unregisterSharedObjectSecurity(ISharedObjectSecurity handler) {
		securityHandlers.remove(handler);
	}
	
	/** {@inheritDoc} */
	public Set<ISharedObjectSecurity> getSharedObjectSecurity() {
		return Collections.unmodifiableSet(securityHandlers);
	}

	/** {@inheritDoc} */
    @Override
	public IPersistenceStore getStore() {
		return so.getStore();
	}

	/** {@inheritDoc} */
    @Override
	public String getName() {
		return so.getName();
	}

	/** {@inheritDoc} */
    @Override
	public void setName(String name) {
		so.setName(name);
	}

	/** {@inheritDoc} */
    @Override
	public String getPath() {
		return so.getPath();
	}

	/** {@inheritDoc} */
    @Override
	public void setPath(String path) {
		so.setPath(path);
	}

	/** {@inheritDoc} */
    @Override
	public String getType() {
		return so.getType();
	}

	/** {@inheritDoc} */
    public boolean isPersistentObject() {
		return so.isPersistentObject();
	}

	/** {@inheritDoc} */
    public void beginUpdate() {
        // Make sure only one thread can update the SO
		lock.lock();
		so.beginUpdate();
	}

	/** {@inheritDoc} */
    public void beginUpdate(IEventListener listener) {
        // Make sure only one thread can update the SO
		lock.lock();
        so.beginUpdate(listener);
	}

	/** {@inheritDoc} */
    public void endUpdate() {
		// End update of SO
        so.endUpdate();
		lock.unlock();
	}
 
	/** {@inheritDoc} */ 
    public int getVersion() { 
		return so.getVersion();
	}

	/** {@inheritDoc} */
    public void sendMessage(String handler, List arguments) {
		beginUpdate();
		try {
			
			so.sendMessage(handler, arguments);
	
		} finally {
			endUpdate(); 
		}

		// Invoke method on registered handler
		String serviceName, serviceMethod;
        // Find out last dot position
        int dotPos = handler.lastIndexOf('.');
        // If any, split service name and service method name
        if (dotPos != -1) {
			serviceName = handler.substring(0, dotPos);
			serviceMethod = handler.substring(dotPos + 1);
		} else {
            // Otherwise only service method name is available
            serviceName = "";
			serviceMethod = handler;
		}

        // Get previously registred handler for service
        Object soHandler = getServiceHandler(serviceName);
		if (soHandler == null && hasParent()) {
			// No custom handler, check for service defined in the scope's
			// context
			IContext context = getParent().getContext();
			try {
				// The bean must have a name of
				// "<SharedObjectName>.<DottedServiceName>.soservice"
				soHandler = context.getBean(so.getName() + '.' + serviceName
						+ ".soservice");
			} catch (Exception err) {
				log.debug("No such bean");
			}
		}

        // Once handler is found, find matching method
        if (soHandler != null) {
            // With exact params...
            Object[] methodResult = ServiceUtils.findMethodWithExactParameters(
					soHandler, serviceMethod, arguments);
            // Or at least with suitable list params
            if (methodResult.length == 0 || methodResult[0] == null) {
				methodResult = ServiceUtils.findMethodWithListParameters(
						soHandler, serviceMethod, arguments);
			}

            // If method is found...
            if (methodResult.length > 0 && methodResult[0] != null) {
				Method method = (Method) methodResult[0];
				Object[] params = (Object[]) methodResult[1];
                //...try to invoke it and handle exceptions
                try {
					method.invoke(soHandler, params);
				} catch (Exception err) {
					log.error("Error while invoking method " + serviceMethod
							+ " on shared object handler " + handler, err);
				}
			}
		}

		// Notify server listeners
    	for (ISharedObjectListener listener : serverListeners) {
    		listener.onSharedObjectSend(this, handler, arguments);
    	}
    }

	/** {@inheritDoc} */
    @Override
	public boolean removeAttribute(String name) {
    	boolean success;
        // Begin update of shared object
        beginUpdate();
    	try {
    		// Try to remove attribute
    		success = so.removeAttribute(name);
    	} finally {
    		// End update of SO
    		endUpdate();
    	}

        // Notify listeners on success and return true
        if (success) {
			for (ISharedObjectListener listener : serverListeners) {
				listener.onSharedObjectDelete(this, name);
			}
        }
		return success;
	}

	/** {@inheritDoc} */
    @Override
	public void removeAttributes() {
        // Begin update
        beginUpdate();
        try {
        	// Remove all attributes
        	so.removeAttributes();
        } finally {
        	// End update
        	endUpdate();
        }

        // Notify listeners on atributes clear
		for (ISharedObjectListener listener : serverListeners) {
			listener.onSharedObjectClear(this);
		}
    }

	/** {@inheritDoc} */
    @Override
	public void addEventListener(IEventListener listener) {
		super.addEventListener(listener);
		so.register(listener);

		for (ISharedObjectListener soListener : serverListeners) {
			soListener.onSharedObjectConnect(this);
		}
    }

	/** {@inheritDoc} */
    @Override
	public void removeEventListener(IEventListener listener) {
		so.unregister(listener);
		super.removeEventListener(listener);
		if (!so.isPersistentObject() && (so.getListeners() == null || so.getListeners().isEmpty())) {
			getParent().removeChildScope(this);
		}

		for (ISharedObjectListener soListener : serverListeners) {
			soListener.onSharedObjectDisconnect(this);
		}
    }

	/** {@inheritDoc} */
    @Override
	public boolean hasAttribute(String name) {
		return so.hasAttribute(name);
	}

	/** {@inheritDoc} */
    @Override
	public Object getAttribute(String name) {
		return so.getAttribute(name);
	}

	/** {@inheritDoc} */
    @Override
	public Map<String, Object> getAttributes() {
		return so.getAttributes();
	}

	/** {@inheritDoc} */
    @Override
	public Set<String> getAttributeNames() {
		return so.getAttributeNames();
	}

	/** {@inheritDoc} */
    public Map<String, Object> getData() {
		return so.getData();
	}

    /**
     * Return security handlers for this shared object or <code>null</code> if none are found.
     * 
     * @return
     */
    private Set<ISharedObjectSecurity> getSecurityHandlers() {
    	ISharedObjectSecurityService security = (ISharedObjectSecurityService) ScopeUtils.getScopeService(getParent(), ISharedObjectSecurityService.class);
    	if (security == null)
    		return null;
    	
    	return security.getSharedObjectSecurity();
    }
    
    /**
     * Call handlers and check if connection to the existing SO is allowed.
     * 
     * @return
     */
    protected boolean isConnectionAllowed() {
    	// Check internal handlers first
		for (ISharedObjectSecurity handler: securityHandlers) {
			if (!handler.isConnectionAllowed(this)) {
				return false;
			}
		}
    	
    	// Check global SO handlers next
    	final Set<ISharedObjectSecurity> handlers = getSecurityHandlers();
    	if (handlers == null) {
    		return true;
    	}
    	
    	for (ISharedObjectSecurity handler: handlers) {
    		if (!handler.isConnectionAllowed(this)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Call handlers and check if writing to the SO is allowed.
     * 
     * @param key
     * @param value
     * @return
     */
    protected boolean isWriteAllowed(String key, Object value) {
    	// Check internal handlers first
    	for (ISharedObjectSecurity handler: securityHandlers) {
    		if (!handler.isWriteAllowed(this, key, value)) {
    			return false;
    		}
    	}
    	
    	// Check global SO handlers next
    	final Set<ISharedObjectSecurity> handlers = getSecurityHandlers();
    	if (handlers == null) {
    		return true;
    	}
    	
    	for (ISharedObjectSecurity handler: handlers) {
    		if (!handler.isWriteAllowed(this, key, value)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Call handlers and check if deleting a property from the SO is allowed.
     * 
     * @param key
     * @return
     */
    protected boolean isDeleteAllowed(String key) {
    	// Check internal handlers first
    	for (ISharedObjectSecurity handler: securityHandlers) {
    		if (!handler.isDeleteAllowed(this, key)) {
    			return false;
    		}
    	}
    	
    	// Check global SO handlers next
    	final Set<ISharedObjectSecurity> handlers = getSecurityHandlers();
    	if (handlers == null) {
    		return true;
    	}
    	
    	for (ISharedObjectSecurity handler: handlers) {
    		if (!handler.isDeleteAllowed(this, key)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Call handlers and check if sending a message to the clients connected to the
     * SO is allowed.
     * 
     * @param message
     * @param arguments
     * @return
     */
    protected boolean isSendAllowed(String message, List arguments) {
    	// Check internal handlers first
    	for (ISharedObjectSecurity handler: securityHandlers) {
    		if (!handler.isSendAllowed(this, message, arguments)) {
    			return false;
    		}
    	}
    	
    	// Check global SO handlers next
    	final Set<ISharedObjectSecurity> handlers = getSecurityHandlers();
    	if (handlers == null) {
    		return true;
    	}
    	
    	for (ISharedObjectSecurity handler: handlers) {
    		if (!handler.isSendAllowed(this, message, arguments)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
	/** {@inheritDoc} */
    @Override
	public void dispatchEvent(IEvent e) {
		if (e.getType() != IEvent.Type.SHARED_OBJECT
				|| !(e instanceof ISharedObjectMessage)) {
			// Don't know how to handle this event.
			super.dispatchEvent(e);
			return;
		}

		ISharedObjectMessage msg = (ISharedObjectMessage) e;
		if (msg.hasSource()) {
			beginUpdate(msg.getSource());
		} else {
			beginUpdate();
		}
		try {
			for (ISharedObjectEvent event : msg.getEvents()) {
				switch (event.getType()) {
					case SERVER_CONNECT:
						if (!isConnectionAllowed()) {
							so.returnError(SO_NO_READ_ACCESS);
						} else if (msg.hasSource()) {
							IEventListener source = msg.getSource();
							if (source instanceof BaseConnection) {
								((BaseConnection) source).registerBasicScope(this);
							} else {
								addEventListener(source);
							}
						}
						break;
					case SERVER_DISCONNECT:
						if (msg.hasSource()) {
							IEventListener source = msg.getSource();
							if (source instanceof BaseConnection) {
								((BaseConnection) source)
										.unregisterBasicScope(this);
							} else {
								removeEventListener(source);
							}
						}
						break;
					case SERVER_SET_ATTRIBUTE:
						final String key = event.getKey();
						final Object value = event.getValue();
						if (!isWriteAllowed(key, value)) {
							so.returnAttributeValue(key);
							so.returnError(SO_NO_WRITE_ACCESS);
						} else {
							setAttribute(key, value);
						}
						break;
					case SERVER_DELETE_ATTRIBUTE:
						final String property = event.getKey();
						if (!isDeleteAllowed(property)) {
							so.returnAttributeValue(property);
							so.returnError(SO_NO_WRITE_ACCESS);
						} else {
							removeAttribute(property);
						}
						break;
					case SERVER_SEND_MESSAGE:
						final String message = event.getKey();
						final List arguments = (List) event.getValue();
						
						// Ignore request silently if not allowed
						if (isSendAllowed(message, arguments)) {
							sendMessage(message, arguments);
						}
					
						break;
					default:
						log.warn("Unknown SO event: " + event.getType());
				}
			}
		} finally {
			endUpdate();
		}
	}

	/** {@inheritDoc} */
    @Override
	public boolean setAttribute(String name, Object value) {
    	boolean success;
		beginUpdate();
		try {
			success = so.setAttribute(name, value);
		} finally {
			endUpdate();
		}

		if (success) {
			for (ISharedObjectListener listener : serverListeners) {
				listener.onSharedObjectUpdate(this, name, value);
			}
        }
		return success;
	}

	/** {@inheritDoc} */
    @Override
	public void setAttributes(IAttributeStore values) {
		beginUpdate();
		try {
			so.setAttributes(values);
		} finally {
			endUpdate();
		}

		for (ISharedObjectListener listener : serverListeners) {
			listener.onSharedObjectUpdate(this, values);
		}
    }

	/** {@inheritDoc} */
    @Override
	public void setAttributes(Map<String, Object> values) {
		beginUpdate();
		try {
			so.setAttributes(values);
		} finally {
			endUpdate();
		}

		for (ISharedObjectListener listener : serverListeners) {
			listener.onSharedObjectUpdate(this, values);
		}
    }

	/** {@inheritDoc} */
    @Override
	public String toString() {
		return "Shared Object: " + getName();
	}

	/** {@inheritDoc} */
    public void addSharedObjectListener(
			ISharedObjectListener listener) {
		serverListeners.add(listener);
	}

	/** {@inheritDoc} */
    public void removeSharedObjectListener(
			ISharedObjectListener listener) {
		serverListeners.remove(listener);
	}

	/** {@inheritDoc} */
    public void registerServiceHandler(Object handler) {
		registerServiceHandler("", handler);
	}

	/** {@inheritDoc} */
    public void registerServiceHandler(String name, Object handler) {
		if (name == null) {
			name = "";
		}
		handlers.put(name, handler);
	}

	public void unregisterServiceHandler() {
		unregisterServiceHandler("");
	}

	/** {@inheritDoc} */
    public void unregisterServiceHandler(String name) {
		if (name == null) {
			name = "";
		}
		handlers.remove(name);
	}

	/** {@inheritDoc} */
    public Object getServiceHandler(String name) {
		if (name == null) {
			name = "";
		}
		return handlers.get(name);
	}

	/** {@inheritDoc} */
    public Set<String> getServiceHandlerNames() {
		return Collections.unmodifiableSet(handlers.keySet());
	}

	/**
	 * Locks the shared object instance. Prevents any changes to this object by
	 * clients until the SharedObject.unlock() method is called.
	 */
	public void lock() {
		lock.lock();
	}

	/**
	 * Unlocks a shared object instance that was locked with
	 * SharedObject.lock().
	 */
	public void unlock() {
		lock.unlock();
	}

	/**
	 * Returns the locked state of this SharedObject.
	 * 
	 * @return true if in a locked state; false otherwise
	 */
	public boolean isLocked() {
		return lock.isLocked();
	}

	/** {@inheritDoc} */
    public boolean clear() {
		return so.clear();
	}

	/** {@inheritDoc} */
    public void close() {
		so.close();
		so = null;
	}
    
	/** {@inheritDoc} */
    public void acquire() {
		so.acquire();
	}

	/** {@inheritDoc} */
    public boolean isAcquired() {
		return so.isAcquired();
	}

	/** {@inheritDoc} */
    public void release() {
		so.release();
	}

	/** {@inheritDoc} */
    public ISharedObjectStatistics getStatistics() {
    	return so;
    }
}