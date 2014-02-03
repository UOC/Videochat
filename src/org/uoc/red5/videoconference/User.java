package org.uoc.red5.videoconference;

import org.red5.server.api.IConnection;

public class User {

	private final static String extension = ".flv";
	
	private String fullName; 	// name from user
	private String email; 	// email from user
	private String videoName; 	// name of video
	private IConnection connection;
	
	public User(String fullName, String email, IConnection connection) {
		
		this.fullName = fullName;
		this.email = email;
		this.videoName = email.substring(0,email.toString().indexOf("@"))+extension;
		this.connection = connection;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public IConnection getConnection() {
		return connection;
	}

	public void setConnection(IConnection connection) {
		this.connection = connection;
	}

	public String toString() {
		return (getFullName() + "," + getEmail() + "," + getVideoName() + "," + getConnection());
	}
}
