package org.uoc.red5.videoconference.servlets;

import java.io.IOException;

import java.util.List;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class SearchMeeting
 */
public class SearchMeeting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Log log = LogFactory.getLog(CreateMeeting.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMeeting() {
        super();
        // TODO Auto-generated constructor stub
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

		// mirar si l'usuari pot cercar en aquest context (aula)
		// cridar a checkpermissions (falta fer)
		
		LTIEnvironment lti = new LTIEnvironment(request);
		String language = Utils.getLocale(lti.getLocale());
		
		boolean isAuthenticated = CheckPermissionsLTI.isAuthenticated(request);
		if (isAuthenticated) {

		// agafa els paràmetres de cerca
		String topic = Utils.comprobaNull(lti.getCustomParameter("topic", request));
		String participants = Utils.comprobaNull(lti.getCustomParameter("participants", request));
		String dataInici = Utils.comprobaNull(lti.getCustomParameter("datainici", request));
		String dataFinal = Utils.comprobaNull(lti.getCustomParameter("datafinal", request));
	
		String context = lti.getCourseKey();

			if (context != null && !context.equals("") ) {
					// check permissions
					boolean isMeetingCreator =CheckPermissionsLTI.haveAuthzMeetingCreation(request);
					if (!isMeetingCreator) {
						response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.ismeetingcreator&lang="+language);
					} else {
						List<Meeting> meetings = Utils.findBySearch(context, topic, participants, dataInici, dataFinal);
						log.info("[dwh] [search] Search videoconferences");
						// enviat els resultats al jsp
						request.setAttribute("meetings", meetings);
						//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/meetinglist.jsp?context="+context+"&user="+user);
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/meetinglist.jsp?datainici="+dataInici+"&datafinal="+dataFinal+"&lang="+language);
						dispatcher.forward(request, response);
					}
			} else {
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters&lang="+language);
			}
		} else {
			response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated&lang="+language);						
		}
	}
}
