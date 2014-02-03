package org.uoc.red5.videoconference.dao;

import java.util.List;

import org.uoc.red5.videoconference.pojo.*;

public interface MeetingDAO {

	public void insert(Meeting meeting)
 	 throws MeetingDAOException;

	public void updateDataInici(String id)
	 throws MeetingDAOException;
	
	public void updateFinish(String id)
	 throws MeetingDAOException;
	
	public void updateParticipants(String id, String participants)
 	 throws MeetingDAOException;

	public void updateDataFinal(String id, java.util.Date dataInici)
	 throws MeetingDAOException;

	public void updateCleanRow(String id)
	 throws MeetingDAOException;

	public void updateDataIniciFinal(String id_meeting, java.util.Date dataInici, java.util.Date dataFinal)
	 throws MeetingDAOException;

	public void updateTopic_Description(String id, String topic, String description)
	 throws MeetingDAOException;

	public void delete(String id)
 	 throws MeetingDAOException;

	public Meeting findByPrimaryKey(String id)
 	 throws MeetingDAOException;

	public boolean exist(String id, String context)
	 throws MeetingDAOException;

	public Meeting[] findByContext(String context)
 	 throws MeetingDAOException;
	
	public List<Meeting> findBySearch(String context, String topic, String participant, String dataInici, String dataFinal) 
	 throws MeetingDAOException;

}
