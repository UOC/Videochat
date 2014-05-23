<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%

String server_name = request.getServerName();
String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();


//String language = Utils.getProperty("app.language");
String language = request.getParameter("lang");
VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script>
function popup(mylink, windowname) {
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=600,height=700,scrollbars=yes');
return false;
}

function search() {
	document.search_meeting_form.submit();
}
function view() {
	document.view_meeting_form.submit();
}
function access() {
	document.access_meeting_form.submit();
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css"/>
<title><%=vcl.getMessage("message.selectoption.title")%></title>
</head>


<% boolean isAuthenticated = CheckPermissionsLTI.isAuthenticated(request);
//check authenticated
if (!isAuthenticated) {
	//response.sendRedirect("/jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated");
%>	
	<head></head>
	<body>
	<h1>Not authenticated</h1>
	</body>
	</html>	
<%} else {%>
<body>
<jsp:include page="./logo.jsp" flush="true"/>
<div id="container">
	<div class="txtInfo"><span><a href="<%=url %>/jsp/help/Videoconference_English_Help.pdf" onClick="return popup(this,'Help Document')">Help Document</a></span></div>
	<div id="head">
   	  <div class="baner">
       	<h1><%=vcl.getMessage("message.selectoption.title")%></h1>
        </div>
	</div>
<form action="../Front" name="view_meeting_form">
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.selectoption.question")%></strong></span>
    </div>
        <ul class="botonera">
         <li><a href="javascript:view();"><span><%=vcl.getMessage("message.selectoption.create")%></span></a></li>
       </ul>  
<input type="hidden" name="control" value="view"/>
</form>


<form action="../Front" name="access_meeting_form">
        <ul class="botonera">
         <li><a href="javascript:access();"><span><%=vcl.getMessage("message.selectoption.access")%></span></a></li>
       </ul> 
<input type="hidden" name="control" value="access"/>
</form>

<form action="../Front" name="search_meeting_form">
        <ul class="botonera">
         <li><a href="javascript:search();"><span><%=vcl.getMessage("message.selectoption.search")%></span></a></li>
       </ul>  
 
<input type="hidden" name="control" value="search"/>
</form>

	</div>
</body>

<%}%>