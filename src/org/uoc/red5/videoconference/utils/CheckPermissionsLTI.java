package org.uoc.red5.videoconference.utils;

import javax.servlet.http.HttpServletRequest;


import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.dao.*;
import edu.uoc.lti.LTIEnvironment;

public final class CheckPermissionsLTI {


	
	/**
	 * Return true if the participant have permissions in this meeting.
	 * @param id_meeting
	 * @param id_context
	 * @return
	 */
	public static final boolean isMeetingParticipant(String user, String id_meeting) {
		
		Meeting meeting = null;
		boolean isparticipant = false;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		// Find a meeting object. 
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// check if the user is participant
		String participants = meeting.getParticipants();
		CharSequence s = new String(user);
		if (user!= null && participants != null && participants.contains(s)) {
			isparticipant = true;
		}
		
		return isparticipant;
	}
	
	/**
	 * Comproba que l'usuari especificat pot crear un meeting
	 * en un context donat. La informació esta en la request.
	 * @param user
	 * @param context
	 * @return
	 */
	public static final boolean haveAuthzMeetingCreation(HttpServletRequest httpRequest) {
		
		LTIEnvironment lti = new LTIEnvironment(httpRequest);
		return lti.isCourseAuthorized();
		
	}
	
	public static final boolean isAuthenticated(HttpServletRequest httpRequest) {
	
		LTIEnvironment lti = new LTIEnvironment(httpRequest);
		return lti.isAuthenticated();
		
	}
	
	
}
