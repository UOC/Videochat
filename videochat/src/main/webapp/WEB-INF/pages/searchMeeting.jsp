<%-- 
    Document   : searchMeeting
    Created on : 24-mar-2014, 17:47:41
    Author     : Francesc Fernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
    <head>
       
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/general.css">
    <script type="text/javascript" src="js/videochat.js" ></script>
    <title><spring:message code="message.selectoption.search"/></title>
    </head>
    <body>
    <header id="search"><div class="container"><img src="css/images/logo_invers.png" alt="videochat"/></div>
    
              <div id="idiomes" class="col-md-3 col-md-offset-4">
                    <form:form  name="lang_form" action="searchMeeting.htm" commandName="searchMeetingForm" modelAttribute="course" method="POST">
                        <form:select onchange="changeLanguage(this.value)" path="lang" class="form-control">
                            <form:option value="en"><spring:message code="message.lang.english"/></form:option>
                            <form:option value="ca"><spring:message code="message.lang.catalan"/></form:option>
                            <form:option value="es"><spring:message code="message.lang.spanish"/></form:option>
                            <form:option value="en"><spring:message code="message.lang.polish"/></form:option>
                            <form:option value="en"><spring:message code="message.lang.dutch"/></form:option>
                            <form:option value="en"><spring:message code="message.lang.swedish"/></form:option>
                            <form:option value="en"><spring:message code="message.lang.irish"/></form:option>
                        </form:select>
                    </form:form>
              </div>
    
    </header>
    <div class="container">
        <h3><spring:message code="message.meetinglist.player"/></h3>
        <p><strong><spring:message code="message.meetinglist.text1"/></strong><spring:message code="message.meetinglist.text2"/></p>

        <form:form name="search_meeting_form" action="searchMeeting.htm" commandName="searchMeetingForm" modelAttribute="searchMeeting" method="POST">
                <div class="form-group">
                    <label for="topic" class="control-label"><spring:message code="message.meetinglist.topic"/></label>
                    <form:input type="text" path="topic" class="form-control" id="topic"/>
                </div>
                <div class="form-group">
                    <label for="participants" class="control-label"><spring:message code="message.participants"/></label>
                    <form:input type="text" path="participants" class="form-control" id="participants"/>
                </div>
                <div class="row">
                    <div class="form-group col-xs-6">
                        <label for="from" class="control-label"><spring:message code="message.meetinglist.from"/></label>
                        <form:input type="date" path="start_meeting" class="form-control" id="from" placeholder="dd/mm/yyyy" />
                    </div>
                    <div class="form-group col-xs-6">
                        <label for="to" class="control-label"><spring:message code="message.meetinglist.to"/></label>
                        <form:input type="date" path="end_meeting" class="form-control" id="to" placeholder="dd/mm/yyyy"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="room_id" class="control-label"><spring:message code="message.select.room"/></label>
                    <select name="room_id" class="form-control">
                        <option value="-1"><spring:message code="message.all"/></option>
                         <c:forEach var="item" items="${listRooms}">
                             <option value="<c:out value="${item.getId()}"/>" <c:if test="${item.getId()==searchMeeting.getRoom_Id()}"> selected </c:if>><c:out value="${item.getLabel()}"/></option>
                         </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-warning"><spring:message code="message.meetinglist.button"/></button>
                </form:form>
        <hr/>
        <p><strong><spring:message code="message.meetinglist.text3"/></strong><spring:message code="message.meetinglist.text4"/></p>
        <div class="table-responsive">
            <table class="table table-striped table-condensed table-hover">
                <thead >
                    <tr>
                        <th class="borderW selec col1" scope="col"><spring:message code="label.topic"/></th>
                        <th class="borderW mida  col2" scope="col"><spring:message code="message.participants"/></th>
                        <th class="borderW modif col3" scope="col"><spring:message code="message.meetinglist.from"/></th>
                        <th class="borderW modif col4" scope="col"><spring:message code="message.meetinglist.to"/></th>
                        <th class="borderW modif col5" scope="col"><spring:message code="message.meetinglist.length"/></th>
                        <th class="borderW modif col5" scope="col"><spring:message code="message.meetinglist.view"/></th>
                    </tr>
                </thead>
                <tbody>
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
            </tbody>
        </table>
        </div>
    </div>
</body>
</html>