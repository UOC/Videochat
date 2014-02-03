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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;
import org.red5.server.api.stream.IPlayItem;
import org.red5.server.api.stream.IPlaylistController;
import org.red5.server.api.stream.IServerStream;
import org.red5.server.api.stream.IStreamFilenameGenerator;
import org.red5.server.api.stream.ResourceExistException;
import org.red5.server.api.stream.ResourceNotFoundException;
import org.red5.server.api.stream.IStreamFilenameGenerator.GenerationType;
import org.red5.server.messaging.IFilter;
import org.red5.server.messaging.IMessage;
import org.red5.server.messaging.IMessageComponent;
import org.red5.server.messaging.IMessageInput;
import org.red5.server.messaging.IMessageOutput;
import org.red5.server.messaging.IPassive;
import org.red5.server.messaging.IPipe;
import org.red5.server.messaging.IPipeConnectionListener;
import org.red5.server.messaging.IProvider;
import org.red5.server.messaging.IPushableConsumer;
import org.red5.server.messaging.InMemoryPushPushPipe;
import org.red5.server.messaging.OOBControlMessage;
import org.red5.server.messaging.PipeConnectionEvent;
import org.red5.server.net.rtmp.event.AudioData;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.VideoData;
import org.red5.server.stream.consumer.FileConsumer;
import org.red5.server.stream.message.RTMPMessage;
import org.red5.server.stream.message.ResetMessage;
import org.springframework.core.io.Resource;

/**
 * An implementation for server side stream.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Steven Gong (steven.gong@gmail.com)
 */
