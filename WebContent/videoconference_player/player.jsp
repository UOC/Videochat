<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../jsp/default.css"/>
<title>Videoconference Player</title>
</head>

<%
	//String language = Utils.getProperty("app.language");
	String language = request.getParameter("lang");
	VideoConferenceLanguage vcl = new VideoConferenceLanguage(language);
	//String serverName = Utils.getProperty("server_name");
	String server_name = request.getServerName();
	String url = "http"+(request.isSecure()?"s":"")+"://" + server_name + ":" + request.getServerPort() + request.getContextPath();
	String urlSearch = url + "/SearchMeeting";
%>

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
	
</script>


<body>

<div id="container">
<div class="txtInfo"><span><a href="<%=urlSearch %>"><%=vcl.getMessage("message.player.back")%></a></span></div>
</div>    


<script type="text/javascript" src="swfobject.js"></script>

<div align="center" id='flash_player'>
  <p class="Estilo1">&nbsp;</p>
  <p><%=vcl.getMessage("message.player.flash")%></p>
</div>
<div align="center"></div>
<script type='text/javascript'>

//get the current URL
var url = window.location.toString();
//get the parameters
url.match(/\?(.+)$/);
var params = RegExp.$1;
// split up the query string and store in an
// associative array
var params = params.split("&");
//alert("Params:"+params);
var queryStringList = {}; 

var tmp1 = params[0].split("=");
queryStringList[tmp1[0]] = unescape(tmp1[1]);

//alert(queryStringList[tmp1[0]]);
	
var tmp2 = params[1].split("=");
queryStringList[tmp2[0]] = unescape(tmp2[1]);

//alert(queryStringList[tmp2[0]]);

var tmp4 = params[3].split("=");
queryStringList[tmp4[0]] = unescape(tmp4[1]);


// Provisional Plan B Juliol 2011 - Se añade var tmp3 para alojar id_meeting

var tmp3 = params[2].split("=");
queryStringList[tmp3[0]] = unescape(tmp3[1]);

var tmp5 = params[4].split("=");
queryStringList[tmp5[0]] = unescape(tmp5[1]);
lang_code = queryStringList[tmp5[0]];

//alert(queryStringList[tmp3[0]]);
	

// print all querystring in key value pairs

var so_video2= new SWFObject('player.swf', 'mi_pelicula_video2', '850', '620', '9', '#F7F7F7');	
so_video2.addParam('wmode');
so_video2.addVariable('width','800');
so_video2.addVariable('height','700');

//for(var i in queryStringList){
//valor_entrada[x]=queryStringList[i];
//	x++;

//}
// Provisional Plan B Juliol 2011 - Se añade id_meeting a la lista de parametros
so_video2.addVariable("id_meeting",queryStringList[tmp3[0]]);
so_video2.addVariable("rtmp",queryStringList[tmp2[0]]);
so_video2.addVariable("scope",queryStringList[tmp1[0]]);
so_video2.addVariable("lang",queryStringList[tmp4[0]]);  /////////////////////////////////////  LANGUAGE CODE HERE!!!!

so_video2.write('flash_player');

</script>



</body>
</html>
