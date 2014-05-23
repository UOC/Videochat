package org.uoc.red5.videoconference.servlets;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.VideoConferenceList;
import org.uoc.red5.videoconference.VideoConferenceMetadata;
import org.uoc.red5.videoconference.utils.CheckPermissionsLTI;
import edu.uoc.lti.LTIEnvironment;
import org.uoc.red5.videoconference.utils.Utils;

/**
 * Servlet implementation class ViewMeeting
 */
public class ViewMeeting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Log log = LogFactory.getLog(ViewMeeting.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewMeeting() {
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
		String url1 = "";
		LTIEnvironment lti = new LTIEnvironment(request);
		String language = Utils.getLocale(lti.getLocale());
			
//		if (context != null && session != null && !context.equals("") && !session.equals("")) {
			// check authenticated
			boolean isAuthenticated = CheckPermissionsLTI.isAuthenticated(request);
			if (isAuthenticated) {
				
				String id_meeting = lti.getCustomParameter("id_meeting", request);
						//String server_rtmp = Utils.getProperty("server_name"); 
				String server_name = request.getServerName();
				String server_rtmp = server_name;
				String rtmp = "rtmp://" + server_rtmp + "/oflaDemo";  
				String context = lti.getCourseKey();

				
				if (context != null && rtmp != null) {  
					String path = Utils.getProperty("pathStreaming")+File.separator+context+File.separator+id_meeting;
					VideoConferenceList vcl = new VideoConferenceList(path, true);
					String video_directory = Utils.getProperty("video.directory");
					//String serverName = Utils.getProperty("server_name");
					url1 = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath() + video_directory + File.separator + context + File.separator + id_meeting; 
					
					// construct a xml list of videos by player 
					File file = new File(path);
					String[] directories = file.list(); 
					Map<String, Map> mapInfo = new HashMap<String, Map>();
					Map<String, Object> fileInfo = new HashMap<String, Object>();
					VideoConferenceMetadata vcm = new VideoConferenceMetadata(path);	
					vcm.createVideoconferenceXML();   
					String metadata = vcm.getTextMetadata();
										
					fileInfo.put("url-video", url1);
					fileInfo.put("info-video", metadata); 
					mapInfo.put(String.valueOf(0), fileInfo);
					vcl.createTagVideo(mapInfo);
					try {  
						vcl.printXML();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("[dwh] [play] Play videoconference Room/Meeting: " + id_meeting);
				response.sendRedirect("./videoconference_player/player.jsp?scope=" + context + "&rtmp=rtmp://"+server_name+"/oflaDemo" + "&id_meeting=" + id_meeting + "&url-video=" + url1 + "&lang=" + language);
			} else {
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated&lang="+language);						
			}
//		} else {
//			response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.accessmeeting.failparameters");
//		}
	}
}
