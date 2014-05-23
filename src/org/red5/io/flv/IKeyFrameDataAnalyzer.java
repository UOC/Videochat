package org.red5.io.flv;

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

import java.io.Serializable;

/**
 * Analyzes key frame data
 */
public interface IKeyFrameDataAnalyzer {

    /**
     * Analyze and return keyframe metadata
     * @return           Metadata object
     */
    public KeyFrameMeta analyzeKeyFrames();

    /**
     * Keyframe metadata
     */
    public static class KeyFrameMeta implements Serializable {
		private static final long serialVersionUID = 5436632873705625365L;
		/**
		 * Duration in milliseconds
		 */
		public long duration;
		/**
		 * Only audio frames?
		 */
		public boolean audioOnly;
        /**
         * Keyframe timestamps
         */
		public int timestamps[];
        /**
         * Keyframe positions
         */
		public long positions[];
	}
}
