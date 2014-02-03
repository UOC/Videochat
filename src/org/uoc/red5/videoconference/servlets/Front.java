package org.uoc.red5.videoconference.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IConnection;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.User;
import org.uoc.red5.videoconference.utils.CheckPermissionsLTI;
import edu.uoc.lti.LTIEnvironment;
import org.uoc.red5.videoconference.utils.Utils;

/**
 * Servlet implementation class Front
 */
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Log log = LogFactory.getLog(Front.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Front() {
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
			boolean isAuthenticated = CheckPermissionsLTI.isAuthenticated(request);
			LTIEnvironment lti = new LTIEnvironment();
			String control = lti.getCustomParameter("control", request);
			String language = request.getParameter("lang");;
			
			if (isAuthenticated) {
				
				if (control != null && !control.equals("")) { 
					if (control.equals("view")) {
						response.sendRedirect("CreateMeeting?operation=view&lang="+language);			
					} else if (control.equals("create")) {
						String topic = lti.getCustomParameter("topic", request);
						String description = lti.getCustomParameter("description", request);
						response.sendRedirect("CreateMeeting?operation=create&topic=" + topic + "&description=" + description + "&lang="+language );			
					} else if (control.equals("search")) {
						String topic = lti.getCustomParameter("topic", request);
						String participants = lti.getCustomParameter("participants", request);
						String dataInici = lti.getCustomParameter("datainici", request);
						String dataFinal = lti.getCustomParameter("datafinal", request);
						response.sendRedirect("SearchMeeting?topic=" + topic + "&participants=" + participants + "&datainici=" + dataInici + "&datafinal=" + dataFinal+"&lang="+language);							
					} else if (control.equals("access")) {	
						response.sendRedirect("AccessMeeting?control=entryid&lang="+language);
					} else if (control.equals("play")) {
						String id_meeting = lti.getCustomParameter("id_meeting", request);
						//DemoService ds = new DemoService();
						//ds.getMeetingMetadata(id_meeting);
						response.sendRedirect("ViewMeeting?user=" + "&id_meeting=" + id_meeting + "&lang="+language);			
					

					} else if (control.equals("tancaDecrementa")) {
						String id_meeting = lti.getCustomParameter("id_meeting", request);
						String email = lti.getCustomParameter("email", request);
						// decremento un participant de la taula live_meeting
						Utils.outMeeting(id_meeting);
						//Si l'ussuari s'ha connectat, l'elimino.
						if (SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting) !=null){
							//for (int i=0; i<SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getUsers().size(); i++){ 
								User user = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getUserByEmail(email);
								if (user != null && id_meeting != null) {
									//Elimino l'usuari de la hash.
									SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).removeUser(user);
									//Tancant la conexió.
									IConnection conexio=user.getConnection();
									conexio.close();
									// si només quedava un participant, borra el meeting
									// sinó actualitza participants al meeting
									int num_usuaris = 0;
									if (SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting) != null) {
										num_usuaris = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getNumUsers();
									}
									if (num_usuaris==0) {
										SingletonMeetings.getSingletonMeetings().removeMeeting(id_meeting);
									} else {
										Utils.updateParticipantsMeeting(id_meeting);
									}
								}
							//} 
						}
						response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.tanca.decrementa&lang="+language);		
					}
				} else {
					//Reenviar a escollir opcio
					response.sendRedirect("jsp/selectoption.jsp");					
				}
			} else {
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated&lang="+language);						
			}
//		} else {
//			response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters");
//		}
	}
}
