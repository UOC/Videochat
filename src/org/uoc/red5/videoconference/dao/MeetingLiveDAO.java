package org.uoc.red5.videoconference.dao;

import org.uoc.red5.videoconference.pojo.MeetingLive;

public interface MeetingLiveDAO {

	public void insert(MeetingLive meetingLive)
	 throws MeetingDAOException;

	public void updateNumParticipants(String id, int n_participants)
	 throws MeetingDAOException;

	public void updateBlocked(String id, boolean oper)
	 throws MeetingDAOException;

	public void updateRecording(String id, boolean oper)
	 throws MeetingDAOException;

	public void delete(String id)
	 throws MeetingDAOException;

	public MeetingLive findByPrimaryKey(String id)
	 throws MeetingDAOException;

	public void updateParticipants(String id, String participants)
	 throws MeetingDAOException;

	public int countLiveMeetings()
	 throws MeetingDAOException;

}
