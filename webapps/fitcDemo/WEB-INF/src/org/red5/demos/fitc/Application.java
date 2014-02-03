package org.red5.demos.fitc;
/*
 * File RED5 modified by UOC
 *	
 * dcarrascogi@uoc.edu - Daniel Carrasco	
 * marcgener@uoc.edu - Marc Gener
 *
 *
 */

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IPlayItem;
import org.red5.server.api.stream.IPlaylistSubscriberStream;
import org.red5.server.api.stream.IStreamAwareScopeHandler;
import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.net.rtmp.BaseRTMPHandler;
import org.red5.server.net.rtmp.RTMPConnection;

import org.uoc.red5.videoconference.Meeting;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.User;
import org.uoc.red5.videoconference.pojo.MeetingLive;
import org.uoc.red5.videoconference.utils.Utils;


public class Application extends ApplicationAdapter implements
		IPendingServiceCallback, IStreamAwareScopeHandler{
	
	private static final Log log = LogFactory.getLog(Application.class);
	//private WeakReference<IStreamCapableConnection> conna; 
	
	/** {@inheritDoc} */
    @Override
	public boolean appStart(IScope scope) {
		// init your handler here
		return true;
	}
     
	//----UOC Marc Gener 27/04/2011 ----    
    @Override
    public boolean appConnect(IConnection conn, Object[] params) {
    	
    	IServiceCapableConnection service = (IServiceCapableConnection) conn;
		//log.info("Client connected " + conn.getClient().getId() + " conn "
		//		+ conn);
		//log.info("Setting stream id: " + getClients().size()); // just a unique number
		service
				.invoke("setId", new Object[] { conn.getClient().getId() },
						this); 
	
		/** Per a la aplicacio GrabaVideoConferencia **/
		if(params.length>1){
			
			String context = params[0].toString();
			String id_meeting = params[5].toString();
			
			//log.info("*----------------> appConnect");
			User user=new User(params[1].toString(),params[2].toString(),conn);
			//Comproba si existeix el Meeting
			if(SingletonMeetings.getSingletonMeetings().existsMeeting(id_meeting)){
				Meeting meeting = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting);
				meeting.addUser(user);				
			} else {
				Meeting meeting = new Meeting(context ,params[3].toString(), Utils.getProperty("pathStreaming"), Utils.findMeeting(id_meeting).getDescription());
				meeting.setId_meeting(id_meeting);
				SingletonMeetings.getSingletonMeetings().addMeeting(meeting);
				meeting.addUser(user);
			}
			log.info("[dwh] [connect] User connect: " + user.getFullName() + "  Room/Meeting: " + id_meeting);
			log.info("[dwh] [total] Total users connected in videoconferences rooms: " + SingletonMeetings.getSingletonMeetings().totalUsersConnected());
			// afegeix atributs dins del stream
			conn.setAttribute("id_meeting", id_meeting);
			conn.setAttribute("user", user);
			// actualitza participants al meeting
			MeetingLive meetingLive = Utils.findLiveMeeting(id_meeting);
			if (meetingLive == null) {
				Utils.inMeeting(id_meeting, context);
			} 
			Utils.updateParticipantsMeeting(id_meeting);
		    //log.info("*----------------> Número Users:"+SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getNumUsers());
		}
		return true;
	}  
	//----UOC Marc Gener 27/04/2011 ----

    
    
	/** {@inheritDoc} */
    @Override
	public boolean appJoin(IClient client, IScope scope) {
		//log.info("Client id joined app " + client.getId());
		// If you need the connecion object you can access it via.
		IConnection conn = Red5.getConnectionLocal();
		return true;
	}

	/** {@inheritDoc} */
    public void streamPublishStart(IBroadcastStream stream) {
		// Notify all the clients that the stream had been started
		if (log.isDebugEnabled()) {
			log.debug("stream broadcast start: " + stream.getPublishedName());
		}
		IConnection current = Red5.getConnectionLocal();
		Iterator<IConnection> it = scope.getConnections();
		while (it.hasNext()) {
			IConnection conn = it.next();
			if (conn.equals(current)) {
				// Don't notify current client
				continue;
			}
            
			if (conn instanceof IServiceCapableConnection) {
				((IServiceCapableConnection) conn).invoke("newStream",
						new Object[] { stream.getPublishedName() }, this);
				if (log.isDebugEnabled()) {
					log.debug("sending notification to " + conn);
				}
			}
		}
	}

	/** {@inheritDoc} */
    public void streamRecordStart(IBroadcastStream stream) { 
	}

	/** {@inheritDoc} */
    public void streamBroadcastClose(IBroadcastStream stream) {
	}

	/** {@inheritDoc} */
    public void streamBroadcastStart(IBroadcastStream stream) {
	}

	/** {@inheritDoc} */
    public void streamPlaylistItemPlay(IPlaylistSubscriberStream stream,
			IPlayItem item, boolean isLive) {
	}

	/** {@inheritDoc} */
    public void streamPlaylistItemStop(IPlaylistSubscriberStream stream,
			IPlayItem item) {

	}

	/** {@inheritDoc} */
    public void streamPlaylistVODItemPause(IPlaylistSubscriberStream stream,
			IPlayItem item, int position) {

	}

	/** {@inheritDoc} */
    public void streamPlaylistVODItemResume(IPlaylistSubscriberStream stream,
			IPlayItem item, int position) {

	}

	/** {@inheritDoc} */
    public void streamPlaylistVODItemSeek(IPlaylistSubscriberStream stream,
			IPlayItem item, int position) {

	}

	/** {@inheritDoc} */
    public void streamSubscriberClose(ISubscriberStream stream) {

	}

	/** {@inheritDoc} */
    public void streamSubscriberStart(ISubscriberStream stream) {
	}

	/**
	 * Get streams. called from client
	 * @return iterator of broadcast stream names
	 */ 
	public List<String> getStreams() {
		IConnection conn = Red5.getConnectionLocal();
		return getBroadcastStreamNames(conn.getScope());
	}

	/**
	 * Handle callback from service call. 
	 */
	public void resultReceived(IPendingServiceCall call) {
		//log.info("Received result " + call.getResult() + " for "
		//		+ call.getServiceMethodName());
	}
	
}