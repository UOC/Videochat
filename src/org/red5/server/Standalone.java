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
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ByteBufferAllocator;
*/
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/**
 * Entry point from which the server config file is loaded
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Dominick Accattato (Dominick@gmail.com)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public class Standalone {
 
    /**
     * Initialize Logging
     */
    protected static Log log = LogFactory.getLog(Standalone.class.getName());

	protected static String red5Config = "red5.xml";

	//public static DebugPooledByteBufferAllocator allocator;

	/**
     * Re-throws exception
     * @param e               Exception
     * @throws Throwable      Re-thrown exception
     */
	public static void raiseOriginalException(Throwable e) throws Throwable {
		// Search for root exception
		while (e.getCause() != null) {
			e = e.getCause();
		}
		throw e;
	}

	/**
	 * Main entry point for the Red5 Server usage java Standalone
	 * 
	 * @param args
	 *            String passed in that points to a red5.xml config file
     * @throws Throwable            Base type of all exceptions
	 */
	public static void main(String[] args) throws Throwable {

        //System.setProperty("DEBUG", "true");

		/*
		if (false) {
			allocator = new DebugPooledByteBufferAllocator(true);
			ByteBuffer.setAllocator(allocator);
		}
		*/

		if (args.length == 1) {
			red5Config = args[0];
		}

		long time = System.currentTimeMillis();

		log.info("RED5 Server (http://www.osflash.org/red5)");
		log.info("Loading red5 global context from: " + red5Config);

		// Detect root of Red5 configuration and set as system property
		String root;
		String classpath = System.getProperty("java.class.path");
		File fp = new File(red5Config);
		fp = fp.getCanonicalFile();
		if (!fp.isFile()) {
			// Given file does not exist, search it on the classpath
			String[] paths = classpath.split(System
					.getProperty("path.separator"));
			for (String element : paths) {
				fp = new File(element + '/' + red5Config);
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
		System.setProperty("red5.config_root", root);
		log.info("Setting configuation root to " + root);

		// Setup system properties so they can be evaluated by Jetty
		Properties props = new Properties();

        // Load properties
        props.load(new FileInputStream(root + "/red5.properties"));

        for (Object o : props.keySet()) {
            String key = (String) o;
            if (key != null && !key.equals("")) {
                System.setProperty(key, props.getProperty(key));
            }
        }

        // Store root directory of Red5
		idx = root.lastIndexOf('/');
		root = root.substring(0, idx);
		if (System.getProperty("file.separator").equals("/")) {
			// Workaround for linux systems
			root = '/' + root;
		}

        // Set Red5 root as environment variable
        System.setProperty("red5.root", root);
		log.info("Setting Red5 root to " + root);

		System.setProperty("red5.webapp.root", root+"/webapps/");
		log.info("Setting webapp root to " + root);		
		
		try {
			ContextSingletonBeanFactoryLocator.getInstance(red5Config)
					.useBeanFactory("red5.common");
		} catch (Exception e) {
			// Don't raise wrapped exceptions as their stacktraces may confuse
			// people...
			raiseOriginalException(e);
		}

		long startupIn = System.currentTimeMillis() - time;
		log.info("Startup done in: " + startupIn + " ms");

	}

}
