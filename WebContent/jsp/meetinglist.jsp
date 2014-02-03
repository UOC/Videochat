<%@ page import="java.util.*" %>
<%@ page import="org.uoc.red5.videoconference.pojo.Meeting" %>
<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>


<%
	//String language = Utils.getProperty("app.language");
	//String language_ = (String)request.getParameter("lang");
	String language = request.getParameter("lang");
	VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);

	String s = (String)request.getParameter("s");
	String context = Utils.comprobaNull(request.getParameter("context"));
	String topic = Utils.comprobaNull(request.getParameter("topic"));
	String participants = Utils.comprobaNull(request.getParameter("participants"));
	String dataInici = Utils.comprobaNull(request.getParameter("datainici"));
	String dataFinal = Utils.comprobaNull(request.getParameter("datafinal"));
	String instanceId = request.getParameter("instanceId");

	//String serverName = Utils.getProperty("server_name");
	String server_name = request.getServerName();
	String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();

	if (topic == null) topic = "";
	if (participants == null) participants = "";
	if (dataInici == null) dataInici = "";
	if (dataFinal == null) dataFinal = "";
	
	List meetings = (List)request.getAttribute("meetings");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>
function search() {
	document.search_meeting_form.submit();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="./jsp/default.css"/>
<title><%=vcl.getMessage("message.meetinglist.title")%></title>
</head>

<body>
<img src="./jsp/img/logo_videochat.png" height="85" width="1300"/>
<div id="container">
	<div id="head">
   	  <div class="baner">
       	<h1><%=vcl.getMessage("message.meetinglist.player")%></h1>
   	  </div>
	</div>
        <div class="txtInfo"><span><strong><%=vcl.getMessage("message.meetinglist.text1")%></strong><%=vcl.getMessage("message.meetinglist.text2")%></span>
    </div>
    
    <form name="search_meeting_form" action="Front">
        <span><strong><%=vcl.getMessage("message.meetinglist.topic")%></strong></span><br>
		<span><input type="text" name="topic" id="idtopic" value="<%=topic %>" size="30"/></span><br>
      	<span><strong><%=vcl.getMessage("message.meetinglist.participants")%></strong></span><br>
		<span><input type="text" name="participants" id="idparticipant" value="<%=participants %>" size="30"/></span><br>
        <span><strong><%=vcl.getMessage("message.meetinglist.dates")%></strong></span><br>
		<span><input type="text" name="datainici" id="iddata" value="<%=dataInici %>" size="15"/></span>
		<span>/</span>
		<span><input type="text" name="datafinal" id="iddata" value="<%=dataFinal %>" size="15"/></span>
		<input type="hidden" name="control" value="search"/>
		<!-- input type="hidden" name="context" value="<%=context %>"/>
		<input type="hidden" name="s" value="<%=s %>"/>
		<input type="hidden" name="container" value="DOMAIN.<%=context %>"/>
		<input type="hidden" name="instanceId" value="<%=instanceId %>"/> -->
	</form>

           <ul class="botonera">
         <li><a href="javascript:search();"><span><%=vcl.getMessage("message.meetinglist.button")%></span></a></li>
       </ul>  
 
    	  <div class="baner"></div>
          <div class="txtInfo2"><span><strong><%=vcl.getMessage("message.meetinglist.text3")%></strong><%=vcl.getMessage("message.meetinglist.text4")%></span>
          </div>
	<div class="tabla">
         <table class="fitxers" width="100%" border="0" cellspacing="0" summary="Explicación de la tabla">
  <thead >
  <tr>
    <th class="borderW selec col1" scope="col"><%=vcl.getMessage("message.meetinglist.topic")%></th>
    <th class="borderW mida  col2" scope="col"><%=vcl.getMessage("message.meetinglist.participants")%></th>
    <th class="borderW modif col3" scope="col"><%=vcl.getMessage("message.meetinglist.from")%></th>
    <th class="borderW modif col4" scope="col"><%=vcl.getMessage("message.meetinglist.to")%></th>
    <th class="borderW modif col5" scope="col"><%=vcl.getMessage("message.meetinglist.length")%></th>
    <th class="borderW modif col5" scope="col"><%=vcl.getMessage("message.meetinglist.view")%></th>
  </tr>
    </thead>
  
  <%
  
  	String di = "";
  	String df = "";
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	for (int i=0; i<meetings.size(); i++) {
		Meeting meeting = (Meeting)meetings.get(i);
		String id_meeting = meeting.getId();
		String topic_ = meeting.getTopic();
		String id_context = meeting.getId_context();
		String participants_ = meeting.getParticipants();
		Date dataInici_ = meeting.getBegin();
		Date dataFinal_ = meeting.getEnd();

		
		if (dataInici_ != null && !dataInici_.equals("") && dataFinal_ != null && !dataFinal_.equals("")) {
			di = formatter.format(dataInici_);
			df = formatter.format(dataFinal_);			
		}
		String duracio = Utils.duracioMeeting(dataInici_, dataFinal_);
%>
<tr>
<td><%=Utils.comprobaNull(topic_)%></td>
<td><%=Utils.comprobaNull(participants_)%></td>
<td><%=di%></td>
<td><%=df%></td>
<td><%=Utils.comprobaNull(duracio)%></td>
<td><a href="<%=url %>/Front?control=play&id_meeting=<%=id_meeting%>"><%=vcl.getMessage("message.meetinglist.view")%></a></td>
</tr>
<%}%>
    
  
</table>

    </div>    
</div>

</body>
</html>
