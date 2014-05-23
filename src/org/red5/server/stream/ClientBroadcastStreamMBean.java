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

import java.io.IOException;

import org.red5.server.api.stream.ResourceExistException;
import org.red5.server.api.stream.ResourceNotFoundException;
import org.red5.server.messaging.IProvider;

/**
 * Represents live stream broadcasted from client. As Flash Media Server, Red5 supports
 * recording mode for live streams, that is, broadcasted stream has broadcast mode. It can be either
 * "live" or "record" and latter causes server-side application to record broadcasted stream.
 *
 * Note that recorded streams are recorded as FLV files. The same is correct for audio, because
 * NellyMoser codec that Flash Player uses prohibits on-the-fly transcoding to audio formats like MP3
 * without paying of licensing fee or buying SDK.
 *
 * This type of stream uses two different pipes for live streaming and recording.
 */
public interface ClientBroadcastStreamMBean {

	public void start();

	public void startPublishing();

	public void stop();

	public void close();

	public void saveAs(String name, boolean isAppend) throws IOException,
			ResourceNotFoundException, ResourceExistException;

	public String getSaveFilename();

	public IProvider getProvider();

	public String getPublishedName();

	public void setPublishedName(String name);

}
