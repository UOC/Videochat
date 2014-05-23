<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<%@ page import="edu.uoc.lti.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>
function inmeeting() {
	document.in_meeting_form.submit();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css"/>
<%
	LTIEnvironment lti = new LTIEnvironment(request);
	Utils.getLocale(lti.getLocale());
	
	
	//String language = Utils.getProperty("app.language");
	String language = request.getParameter("lang");
	VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);

	String context = request.getParameter("context");
	String s = (String)request.getParameter("s");
	String instanceId = request.getParameter("instanceId");
	//String serverName = Utils.getProperty("server_name");
	String server_name = request.getServerName();
	String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();
%>
<title><%=vcl.getMessage("message.accessmeeting.title")%></title>
</head>
<body>
<jsp:include page="./logo.jsp" flush="true"/>
<form name="in_meeting_form" action="../AccessMeeting">
<div id="container">
	<div class="txtInfo"><span><a href="<%=url %>/jsp/selectoption.jsp"><%=vcl.getMessage("message.back")%></a></span></div>
	<div id="head">
   	  <div class="baner">
       	<h1><%=vcl.getMessage("message.accessmeeting.title")%></h1>
        </div>
	</div>
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.accessmeeting.text1")%></strong><%=vcl.getMessage("message.accessmeeting.text2")%></span>
    </div>
          <span><strong><%=vcl.getMessage("message.accessmeeting.identifier")%></strong></span>
      <div><input type="text" name="id" id="id_meeting" value="" size="15"/></div>
      
           <ul class="botonera">
         <li><a href="javascript:inmeeting();"><span><%=vcl.getMessage("message.accessmeeting.button")%></span></a></li>
       </ul>  
</div>

<!-- input type="hidden" name="context" value="<%=context %>"/>
<input type="hidden" name="container" value="DOMAIN.<%=context %>"/>
<input type="hidden" name="instanceId" value="<%=instanceId %>"/>
<input type="hidden" name="s" value="<%=s%>"/> -->

</form>

</body>
</html>
