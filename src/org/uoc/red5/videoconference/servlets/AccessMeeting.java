package org.uoc.red5.videoconference.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.Meeting;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.utils.CheckPermissionsLTI;
import org.uoc.red5.videoconference.utils.Utils;
import edu.uoc.lti.LTIEnvironment;

/**
 * Servlet implementation class AccessMeeting
 */
public class AccessMeeting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Log log = LogFactory.getLog(AccessMeeting.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccessMeeting() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// check authenticated

		LTIEnvironment lti = new LTIEnvironment(request);
		boolean isAuthenticated = lti.isAuthenticated();
		String language = Utils.getLocale(lti.getLocale());

		
		if (isAuthenticated) {
			// check permissions
			boolean isMeetingCreator =CheckPermissionsLTI.haveAuthzMeetingCreation(request);

			if (!isMeetingCreator) {
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.ismeetingcreator&lang="+language);
			} else {
								
				String id_meeting = lti.getCustomParameter("id", request);
				String control = lti.getCustomParameter("control", request);
				String context = lti.getCourseKey();

				String t = lti.getResourceTitle();
				String d = lti.getResourceDescription();
				
				System.out.println("language *****====================-----------------------------------> " + language);
								
				// operacio de introduir per pantalla el id del meeting a entrar
				if (control != null && control.equals("entryid")) {
					response.sendRedirect("jsp/accessmeeting.jsp?lang="+language);
				} else {
					// introdueix el server al singleton
					String url_server = "http"+(request.isSecure()?"s":"")+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
					SingletonMeetings.getSingletonMeetings().setUrl_server(url_server);
					// genera meeting automaticament si no existeix
					//if (lti.isInstructor() && control != null && control.equals("record_automated")) {
					if (control != null && control.equals("record_automated")) {	
						id_meeting= lti.getResourceKey();
						// substituim els : per _
						String split[] = id_meeting.split(":");
						if (split.length>1) id_meeting = split[0] + "_" + split[1];
										
						Utils.createMeetingAutomated(id_meeting, context, t, d);
					}
					if (context != null && id_meeting != null) {
						// actualitza l'estat de id_meeting en la taula 
						// live_meeting per possibles incoherencies 
						Utils.updateParticipantsMeeting(id_meeting);
						// check if number rooms now > max rooms available 
						//boolean actualRoomsAvailable = Utils.actualRoomsAvailable(id_meeting);
						// check meeting exist
						boolean existMeeting = Utils.meetingExist(id_meeting, context);
						// per desbloquejar una room ja que alguna anomalia la va deixar bloquejada
						//if (SingletonMeetings.getSingletonMeetings()!=null) {
							if (SingletonMeetings.getSingletonMeetings().getUsers(id_meeting) == null) {
								if (Utils.findLiveMeeting(id_meeting) != null)
									Utils.updateBlocked(id_meeting, false);
							}
						//}
						// check meeting blocked
						boolean blockedMeeting = Utils.isBlockedMeeting(id_meeting);
						// check recording
						boolean meetingRecording = Utils.isMeetingRecording(id_meeting);
						// check meeting finished/close
						boolean isFinished = Utils.isMeetingFinished(id_meeting);
						// check limit participants in meeting
						boolean maximParticipants = Utils.isMaximParticipants(id_meeting); //deprecated
						//boolean maximParticipants = Utils.isMeetingMaxParticipants(id_meeting, context);
						// if the conditions are correct, make meeting
						//if (isMeetingCreator && existMeeting && !blockedMeeting && !isFinished && !maximParticipants && !meetingRecording && actualRoomsAvailable) {
						if (isMeetingCreator && existMeeting && !blockedMeeting && !isFinished && !maximParticipants && !meetingRecording) {
							String server_name = request.getServerName();
							// treure paràmetres del usuari
							String scope = context;
							String fullName = lti.getFullName();
							String email = lti.getEmail();
							// cercar el topic del meeting
							String topic = (String)Utils.findMeeting(id_meeting).getTopic();
							String description = (String)Utils.findMeeting(id_meeting).getDescription();
							String rtmpCon = "rtmp://"+server_name+"/fitcDemo/"+context+"/"+id_meeting;
							if (scope != null && fullName != null && email != null && topic != null && rtmpCon != null) {
								Utils.inMeeting(id_meeting, context);
								// si no existeix el meeting, l'afegeix al singleton
								if(!SingletonMeetings.getSingletonMeetings().existsMeeting(id_meeting)){
									Meeting meeting = new Meeting(context ,topic, Utils.getProperty("pathStreaming"), Utils.findMeeting(id_meeting).getDescription());
									meeting.setId_meeting(id_meeting);
									meeting.setLanguage(language);
									SingletonMeetings.getSingletonMeetings().addMeeting(meeting);
								}
								//log.info("[dwh] User connect: " + fullName + "  Room/Meeting: " + id_meeting);
								// Prevent caching of Servlet output
								response.setHeader("Cache-Control","no-cache"); 
								response.setHeader("Pragma","no-cache");
								response.setDateHeader ("Expires", 0);
								response.sendRedirect("./videoconference_recorder/GrabaVideoConferencia.jsp?scope=" + scope + "&id_meeting=" + id_meeting + "&fullName=" + fullName + "&email=" + email + "&topic=" + topic + "&text_description=" + description + "&rtmpCon=" + rtmpCon + "&s=no operatiu"+ "&lang=" + language);
							} else {
								response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters&lang="+language);
							}
						// depenent sortirà una pantalla d'avis
						} else if (!existMeeting) {
							response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.notexist&lang="+language);
						//} else if (blockedMeeting || meetingRecording) {
						} else if (blockedMeeting) {	
							response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.blockedmeeting&lang="+language);			
						} else if (isFinished) {
							response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.isfinished&lang="+language);
						} else if (maximParticipants) {
							response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.maximparticipants&lang="+language);
						//} else if (!actualRoomsAvailable) {
							//response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.maximrooms");
						} else if (meetingRecording) {
							//response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.recording");
							response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.blockedmeeting&lang="+language);			
						}
					} else {
						response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters&lang="+language);
					}		
				}
			}
		} else {
			response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated&lang="+language);						
		}
	}
}
