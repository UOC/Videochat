<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<%
String message = request.getParameter("message");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css"/>
<title>Videoconference</title>
</head>

<body>
<jsp:include page="./logo.jsp" flush="true"/>
<div id="container">
	<div id="head">
   	  <div class="baner">
       	<h1>Message</h1>
        </div>
	</div>

<%
VideoConferenceLanguage vcl = new VideoConferenceLanguage("ca");
if(message.equals("login")){
%>  
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.room.inexistent")%></strong></span></div>
    
<% 
}else if(message.equals("max")){
%>
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.room.plena")%></strong></span></div>
<%
}else if(message.equals("error")){
%>
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.error.indeterminate")%></strong></span></div>
<%
}else if(message.equals("tancaDecrementa")){
%>
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.tanca.decrementa")%></strong></span></div>
<%
}
%>
       <ul class="botonera">
         <li><a href="#"><span>Back</span></a></li>
       </ul>  
</div>
</body>
</html>

