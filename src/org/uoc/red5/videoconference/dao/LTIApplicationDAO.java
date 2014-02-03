package org.uoc.red5.videoconference.dao;

import java.util.List;
import edu.uoc.lti.LTIApplication;

public interface LTIApplicationDAO {

	public void insert(LTIApplication lti)
	 throws MeetingDAOException;

	public void update(LTIApplication lti)
	 throws MeetingDAOException;

	public void delete(String id)
	 throws MeetingDAOException;

	public edu.uoc.lti.LTIApplication findByPrimaryKey(int id)
	 throws MeetingDAOException;

	public List<edu.uoc.lti.LTIApplication> findBySearch(String context, String name) 
			 throws MeetingDAOException;
}
