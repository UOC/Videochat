package org.uoc.red5.videoconference.pojo;

public class MeetingLive {

	private String id;
	private String id_context;
	private boolean blocked;
	private boolean recording;
	private int n_participants;
	private String participants;
	
	public String getId() {
		return id;
	}
	public boolean isRecording() {
		return recording;
	}
	public void setRecording(boolean recording) {
		this.recording = recording;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId_context() {
		return id_context;
	}
	public void setId_context(String id_context) {
		this.id_context = id_context;
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public int getN_participants() {
		return n_participants;
	}
	public void setN_participants(int n_participants) {
		this.n_participants = n_participants;
	}
	public String getParticipants() {
		return participants;
	}
	public void setParticipants(String participants) {
		this.participants = participants;
	}
	
}
