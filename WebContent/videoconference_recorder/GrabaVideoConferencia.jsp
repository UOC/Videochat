<%@ page import="org.uoc.red5.videoconference.utils.*,edu.uoc.lti.LTIEnvironment" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
LTIEnvironment lti = new LTIEnvironment(request);
if (lti.isAuthenticated()) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<META http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<!-- <link rel="stylesheet" type="text/css" href="default.css"/> -->
<title>VideoConference Recorder</title>
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<script type='text/javascript'>

	function flashMovie(movieName)  
	{  
		if(window.document[movieName])  
		{  
			return window.document[movieName];  
		}  
		else  
		{  
			return document.getElementById(movieName);    
		}  
		  
	}  

	function sendToFlash(value) {
		flashMovie("mi_pelicula_video2").sendToFlash(value);
	}
	
	function flashReady(){
		sendToFlash(lang_code);
	}

	function redirect_to_player(url) {
		window.open(url, "_self");
	}
</script>

<% 
	String context = request.getParameter("scope");
	String fullName = lti.getFullName();
	String email = request.getParameter("email");
	String rtmpCon = request.getParameter("rtmpCon");
	String topic = request.getParameter("topic");
	String text_description = request.getParameter("text_description");
	String id_meeting = request.getParameter("id_meeting");
	String s = request.getParameter("s");
	String control = request.getParameter("control");
	String lang = request.getParameter("lang");
	VideoConferenceLanguage vcl = new VideoConferenceLanguage(lang);
%>
</head>

<body>
<script language="javascript">AC_FL_RunContent = 0;</script>
<script type="text/javascript" src="swfobject.js"></script>
<script type='text/javascript' src='../js/setup.js'></script>

<div align="center" id='flash_player'>
  <p class="Estilo1">&nbsp;</p>
  <p><%=vcl.getMessage("message.grabavideoconferencia.flash")%></p>
</div>
<div align="center"></div>
<script type='text/javascript'> 

var url = window.location.toString();
//get the parameters
url.match(/\?(.+)$/);
var params = RegExp.$1;
// split up the query string and store in an
// associative array
var params = params.split("&");
var queryStringList = {};
var scope;
var fullName;
var email;
var rtmpCon;
var topic;
var text_description;
var id_meeting;
var s;
var lang;


for(var i=0;i<params.length;i++)
{
	var tmp = params[i].split("=");
	queryStringList[tmp[0]] = unescape(tmp[1]);
}

var so_video2= new SWFObject('GrabaVideoConferencia.swf', 'GrabaVideoConferencia', '850', '620', '9', '#F7F7F7');	
	so_video2.addParam('wmode','transparent');
	so_video2.addVariable('width','850');
	so_video2.addVariable('height','620');
	so_video2.addVariable('server_converter',server_converter); 
	so_video2.addVariable('connection_server',connection_server);
		
	for(var i in queryStringList){
   	    if(i=='scope'){
    		so_video2.addVariable('scope',queryStringList[i]);	
    		scope=queryStringList[i];
    	}
    	if(i=='fullName'){
    		so_video2.addVariable('fullName',"<%= fullName %>");
    		fullName="<%= fullName %>";
    	}
    	if(i=='email'){
    		so_video2.addVariable('email',queryStringList[i]);
    		email=queryStringList[i];
    	}
    	if(i=='rtmpCon'){
    		so_video2.addVariable('rtmpCon',queryStringList[i]);
    		rtmpCon=queryStringList[i];
    	}
    	if(i=='topic'){
    		so_video2.addVariable('topic',queryStringList[i]);
    		topic=queryStringList[i];
    	}
    	if(i=='text_description'){
    		so_video2.addVariable('text_description',queryStringList[i]);
    		topic=queryStringList[i];
    	}
    	if(i=='id_meeting'){
    		so_video2.addVariable('id_meeting',queryStringList[i]);
    		topic=queryStringList[i];
    	} 
    	if(i=='s'){
    		so_video2.addVariable('s',queryStringList[i]);
    		topic=queryStringList[i];
    	}
		if(i=='lang'){
			so_video2.addVariable('lang', queryStringList[i]);
			lang = queryStringList[i];
		}
	}

so_video2.write('flash_player');
</script>
<script>

function tanca_finestra_decrementa_usuari()
{ 
	var url = window.location.toString();
	//get the parameters
	url.match(/\?(.+)$/);
	var params = RegExp.$1;
	// split up the query string and store in an
	// associative array
	var params = params.split("&");
	var queryStringList = {};
	var scope;
	var fullName;
	var email; 
	var rtmpCon;
	var topic;
	var text_description;
	var id_meeting;
	var s;
	var lang;
 
	for(var i=0;i<params.length;i++)
	{
		var tmp = params[i].split("=");
		queryStringList[tmp[0]] = unescape(tmp[1]);
	}
    for(var i in queryStringList){
   	    if(i=='scope'){
    		scope=queryStringList[i];
    	}
    	if(i=='fullName'){
    		fullName="<%= fullName %>"; 
    	}
    	if(i=='email'){
    		email=queryStringList[i];
    	}
    	if(i=='rtmpCon'){
    		rtmpCon=queryStringList[i];
    	}
    	if(i=='topic'){
    		topic=queryStringList[i];  
    	} 
    	if(i=='text_description'){
    		text_description=queryStringList[i];  
    	} 
    	if(i=='id_meeting'){
    		id_meeting=queryStringList[i];  
    	} 
    	if(i=='s'){
    		s=queryStringList[i];  
    	} 
    	if(i=='lang'){
    		lang=queryStringList[i];  
    	} 

    }  
	
    //window.open(server_converter+'Front?control=tancaDecrementa'+'&context='+scope + '&fullName='+fullName + '&email=' + email + '&rtmpCon='+ rtmpCon + '&topic=' + topic + '&text_description=' + text_description +  '&id_meeting=' + id_meeting + "&s=" + s);
    //window.close();
    //var cadena = server_converter+'Front?control=tancaDecrementa'+'&context='+scope + '&fullName='+fullName + '&email=' + email + '&rtmpCon='+ rtmpCon + '&topic=' + topic + '&text_description=' + text_description +  '&id_meeting=' + id_meeting + "&s=" + s;
    //document.forms["myform"].submit();
    document.forms["tanca_decrementa"].submit();
} 
</script>   

<form name="tanca_decrementa" action="../Front">
	<input type="hidden" name="control" value="tancaDecrementa"/>
	<input type="hidden" name="context" value="<%=context%>"/>
	<input type="hidden" name="fullName" value="<%=fullName%>"/>
	<input type="hidden" name="email" value="<%=email%>"/>
	<input type="hidden" name="rtmpCon" value="<%=rtmpCon%>"/>
	<input type="hidden" name="topic" value="<%=topic%>"/>
	<input type="hidden" name="text_description" value="<%=text_description%>"/>
	<input type="hidden" name="id_meeting" value="<%=id_meeting%>"/>
	<input type="hidden" name="s" value="<%=s%>"/>
	<input type="hidden" name="lang" value="<%=lang%>"/>
</form>

</body> 

</html>
<% } else {

	response.sendRedirect("jsp/displayvideoconferencemessage.jsp?message=message.notauthenticated");
} %>