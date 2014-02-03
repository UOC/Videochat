<%@ page import="org.uoc.red5.videoconference.utils.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.uoc.red5.videoconference.*" %>
<%@ page import="java.io.File" %>
<%@ page import="org.red5.server.webapp.oflaDemo.DemoService" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
Map list = (HashMap)request.getAttribute("list");
System.out.println("list ---------> " + list);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Videoconference list</title>
</head>
<body>

		<h1>Videoconference list</h1>

<%
	Object[] keys =  list.keySet().toArray();
	for (int i=keys.length-1; i>=0; i--) {%>
		<% String key = (String)keys[i];%>
		<TABLE BORDER=1 WIDTH=300><b>Room:&nbsp;<%=key%></b>
		<TD WIDTH=100>
		<div style="overflow: auto; width: 700px;">
		<table class="grey4" width="100%" border="0" cellpadding="10" cellspacing="0">
		<% 
		Map list2 = (HashMap)list.get(key);
		for (int j=0; j<list2.size(); j++) {
			Map list3 = (HashMap)list2.get(String.valueOf(j));
			String url = (String)list3.get("url-video");
			String info = (String)list3.get("info-video");
			%>
			<%=j%>&nbsp;-&nbsp;<%=info%><br>
		<%}%>
		</table>
		</div>
		</TD>
		</TABLE><br>		
	<% } %>
		
</body>
</html>