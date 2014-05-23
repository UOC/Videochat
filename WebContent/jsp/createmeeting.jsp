<%@ page import="org.uoc.red5.videoconference.utils.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>
function create() {
	if (valida_dades())
		document.create_meeting_form.submit();
}

function valida_dades() {

	var resultat = false;
	var missatge = "";

	if (document.create_meeting_form.topic.value != "" && document.create_meeting_form.description.value != "") {
		resultat = true;
	} else {
		missatge = "topic and description should be informed";
	}	 
	
	if (!resultat) {
		alert(missatge);
	}
	
	return resultat;
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css"/>
<%
	//String language = Utils.getProperty("app.language");
	String language = request.getParameter("lang");
	VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);

	String context = request.getParameter("context");
	String s = (String)request.getParameter("s");
	String id = (String)request.getParameter("id");
	String instanceId = request.getParameter("instanceId");
	//String serverName = Utils.getProperty("server_name");
	String server_name = request.getServerName();
	String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();
%>
<title><%=vcl.getMessage("message.createmeeting.title")%></title>
</head>
<body>
<jsp:include page="./logo.jsp" flush="true"/>
<form name="create_meeting_form" action="../Front">
<div id="container">
	<div class="txtInfo"><span><a href="<%=url %>/jsp/selectoption.jsp"><%=vcl.getMessage("message.back")%></a></span></div>
	<%if (id == null || id.equals("")) {%>
	<div id="head">
   	  <div class="baner">
       	<h1><%=vcl.getMessage("message.createmeeting.title")%></h1>
        </div>
	</div>
    <div class="txtInfo"><span><strong><%=vcl.getMessage("message.createmeeting.text1")%></strong><%=vcl.getMessage("message.createmeeting.text2")%></span>
    </div>
          <span><strong><%=vcl.getMessage("message.createmeeting.topic")%></strong></span>
      <div><input type="text" name="topic" id="idtopic" value="" size="25" maxlength="24"/></div>
      <span><strong><%=vcl.getMessage("message.createmeeting.description")%></strong></span>
      <div><input type="text" name="description" id="iddescripcio" value="" size="100" maxlength="150"/><br/></div>
      
           <ul class="botonera">
         <li><a href="javascript:create();"><span><%=vcl.getMessage("message.createmeeting.button")%></span></a></li>
       </ul>  
 	<%} %>
 	<%if (id != null && !id.equals("null")) {%>
		<div id="head">
   		  <div class="baner">
       		<h1><%=vcl.getMessage("message.createmeeting.created")%></h1>
         </div>
		</div>
    	<div class="txtInfo"><span><strong><%=vcl.getMessage("message.createmeeting.text3")%></strong><%=vcl.getMessage("message.createmeeting.text4")%></span>     
  		</div>
  		<center><font size="4" color="red"><%=id %></font></center>
		<div class="txtInfo"><span><a href="<%=url %>/jsp/accessmeeting.jsp"><%=vcl.getMessage("message.createmeeting.access")%></a></span></div>
	<%} %>    
</div>


<input type="hidden" name="control" value="create"/>
<!--input type="hidden" name="context" value="<%=context %>"/>
<input type="hidden" name="container" value="DOMAIN.<%=context %>"/>
<input type="hidden" name="instanceId" value="<%=instanceId %>"/>
<input type="hidden" name="s" value="<%=s%>"/> -->

</form>
</body>
</html>
