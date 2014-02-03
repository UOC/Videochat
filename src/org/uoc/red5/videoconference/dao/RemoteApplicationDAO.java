package org.uoc.red5.videoconference.dao;

import java.util.List;

import org.uoc.red5.videoconference.pojo.RemoteApplication;

public interface RemoteApplicationDAO {

	public void insert(RemoteApplication lti)
	 throws MeetingDAOException;

	public void update(RemoteApplication lti)
	 throws MeetingDAOException;

	public void delete(String id)
	 throws MeetingDAOException;

	public RemoteApplication findByPrimaryKey(int id)
	 throws MeetingDAOException;

	public List<RemoteApplication> findBySearch(String context, String name) 
			 throws MeetingDAOException;
}
