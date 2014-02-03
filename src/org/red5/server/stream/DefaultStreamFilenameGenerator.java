
package org.red5.server.stream;
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

import java.io.File;

import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.stream.IStreamFilenameGenerator;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.utils.Utils;


/**
 * Default filename generator for streams. The files will be stored in a
 * directory "streams" in the application folder. Option for changing directory
 * streams are saved to is investigated as of 0.6RC1.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (bauch@struktur.de)
 */
public class DefaultStreamFilenameGenerator implements IStreamFilenameGenerator {

    /**
     * Generate stream directory based on relative scope path. The base directory is
     * <code>streams</code>, e.g. a scope <code>/application/one/two</code> will
     * generate a directory <code>/streams/one/two</code> inside the application.
     * 
     * @param scope            Scope
     * @return                 Directory based on relative scope path
     */
    private String getStreamDirectory(IScope scope) {
		final StringBuilder result = new StringBuilder();
		final IScope app = ScopeUtils.findApplication(scope);
		while (scope != null && scope != app) {
			result.insert(0, scope.getName() + "/");
			scope = scope.getParent();  
		}
		
		//UOC
		IScope parent=scope;
    	for(int i=1; i<scope.getDepth()-1; i++){
    		parent=parent.getParent();
    	}
    	
		if(parent.getName().equals("fitcDemo")){
			String id_meeting = Utils.getMeetingId(Red5.getConnectionLocal());
			String path = SingletonMeetings.getSingletonMeetings().getRecordPath(id_meeting);
			return path;
		}
		else{
			return "streams/" + result.toString();
		}
		//
	}

	/** {@inheritDoc} */
    public String generateFilename(IScope scope, String name, GenerationType type) {
		return generateFilename(scope, name, null, type);
	}

	/** {@inheritDoc} */
    public String generateFilename(IScope scope, String name, String extension, GenerationType type) {
    	//UOC
    	String result=null;
    	
    	IScope parent=scope;
    	for(int i=1; i<scope.getDepth(); i++){
    		parent=parent.getParent();
    	}
    	
 		if(parent.getName().equals("fitcDemo")){
    		result = getStreamDirectory(scope) + "/" + name;
    	}
    	else{
    		result = getStreamDirectory(scope) + name;
    		if (extension != null && !extension.equals("")) {
    			result += extension;
    		}
    	}
        //
    
		
		return result+File.separator;
	}

}