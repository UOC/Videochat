/*
 * Copyright (c) 2007 Universitat Oberta de Catalunya
 * 
 * This file is part of Campus Virtual de Programari Lliure (CVPLl) - OSID Dummy.  
 *
 * CVPLl is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 2 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details, currently published 
 * at http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in 
 * the root folder of this distribution.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  
 */
 
/**
 * @author Jordi Massaguer / openTrends / jordi.massaguer@opentrends.net
 * @author Sergi Kirchner / openTrends / skirchner@opentrends.net
 * @author Joaquim Raurell / openTrends / joaquim.raurell@opentrends.net
 * @author Juan Garcia Vila <joan.garcia@tecsidel.es> <sunfriend@gmail.com>
 * @author Marc Gener <marcgener@uoc.edu>
 */
 

package org.uoc.podcast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ConfigurationProperties {
	
	public static String CONFIG_PATH = "/org/uoc/podcast/";
	public static String CONFIG_FILE = "application.properties";

	private static ConfigurationProperties m_ConfigurationProperties;
	private Properties props;
	
    public static ConfigurationProperties getInstance() { 
		if( m_ConfigurationProperties == null) {
			m_ConfigurationProperties = new ConfigurationProperties( );
		}
		return m_ConfigurationProperties; 
	}
    
    private ConfigurationProperties() {
		props = new Properties( );
		
		try {
			InputStream is=this.getClass().getResourceAsStream( CONFIG_PATH + CONFIG_FILE );
			props.load( is );
		} catch( FileNotFoundException e) {
			e.printStackTrace();
		} catch( IOException e) {
			e.printStackTrace();
		}
	}
    
    public String getProperty(String key) {
    	return props.getProperty(key);
    }
    
 }