public class ServerStream extends AbstractStream implements IServerStream,
		IFilter, IPushableConsumer, IPipeConnectionListener {
    /**
     * Logger
     */
    private static final Log log = LogFactory.getLog(ServerStream.class);

    /**
     * Enumeration for states
     */
    private enum State {
		UNINIT, CLOSED, STOPPED, PLAYING, PAUSED
	}

    /**
     * Current state
     */
    private State state;
    /**
     * Stream published name
     */
	private String publishedName;
    /**
     * Actual playlist controller
     */
	private IPlaylistController controller;
    /**
     * Default playlist controller
     */
	private IPlaylistController defaultController;
    /**
     * Rewind flag state
     */
	private boolean isRewind;
    /**
     * Random flag state
     */
	private boolean isRandom;
    /**
     * Repeat flag state
     */
	private boolean isRepeat;
    /**
     * List of items in this playlist
     */
	private List<IPlayItem> items;

    /**
     * Current item index
     */
	private int currentItemIndex;
    /**
     * Current item
     */
	private IPlayItem currentItem;
    /**
     * Message input
     */
	private IMessageInput msgIn;
    /**
     * Message output
     */
	private IMessageOutput msgOut;
    /**
     * Pipe for recording
     */
	private IPipe recordPipe;
	/**
	 * The filename we are recording to.
	 */
	private String recordingFilename;
    /**
     * Scheduling service
     */
	private ISchedulingService scheduler;
    /**
     * Live broadcasting scheduled job name
     */
	private String liveJobName;
    /**
     * VOD scheduled job name
     */
	private String vodJobName;
    /**
     * VOD start timestamp
     */
	private long vodStartTS;
    /**
     * Server start timestamp
     */
	private long serverStartTS;
    /**
     * Next msg's video timestamp
     */
	private long nextVideoTS;
    /**
     * Next msg's audio timestamp
     */
	private long nextAudioTS;
    /**
     * Next msg's data timestamp
     */
	private long nextDataTS;
    /**
     * Next msg's timestamp
     */
	private long nextTS;
    /**
     * Next RTMP message
     */
	private RTMPMessage nextRTMPMessage;

	/** Constructs a new ServerStream. */
    public ServerStream() {
		defaultController = new SimplePlaylistController();
		items = new ArrayList<IPlayItem>();
		state = State.UNINIT;
	}

	/** {@inheritDoc} */
	synchronized public void addItem(IPlayItem item) {
		items.add(item);
	}

	/** {@inheritDoc} */
	synchronized public void addItem(IPlayItem item, int index) {
		items.add(index, item);
	}

	/** {@inheritDoc} */
	synchronized public void removeItem(int index) {
		if (index < 0 || index >= items.size()) {
			return;
		}
		items.remove(index);
	}

	/** {@inheritDoc} */
	synchronized public void removeAllItems() {
		items.clear();
	}

	/** {@inheritDoc} */
    public int getItemSize() {
		return items.size();
	}

	/** {@inheritDoc} */
    public int getCurrentItemIndex() {
		return currentItemIndex;
	}

    /** {@inheritDoc} */
    public IPlayItem getCurrentItem() {
        return currentItem;
    }

    /** {@inheritDoc} */
    public IPlayItem getItem(int index) {
		try {
			return items.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/** {@inheritDoc} */
    synchronized public void previousItem() {
		stop();
		moveToPrevious();
		if (currentItemIndex == -1) {
			return;
		}
		IPlayItem item = items.get(currentItemIndex);
		play(item);
	}

	/** {@inheritDoc} */
    synchronized public boolean hasMoreItems() {
    	int nextItem = currentItemIndex + 1;
    	if (nextItem >= items.size() && !isRepeat) {
    		return false;
    	} else {
    		return true;
    	}
    }

	/** {@inheritDoc} */
    synchronized public void nextItem() {
		stop();
		moveToNext();
		if (currentItemIndex == -1) {
			return;
		}
		IPlayItem item = items.get(currentItemIndex);
		play(item);
	}

	/** {@inheritDoc} */
    synchronized public void setItem(int index) {
		if (index < 0 || index >= items.size()) {
			return;
		}
		stop();
		currentItemIndex = index;
		IPlayItem item = items.get(currentItemIndex);
		play(item);
	}

	/** {@inheritDoc} */
    public boolean isRandom() {
		return isRandom;
	}

	/** {@inheritDoc} */
    public void setRandom(boolean random) {
		isRandom = random;
	}

	/** {@inheritDoc} */
    public boolean isRewind() {
		return isRewind;
	}

	/** {@inheritDoc} */
    public void setRewind(boolean rewind) {
		isRewind = rewind;
	}

	/** {@inheritDoc} */
    public boolean isRepeat() {
		return isRepeat;
	}

	/** {@inheritDoc} */
    public void setRepeat(boolean repeat) {
		isRepeat = repeat;
	}

	/** {@inheritDoc} */
    public void setPlaylistController(IPlaylistController controller) {
		this.controller = controller;
	}

	/** {@inheritDoc} */
	public void saveAs(String name, boolean isAppend) throws IOException,
			ResourceNotFoundException, ResourceExistException {
		try {
		IScope scope = getScope();
		IStreamFilenameGenerator generator = (IStreamFilenameGenerator) ScopeUtils
		.getScopeService(scope, IStreamFilenameGenerator.class,
				DefaultStreamFilenameGenerator.class);
		
		String filename = generator.generateFilename(scope, name, ".flv", GenerationType.RECORD);
		Resource res = scope.getContext().getResource(filename);
		if (!isAppend) {
			if (res.exists()) {
				// Per livedoc of FCS/FMS:
				// When "live" or "record" is used,
				// any previously recorded stream with the same stream URI is deleted.
				if (!res.getFile().delete())
					throw new IOException("file could not be deleted");
			}
		} else {
			if (!res.exists()) {
				// Per livedoc of FCS/FMS:
				// If a recorded stream at the same URI does not already exist,
				// "append" creates the stream as though "record" was passed.
				isAppend = false;
			}
		}

		if (!res.exists()) {
			// Make sure the destination directory exists
				try {
			String path = res.getFile().getAbsolutePath();
			int slashPos = path.lastIndexOf(File.separator);
			if (slashPos != -1) {
				path = path.substring(0, slashPos);
			}
			File tmp = new File(path);
			if (!tmp.isDirectory()) {
				tmp.mkdirs();
			}
				} catch (IOException err) {
					log.error("Could not create destination directory.", err);
				}
				res = scope.getResource(filename);
		}

		if (!res.exists()) {
				if (!res.getFile().canWrite()) {
					log.warn("File cannot be written to "
							+ res.getFile().getCanonicalPath());
				}
			res.getFile().createNewFile();
		}
		FileConsumer fc = new FileConsumer(scope, res.getFile());
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		if (isAppend) {
			paramMap.put("mode", "append");
		} else {
			paramMap.put("mode", "record");
		}
			if (null == recordPipe) {
				recordPipe = new InMemoryPushPushPipe();
			}
		recordPipe.subscribe(fc, paramMap);
		recordingFilename = filename;
		} catch (IOException e) {
			log.warn("Save as exception", e);
		}
	}

	/** {@inheritDoc} */
	public String getSaveFilename() {
		return recordingFilename;
	}

	/** {@inheritDoc} */
    public IProvider getProvider() {
		return this;
	}

	/** {@inheritDoc} */
    public String getPublishedName() {
		return publishedName;
	}

	/** {@inheritDoc} */
    public void setPublishedName(String name) {
		publishedName = name;
	}

	/**
	 * Start this server-side stream
	 */
	public void start() {
		if (state != State.UNINIT) {
			throw new IllegalStateException("State " + state
					+ " not valid to start");
		}
		if (items.size() == 0) {
			throw new IllegalStateException(
					"At least one item should be specified to start");
		}
		if (publishedName == null) {
			throw new IllegalStateException(
					"A published name is needed to start");
		}
		// publish this server-side stream
		IProviderService providerService = (IProviderService) getScope()
				.getContext().getBean(IProviderService.BEAN_NAME);
		providerService
				.registerBroadcastStream(getScope(), publishedName, this);
		Map<Object, Object> recordParamMap = new HashMap<Object, Object>();
		recordPipe = new InMemoryPushPushPipe();
		recordParamMap.put("record", null);
		recordPipe.subscribe((IProvider) this, recordParamMap);
		recordingFilename = null;
		scheduler = (ISchedulingService) getScope().getContext().getBean(
				ISchedulingService.BEAN_NAME);
		state = State.STOPPED;
		currentItemIndex = -1;
		nextItem();
	}

    /**
     * Stop this server-side stream
     */
    public synchronized void stop() {
		if (state != State.PLAYING && state != State.PAUSED) {
			return;
		}
		if (liveJobName != null) {
			scheduler.removeScheduledJob(liveJobName);
			liveJobName = null;
		}
		if (vodJobName != null) {
			scheduler.removeScheduledJob(vodJobName);
			vodJobName = null;
		}
		if (msgIn != null) {
			msgIn.unsubscribe(this);
			msgIn = null;
		}
		if (nextRTMPMessage != null) {
			nextRTMPMessage.getBody().release();
		}
		state = State.STOPPED;
	}

	/** {@inheritDoc} */
	public void pause() {
		if (state == State.PLAYING) {
			state = State.PAUSED;
		} else if (state == State.PAUSED) {
			state = State.PLAYING;
			vodStartTS = 0;
			serverStartTS = System.currentTimeMillis();
			scheduleNextMessage();
		}
	}
	
	/** {@inheritDoc} */
	public void seek(int position) {
		if (state != State.PLAYING && state != State.PAUSED)
			// Can't seek when stopped/closed
			return;
		
		sendVODSeekCM(msgIn, position);
	}
	
	/** {@inheritDoc} */
    public synchronized void close() {
		if (state == State.PLAYING || state == State.PAUSED) {
			stop();
		}
		if (msgOut != null) {
			msgOut.unsubscribe(this);
		}
		recordPipe.unsubscribe((IProvider) this);
		state = State.CLOSED;
	}

	/** {@inheritDoc} */
    public void onOOBControlMessage(IMessageComponent source, IPipe pipe,
			OOBControlMessage oobCtrlMsg) {
	}

	/** {@inheritDoc} */
    public void pushMessage(IPipe pipe, IMessage message) throws IOException {
		pushMessage(message);
	}


    /**
     * Pipe connection event handler. There are two types of pipe connection events so far,
     * provider push connection event and provider disconnection event.
     *
     * Pipe events handling is the most common way of working with pipes.
     *
     * @param event        Pipe connection event context
     */
    public void onPipeConnectionEvent(PipeConnectionEvent event) {
		switch (event.getType()) {
			case PipeConnectionEvent.PROVIDER_CONNECT_PUSH:
				if (event.getProvider() == this
						&& (event.getParamMap() == null || !event.getParamMap()
								.containsKey("record"))) {
					this.msgOut = (IMessageOutput) event.getSource();
				}
				break;
			case PipeConnectionEvent.PROVIDER_DISCONNECT:
				if (this.msgOut == event.getSource()) {
					this.msgOut = null;
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Play a specific IPlayItem.
	 * The strategy for now is VOD first, Live second.
	 * Should be called in a synchronized context.
     *
	 * @param item        Item to play
	 */
	private void play(IPlayItem item) {
        // Return if already playing
        if (state != State.STOPPED) {
			return;
		}
        // Assume this is not live stream
        boolean isLive = false;
        // Get provider service from Spring bean factory
        IProviderService providerService = (IProviderService) getScope()
				.getContext().getBean(IProviderService.BEAN_NAME);
		msgIn = providerService.getVODProviderInput(getScope(), item.getName());
		if (msgIn == null) {
			msgIn = providerService.getLiveProviderInput(getScope(), item
					.getName(), true);
			isLive = true;
		}
		if (msgIn == null) {
			log
					.warn("ABNORMAL Can't get both VOD and Live input from providerService");
			return;
		}
		state = State.PLAYING;
		currentItem = item;
		sendResetMessage();
		msgIn.subscribe(this, null);
		if (isLive) {
			if (item.getLength() >= 0) {
				liveJobName = scheduler.addScheduledOnceJob(item.getLength(),
						new IScheduledJob() {
							/** {@inheritDoc} */
                            public void execute(ISchedulingService service) {
								synchronized (ServerStream.this) {
									if (liveJobName == null) {
										return;
									}
									liveJobName = null;
									onItemEnd();
								}
							}
						});
			}
		} else {
			long start = item.getStart();
			if (start < 0) {
				start = 0;
			}
			sendVODInitCM(msgIn, (int) start);
			startBroadcastVOD();
		}
	}

    /**
     * Play next item on item end
     */
    private void onItemEnd() {
		nextItem();
	}

    /**
     * Push message
     * @param message     Message
     */
    private void pushMessage(IMessage message) throws IOException {
		msgOut.pushMessage(message);
		recordPipe.pushMessage(message);
	}

    /**
     * Send reset message
     */
    private void sendResetMessage() {
        // Send new reset message
    	try {
    		pushMessage(new ResetMessage());
    	} catch (IOException err) {
    		log.error("Error while sending reset message.", err);
    	}
	}

    /**
     * Begin VOD broadcasting
     */
    private void startBroadcastVOD() {
		nextVideoTS = nextAudioTS = nextDataTS = 0;
		nextRTMPMessage = null;
		vodStartTS = 0;
		serverStartTS = System.currentTimeMillis();
		scheduleNextMessage();
	}

	/**
	 * Pull the next message from IMessageInput and schedule
	 * it for push according to the timestamp.
	 */
	private void scheduleNextMessage() {
		boolean first = nextRTMPMessage == null;

		nextRTMPMessage = getNextRTMPMessage();
		if (nextRTMPMessage == null) {
			onItemEnd();
			return;
		}

		IRTMPEvent rtmpEvent = null;

		if (first) {
			rtmpEvent = nextRTMPMessage.getBody();
			// FIXME hack the first Metadata Tag from FLVReader
			// the FLVReader will issue a metadata tag of ts 0
			// even if it is seeked to somewhere in the middle
			// which will cause the stream to wait too long.
			// Is this an FLVReader's bug?
			if (!(rtmpEvent instanceof VideoData)
					&& !(rtmpEvent instanceof AudioData)
					&& rtmpEvent.getTimestamp() == 0) {
				rtmpEvent.release();
				nextRTMPMessage = getNextRTMPMessage();
				if (nextRTMPMessage == null) {
					onItemEnd();
					return;
				}
			}
		}

		rtmpEvent = nextRTMPMessage.getBody();
		if (rtmpEvent instanceof VideoData) {
			nextVideoTS = rtmpEvent.getTimestamp();
			nextTS = nextVideoTS;
		} else if (rtmpEvent instanceof AudioData) {
			nextAudioTS = rtmpEvent.getTimestamp();
			nextTS = nextAudioTS;
		} else {
			nextDataTS = rtmpEvent.getTimestamp();
			nextTS = nextDataTS;
		}
		if (first) {
			vodStartTS = nextTS;
		}
		long delta = nextTS - vodStartTS
				- (System.currentTimeMillis() - serverStartTS);

		vodJobName = scheduler.addScheduledOnceJob(delta, new IScheduledJob() {
			/** {@inheritDoc} */
            public void execute(ISchedulingService service) {
				synchronized (ServerStream.this) {
					if (vodJobName == null) {
						return;
					}
					vodJobName = null;
					try {
						pushMessage(nextRTMPMessage);
			    	} catch (IOException err) {
			    		log.error("Error while sending message.", err);
			    	}
					nextRTMPMessage.getBody().release();
					long start = currentItem.getStart();
					if (start < 0) {
						start = 0;
					}
					if (currentItem.getLength() >= 0
							&& nextTS - currentItem.getStart() > currentItem
									.getLength()) {
						onItemEnd();
						return;
					}
					if (state == State.PLAYING) {
						scheduleNextMessage();
					} else {
						// Stream is paused, don't load more messages
						nextRTMPMessage = null;
					}
				}
			}
		});
	}

	/**
     * Getter for next RTMP message.
     *
     * @return  Next RTMP message
     */
    private RTMPMessage getNextRTMPMessage() {
		IMessage message;
		do {
            // Pull message from message input object...
			try {
				message = msgIn.pullMessage();
			} catch (IOException err) {
				log.error("Error while pulling message.", err);
				message = null;
			}
            // If message is null then return null
            if (message == null) {
				return null;
			}
		} while (!(message instanceof RTMPMessage));
        // Cast and return
        return (RTMPMessage) message;
	}

    /**
     * Send VOD initialization control message
     * @param msgIn            Message input
     * @param start            Start timestamp
     */
    private void sendVODInitCM(IMessageInput msgIn, int start) {
        // Create new out-of-band control message
        OOBControlMessage oobCtrlMsg = new OOBControlMessage();
        // Set passive type
        oobCtrlMsg.setTarget(IPassive.KEY);
        // Set service name of init
        oobCtrlMsg.setServiceName("init");
        // Create map for parameters
        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        // Put start timestamp into Map of params
        paramMap.put("startTS", start);
        // Attach to OOB control message and send it
        oobCtrlMsg.setServiceParamMap(paramMap);
		msgIn.sendOOBControlMessage(this, oobCtrlMsg);
	}

    /**
     * Send VOD seek control message
     * 
     * @param msgIn				Message input
     * @param position			New timestamp to play from
     */
	private void sendVODSeekCM(IMessageInput msgIn, int position) {
		OOBControlMessage oobCtrlMsg = new OOBControlMessage();
		oobCtrlMsg.setTarget(ISeekableProvider.KEY);
		oobCtrlMsg.setServiceName("seek");
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		paramMap.put("position", new Integer(position));
		oobCtrlMsg.setServiceParamMap(paramMap);
		msgIn.sendOOBControlMessage(this, oobCtrlMsg);
		synchronized (this) {
			// Reset properties
			vodStartTS = 0;
			serverStartTS = System.currentTimeMillis();
			if (nextRTMPMessage != null) {
				try {
					pushMessage(nextRTMPMessage);
		    	} catch (IOException err) {
		    		log.error("Error while sending message.", err);
		    	}
				nextRTMPMessage.getBody().release();
				nextRTMPMessage = null;
			}
			ResetMessage reset = new ResetMessage();
			try {
				pushMessage(reset);
	    	} catch (IOException err) {
	    		log.error("Error while sending message.", err);
	    	}
			scheduleNextMessage();
		}
	}
	
	/**
	 * Move to the next item updating the currentItemIndex.
	 * Should be called in synchronized context.
	 */
	private void moveToNext() {
		if (currentItemIndex >= items.size()) {
			currentItemIndex = items.size() - 1;
		}
		if (controller != null) {
			currentItemIndex = controller.nextItem(this, currentItemIndex);
		} else {
			currentItemIndex = defaultController.nextItem(this,
					currentItemIndex);
		}
	}

	/**
	 * Move to the previous item updating the currentItemIndex.
	 * Should be called in synchronized context.
	 */
	private void moveToPrevious() {
		if (currentItemIndex >= items.size()) {
			currentItemIndex = items.size() - 1;
		}
		if (controller != null) {
			currentItemIndex = controller.previousItem(this, currentItemIndex);
		} else {
			currentItemIndex = defaultController.previousItem(this,
					currentItemIndex);
		}
	}
}