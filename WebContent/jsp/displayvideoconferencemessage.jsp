<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css"/>
<title>Videoconference</title>
</head>
<body>
<jsp:include page="./logo.jsp" flush="true"/>

<%
String message = request.getParameter("message");
//String language = Utils.getProperty("app.language");
String language = request.getParameter("lang");
if (language == null || language.equals("")) language = "en"; // per defecte english
VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);
//String serverName = Utils.getProperty("server_name");
String server_name = request.getServerName();
String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();
%>

<div id="container">
	<div id="head">
   	  <div class="baner">
       	<h1>Message</h1>
        </div>
	</div>
    <div class="txtInfo"><span><strong><%=vcl.getMessage(message)%></strong></span>
    </div>
    
</div>


</body>
</html>