package org.uoc.red5.videoconference.servlets;


import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import org.json.JSONObject;
import org.json.JSONException;*/

import org.uoc.red5.videoconference.pojo.RemoteApplication;
import edu.uoc.lti.*;
import org.uoc.red5.videoconference.utils.Utils;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class BLTIConsumer
 * <hr>
			<p>
			Note: Unpublished drafts of IMS Specifications are only available to IMS members and any software based on
			an unpublished draft is subject to change.
			Sample code is provided to help developers understand the specification more quickly.
			Simply interoperating with this sample implementation code does not
			allow one to claim compliance with a specification.
			<p>
			<a href=http://www.imsglobal.org/toolsinteroperability2.cfm>IMS Learning Tools Interoperability Working Group</a> <br/>
			<a href="http://www.imsglobal.org/ProductDirectory/directory.cfm">IMS Compliance Detail</a> <br/>
			<a href="http://www.imsglobal.org/community/forum/index.cfm?forumid=11">IMS Developer Community</a> <br/>
			<a href="http:///www.imsglobal.org/" class="footerlink">&copy; 2009 IMS Global Learning Consortium, Inc.</a> under the Apache 2 License.</p>

 */
public class RemoteConsumer extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private final static Logger log = Logger.getLogger(BLTIConsumer.class.toString());
	public static final String[] paramsDefecte = {"url","provider", "s","username", "password","container","instanceId","Submit", "overwriteUrl", "launchurl"};

	private boolean indicada_capsalera = false;
	private boolean is_json_output = false;
	private boolean urlformat = true;
	private boolean lmspw 	= true;	 
	private final static String SESSION_VARIABLE = "\\$S\\$";
	private String 	xmldesc	= null;

    /**
     * Default constructor. 
     */
    public RemoteConsumer() {
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
		
		PrintWriter out = response.getWriter();
		Logger log = Logger.getLogger(this.getClass().getName());
		String appName 	= "";
		String codification_response = BasicLTIUtilUocWrapper.DESTINATION_CODIFICATION;
		indicada_capsalera = false;
		String error_url = null;
		//Get if is JSON
		is_json_output = BasicLTIUtilUocWrapper.is_json_output(request);
		
		try {
			BasicLTIUtilUocWrapper basicLTIUtilUoc = new BasicLTIUtilUocWrapper();
			String remoteIp = basicLTIUtilUoc.getClientIpAddressFromBalanceador(request);
			LTIEnvironment LTIEnvironment = (LTIEnvironment)request.getSession().getAttribute("LTI");
			if (LTIEnvironment!=null) {
				error_url = request.getParameter(BasicLTIUtilUocWrapper.PARAM_ERROR_URL);
					
					
				Utils U = new Utils();
				int id = -1;
				try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (NumberFormatException nfe) {
					id = -1;
				}
				RemoteApplication app = U.findRemoteApplicationById(id);
	
				if (app!=null) {
					  appName 				= app.getName();
			
					  String endpoint 				= app.getToolurl();
					  
					  String launchurl = request.getParameter("launchurl");
					  boolean overwriteUrl = "1".equals(request.getParameter("overwriteUrl")) && launchurl!=null && launchurl.length()>0;
					  if (overwriteUrl)
						  	endpoint = launchurl;
							  
					  boolean debug					= app.isDebugmode();
					  boolean enableDebug 			= "1".equals(request.getParameter("enableDebug"));
					  if (enableDebug)
						debug = enableDebug;
					  //TODO mirar d'agafar si es en popup o no
					  boolean show_pop_up			= app.getLaunchinpopup()==1;
					  if (LTIEnvironment.getSessionId()!=null)
						  endpoint = endpoint.replaceAll(SESSION_VARIABLE, LTIEnvironment.getSessionId());
					  response.sendRedirect(endpoint);
	
				  } else {
						  log.info("LTIConsumer NOT FOUND!!! "+remoteIp);
						  response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.room.inexistent");
				  } 
			} else {
				log.warn("RemoteConsumer NOT AUTHENTICATED!!! "+remoteIp);
				response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated");
			}
				  
			  addFooter(out);
			  
		} catch ( Exception e) {
			  log.error("Exception "+e.getMessage(), e);
			  redirecciona(error_url, "Exception check the logs", appName, codification_response, out, response, log);
		} 
	}

	/**
	 * Posa la capsalera
	 * @param appName
	 * @param codification_response
	 * @param out
	 */
	private void posaCapsalera(String appName, String codification_response,
			PrintWriter out) {
		if (!indicada_capsalera && !is_json_output) {	
			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
			out.println("<html>\n<head>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+codification_response+"\" />");
			out.println ("<title>"+appName+"</title></head><body>");
			indicada_capsalera = true;
		}		
	}

	/**
	 * 
	 * @param out
	 */
	private void addFooter(PrintWriter out) {
		if (!is_json_output)
			out.println("</body><html>");
	}
	/**
	 * 
	 * Redirecciona a la url que se indica  
	 * @param return_url
	 * @param error_msg
	 * @param appName
	 * @param codification_response
	 * @param out
	 * @param response
	 * @param log
	 */
	private void redirecciona(String return_url, String error_msg, String appName, String codification_response, PrintWriter out, HttpServletResponse response, Logger log) {
		try {
			/*if (is_json_output ) {
				JSONObject arrayObj=new JSONObject();
				arrayObj.put("ERROR",error_msg);
				out.println(arrayObj.toString());
			} else {*/
				if (return_url==null || "".equals(return_url)) {
				  posaCapsalera(appName, codification_response, out);
				  out.println(error_msg);
				} else {
				  response.sendRedirect(return_url);
				}
			//}
		} catch (IOException io) {
			
			out.println(error_msg);
			log.error("IOException "+io.getMessage(), io);
			
		}/* catch (JSONException  ejson) {
			
			out.println(error_msg);
			log.error("JSONException "+ejson.getMessage(), ejson);
			
		}*/
	}
	

}
