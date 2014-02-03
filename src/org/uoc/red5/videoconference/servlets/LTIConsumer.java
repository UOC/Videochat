package org.uoc.red5.videoconference.servlets;


import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties ;
import java.util.Enumeration ;

/*import org.json.JSONObject;
import org.json.JSONException;*/

import edu.uoc.lti.*;
import org.uoc.red5.videoconference.utils.Utils;
import org.apache.log4j.Logger;

import static org.imsglobal.basiclti.BasicLTIConstants.CUSTOM_PREFIX;
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
public class LTIConsumer extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private final static Logger log = Logger.getLogger(BLTIConsumer.class.toString());
	public static final String[] paramsDefecte = {"url","provider", "s","username", "password","container","instanceId","Submit", "overwriteUrl", "launchurl"};

	private boolean indicada_capsalera = false;
	private boolean is_json_output = false;
	private boolean urlformat = true;
	private boolean lmspw 	= true;	 
	private String 	xmldesc	= null;
	private final static String SESSION_ID_FIELD = "sessionId";

    /**
     * Default constructor. 
     */
    public LTIConsumer() {
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
				LTIApplication lti = U.findLTIApplicationById(id);
	
				if (lti!=null) {
				  appName 				= lti.getName();
		
				  boolean sendBase64Encoded = true;//propertiesManager.getBase64Encoded();
				  if (request.getParameter("base64Encoded")!=null && request.getParameter("base64Encoded").length()>0){
					  sendBase64Encoded = "1".equals(request.getParameter("base64Encoded"));
				  }
				  boolean sendISOEncoded = true;//propertiesManager.getISOEncoded();
				  if (request.getParameter("isoEncoded")!=null && request.getParameter("isoEncoded").length()>0){
					  sendISOEncoded = "1".equals(request.getParameter("isoEncoded"));
				  }
				  boolean sendUTF8Encoded 	= true;//propertiesManager.getUTF8Encoded();
				  if (request.getParameter("utf8Encoded")!=null && request.getParameter("utf8Encoded").length()>0){
					  sendUTF8Encoded = "1".equals(request.getParameter("utf8Encoded"));
				  }
				  String return_url		= "";//lti.propertiesManager.getReturnUrlProvider();
				  if (request.getParameter("return_url")!=null && request.getParameter("return_url").length()>0){
					  return_url = request.getParameter("return_url");
				  }
				
				  codification_response = sendISOEncoded && !sendUTF8Encoded?BasicLTIUtilUocWrapper.DESTINATION_CODIFICATION:BasicLTIUtilUocWrapper.DESTINATION_CODIFICATION_UTF8;
				  			  
		
					      getResponseHeaders(response, codification_response);
		
						  posaCapsalera(appName, codification_response, out);
		
					  	 
						  String cur_url = request.getRequestURL().toString();
						
						  String oauth_consumer_secret	= lti.getPassword();
						  String oauth_consumer_key 	= lti.getResourcekey();
						  String org_id 				= lti.getOrganizationid();
						  String org_desc 				= lti.getOrganizationurl();
						  String endpoint 				= lti.getToolurl();
						  
						  String launchurl = request.getParameter("launchurl");
						  boolean overwriteUrl = "1".equals(request.getParameter("overwriteUrl")) && launchurl!=null && launchurl.length()>0;
						  if (overwriteUrl)
							  	endpoint = launchurl;
						  
						  boolean debug					= lti.isDebugmode();
						  boolean enableDebug 			= "1".equals(request.getParameter("enableDebug"));
						  if (enableDebug)
							  debug = enableDebug;
						  //Forcem que no es faci debug si la sortida es json
						  if (is_json_output)
							  debug = false;
						  boolean show_pop_up			= lti.getLaunchinpopup()==1;
						  String preferredHeight		= lti.getPreferheight();
						  String[][] customParameters	= Utils.parseCustomParameters(lti.getCustomparameters());
						  
						  //Afegim tots els params que rebem que no siguin els per defecte
						  String parameterName = "";
						  boolean addParameter = true;
						
						  Properties propertiesRequestParams = new Properties();
						  for(Enumeration<?> en = request.getParameterNames(); en.hasMoreElements(); ){
							parameterName = (String)en.nextElement();
							addParameter = true;
							for (int i=0; i<paramsDefecte.length; i++)
							{
								if (paramsDefecte[i].equals(parameterName)){
									addParameter = false;
									break;
								}
							}
							if (addParameter)
							{
								propertiesRequestParams.setProperty(parameterName, request.getParameter(parameterName));
							}
						  }
						  if (LTIEnvironment.getSessionId()!=null)
							  propertiesRequestParams.setProperty(SESSION_ID_FIELD,LTIEnvironment.getSessionId());
						  
						  if (debug) {
							  printDebugOutput(request, out, cur_url, endpoint, 
										oauth_consumer_key, oauth_consumer_secret, org_desc, org_id, "",
										customParameters, propertiesRequestParams);
						  }
						  // Ignore the organizational info if this is not an LMS password
						  if ( ! lmspw ) org_id = null;
						
						  BLTIGeneratorFromSession bltiGenerator = new BLTIGeneratorFromSession();
						  
						  Properties info = new Properties();
						  Properties postProp = new Properties();
						  if ( urlformat ) {
							  postProp = bltiGenerator.getLMSData(lti, LTIEnvironment, propertiesRequestParams, 
									  sendBase64Encoded, sendUTF8Encoded, sendISOEncoded, return_url);
						  } else {
						    if ( BasicLTIUtilUocWrapper.parseDescriptor(info, postProp, xmldesc) ) {
						    	postProp = bltiGenerator.getLMSData(lti, LTIEnvironment, propertiesRequestParams, 
										  sendBase64Encoded, sendUTF8Encoded, sendISOEncoded, return_url);
						      endpoint = info.getProperty("launch_url");
						      if ( endpoint == null ) {
						    	  log.error("<p>Error, did not find a launch_url or secure_launch_url in the XML descriptor</p>\n");
						    	  out.println("<p>Error, did not find a launch_url or secure_launch_url in the XML descriptor</p>\n");
							        return;
						      }
						      endpoint = endpoint.replace("CUR_URL",cur_url.replace("lms.jsp", "tool.jsp"));
						    }
						  }
						  
						  String tool_consumer_instance_contact_email = null;
						  String tool_consumer_instance_url = null;
						
						  // Off to the races with BasicLTI...
						  //if ( org_secret.equals("") ) org_secret = null;
						  postProp = BasicLTIUtilUocWrapper.encodePostData(postProp);
						  postProp = BasicLTIUtilUocWrapper.convertToProperties(BasicLTIUtilUocWrapper.signProperties(
							      BasicLTIUtilUocWrapper.convertToMap(postProp), endpoint, "POST",
							      oauth_consumer_key, oauth_consumer_secret,
							      org_id,
							      org_desc,
							      tool_consumer_instance_url, org_desc,
							      tool_consumer_instance_contact_email));
		
						  String postData = ""; 
								  
						  /*if (is_json_output)
							  postData = BasicLTIUtilUocWrapper.postLaunchJSON(postProp, endpoint);
						  else*/ 
							  postData = BasicLTIUtilUocWrapper.postLaunchHTML(postProp, endpoint, debug, show_pop_up, preferredHeight);
						  String custom_domain_code 	= getValue(postProp, "custom_domain_code", sendBase64Encoded, sendISOEncoded,sendUTF8Encoded);
		
						  String custom_username 	= getValue(postProp, "custom_username", sendBase64Encoded, sendISOEncoded,sendUTF8Encoded);
						  
						  log.info("BLTI_C userlogin:'"+custom_username+"' provider:'"+appName+"' custom_domain_code:'"+custom_domain_code+"' endpoint:'"+endpoint+"' remoteIP:'"+remoteIp+"'");
		
						  out.println(postData);
				  
	
				  } else {
						  log.info("LTIConsumer NOT FOUND!!! "+remoteIp);
						  response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.room.inexistent");
				  } 
			} else {
				log.warn("LTIConsumer NOT AUTHENTICATED!!! "+remoteIp);
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
	
	/**
	 * Obte la resposta segons si es es JSON i els params configurats 
	 * @param response
	 */
	private void getResponseHeaders(HttpServletResponse response, String codification_response) {
		
		String contentType = BasicLTIConstantsUocWrapper.CONTENT_TYPE_OUTPUT_HTML;
		if (is_json_output)
			contentType = BasicLTIConstantsUocWrapper.CONTENT_TYPE_OUTPUT_JSON;
		
		response.setContentType(contentType+"; charset="+codification_response);
		response.setCharacterEncoding(codification_response.toLowerCase());
		response.addDateHeader("Expires", System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L));
	    response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.addHeader("Pragma", "no-cache");
	}
	
	/**
	 * Get vaues from property
	 * @param postProp
	 * @param property
	 * @param sendBase64Encoded
	 * @param sendISOEncoded
	 * @param sendUTF8Encoded
	 * @return
	 */
	private String getValue(Properties postProp, String property, boolean sendBase64Encoded,
			boolean sendISOEncoded, boolean sendUTF8Encoded) {
		String str = "";
		Object obj = postProp.get(property);
	  	  if (obj!=null) {
	  		str = (String) obj;
	  		//Mirem si hem de decodificar
		    	if (sendBase64Encoded) {
		    		str = BasicLTIUtilUocWrapper.decodeISO(str);
		    		str = BasicLTIUtilUocWrapper.decodeBase64(str);
		    	} 
		    	else  {
			    	if (sendISOEncoded) {
			    		str = BasicLTIUtilUocWrapper.decodeISO(str);
			    	} else {
				    	if (sendUTF8Encoded){
				    		str = BasicLTIUtilUocWrapper.decodeUTF8(str);
				    	}
			    	}
		    	}
	  		
	  	  }
	    return str;
	}
	
	private void printDebugOutput(HttpServletRequest request, PrintWriter out, String cur_url, String endpoint, 
			String oauth_consumer_key, String oauth_consumer_secret, String org_desc, String org_id, String org_secret,
			String[][] customParameters, Properties propertiesRequestParams) {
		  
		  //TODO review this point
		  String urlformatstr = request.getParameter("format");
		  urlformat = urlformatstr == null || ! urlformatstr.equals("XML");
		  String lmspwstr = request.getParameter("lmspw");
		  lmspw = lmspwstr == null || ! lmspwstr.equals("Resource");
		
		  String default_desc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		    "<basic_lti_link xmlns=\"http://www.imsglobal.org/services/cc/imsblti_v1p0\" \n" +
		    "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
		    "  <title>A Simple Descriptor</title>\n" +
		    "  <custom>\n" +
		    "    <parameter key=\"Cool:Factor\">120</parameter>\n" +
		    "  </custom>\n" +
		    "  <launch_url>CUR_URL</launch_url>\n" +
		    "</basic_lti_link>\n".replace("CUR_URL",cur_url.replace("lms.jsp", "tool.jsp"));
		
		  // To keep roundtrips from adding backslashes to double quotes
		  xmldesc = request.getParameter("xmldesc");
		  if ( xmldesc == null ) xmldesc = default_desc;
		  // xmldesc = str_replace("\\\"","\"",$_REQUEST["xmldesc"]);
		
		  out.println("<form method=\"post\">\n");
		  out.println("<p><select name=\"format\" onchange=\"this.form.submit();\">\n");
		  out.println("<option value=\"URL\">URL plus Secret</option>\n");
		  if ( urlformat ) {
		    out.println("<option value=\"XML\">XML Descriptor</option>\n");
		  } else {
		    out.println("<option value=\"XML\" selected=\"selected\">XML Descriptor</option>\n");
		  }
		  out.println("</select>");
		  out.println("<fieldset><legendjava.io.UnsupportedEncodingException>Add New BasicLTI Resource</legend>");
	
		  if ( urlformat ) {
			  out.println("Launch URL: <input size=\"80\" type=\"text\" name=\"endpoint\" value=\""+
					  		(endpoint == null ? "" : BasicLTIUtilUocWrapper.htmlspecialchars(endpoint))+"\">\n");
		  } else {
			  out.println("XML BasicLTI Resource Descriptor: <br/> <textarea name=\"xmldesc\" rows=\"10\" cols=\"80\">"+
	                 BasicLTIUtilUocWrapper.htmlspecialchars(xmldesc)+"</textarea>\n");
		  }
		  out.println("<br/>Resource Key: <input type\"text\" name=\"key\" value=\""+
		  BasicLTIUtilUocWrapper.htmlspecialchars(oauth_consumer_key)+"\">\n");
		  out.println("<br/>Resource Secret: <input type\"text\" name=\"secret\" value=\""+
		  //20120420 Ocultar informaci� en habilitar el debug http://jira.uoc.edu/jira/browse/BLTI_C-69
		  //BasicLTIUtilUocWrapper.htmlspecialchars(oauth_consumer_secret)+"\">\n");
		  "*******\">\n");
		  out.println("<br/><input type=\"submit\" value=\"Submit\">\n");
		  out.println("</fieldset><p>");
		  out.println("<fieldset><legend>LMS Administrator Data</legend>\n");
		  out.println("LMS name: <input type\"text\" name=\"org_desc\" value=\""+
		  BasicLTIUtilUocWrapper.htmlspecialchars(org_desc)+"\">\n");
		  out.println("<br/>LMS key: <input type\"text\" name=\"org_id\" value=\""+
		  BasicLTIUtilUocWrapper.htmlspecialchars(org_id)+"\">\n");
		  out.println("<br/>LMS secret: <input type\"text\" name=\"org_secret\" value=\""+
		  BasicLTIUtilUocWrapper.htmlspecialchars(org_secret)+"\">\n");
		  out.println("<br/>If both a resource secret and LMS secret are entered - the LMS secret is used.\n");
		  out.println("</fieldset>");
		  //Adding custom values
		  out.println("<p>Custom Parameters:");
		  out.println("<ul>");
		  for (int i=0; i<customParameters.length; i++) {
		  	out.println("<li>"+CUSTOM_PREFIX+customParameters[i][0]+"="+customParameters[i][1]+"</li>");
		  }
		  out.println("</ul></p>"); 
		  //Adding request params values
		  out.println("<p>Parameters From Request:");
		  out.println("<ul>");
		  String parameterName = null;
		  for (Enumeration<?> en=propertiesRequestParams.keys(); en.hasMoreElements();) {
	    	parameterName = (String)en.nextElement();
	    	 out.println("<li>"+CUSTOM_PREFIX+parameterName+"="+propertiesRequestParams.getProperty(parameterName)+"</li>");
		  }
		  out.println("</ul></p>");  
		  out.println("<input type=\"submit\" value=\"Submit\">\n");
		  out.println("</form>");
		  out.println("<p>Note that if you are launching to tool.jsp, it \n");
		  out.println("accepts a \"12345/secret\" as a valid resource key/secret.\n");
		  out.println("and lmsng.school.edu/secret as a valid LMS key/secret.\n");
		  out.println("<hr>");
	}

}
