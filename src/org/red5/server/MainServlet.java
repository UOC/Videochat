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

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.red5.server.jmx.JMXAgent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Entry point from which the server config file is loaded while
 * running within a J2EE application container.
 *
 * This listener should be registered after Log4jConfigListener in web.xml,
 * if the latter is used.
 *
 * @author The Red5 Project (red5@osflash.org)
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class MainServlet extends HttpServlet implements ServletContextListener {

	// Initialize Logging
	public static Logger logger = Logger.getLogger(MainServlet.class.getName());

	protected static String red5Config = "/WEB-INF/applicationContext.xml";

	private static ServletContext servletContext;

	protected ConfigurableWebApplicationContext applicationContext;

	/**
	 * Clearing the in-memory configuration parameters, we will receive
	 * notification that the servlet context is about to be shut down
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Webapp shutdown");

		JMXAgent.shutdown();

		applicationContext.close();
	}

	/**
	 * Main entry point for the Red5 Server as a war
	 */
	//Notification that the web application is ready to process requests
	public void contextInitialized(ServletContextEvent sce) {
		if (null != servletContext) {
			return;
		}
		servletContext = sce.getServletContext();
		String prefix = servletContext.getRealPath("/");

		long time = System.currentTimeMillis();

		logger.info("RED5 Server (http://www.osflash.org/red5)");
		logger.info("Loading red5 global context from: " + red5Config);
		logger.info("Path: " + prefix);

		try {
			// Detect root of Red5 configuration and set as system property
			String root;
			String classpath = System.getProperty("java.class.path");
			File fp = new File(prefix + red5Config);
			fp = fp.getCanonicalFile();
			if (!fp.isFile()) {
				// Given file does not exist, search it on the classpath
				String[] paths = classpath.split(System
						.getProperty("path.separator"));
				for (String element : paths) {
					fp = new File(element + "/" + red5Config);
					fp = fp.getCanonicalFile();
					if (fp.isFile()) {
						break;
					}
				}
			}
			if (!fp.isFile()) {
				throw new Exception("could not find configuration file "
						+ red5Config + " on your classpath " + classpath);
			}

			root = fp.getAbsolutePath();
			root = root.replace('\\', '/');
			int idx = root.lastIndexOf('/');
			root = root.substring(0, idx);
			//update classpath
			System.setProperty("java.class.path", classpath
					+ File.pathSeparatorChar + root + File.pathSeparatorChar
					+ root + "/classes");
			logger.debug("New classpath: "
					+ System.getProperty("java.class.path"));
			//set configuration root
			System.setProperty("red5.config_root", root);
			logger.info("Setting configuation root to " + root);

			// Setup system properties so they can be evaluated by Jetty
			Properties props = new Properties();
			props.load(new FileInputStream(root + "/red5.properties"));
			Iterator it = props.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key != null && !key.equals("")) {
					System.setProperty(key, props.getProperty(key));
				}
			}

			// Store root directory of Red5
			idx = root.lastIndexOf('/');
			root = root.substring(0, idx);
			if (System.getProperty("file.separator").equals("/")) {
				// Workaround for linux systems
				root = "/" + root;
			}
			System.setProperty("red5.root", root);
			logger.info("Setting Red5 root to " + root);

			Class contextClass = org.springframework.web.context.support.XmlWebApplicationContext.class;
			applicationContext = (ConfigurableWebApplicationContext) BeanUtils
					.instantiateClass(contextClass);

			String[] strArray = servletContext.getInitParameter(
					ContextLoader.CONFIG_LOCATION_PARAM).split(", ");
			logger.info("Config location files: " + strArray.length);
			applicationContext.setConfigLocations(strArray);
			applicationContext.setServletContext(servletContext);
			applicationContext.refresh();

			//set web application context as an attribute of the servlet context
			//so that it may be located via Springs WebApplicationContextUtils
			servletContext
					.setAttribute(
							WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
							applicationContext);

			ConfigurableBeanFactory factory = applicationContext
					.getBeanFactory();
			//register default
			// add the context to the parent
			factory.registerSingleton("default.context", applicationContext);

		} catch (Throwable e) {
			logger.error(e);
		}

		long startupIn = System.currentTimeMillis() - time;
		logger.info("Startup done in: " + startupIn + " ms");

	}

}
