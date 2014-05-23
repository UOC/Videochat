package org.uoc.red5.videoconference;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.red5.server.BaseConnection;
import org.red5.server.api.IConnection;
import org.red5.server.api.IContext;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.stream.IClientBroadcastStream;
import org.uoc.red5.videoconference.utils.Utils;
import org.red5.server.api.stream.IClientStream;
import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.IStreamPublishSecurity;
import org.red5.server.api.stream.IStreamSecurityService;
import org.red5.server.net.rtmp.BaseRTMPHandler;
import org.red5.server.net.rtmp.Channel;
import org.red5.server.net.rtmp.RTMPConnection;
import org.red5.server.net.rtmp.status.Status;
import org.red5.server.net.rtmp.status.StatusCodes;	
import org.red5.server.stream.*;


	/** Stream service UOC by Daniel Carrasco & Marc Gener */
	/** This Class publish and stop all streams of 1 scope. */ 

   public class StreamServiceUOC extends StreamService {
		
	  private static Logger logger = Logger.getLogger(StreamServiceUOC.class);
	  protected Map<IConnection, IScope> connToScope = new ConcurrentHashMap<IConnection, IScope>();
	  private IConnection conexio;
	  private String fullName="";
	  private String videoName;
	  private String email;
	  
	  
	  public StreamServiceUOC(){}
		
	  private int getCurrentStreamId() {
			// TODO: this must come from the current connection!
			return BaseRTMPHandler.getStreamId();
	  }
		
	  private void sendNSFailed(RTMPConnection conn, String description, String name, int streamId) {
			Status failed = new Status(StatusCodes.NS_FAILED);
			failed.setClientid(streamId);
			failed.setDesciption(description);
			failed.setDetails(name);
			failed.setLevel("error");

			// FIXME: there should be a direct way to send the status
			Channel channel = conn.getChannel((byte) (4 + ((streamId-1) * 5)));
			channel.sendStatus(failed);
	  }
		
	  public void publishAllStreams(String mode){
		   IConnection connexio = Red5.getConnectionLocal();
					   
		   if (!(connexio instanceof IStreamCapableConnection)) {
				 return;
		   }
		   IScope scope = connexio.getScope();
		   String id_meeting = Utils.getMeetingId(Red5.getConnectionLocal());
		   Hashtable<String, User> users_=SingletonMeetings.getSingletonMeetings().getUsers(id_meeting);
		   logger.info("[dwh] [init record] Init record videoconference Room/Meeting: " + id_meeting + " Number of Users: " + users_.size());
		   Enumeration <User> users = users_.elements();
		   for (int i=0; users.hasMoreElements(); i++){
			    User user=users.nextElement();
			    conexio=user.getConnection();
			    fullName=user.getFullName();	
			    videoName=user.getVideoName();
			
			    IStreamCapableConnection streamConn = (IStreamCapableConnection) conexio;
				
			    int streamId = getCurrentStreamId();
			
			   if (videoName != null || "".equals(videoName)) {     

				   IStreamSecurityService security = (IStreamSecurityService) ScopeUtils.getScopeService(scope, IStreamSecurityService.class);
				if (security != null) {
					Set<IStreamPublishSecurity> handlers = security.getStreamPublishSecurity();
					for (IStreamPublishSecurity handler: handlers) {
						if (!handler.isPublishAllowed(scope, videoName, mode)) {
							sendNSFailed((RTMPConnection) streamConn, "You are not allowed to publish the stream.", videoName, streamId);
							return;
						}
					}
				}
				IBroadcastScope bsScope = getBroadcastScope(scope, videoName);
				if (bsScope != null && !bsScope.getProviders().isEmpty()) {
					// Another stream with that name is already published.
					Status badName = new Status(StatusCodes.NS_PUBLISH_BADNAME);
					badName.setClientid(streamId);
					badName.setDetails(videoName);
					badName.setLevel("error");

					// FIXME: there should be a direct way to send the status
					Channel channel = ((RTMPConnection) streamConn).getChannel((byte) (4 + ((streamId-1) * 5)));
					channel.sendStatus(badName);
					return;
				}
				IClientStream stream = streamConn.getStreamById(streamId);
			   if (stream != null) {
				if (stream != null && !(stream instanceof IClientBroadcastStream)) {
					return;
				}
				boolean created = false;
				if (stream == null) {
					stream = streamConn.newBroadcastStream(streamId);
					created = true;
				}
				   //System.out.println("stream (bs) --------------------------> " + stream);	 
				IClientBroadcastStream bs = (IClientBroadcastStream) stream;
			   //System.out.println("scope (bs) --------------------------> " + bs);	 

				try {
					bs.setPublishedName(videoName);
					IContext context = conexio.getScope().getContext();
					IProviderService providerService = (IProviderService) context
							.getBean(IProviderService.BEAN_NAME);
					
					if (IClientStream.MODE_RECORD.equals(mode)) {
						bs.start();
						Meeting meeting=SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting);
						bs.saveAsUOC(videoName, user, meeting, false); 
						//logger.info("----------------> Inici gravació del video: " + bs.getPublishedName());
					} else if (IClientStream.MODE_APPEND.equals(mode)) {
						bs.start();
						bs.saveAs(videoName, true);
					} else if (IClientStream.MODE_LIVE.equals(mode)) {
						bs.start();
					}
					bs.startPublishing();
					
				} catch (IOException e) {
					Status accessDenied = new Status(StatusCodes.NS_RECORD_NOACCESS);
					accessDenied.setClientid(streamId);
					accessDenied.setDesciption("The file could not be created/written to.");
					accessDenied.setDetails(videoName);
					accessDenied.setLevel("error");

					// FIXME: there should be a direct way to send the status
					Channel channel = ((RTMPConnection) streamConn).getChannel((byte) (4 + ((streamId-1) * 5)));
					channel.sendStatus(accessDenied);
					bs.close();
					if (created)
						streamConn.deleteStreamById(streamId);
					return;
				} catch (Exception e) {
					logger.warn("publish caught Exception");
			   }
			  }
			 }
		   }
		   
		   	// Marc Gener 27/04/2011
		   	users = users_.elements();
		   	Hashtable ht = new Hashtable();
		   	while(users.hasMoreElements()) {
		   		User user = users.nextElement();
			   	ht.put(user.getFullName(), user);
		   	}
		   	//logger.info("videoname -------------------------------------> " + videoName);
			Utils.initRecordMeeting(SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getScope(), id_meeting, ht);
		   	// Marc Gener 27/04/2011
		   
		}
		   
	 public void stopAllStreams() {
			 
		     IConnection connexio = Red5.getConnectionLocal();
		     IScope scope = connexio.getScope();	
		     
		     String id_meeting = Utils.getMeetingId(Red5.getConnectionLocal());
		     Hashtable<String, User> users_=SingletonMeetings.getSingletonMeetings().getUsers(id_meeting);
		     Enumeration <User> users = users_.elements();
			 logger.info("[dwh] [stop record] Stop record videoconference Room/Meeting: " + id_meeting + " Number of Users: " + users_.size());
			 for (int i=0; users.hasMoreElements(); i++){
				User user=users.nextElement();
				conexio=user.getConnection();
				email=user.getEmail();
		 		
				if (!(conexio instanceof IStreamCapableConnection)) {
					return;
				}
				IClientStream stream = ((IStreamCapableConnection) conexio)
						.getStreamById(getCurrentStreamId());
				if (stream != null) {
					if (stream instanceof IClientBroadcastStream) {
						IClientBroadcastStream bs = (IClientBroadcastStream) stream;
						IBroadcastScope bsScope = getBroadcastScope(conexio.getScope(), bs
								.getPublishedName());
						if (bsScope != null && conexio instanceof BaseConnection) {
							((BaseConnection) conexio).unregisterBasicScope(bsScope);
						}
						bs.stop();
						bs.close();
						//logger.info("----------------> Final gravació del video: " + bs.getPublishedName());
					} 
				}  
				//Eliminant usuari de la hash.
				SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).removeUser(SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getUserByEmail(email));
				//Tancant la conexió.
				conexio.close();   
			 } 
			  
			// esborra el meeting
			SingletonMeetings.getSingletonMeetings().removeMeeting(id_meeting);
		 	//logger.info("------------------------------> removeRoom: " + id_meeting); 
		   	// Marc Gener 27/04/2011
			String path = Utils.findMeeting(id_meeting).getPath();
			VideoConferenceMetadata vcm = new VideoConferenceMetadata(path);	
			Utils.stopRecordMeeting(id_meeting);
			// actualitza la taula meeting fi de gravació
			vcm.updateEndVideoTime();
		 	// esborra la taula live_meeting
		 	Utils.deleteMeetingLive(id_meeting);
		 	//logger.info("------------------------------> deleteMeetingLive: " + id_meeting); 
		 	// Marc Gener 27/04/2011
	 
	 }
	
}
