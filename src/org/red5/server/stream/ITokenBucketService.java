package org.red5.server.stream;

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

/**
 * A service used to create and manage token buckets.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Steven Gong (steven.gong@gmail.com)
 */
public interface ITokenBucketService {
	public static final String KEY = "TokenBucketService";

	/**
	 * Create a token bucket.
	 * @param capacity Capacity of the bucket.
	 * @param speed Speed of the bucket. Bytes per millisecond.
	 * @return <tt>null</tt> if fail to create.
	 */
	ITokenBucket createTokenBucket(long capacity, long speed);

	/**
	 * Remove this bucket.
	 * 
	 * @param bucket      Bucket to remove
	 */
	void removeTokenBucket(ITokenBucket bucket);
}
