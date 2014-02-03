package org.uoc.red5.videoconference.servlets;

import java.io.File;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.utils.CheckPermissionsLTI;
import edu.uoc.lti.LTIEnvironment;
import org.uoc.red5.videoconference.utils.Utils;

/**
 * Servlet implementation class CreateMeeting
 */
public class CreateMeeting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Log log = LogFactory.getLog(CreateMeeting.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMeeting() {
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
		
		String operation = request.getParameter("operation");
		LTIEnvironment lti = new LTIEnvironment(request);
		String language = Utils.getLocale(lti.getLocale());
		
		if (operation != null) {
			// check authenticated
			boolean isAuthenticated = CheckPermissionsLTI.isAuthenticated(request);
			if (isAuthenticated) {
				// check permissions
				boolean isMeetingCreator =CheckPermissionsLTI.haveAuthzMeetingCreation(request);
				if (!isMeetingCreator) {
					response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.ismeetingcreator");
				} else {
					String context = lti.getCourseKey();
					if (operation.equals("view")) {
						response.sendRedirect("jsp/createmeeting.jsp?lang="+language);
					} else if (operation.equals("create")) {
						// db operations
						String topic = lti.getCustomParameter("topic", request);
						String description = lti.getCustomParameter("description", request);
						String id_meeting = Utils.getMeetingId();
						// insereix un objecte Meeting a la bd
						Meeting meeting = new Meeting();
						meeting.setId(id_meeting);
						meeting.setTopic(topic);
						meeting.setDescription(description);
						meeting.setFinish(false);
						meeting.setId_context(context);
						meeting.setPath(Utils.getProperty("pathStreaming")+File.separator+context+File.separator+id_meeting);
						Utils.insertMeeting(meeting);
						response.sendRedirect("jsp/createmeeting.jsp?id=" + id_meeting + "&lang="+language);
					}
				}
			} else {
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated&lang="+language);						
			}
		} else {
			response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters&lang="+language);
		}		
	}
}
