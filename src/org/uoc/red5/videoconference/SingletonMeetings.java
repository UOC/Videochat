package org.uoc.red5.videoconference;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.so.SharedObject;
import org.uoc.red5.videoconference.utils.Utils;


public class SingletonMeetings {

	
	// comuns a tots els meetings
	private static SingletonMeetings singletonMeetings = null;	// singletons instancied
	private String num_max_connections;	// maximum connections permited in meeting
	private Hashtable<String, Meeting> meetings = new Hashtable<String, Meeting>();
	private String url_server = "";
	protected static Log log = LogFactory.getLog(SingletonMeetings.class.getName());
	private Hashtable<String, String> view_meetings = new Hashtable<String, String>(); 
	
	public static SingletonMeetings getSingletonMeetings(){
		
		if( singletonMeetings == null ){
			singletonMeetings = new SingletonMeetings();
		}
		return singletonMeetings;
	}
	
	public SingletonMeetings(){
		initialize();
	}
	
	/**********************************************************/
	/*                     INITIALIZATION                     */
	/**********************************************************/
	
	private void initialize(){
		num_max_connections = Utils.getProperty("num.max.conexion");
	}
	
	public Meeting getMeeting(String id_meeting) {
		Meeting meeting=null;
		if(meetings.get(id_meeting)!=null){
			meeting = meetings.get(id_meeting);
		}
		return meeting;
	}
	
	public void addMeeting(Meeting meeting) {
		meetings.put(meeting.getId_meeting(), meeting);
	}
	
	public void removeMeeting(String id_meeting) {
		meetings.remove(id_meeting);
	}
	
	public boolean existsMeeting(String id_meeting) {
		return (meetings.containsKey(id_meeting));
	}

	public void addViewMeeting(String id_room, String id_meeting) {
		String url = url_server + "/Front?control=play&id_meeting="+ id_meeting;
		view_meetings.put(id_room, url);
	}
	
	public String getViewMeeting(String id_room) {
		return view_meetings.get(id_room);
	}

	public boolean isMeetingRecorded(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		return meeting.isRec();
	}
	
	public void startRecord(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		meeting.startRecord();
	}

	public void stopRecord(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		meeting.setRec(false);
	}
	
	public void addChatLine(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		meeting.addLine();
		
	}

	public int getChatLines(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		return meeting.getLine();
		
	}

	public java.io.FileOutputStream getFs(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		return meeting.getFs();
	}
	
	public void setFs(String id_meeting, java.io.FileOutputStream fs) {
		Meeting meeting = this.getMeeting(id_meeting);
		meeting.setFs(fs);
	}

	public SharedObject getSo(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		return meeting.getSo();
	}

	public void setSo(String id_meeting, SharedObject so) {
		//System.out.println("id_meeting ----------> " + id_meeting);
		Meeting meeting = this.getMeeting(id_meeting);
		meeting.setSo(so);
	}

	public String getRecordPath(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		return meeting.getRecPath();
	}
	
	public Hashtable<String,User> getUsers(String id_meeting) {
		Meeting meeting = this.getMeeting(id_meeting);
		if (meeting == null) return null;
		else return meeting.getUsers();
	}
	
	public int totalUsersConnected() {
		int num_users_connected = 0; 
		Enumeration keys = meetings.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			int users = ((Meeting)meetings.get(key)).getNumUsers();
			num_users_connected = num_users_connected + users;
		}
		return num_users_connected;
	}

	public String getUrl_server() {
		return this.url_server;
	}

	public void setUrl_server(String url_server) {
		this.url_server = url_server;
	}
	
	
}
