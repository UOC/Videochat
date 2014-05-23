package org.uoc.red5.videoconference;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.red5.server.so.SharedObject;
import org.uoc.red5.videoconference.utils.Utils;


public class Meeting {

	private int line; 	// number of line of chat from room
	private String recPath; 	// path from record videos
	private boolean rec; 	// record now (true or false)
	private String scope; 	// name form room
	private String topic; 	// topic for room
	private String description;	// description of room
	private java.io.FileOutputStream fs;	// file stream for chat
	private Hashtable<String,User> users = new Hashtable<String,User>();	// user of the room
	private SharedObject so;	// sharedobject
	private boolean close;	// close the room by new users
	private String id_meeting;
	private String language;
	
	public Meeting(String scope, String topic, String recPath, String description) {
		this.scope = scope;
		this.topic = topic;
		this.description = description;
		this.recPath = recPath;
		this.line = 1;
		this.rec = false;
		this.close = false;
		this.id_meeting = "";
	}
	
	public String getId_meeting() {
		return id_meeting;
	}

	public void setId_meeting(String id_meeting) {
		this.id_meeting = id_meeting;
	}

	public int getLine() {
		return line;
	}

	public void addLine() {
		this.line++;
	}
	
	public String getRecPath() {
		return recPath;
	}
	
	public void setRecPath(String recPath) {
		this.recPath = recPath;
	}
	
	public boolean isRec() {
		return rec;
	}
	
	public void setRec(boolean rec) {
		this.rec = rec;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public java.io.FileOutputStream getFs() {
		return fs;
	}
	
	public void setFs(java.io.FileOutputStream fs) {
		this.fs = fs;
	}
	
	public Hashtable<String, User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		this.users.put(user.getEmail(), user);
	}
		
	public void removeUser(User user) {
		this.users.remove(user.getEmail());
	}
	
	public int getNumUsers() {
		return users.size();
	}

	public SharedObject getSo() {
		return so;
	}

	public void setSo(SharedObject so) {
		this.so = so;
	}
	
	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void startRecord(){
		try{
			if( this.rec == false ){
				String path = Utils.getProperty("pathStreaming") + File.separator;
				// UOC Marc Gener - 04/05/2011 
				//recPath = VideoConferenceUtils.makeVideoConferenceDirectory(path + scope);
				// UOC Marc Gener - 04/05/2011
				//Antoni Bertran solventar problemes ":"
				//if (scope!=null)
					//scope = scope.replaceAll(":", "_");

				recPath = path + File.separator + scope + File.separator + id_meeting;;				
				//Log.info("recPath -----------------------------> " + recPath);
				File f = new File(recPath + File.separator + Utils.VAR_XAT);
			    fs = new java.io.FileOutputStream(f,true);
			    this.rec = true;
			}
		}
		catch(IOException e){e.printStackTrace();
		}
	} 
	
	public User getUserByEmail(String email){
		User user=null;
		if(users.get(email)!=null){
			 user=this.users.get(email);
		}
		return user;
	}
	
	public String isOutOfBounds(){
		if(this.getNumUsers()>Integer.parseInt(Utils.getProperty("num.max.conexion"))){
			return "true";
		}else{
			return "false";
		}
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
