<%-- 
    Document   : searchMeeting
    Created on : 24-mar-2014, 17:47:41
    Author     : Francesc Fernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="/WEB-INF/tlds/custom-functions.tld" prefix="fn" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn_general" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <script type="text/javascript" src="js/videochat.js" ></script>
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootbox.min.js"></script>
        <title><spring:message code="message.selectoption.search"/></title>
    </head>
    <body>
    <header id="search"><div class="container"><img src="css/images/logo_invers.png" alt="videochat"/></div>
            <% String locale = request.getParameter("lang"); %>

            <%String cat = "ca";%>       
            <%String es = "es";%> 
            <%String en = "en";%>
            <%String fr = "fr";%> 
            <%String ga = "ga";%> 
            <%String nl = "nl";%> 
            <%String pl = "pl";%> 
            <%String sv = "sv";%> 
            <%String it = "it";%> 
            <%String hr = "hr";%> 
            <%String de = "de";%> 
            <%String fi = "fi";%> 
        <div id="idiomes" class="col-md-3 col-md-offset-4">
            <form:form  name="lang_form" action="searchMeeting.htm" commandName="searchMeetingForm" modelAttribute="course" method="POST">
                <form:select onchange="changeLanguage(this.value)" path="lang" class="form-control">

                    <c:choose>                        
                        <c:when test="<%=en.equalsIgnoreCase(locale)%>" >
                            <form:option value="en" selected="true"><spring:message code="message.lang.english"/></form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="en" ><spring:message code="message.lang.english"/></form:option>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="<%=cat.equalsIgnoreCase(locale)%>" >
                            <form:option value="ca" selected="true"><spring:message code="message.lang.catalan"/></form:option>                            
                        </c:when>
                        <c:otherwise>
                            <form:option value="ca" ><spring:message code="message.lang.catalan"/></form:option>                            
                        </c:otherwise>
                    </c:choose> 

                    <c:choose>
                        <c:when test="<%=es.equalsIgnoreCase(locale)%>" >
                            <form:option value="es" selected="true"><spring:message code="message.lang.spanish"/></form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="es"><spring:message code="message.lang.spanish"/></form:option>
                        </c:otherwise>
                    </c:choose>
                                                       
                    <c:choose>
                        <c:when test="<%=pl.equalsIgnoreCase(locale)%>" >
                            <form:option value="pl" selected="true"><spring:message code="message.lang.polish"/></form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="pl"><spring:message code="message.lang.polish"/></form:option>
                        </c:otherwise>
                    </c:choose>
                        
                    <c:choose>
                        <c:when test="<%=nl.equalsIgnoreCase(locale)%>" >
                            <form:option value="nl" selected="true"><spring:message code="message.lang.dutch"/></form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="nl"><spring:message code="message.lang.dutch"/></form:option>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                      <c:when test="<%=sv.equalsIgnoreCase(locale)%>" >
                          <form:option value="sv" selected="true"><spring:message code="message.lang.swedish"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="sv"><spring:message code="message.lang.swedish"/></form:option>
                      </c:otherwise>
                  </c:choose>
                    <c:choose>
                      <c:when test="<%=ga.equalsIgnoreCase(locale)%>" >
                          <form:option value="ga" selected="true"><spring:message code="message.lang.irish"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="ga"><spring:message code="message.lang.irish"/></form:option>
                      </c:otherwise>
                  </c:choose>
                    <c:choose>
                      <c:when test="<%=fr.equalsIgnoreCase(locale)%>" >
                          <form:option value="fr" selected="true"><spring:message code="message.lang.french"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="fr"><spring:message code="message.lang.french"/></form:option>
                      </c:otherwise>
                  </c:choose>
                  <c:choose>
                      <c:when test="<%=it.equalsIgnoreCase(locale)%>" >
                          <form:option value="it" selected="true"><spring:message code="message.lang.italian"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="it"><spring:message code="message.lang.italian"/></form:option>
                      </c:otherwise>
                  </c:choose>
                    <c:choose>
                      <c:when test="<%=hr.equalsIgnoreCase(locale)%>" >
                          <form:option value="hr" selected="true"><spring:message code="message.lang.hrvatski"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="hr"><spring:message code="message.lang.hrvatski"/></form:option>
                      </c:otherwise>
                  </c:choose>
                    <c:choose>
                      <c:when test="<%=de.equalsIgnoreCase(locale)%>" >
                          <form:option value="de" selected="true"><spring:message code="message.lang.german"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="de"><spring:message code="message.lang.german"/></form:option>
                      </c:otherwise>
                  </c:choose>
                    <c:choose>
                      <c:when test="<%=fi.equalsIgnoreCase(locale)%>" >
                          <form:option value="fi" selected="true"><spring:message code="message.lang.finnish"/></form:option>
                      </c:when>
                      <c:otherwise>
                          <form:option value="fi"><spring:message code="message.lang.finnish"/></form:option>
                      </c:otherwise>
                  </c:choose>

                </form:select>
            </form:form>
        </div>

    </header>
    <div class="container">
        <h3><spring:message code="message.meetinglist.player"/></h3>
        <p><strong><spring:message code="message.meetinglist.text1"/></strong><spring:message code="message.meetinglist.text2"/>
        <c:choose>
            <c:when test="${show_mobile_connection}">
            <!-- Button trigger modal -->
            <button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModalData">
              <spring:message code="message.showMobileData"/>
            </button>

            <!-- Modal -->
            <div class="modal fade" id="myModalData" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel"><spring:message code="message.showMobileData"/></h4>
                  </div>
                  <div class="modal-body">
                       <c:set var="request"    value="${pageContext.request}"/>
                       <c:set var="scheme"     value="${request.scheme}"     />
                       <c:set var="serverName" value="${request.serverName}" />
                       <c:set var="port"       value=":${request.serverPort}" />

                        <c:if test="${port eq ':80' and fn_general:toLowerCase(scheme) eq 'http'}">
                            <c:remove var="port"/>
                        </c:if>
                        <c:if test="${port eq ':443' and fn_general:toLowerCase(scheme) eq 'https'}">
                            <c:remove var="port"/>
                        </c:if>
                        <p><strong><spring:message code="message.urlMobile"/>: ${scheme}://${serverName}${port}</strong> </p>
                        <p><strong><spring:message code="message.username"/>: ${user.getEmail()}</strong> </p>
                        <p><strong><spring:message code="message.token"/>: ${user.getToken_access()}</strong> </p>
                      </ul>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="message.close"/></button>
                  </div>
                </div>
              </div>
            </div>
            </c:when>
          </c:choose>
        
        </p>
        

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
                        <th class="borderW modif col4" scope="colgroup"><spring:message code="message.meetinglist.actions"/></th>
                        <th class="borderW modif col4" scope="colgroup"></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listMR}">
                        <tr>
                            <td><c:out value="${item.getTopic()}"/>
                                <c:if test="${item.getFinished()==0}"> <div class="alert alert-info"><spring:message code="meeting.notfinished"/></div></c:if>
                            </td>
                            <td><c:forEach items="${item.getParticipants()}" var="participant" varStatus="status">
                                    ${participant.getPk().getUser().getFullname()}
                                    <c:if test="${status.count<item.getParticipants().size()}">,</c:if>
                                </c:forEach></td>
                            <td>${item.getStart_record_txt()}</td>
                            <td>${item.getEnd_record_txt()}</td>
                            <td>${item.getTotal_time_txt()}</td>
                            <td>
                                <button  type="button" class="btn btn-primary" data-dismiss="modal" onclick="viewMeeting(${item.getId()})"><spring:message code="btn.view"/></button>
                            </td>
                          

                            
                          
                            <c:if test="${(fn:contains(listUM,item.getId()) || user_is_instructor )}">
                                <td >

                                    <button  type="button" class="btn btn-danger" data-dismiss="modal" onclick="deleteMeeting(${item.getId()})"><spring:message code="btn.title.delete"/></button>

                                </td>

                            </c:if>

                        </tr>
                    </c:forEach>  
                </tbody>
            </table>
        </div>
        <footer class="row"> 
            <div style="float: left; margin-top: 0pt; margin-left: 50px;text-align: justify; width: 600px;"><span style="font-size:9px;">This project has been funded with support from the Lifelong Learning Programme of the European Commission.  <br />
This site reflects only the views of the authors, and the European Commission cannot be held responsible for any use which may be made of the information contained therein.</span>
</div>
		 &nbsp;	<img src="css/images/EU_flag.jpg" alt="" />
        </footer>

    </div>

    <script>
        var deleteMeeting = function(idMeeting) {
            bootbox.confirm("<spring:message code="warning.delete.session"/>", function(result) {
             
                if(result){
                    location.href = "searchMeetingModified.htm?id=" + idMeeting;
                }
            });    
        }
        
        var viewMeeting = function(idMeeting){
             location.href = "player.htm?id="+idMeeting;            
        }
        $(document).ready(function() {
          try{
              window.moveTo(0, 0);
              window.resizeTo(screen.availWidth, screen.availHeight);
          }catch (e) {
          }
        });

    </script>


</body>
</html>