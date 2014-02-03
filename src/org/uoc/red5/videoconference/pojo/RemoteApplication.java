package org.uoc.red5.videoconference.pojo;

import java.util.Date;

public class RemoteApplication {
	
	private int id;
	private String toolurl;
	private String name;
	private String description;
	private short launchinpopup;
	private boolean debugmode;
	private Date registered;
	private Date updated;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getToolurl() {
		return toolurl;
	}
	public void setToolurl(String toolurl) {
		this.toolurl = toolurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public short getLaunchinpopup() {
		return launchinpopup;
	}
	public void setLaunchinpopup(short launchinpopup) {
		this.launchinpopup = launchinpopup;
	}
	public boolean isDebugmode() {
		return debugmode;
	}
	public void setDebugmode(boolean debugmode) {
		this.debugmode = debugmode;
	}
	public Date getRegistered() {
		return registered;
	}
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
		
}
