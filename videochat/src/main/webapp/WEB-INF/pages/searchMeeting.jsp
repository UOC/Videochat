<%-- 
    Document   : searchMeeting
    Created on : 24-mar-2014, 17:47:41
    Author     : Francesc Fernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <script>
function search() {
	document.search_meeting_form.submit();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="http://videoconference.speakapps.org:8080/red5/jsp/default.css"/>
<title>Search session</title>
    </head>
    <body>
        <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-35476259-6', 'speakapps.org');
  ga('send', 'pageview');

</script> 
<img src="http://videoconference.speakapps.org:8080/red5/jsp/img/logo_videochat.png" height="85" width="1300"/>
<div id="container">
	<div id="head">
   	  <div class="baner">
       	<h1>Player</h1>
   	  </div>
	</div>
        <div class="txtInfo"><span><strong>Select a session to play.</strong>Use the following search options to find a session to play.</span>
    </div>
    
    <form name="search_meeting_form" action="">
        <span><strong>Topic</strong></span><br>
		<span><input type="text" name="topic" id="idtopic" value="" size="30"/></span><br>
      	<span><strong>Participants</strong></span><br>
		<span><input type="text" name="participants" id="idparticipant" value="" size="30"/></span><br>
        <span><strong>From (dd/mm/yyyy) To (dd/mm/yyyy)</strong></span><br>
		<span><input type="text" name="datainici" id="iddata" value="" size="15"/></span>
		<span>/</span>
		<span><input type="text" name="datafinal" id="iddata" value="" size="15"/></span>
		<input type="hidden" name="control" value="search"/>
		<!-- input type="hidden" name="context" value="null"/>
		<input type="hidden" name="s" value="null"/>
		<input type="hidden" name="container" value="DOMAIN.null"/>
		<input type="hidden" name="instanceId" value="null"/> -->
	</form>

           <ul class="botonera">
         <li><a href="javascript:search();"><span>Search</span></a></li>
       </ul>  
 
    	  <div class="baner"></div>
          <div class="txtInfo2"><span><strong>Results.</strong>Select the session you are interested to play.</span>
          </div>
	<div class="tabla">
         <table class="fitxers" width="100%" border="0" cellspacing="0" summary="Explicación de la tabla">
  <thead >
  <tr>
    <th class="borderW selec col1" scope="col">Topic</th>
    <th class="borderW mida  col2" scope="col">Participants</th>
    <th class="borderW modif col3" scope="col">From (date)</th>
    <th class="borderW modif col4" scope="col">To (date)</th>
    <th class="borderW modif col5" scope="col">Length</th>
    <th class="borderW modif col5" scope="col">View</th>
  </tr>
    </thead>
     
 <c:forEach var="item" items="${listMR}">
<tr>
<td><c:out value="${item.getTopic()}"/></td>
<td><c:forEach items="${item.getParticipants()}" var="participant">
        ${participant.getPk().getUser().getFullname()},  
     </c:forEach></td>
<td>${item.getStart_meeting_txt()}</td>
<td>${item.getEnd_meeting_txt()}</td>
<td>${item.getTotal_time_txt()}</td>
<td><a href="player.htm?id=${item.getId()}">View</a></td>
    </tr>
</c:forEach>  
</table>

    </div>    
</div>

</body>
    </body>
</html>