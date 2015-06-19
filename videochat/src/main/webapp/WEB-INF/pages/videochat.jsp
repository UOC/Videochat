<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- 
    Document   : videochat
    Created on : 25/03/2014, 21:58:38
    Author     : antonibertranbellido
--%>
<!DOCTYPE html>
<% int videochat_version = 10; %>

<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><spring:message code="header.videochat.recorder"/></title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <!-- Optional theme -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/general.css?version=<c:out value="${videochat_version}"/>">
        <link href="js/bootstrap-tour/css/bootstrap-tour-standalone.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/swfobject.js" ></script>
        <script type="text/javascript" src="js/videochat.js?version=<c:out value="${videochat_version}"/>" ></script>
        <!-- VIDEO PLAYER JS -->
        <script type="text/javascript" src="js/jwplayer/jwplayer.js"></script>
        <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
    </head> 
    <body>
        <% String locale = request.getParameter("lang"); 
        int add_new_line = 2;
        int max_participants = edu.uoc.util.Constants.MAX_PARTICIPANTS;
            try{
                if (request.getAttribute("max_participants")!=null){
                    max_participants = ((Integer)request.getAttribute("max_participants")).intValue();
                }
            }
            catch (NumberFormatException nfe) {
                //nothing
            }

        int size_of_video = 4;
        int width_of_video = 220;
        int height_of_video = 141;
        String concat_size_video = "";
        switch(max_participants){
            case 2: //only one
                add_new_line = 2;
                size_of_video = 6;
                width_of_video = 330;
                height_of_video = 210;
                concat_size_video = "_big";
                break;
            case 4: 
                add_new_line = 2;
                size_of_video = 6;
                width_of_video = 330;
                height_of_video = 210;
                concat_size_video = "_big";
                break;
            default: 
                add_new_line = max_participants /2;
        }
        pageContext.setAttribute("add_new_line", add_new_line);
        pageContext.setAttribute("size_of_video", size_of_video);
        pageContext.setAttribute("width_of_video", width_of_video);
        pageContext.setAttribute("concat_size_video", concat_size_video);
        pageContext.setAttribute("height_of_video", height_of_video);
        %>

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
        <%String fi = "fi";%> 
        <%String de = "de";%> 

        
<div class="modal fade" id="modal_headphones" tabindex="-1" role="dialog" aria-labelledby="modal_headphonesLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="modal_headphonesLabel"><spring:message code="title.use_headphones"/></h4>
      </div>
      <div class="modal-body">
            <img src="css/images/headphones.png">
            <div style="float:left">
            <p><spring:message code="message.use_headphones"/></p>
            <p><input type="checkbox" id="not_show_again" /> <spring:message code="message.not_show_again"/></p>
            </div>            
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="message.cancel"/></button>
      </div>
    </div>
  </div>
</div>
      <%-- abertranb - 20150611 - disabled archivebutton --%>
        <%-- modal form gravació-->
        <div class="modal fade" id="formModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><spring:message code="txt.m1.important"/></h4>
                    </div>
                    <div class="modal-body">
                        <p><strong><spring:message code="txt.m4.confirm"/></strong></p>
                        <div><strong><spring:message code="txt.topic"/></strong>:<input type="text" name="topic_meeting" id="topic_meeting" value="" /></div>
                        <div><strong><spring:message code="txt.description"/></strong>:<input type="text" name="description_meeting" id="description_meeting" value="" /></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-warning"><spring:message code="btn.canceltxt"/></button>
                        <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="closeMeetingRequest()"><spring:message code="btn.saveclose"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
        <!-- /.modal --%>
        <%-- END - abertranb - 20150611 - disabled archivebutton --%>
          <!-- modal del botó test -->
        <div class="modal fade" id="test-environment">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><spring:message code="txt.title.test"/></h4>
                    </div>
                    <div class="modal-body" id="modal_test_environment_body">
                        <div><strong><spring:message code="txt.msg.test"/></strong></div>
                        <div id="test-flash-content">
                            <p><spring:message code="message.grabavideoconferencia.flash"/></p>
                            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal" data-dismiss="modal"><spring:message code="message.cancel"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
        <!-- END modal del botó test -->
        <div class="container">
                    <div class="row">
                        <div class="col-md-3 col-md-offset-9">
                            <button type="button" id="button-test-environment" data-toggle="modal" data-target="#test-environment" class="btn btn-warning" title="<spring:message code="btn.title.test"/>"><i class="glyphicon glyphicon-info-sign"/></i>&nbsp;<spring:message code="btn.title.test"/></button>
                            <button type="button" id="button-help" class="btn btn-info" title="<spring:message code="btn.title.help"/>"><i class="glyphicon glyphicon-question-sign"/></i>&nbsp;<spring:message code="btn.title.help"/></button>
                        </div>
                    </div>  
            <header class="row">
                <div class="col-md-4"><img src="css/images/logo.png" alt="videochat"/></div>
                <div id="idiomes" class="col-md-3 col-md-offset-4">
                    <form:form  name="lang_form" action="videochat.htm" commandName="videochat" modelAttribute="course" method="POST">
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
                                    <form:option value="hr" selected="true"><spring:message code="message.lang.italian"/></form:option>
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
                <c:choose>
                   <c:when test="${not disable_back_button}" >
                <div id="close" class="col-md-1">
                    <span class="glyphicon glyphicon-remove btn-exit-videochat" title="<spring:message code="btn.title.exit"/>" id="button-exit"></span>
                </div>
                   </c:when>
                </c:choose>
            </header>
        <c:choose>
            <c:when test="${not disable_back_button}" >
                <div class="row">
                    <div class="col-md-2">
                        <h4><c:out value="${sMeeting.getId_room().getLabel()}"/></h4>
                    </div>
                    <div class="col-md-10 dashed">
                        <div class="row">
                            <div class="col-md-11 col-md-offset-1">
                                <div class="row">
                                    <label><spring:message code="label.topic"/>:</label> 
                                    <a href="#" id="topic_metting_record" data-type="text" data-pk="${sMeeting.getId()}" data-url="rest/topic.json" data-title="<spring:message code="txt.m4.confirm"/>">${sMeeting.getTopic()}</a>
                                    <a href="#" id="topic_metting_record_pencil"><i class="glyphicon glyphicon-pencil"></i></a>
                                </div>
                                <div class="row">
                                    <label><spring:message code="label.description"/>:</label> 
                                    <a href="#" id="description_metting_record" data-type="text" data-pk="${sMeeting.getId()}" data-url="rest/description.json" data-title="<spring:message code="label.description"/>">${sMeeting.getDescription()}</a>
                                    <a href="#" id="description_metting_record_pencil"><i class="glyphicon glyphicon-pencil"></i></a>
                                </div>
                            </div>        
                        </div>        
                    </div>
                </div>
            </c:when>
        </c:choose>    
            <div id="messages"></div>
            <div class="row wrapper_buttons">   
                <div class="col-md-3 col-xs-7">
                    <!-- modal del botó RECORD -->
                    <div class="modal fade" id="record">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title"><spring:message code="txt.m1.important"/></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-10 col-md-offset-1">
                                            <strong><spring:message code="txt.m1.confirm"/></strong>
                                        </div>
                                    </div>    
                                    <div class="row">
                                        <div class="col-md-6 col-md-offset-3">
                                            <div class="alert alert-warning" role="alert">
                                                <i class="glyphicon glyphicon-warning-sign"></i> <spring:message code="txt.m1.warning"/>
                                            </div>    
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" data-dismiss="modal" data-dismiss="modal" class="btn btn-default"><spring:message code="btn.canceltxt"/></button>
                                    <button type="button" data-dismiss="modal" data-dismiss="modal" class="btn btn-warning" onclick="startRecordRequest()"><spring:message code="btn.continue"/></button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div>
                    <!-- END modal del botó RECORD -->
                    <%-- abertranb - 20150611 - disabled archivebutton --%>
                    <%--c:choose>
                        <c:when test="${auto_recording}" >
                    <button type="button" class="btn btn-warning" data-toggle="modal" id="button-archive" data-target="#archive" disabled="true"><span class="glyphicon glyphicon-save" title="<spring:message code="btn.arch.close"/>"></span> <spring:message code="btn.arch.close"/></button>
                        </c:when>
                        <c:otherwise>
                    <button type="button" id="button-record-stop" class="btn btn-warning" data-toggle="modal" data-target="#Recorded" title="<spring:message code="btn.stop"/>"><span class="glyphicon glyphicon-stop"></span> <spring:message code="btn.stop"/></button>
                    <button type="button" id="button-play" class="btn btn-success" data-toggle="modal" data-target="#playSession" title="<spring:message code="viewsession"/>"><span class="glyphicon glyphicon-play"></span> <spring:message code="viewsession"/></button>
                        </c:otherwise>
                    </c:choose--%>
                    <div class="col-md-2">
                        <button type="button" id="button-record" class="btn btn-danger" data-toggle="modal" data-target="#record" title="<spring:message code="btn.record"/>"><span class="glyphicon glyphicon-record"></span> <spring:message code="btn.record"/></button>
                        <button type="button" id="button-record-stop" class="btn btn-warning" data-toggle="modal" data-target="#Recorded" title="<spring:message code="btn.stop"/>"><span class="glyphicon glyphicon-stop"></span> <spring:message code="btn.stop"/></button>
                    </div>    
                    <div class="col-md-2 col-md-offset-3">
                        <button type="button" id="button-play" class="btn btn-success" data-toggle="modal" data-target="#playSession" title="<spring:message code="viewsession"/>"><span class="glyphicon glyphicon-play"></span> <spring:message code="viewsession"/></button>
                    </div>    

                    <%-- END abertranb - 20150611 - disabled archivebutton --%>
                    <!-- modal del botó RECORD -->
                    <div class="modal fade" id="Recorded">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title"><spring:message code="txt.m1.important"/></h4>
                                </div>
                                <div class="modal-body">
                                    <p><strong><spring:message code="txt.m3.confirm"/></strong></p>
                                    <div><strong><spring:message code="txt.m1.important"/></strong>: <spring:message code="txt.m3.warning1.2"/></div>
                                    <div><strong><spring:message code="txt.m3.warning2.1"/></strong>: <spring:message code="txt.m3.confirm"/></div>
                                    <div><strong><spring:message code="txt.m3.warning3.1"/></strong>: <spring:message code="txt.m3.warning3.2"/></div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-warning" data-dismiss="modal"><spring:message code="txt.m3.warning1.1"/></button>
                                    <c:choose><c:when test="${not auto_recording}" >
                                            <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="stopRecordRequest()"><spring:message code="txt.m3.warning2.1"/></button>
                                        </c:when></c:choose>
                                    <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="showFormCloseMeetingRequest()"><spring:message code="btn.saveclose"/></button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div>
                    <!-- /.modal -->
                    <!-- END modal del botó RECORD -->
                    <c:choose>
                        <c:when test="${not auto_recording}" >
                    <div class="modal fade" id="playSession" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog">
                          <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            </div>
                            <div class="modal-body" id="modal-body-player">
                            </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                        </c:when>
                    </c:choose>
                </div>
                <%-- abertranb - 20150611 - disabled archivebutton --%>
                <div class="col-md-2 col-md-offset-7 col-xs-5">
                    <%--c:choose>
                        <c:when test="${not auto_recording}" >
                    <button type="button" class="btn btn-warning" data-toggle="modal" id="button-archive" data-target="#archive" disabled="true"><span class="glyphicon glyphicon-save" title="<spring:message code="btn.arch.close"/>"></span> <spring:message code="btn.arch.close"/></button>
                        </c:when>
                    </c:choose >
                                        
                    <!-- modal del botó RECORD -->
                    <div class="modal fade" id="archive">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title"><spring:message code="txt.m1.important"/></h4>
                                </div>
                                <div class="modal-body">
                                    <div><strong><spring:message code="txt.m2.confirm"/></strong></div>
                                    <div><spring:message code="txt.m2.warning"/></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal" data-dismiss="modal"><spring:message code="txt.m3.warning1.1"/></button>
                                    <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="showFormCloseMeetingRequest()"><spring:message code="btn.continue"/></button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div>
                    <!-- END modal del botó RECORD -->
                    
                    <%-- END abertranb - 20150611 - disabled archivebutton --%>
                </div>
                    
            </div>
            <div class="row wrapper_content">
                <div class="col-md-8 participants">
                    <div class="my-inner">
                        <div class="row header_participants">
                            <h4 class="col-md-10 col-xs-6"><spring:message code="txt.participants"/></h4>
                            <div class="btn-group col-md-2 col-xs-6">
                                <button type="button" class="btn btn-warning" id="button-reload" title="<spring:message code="btn.title.reload"/>"><span class="glyphicon glyphicon-repeat"></span></button>
                                <button type="button" class="btn btn-warning" id="button-lock" title="<spring:message code="btn.title.lock"/>"><span class="unlock" id="span-lock"></span></button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-<c:out value="${size_of_video}"/> participant" id="user-1">
                                <div class="participant_content<c:out value="${concat_size_video}"/>">
                                    <c:choose>
                                        <c:when test="${not empty sUserMeeting.getExtra_role()}" >
                                            <span class='user-role'><c:out value="${sUserMeeting.getExtra_role()}"/></span>
                                        </c:when>
                                    </c:choose>
                                            <div id="nom-1"><c:out value="${(sUser.getFullname().length()>19?sUser.getFullname().substring(0,19):sUser.getFullname())}"/>&nbsp;
                                        <button type="button" class="btn btn-warning btn-mini-videochat" id="button-config" title="<spring:message code="btn.title.settings"/>">
                                           <span class="glyphicon glyphicon-cog"></span></button>
                                        <button type="button" id="button-volume" class="btn btn-warning btn-mini-videochat" title="<spring:message code="btn.title.mute"/>"><span class="glyphicon glyphicon-volume-up" id="span-volume"/></span></button>
                                        <a href="#" id="button-video-enable" title="<spring:message code="btn.title.enableVideo"/>"><image src="css/images/videoXat.png" id="video-enable"/></a>
                                    </div>
                                    <div id="videochat_stream">
                                        <p><spring:message code="message.grabavideoconferencia.flash"/></p>
                                        <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
                                    </div>
                                </div>
                            </div>
                                       
                        <c:forEach var="i" begin="2" end="${max_participants}">
                            <div class="col-md-<c:out value="${size_of_video}"/> participant" id="user-<c:out value="${i}"/>">
                                <div class="participant_content<c:out value="${concat_size_video}"/>">
                                    <span class='user-role'></span>
                                    <div id="nom-<c:out value="${i}"/>">&nbsp;</div>
                                    <div id="main-user-video-<c:out value="${i}"/>"><div id="user-video-<c:out value="${i}"/>"><img src="css/images/participant.png" class="participant_image_content<c:out value="${concat_size_video}"/>" alt="participant <c:out value="${i}"/>"></div></div>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${i % add_new_line==0}" >
                                    </div>
                                    <c:choose>
                                        <c:when test="${i<max_participants}" >
                                    <div class="row">
                                        </c:when>
                                    </c:choose>
                                </c:when>
                            </c:choose>
                                                    
                        </c:forEach>
                    </div>
                </div>
                <div class="col-md-4 aside">
                    <div class="my-inner">
                        <div class="header_chat row">
                            <h4><spring:message code="txt.chat.title"/></h4>
                        </div>
                        <div class="wrapper_chat" id="chatContainer">

                        </div> 
                        <p><spring:message code="txt.chat.message1"/></p>
                        <div class="row">
                            <div class="col-xs-10 col-md-9"><input type="text" class="form-control" id="messageTxt" maxlength="100" /></div>
                            <div class="col-xs-2 col-md-3"><button class="btn btn-warning" name="button-sendMessage" id="button-sendMessage" title="<spring:message code="txt.chat.send"/>"><spring:message code="txt.chat.send"/></button></div>
                        </div>
                    </div>
                </div>
            </div>



        <footer class="row"> 
            <div style="float: left; margin-top: 0pt; margin-left: 50px;text-align: justify; width: 600px;"><span style="font-size:9px;">This project has been funded with support from the Lifelong Learning Programme of the European Commission.  <br />
This site reflects only the views of the authors, and the European Commission cannot be held responsible for any use which may be made of the information contained therein.</span>
</div>
         &nbsp; <img src="css/images/EU_flag.jpg" alt="" />
        </footer>
        </div>  

        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootbox.min.js"></script>
        <script src="js/bootstrap-tour/js/bootstrap-tour-standalone.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
        <script src="js/js.cookie.js"></script>
        <script>
            
                                        function showFlashConfiguration() {
                                                var flash = swfobject.getObjectById("videochat_stream_id");
                                                flash.showSettingsFromJS();
                                        }
                                        function start_test() {
                                            var flash = swfobject.getObjectById("test-flash_id");
                                            if (!flash) {

                                                var flashvars = {
                                                    publishName: "${fn:replace(sUserMeeting.getStreamKey(), ":", ".")}",
                                                    rmtpServer: "rtmp://${wowza_stream_server}/videochat",
                                                };
                                                var params = {
                                                };
                                                var attributes = {
                                                    id: "test-flash_id"
                                                };
                                                swfobject.switchOffAutoHideShow();
                                                swfobject.embedSWF("FlashRTMPPlayer/recorder_tester.swf", "test-flash-content", "577", "330", "11.1.0", "expressInstall.swf", flashvars, params, attributes);                                                        
                                            }
                                            num_max_of_load_translations = 5;
                                            setTranslations();
                                        }
                                        var external_window = null;
                                        var array_messages = Array();
                                        var disabled_video = false;
                                        var showed_tourAcceptedConnection = false;
                                        
                                        array_messages ['--vc-system-start-record--'] = "<spring:message code="system_chat_message_1"/>";
                                        array_messages ['--vc-system-stop-record--'] = "<spring:message code="system_chat_message_2"/>";
                                        array_messages ['--vc-system-close-session--'] = "<spring:message code="system_chat_message_3"/>";
                                        array_messages ['--vc-system-lock-session--'] = "<spring:message code="system.lock.session"/>";
                                        array_messages ['--vc-system-unlock-session--'] = "<spring:message code="system.unlock.session"/>";
                                        array_messages ['--vc-system-changed-topic--'] = "<spring:message code="system.changed.topic.message"/>";
                                        array_messages ['--vc-system-changed-description--'] = "<spring:message code="system.user.changed.description"/>";
                                        var meeting_is_recorded = ${is_recorded};
                                        var meeting_is_first_recording = ${not is_recorded};
                                        var meeting_is_closed = false;
                                        var micro_is_muted = false;
                                        var meeting_is_locked = false;
                                        var num_max_of_load_translations = 5;
                                        $(document).ready(function() {
                                            try{
                                                window.moveTo(0, 0);
                                                window.resizeTo(screen.availWidth, screen.availHeight);
                                            }catch (e) {
                                            }
                                            $('#message').hide();
                                            <c:choose>
                                                <c:when test="${not disable_back_button}" >
                                            $(window).bind('beforeunload', function() {
                                                if (!allowed_return) {
                                                    disconnectedUserAjax("${sUser.getUsername()}", true, false);
                                                    setTimeout(function() {
                                                        setTimeout(function() {
                                                            connectUserAjax("${sUser.getUsername()}");
                                                        }, 1000);
                                                    }, 1);

                                                    return "<spring:message code="message.exit"/>"
                                                }
                                            });
                                                </c:when>
                                            </c:choose>
                                            $('#button-play').hide();
                                            $('#button-record-stop').hide();
                                            $('#button-volume').hide();
                                            $('#button-record').hide();
                                            <%-- abertranb - 20150611 - disabled archivebutton 
                                                $('#button-archive').hide();
                                            --%>
                                            $('#button-sendMessage').hide();
                                            $('#button-lock').hide();
                                            $("#button-video-enable").hide();
                                            $("#topic_metting_record_pencil").hide();
                                            $("#description_metting_record_pencil").hide();
                                            
                                            if (Cookies.get('show_videochat_headphone')!=1) {
                                                $('#modal_headphones').modal('show');
                                            } else {
                                                showTourInit(false);                                                   
                                            }
                                            $('#modal_headphones').on('hide.bs.modal', function (e) {
                                                showTourInit(false);                                                   
                                            });
                                            $('#not_show_again').click(function() {                                                
                                                Cookies.set('show_videochat_headphone', $(this).is(":checked")?1:0);
                                            });
                                            
                                        <c:forEach items="${participants}" var="item">
                                           // var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKey()}","${not empty item.getExtra_role()?item.getExtra_role():""}");
                                           // registeredUser(participant);   
                                        </c:forEach>
                                            $("#button-help").click(
                                                    function() {
                                                        if (swf_is_ready) 
                                                        {
                                                           showTourAcceptedConnection (true);
                                                        } else {
                                                           showTourInit(true); 
                                                        }   
                                                    }
                                            );
                                            
                                            $("#button-sendMessage").click(
                                                    function() {
                                                        sendChatMessage();
                                                    }
                                            );
                                            $("#button-video-enable").click(
                                                function() {
                                                    var flash = swfobject.getObjectById("videochat_stream_id");
                                            
                                                    if (disabled_video) {
                                                        flash.enableCameraFromJS();
                                                        $("#video-enable").attr("src", "css/images/videoXat.png");
                                                        $("#video-enable").attr("alt", "<spring:message code="btn.title.disableVideo"/>");
                                                        $("#button-video-enable").attr("title", "<spring:message code="btn.title.disableVideo"/>");
                                                    } else {
                                                        flash.disableCameraFromJS();
                                                        $("#video-enable").attr("src", "css/images/videoXatClose.png");
                                                        $("#video-enable").attr("alt", "<spring:message code="btn.title.enableVideo"/>");
                                                        $("#button-video-enable").attr("title", "<spring:message code="btn.title.enableVideo"/>");
                                                    }
                                                    disabled_video = !disabled_video;
                                                }
                                            );
                                            $("#button-lock").click(
                                                    function() {
                                                        lockMeetingRequest();
                                                    }
                                            );
                                            $("#button-test-environment").click(
                                                    function() {
                                                        var is_win = navigator.appVersion.indexOf("Win")!=-1;
                                                        var chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());

                                                        if (is_win && !chrome) {
                                                            window.open('testEnvironment.htm?lang=<%=locale%>');
                                                            $('#test-environment').modal('hide') ;
                                                        } else {
                                                            start_test();
                                                        }
                                                        
                                                    
                                                    }
                                            );
                                            $('#test-environment').on('shown.bs.modal', function () {
                                                var is_win = navigator.appVersion.indexOf("Win")!=-1;
                                                var chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());

                                                if (is_win && !chrome) {
                                                    $('#test-environment').modal('hide') ;
                                                }
                                            });

                                            $('#messageTxt').keydown(function(e) {
                                                if (e.keyCode == 13) {
                                                    sendChatMessage();
                                                }
                                            });
                                            $("#button-volume").click(
                                                    function() {
                                                        var flash = swfobject.getObjectById("videochat_stream_id");
                                                        flash.muteUnMuteFromJS();
                                                        if (micro_is_muted) {
                                                            $("#span-volume").removeClass("glyphicon-volume-off");
                                                            $("#span-volume").addClass("glyphicon-volume-up");
                                                        } else {
                                                            $("#span-volume").removeClass("glyphicon-volume-up");
                                                            $("#span-volume").addClass("glyphicon-volume-off");
                                                        }
                                                        micro_is_muted = !micro_is_muted;
                                                    }
                                            );
                                            $("#button-config").click(
                                                    function() {
                                                        showFlashConfiguration();
                                                    }
                                            );
                                            
                                            $("#button-reload").click(
                                                    function() {
                                                        allowed_return = true;
                                                        location.reload();
                                                    }
                                            );
                                            $("#button-configuration").click(
                                                    function() {
                                                    bootbox.alert("<spring:message code="message.configuration.manage"/>", function() {
                                                        });
                                                    }
                                            );

                                            $("#button-exit").click(
                                                    function() {
                                                        disconnectedUserAjax("${sUser.getUsername()}", true, goToSearchMeeting);
                                                    }
                                            );                                
                                        });
                                        var steps_default = [
                                              {
                                                element: "#button-test-environment",
                                                title: "<spring:message code="txt.title.test"/>",
                                                content: "<spring:message code="txt.msg.test"/>",
                                                placement: "left"
                                              },
                                              {
                                                element: "#lang",
                                                title: "<spring:message code="message.lang.title"/>",
                                                content: "<spring:message code="message.lang.title.help"/>",
                                                placement: "bottom"
                                              },
                                              {
                                                element: "#user-1",
                                                title: "<spring:message code="message.user.title"/>",
                                                content: "<spring:message code="message.user.title.help"/>",
                                                placement: "bottom"
                                              },
                                              {
                                                element: "#button-config",
                                                title: "<spring:message code="btn.title.settings"/>",
                                                content: "<spring:message code="message.settings.help"/>",
                                                placement: "top"
                                              },
                                              {
                                                element: "#button-reload",
                                                title: "<spring:message code="btn.title.reload"/>",
                                                content: "<spring:message code="message.reload.help"/>",
                                                placement: "top"
                                              },
                                              {
                                                element: "#button-exit",
                                                title: "<spring:message code="btn.title.exit"/>",
                                                content: "<spring:message code="message.exit.help"/>",
                                                placement: "left"
                                              }
                                            ];
                                        var showTourInit = function(force) {
                                        // Instance the tour
                                            var tour = new Tour({
                                              steps: steps_default});

                                            // Initialize the tour
                                            tour.init();
                                            if(force) {
                                                tour.restart();
                                            }
                                            // Start the tour
                                            tour.start(force); 
                                        }
                                        var showTourAcceptedConnection = function(force) {
                                        // Instance the tour
                                            var tourAcceptedConnection = new Tour({
                                              steps: [
                                            <c:choose>
                                                <c:when test="${not auto_recording}" >
                                              {
                                                element: "#button-record",
                                                title: "<spring:message code="btn.record"/>",
                                                content: "<spring:message code="record.help"/>",
                                                placement: "top"
                                              },
                                                      {
                                                element: "#topic_metting_record_pencil",
                                                title: "<spring:message code="btn.set_topic"/>",
                                                content: "<spring:message code="set_topic.help"/>",
                                                placement: "top"
                                              },
                                              <%-- abertranb - 20150611 - disabled archivebutton 
                                              {
                                                element: "#button-archive",
                                                title: "<spring:message code="btn.arch.close"/>",
                                                content: "<spring:message code="arch.close.help"/>",
                                                placement: "left"
                                              },--%>
                                                </c:when>
                                            </c:choose>
                                              {
                                                element: "#button-config",
                                                title: "<spring:message code="btn.title.settings"/>",
                                                content: "<spring:message code="message.settings.help"/>",
                                                placement: "top"
                                              },                                              {
                                                element: "#button-volume",
                                                title: "<spring:message code="btn.title.mute"/>",
                                                content: "<spring:message code="mute.help"/>",
                                                placement: "top"
                                              },
                                              {
                                                element: "#button-video-enable",
                                                title: "<spring:message code="btn.title.disableVideo"/>",
                                                content: "<spring:message code="enableVideo.help"/>",
                                                placement: "top"
                                              },                                                      
                                              {
                                                element: "#button-reload",
                                                title: "<spring:message code="btn.title.reload"/>",
                                                content: "<spring:message code="message.reload.help"/>",
                                                placement: "top"
                                              },
                                              {
                                                element: "#button-lock",
                                                title: "<spring:message code="btn.title.lock"/>",
                                                content: "<spring:message code="message.lock.help"/>",
                                                placement: "bottom"
                                              },                                                      
                                              {
                                                element: "#button-sendMessage",
                                                title: "<spring:message code="message.chatMessage"/>",
                                                content: "<spring:message code="message.save_and_close.help"/>",
                                                placement: "left"
                                              },
                                              {
                                                element: "#button-exit",
                                                title: "<spring:message code="btn.title.exit"/>",
                                                content: "<spring:message code="message.exit.help"/>",
                                                placement: "left"
                                              }
                                            ]});

                                            // Initialize the tour
                                            tourAcceptedConnection.init();
                                            if(force) {
                                                tourAcceptedConnection.restart();
                                            }
                                            // Start the tour
                                            tourAcceptedConnection.start(force); 
                                        }
                                        var showTourStopRecording = function () {
                                            // Instance the tour
                                            var tourVideoPreview = new Tour({
                                              steps: [
                                              {
                                                element: "#button-play",
                                                title: "<spring:message code="viewsession"/>",
                                                content: "<spring:message code="viewsession.help"/>"
                                              }
                                              <%-- abertranb - 20150611 - disabled archivebutton 
                                              ,{
                                                element: "#button-archive",
                                                title: "<spring:message code="message.save_and_close"/>",
                                                content: "<spring:message code="message.save_and_close.help"/>",
                                                placement: "left"
                                              }--%>
                                            ]});

                                            // Initialize the tour
                                            tourVideoPreview.init();
                                            tourVideoPreview.restart();//restart tour 
                                            // Start the tour
                                            tourVideoPreview.start(true);                                        
                                        }
                                        var sendChatMessageFlash = function (message) {
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            flash.sendChatMessage(message);
                                        }
                                        var sendChatMessage = function() {
                                            var message = $("#messageTxt").val();
                                            if (message.length > 0) {
                                                sendChatMessageFlash(message);
                                                $("#messageTxt").val("");
                                            }
                                        }
                                        var swf_is_ready = false;
                                        var flashvars = {
                                            debug: "0",
                                            publishName: "${fn:replace(sUserMeeting.getStreamKey(), ":", ".")}",
                                            record_method: 2,
                                            quality_version: 20,
                                            rmtpServer: "rtmp://${wowza_stream_server}/videochat",
                                            userkey: "${sUser.getUsername()}",
                                            username: "${sUser.getFullname()}",
                                            roomID: "${sMeeting.getPath()}",
                                            use_tick: "1",
                                            total_num_request_call_registered: 9,
                                            <c:choose>
                                                <c:when test="${not empty sUserMeeting.getExtra_role()}" >
                                                    extra_role: "${sUserMeeting.getExtra_role()}",
                                                </c:when>
                                            </c:choose>
                                            width_of_video: "<c:out value="${width_of_video}"/>",
                                            height_of_video: "<c:out value="${height_of_video}"/>",
                                        };
                                        var params = {
                                            wmode: "opaque"
                                        };
                                        var attributes = {
                                            id: "videochat_stream_id"
                                        };


                                        var  callbackFlashLoaded = function(e) {
                                            //TODO show error
                                            //console.log(e);
                                        }

                                        swfobject.embedSWF("FlashRTMPPlayer/recorder.swf?version=<c:out value="${videochat_version}"/>", "videochat_stream", "<c:out value="${width_of_video}" />", "<c:out value="${height_of_video}" />", "11.1.0", "expressInstall.swf", flashvars,params, attributes, callbackFlashLoaded);
                                        var notifiedExteralTool = false;
                                        var checkIfNotifyUser = function() {
                                        <c:choose>
                                            <c:when test="${not empty url_notify_started_recording}" >
                                                if (swf_is_ready && array_streams.length+1 == <c:out value="${max_participants}"/> && !notifiedExteralTool){
                                                    notifiedExteralTool = true;
                                                    notifyExternaleTool();
                                                }
                                            </c:when>
                                        </c:choose>  
                                        }
                                        var get_participants_ajax = function() {
                                            $.ajax({
                                                url: 'rest/videochat/participants/<c:out value="${sUser.getEmail()}"/>/<c:out value="${sUser.getToken_access()}"/>/<c:out value="${sMeeting.getId()}"/>',
                                                type: "GET",
                                                success: function(response) {
                                                    if (response.participants) {
                                                        for (index=0; index<response.participants.length; index++){    
                                                            if (response.participants[index].username!='${sUser.getUsername()}'){
                                                                var participant = new StreamObject(response.participants[index].username, response.participants[index].fullname, response.participants[index].stream_key,response.participants[index].role_extra?response.participants[index].role_extra:"");
                                                                registeredUser(participant);   
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                        
                                         var get_meeting_data_ajax = function() {
                                            $.ajax({
                                                url: 'rest/videochat/meeting/<c:out value="${sUser.getEmail()}"/>/<c:out value="${sUser.getToken_access()}"/>/<c:out value="${sMeeting.getId()}"/>',
                                                type: "GET",
                                                success: function(response) {
                                                    if (response.meeting.topic) {
                                                        $("#topic_metting_record").editable('setValue', response.meeting.topic);
                                                        $("#description_metting_record").editable('setValue', response.meeting.description);
                                                    }
                                                }
                                            });
                                        }
                                        
                                        var intervalGetParticipants = true;
                                        var setSWFIsReady = function() {
                                            if (!swf_is_ready) {
                                                if (${is_recorded}) {
                                                    try {
                                                        var flash = swfobject.getObjectById("videochat_stream_id");
                                                        flash.setIsRecordingFromJS();
                                                    } catch (e) {
                                                        console.log("Can't set recording to true", e);
                                                    }
                                                }
                                                
                                                get_participants_ajax();
                                                <%-- 20150527 - abertranb - disable ajax loader
                                                setInterval(
                                                function() {
                                                    if (array_streams.length<<c:out value="${max_participants}"/>) {
                                                        get_participants_ajax();
                                                    }
                                                },
                                                5000
                                                );--%>
                                            }
                                            currentUserAcceptConnection();
                                            swf_is_ready = true;                                            
                                            $('#button-volume').show();
                                            $('#button-video-enable').show();
                                            <c:choose>
                                                <c:when test="${not auto_recording}" >
                                                    if (${is_recorded}) {
                                                        $('#button-record-stop').show();
                                                    } else {
                                                        $('#button-record').show();
                                                    }                                                    
                                                    <%-- abertranb - 20150611 - disabled archivebutton 
                                                    $('#button-archive').show();
                                                    --%>
                                                </c:when>
                                            </c:choose>
                                            $("#topic_metting_record_pencil").show();
                                            $("#description_metting_record_pencil").show();
                                            //$('#topic_metting_record').editable();
                                            $("#topic_metting_record_pencil").click(function(e) {
                                                e.stopPropagation();
                                                e.preventDefault();
                                                $("#topic_metting_record").editable('option', 'validate', function(v) {
                                                    if(!v) return "<spring:message code="txt.m4.confirm"/>";
                                                });
                                                $("#topic_metting_record").editable("toggle");
                                                $('#topic_metting_record').on('save', function(e, params) {
                                                    sendChatMessageFlash('--vc-system-changed-topic--');
                                                });
                                            }); 
                                            //$('#description_metting_record').editable();
                                            $("#description_metting_record_pencil").click(function(e) {
                                                e.stopPropagation();
                                                e.preventDefault();
                                                $("#description_metting_record").editable("toggle");
                                                $('#description_metting_record').on('save', function(e, params) {
                                                    sendChatMessageFlash('--vc-system-changed-description--');
                                                });
                                            }); 
                                            $('#button-sendMessage').show();
                                            $('#button-lock').show();
                                            if (!showed_tourAcceptedConnection) {
                                                showTourAcceptedConnection(false);
                                                showed_tourAcceptedConnection = true;
                                            }
                                        }

                                        var stopRecordRequest = function() {
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            flash.stopRecordFromJS();
                                        }
                                        
                                        var showVideoPreview = function() {
                                            $('#button-play').show();
                                            showTourStopRecording();
                                        }
                                        
                                        $('#button-play').on('click', function(e) {
                                            e.preventDefault();
                                            var url = 'view_session.htm';
                                            $("#modal-body-player").html('<iframe width="100%" height="95%" frameborder="0" scrolling="yes" allowtransparency="true" src="'+url+'"></iframe>');
                                        });

                                        $('#playSession').on('show.bs.modal', function () {

                                            $(this).find('.modal-dialog').css({
                                                      width:'95%', //choose your width
                                                      height:'95%', 
                                                      'padding':'0'
                                               });
                                             $(this).find('.modal-header').css({
                                                      height:'6%', 
                                                      'border-radius':'0',
                                                      'padding':'10'
                                               });
                                             $(this).find('.modal-content').css({
                                                      height:'100%', 
                                                      'border-radius':'0',
                                                      'padding':'0'
                                               });
                                             $(this).find('.modal-body').css({
                                                      width:'auto',
                                                      height:'95%', 
                                                      'padding':'0'
                                               });
                                        });

                                        var showFormCloseMeetingRequest = function() {
                                            <c:choose>
                                                <c:when test="${auto_recording}" >
                                            if (true) {
                                                </c:when>
                                                <c:otherwise>
                                            if ($('#button-record-stop').is(":visible")) {
                                                </c:otherwise>
                                            </c:choose>
                                                stopRecordRequest();
                                            }                                                
                                            
                                    <c:choose><c:when test="${auto_recording}" >
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            <c:choose>
                                                <c:when test="${not empty url_notify_ended_recording}" >
                                                    try {
                                                        var json = {};
                                                        $.ajax({
                                                            url: 'rest/usermeeting_notify.json',
                                                            data: JSON.stringify(json),
                                                            type: "DELETE",
                                                            beforeSend: function(xhr) {
                                                                xhr.setRequestHeader("Accept", "application/json");
                                                                xhr.setRequestHeader("Content-Type", "application/json");
                                                            }                                                                
                                                            }).done(function() {
                                                            }).fail(function(e) {
                                                                    bootbox.alert("<spring:message code="error.closing.session"/>");
                                                                    //console.log(e);
                                                              }).always (function () {
                                                                flash.closeMeetingFromJS();
                                                              });
                                                    } catch (e) {
                                                        flash.closeMeetingFromJS();
                                                    }
                                                </c:when>
                                                <c:otherwise>
                                                    flash.closeMeetingFromJS();
                                                </c:otherwise>
                                            </c:choose>
                                                                                          
                                            </c:when>
                                            <c:otherwise>
                                                <%--abertranb - 20150611 - disabled archivebutton 
                                                
                                            $('#formModal').modal('show');
                                                
                                                <%-- END - abertranb - 20150611 - disabled archivebutton --%>
                                                closeMeetingRequestFlash();
                                            </c:otherwise>
                                        </c:choose>
                                            
                                        }
                                        
                                        var closeMeetingRequestFlash= function() {
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            flash.closeMeetingFromJS();            
                                        }

                                        var closeMeetingRequest = function() {
                                            if (meeting_is_recorded) {
                                                
                                                //Save the topic and description
                                                var topic = $('#topic_meeting').val();
                                                var description = $('#description_meeting').val();
                                                if (topic.length > 0) {
                                                    saveTopicDescription(topic, description);
                                                    closeMeetingRequestFlash();
                                                } else {
                                                    bootbox.alert("<spring:message code="txt.m4.confirm"/>");
                                                }
                                            } else {
                                                bootbox.alert("<spring:message code="message.recorded.close"/>");
                                            }
                                        }

                                        var startRecordRequest = function() {
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            flash.startRecordFromJS();
                                            $('#button-play').hide();
                                            
                                        }


                                        var lockMeetingRequest = function() {
                                            if (!meeting_is_closed) {
                                                var flash = swfobject.getObjectById("videochat_stream_id");
                                                flash.lockMeetingFromJS();
                                            } else {
                                                bootbox.alert("<spring:message code="btn.closed.session"/>");
                                            }
                                        }
                                        var array_streams = Array();

                                        var registeredUser = function(info) {
                                            if (info.userkey=='${sUser.getUsername()}') {
                                               return;
                                            }
                                            var pos = returnPositionUser(info.userkey, array_streams);
                                            if (pos>=0){
                                                pos ++;
                                            }
                                            streamObj = new StreamObject(info.userkey, info.username, info.publishName, info.extra_role);
                                            
                                            if (array_streams.length < <c:out value="${max_participants}"/> || pos >= 0) {
                                                if (pos < 0) {
                                                    array_streams.push(streamObj);
                                                    pos = array_streams.length + 1;
                                                /*} else {
                                                    //canviem
                                                    var array_streams_temp = array_streams;
                                                    array_streams_temp.splice(pos, 1, streamObj);
                                                    array_streams = array_streams_temp;
                                                }*/
                                             
                                                $("#nom-" + pos).html(info.username);
                                                var Role = "";
                                                if (info.extra_role!=""){
                                                    Role = "<span>"+info.extra_role+"</span>";
                                                }
                                                $("#user-" + pos + " .user-role ").html(Role);
                                            }

                                                <c:choose>
                                                    <c:when test="${not empty url_notify_started_recording}" >
                                                        checkIfNotifyUser();
                                                    </c:when>
                                                </c:choose>  

                                            <%-- <c:choose>
                                                <c:when test="${auto_recording}" >
                                                    if (array_streams.length+1 == <c:out value="${max_participants}"/>){
                                                       startRecordRequest();
                                                    }
                                                </c:when>
                                            </c:choose> --%>

            <% /*
                         jwplayer("user-video-"+(pos)).setup({
                         file: "rtmp://${wowza_stream_server}:1935/videochat/"+info.publishName,
                         image: "",
                         width: 220,
                         height: 141,
                         controls: 'false',
                         rtmp.bufferlength: 0, //TODO test http://support.jwplayer.com/customer/portal/articles/1413113-configuration-options-reference#rtmp
                         icons: 'false',
                         modes: [
                         { type: "html5" },
                         { type: "flash", src: "./js/jwplayer/jwplayer.flash.swf" }
                         ],
                         //flashplayer: "./js/jwplayer/jwplayer.flash.swf",
                         events:{
                         onReady: function(e){
                         // Fires when player is ready, can disable Play button until this is fired for all videos i.e.
                         },
                         onBuffer: function(t){
                         // Buffer not exposed in RMTP playback
                         },
                         onPlay: function() {
                         },
                         onTime: function(t) {
                         }
                         }
                         });
                         jwplayer("user-video-"+(pos)).play();    */%>
                                                var flashvars = {
                                                    debug: "0",
                                                    publishName: info.publishName,
                                                    rmtpServer: "rtmp://${wowza_stream_server}/videochat",
                                                    userkey: info.userkey,
                                                    username: info.username,
                                                    roomID: "${sMeeting.getPath()}"
                                                };
                                                var params = {
                                                };
                                                var attributes = {
                                                    id: "play_stream_id_" + (pos)
                                                };
                                                if (!swfobject.hasFlashPlayerVersion("11.0.0")) {
                                                    bootbox.alert("<spring:message code="message_version_flash"/>", function() {
                                                        });
                                                }
                                                swfobject.embedSWF("FlashRTMPPlayer/player.swf?version=<c:out value="${videochat_version}"/>", "user-video-" + (pos), "<c:out value="${width_of_video}"/>", "<c:out value="${height_of_video}"/>", "11.0.0", "expressInstall.swf", flashvars, params, attributes);

                                            }
                                        }
                                        
                                        

                                        var saveTopicDescription = function(topic, description) {
                                            var json = {"request": topic, "extraParam": description};

                                            $.ajax({
                                                url: 'rest/meetingsession.json',
                                                data: JSON.stringify(json),
                                                type: "POST",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                        }
                                        
                                        var notifyExternaleTool = function() {
                                            <c:choose>
                                                <c:when test="${auto_recording}" >
                                                    if ($('#button-record-stop').prop('disabled')==true) {
                                                        startRecordRequest();
                                                    }
                                                </c:when>
                                            </c:choose>

                                            <c:choose>
                                                <c:when test="${not empty url_notify_started_recording}" >                                        
                                                    /*if (!external_window) {
                                                        external_window = window.open("<c:out value="${url_notify_started_recording}" escapeXml="false"/>","<c:out value="${window_focus_name}" />"+Math.floor((Math.random() * 1000) + 1));
                                                        if (!external_window) {
                                                            message = "<spring:message code="error.allow.popup"/>".replace("%1", '<a href="<c:out value="${url_notify_started_recording}" escapeXml="false"/>" target="<c:out value="${window_focus_name}" />"+Math.floor((Math.random() * 1000) + 1)>')+'</a>';

                                                            $('#message').html(message);
                                                            $('#message').addClass("alert");
                                                            $('#message').addClass("alert-danger");
                                                            $('#message').show();
                                                            bootbox.alert(message, function() {
                                                                if (!external_window ) {
                                                                    external_window = window.open("<c:out value="${url_notify_started_recording}" escapeXml="false"/>","<c:out value="${window_focus_name}" />"+Math.floor((Math.random() * 1000) + 1));
                                                                }
                                                            });
                                                         }
                                              #button-record      } 
                                                    setFocusWindow();*/                                                                                                                try {
                                                    var json = {};
                                                    $.ajax({
                                                        url: 'rest/usermeeting_notify.json',
                                                        data: JSON.stringify(json),
                                                        type: "POST",
                                                        beforeSend: function(xhr) {
                                                            xhr.setRequestHeader("Accept", "application/json");
                                                            xhr.setRequestHeader("Content-Type", "application/json");
                                                        }                                                                
                                                        }).done(function() {
                                                        }).fail(function(e) {
                                                                bootbox.alert("<spring:message code="error.registering.session"/>");
                                                                //console.log(e);
                                                          }).always (function () {

                                                          });
                                                } catch (e) {
                                                }

                                                        
                                                </c:when>
                                            </c:choose>
                                        }
                                        var setFocusWindow = function() {
                                            <c:choose>
                                                <c:when test="${not empty url_notify_started_recording}" >                                        
                                            if (external_window) {
                                                external_window.focus();
                                            }
                                                </c:when>
                                            </c:choose>
                                        }

                                        var currentUserAcceptConnection = function() {
                                            var json = {"request": "${sUserMeeting.getStreamKey()}"};

                                            $.ajax({
                                                url: 'rest/meeting.json',
                                                data: JSON.stringify(json),
                                                type: "GET",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                        }

                                        var newChatMessage = function(info) {
                                            var dt = new Date();
                                            var hours = dt.getHours();
                                            if (hours < 10) {
                                                hours = "0" + hours;
                                            }
                                            var minutes = dt.getMinutes();
                                            if (minutes < 10) {
                                                minutes = "0" + minutes;
                                            }
                                            var seconds = dt.getSeconds();
                                            if (seconds < 10) {
                                                seconds = "0" + seconds;
                                            }
                                            var message = info.message;
                                            if (message in array_messages) {
                                                if (message === '--vc-system-lock-session--' && !meeting_is_locked) {
                                                    message = '--vc-system-unlock-session--';
                                                }
                                                if ( (message === '--vc-system-changed-topic--' || 
                                                      message === '--vc-system-changed-description--')
                                                    &&
                                                      info.userkey != "${sUser.getUsername()}") {
                                                
                                                      get_meeting_data_ajax();
                                                    
                                                }
                                                message = array_messages[message];
                                                
                                            }
                                            var time = hours + ":" + minutes + ":" + seconds;
                                            var str = "<p><b>" + time + " - " + info.username + ":</b> " + message + "</p>";
                                            $("#chatContainer").append(str);
                                            var height = $("#chatContainer").get(0).scrollHeight;
                                            $("#chatContainer").scrollTop(height);
                                            if (info.userkey == "${sUser.getUsername()}") {
                                                var json = {"request": message};

                                                $.ajax({
                                                    url: 'rest/chat.json',
                                                    data: JSON.stringify(json),
                                                    type: "POST",
                                                    beforeSend: function(xhr) {
                                                        xhr.setRequestHeader("Accept", "application/json");
                                                        xhr.setRequestHeader("Content-Type", "application/json");
                                                    },
                                                    success: function(response) {
                                                        //console.log(response);
                                                    }
                                                });
                                            }
                                        }

                                        var startedRecord = function() {
                                            $('#button-record').hide();
                                            meeting_is_recorded = true;
                                            $('#button-record-stop').attr('disabled', false);

                                            var json = {"request": "${sUserMeeting.getStreamKey()}"};

                                            $.ajax({
                                                url: 'rest/meeting.json',
                                                data: JSON.stringify(json),
                                                type: "PUT",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                            <c:choose>
                                                <c:when test="${not auto_recording}" >
                                                    $('#button-record-stop').show();
                                                </c:when>
                                            </c:choose>        
                                        }

                                        var stoppedRecord = function() {
                                            <c:choose>
                                                <c:when test="${not auto_recording}" >
                                                    $('#button-record').show();
                                                    $('#button-record-stop').hide();
                                                    if (!meeting_is_closed) {
                                                        showVideoPreview();                                            
                                                    }
                                                </c:when>
                                            </c:choose>        
                                        }

                                        var stoppedRecordAjax = function() {
                                            var json = {"request": "${sUserMeeting.getStreamKey()}"};

                                            $.ajax({
                                                url: 'rest/meeting.json',
                                                data: JSON.stringify(json),
                                                type: "POST",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                        }

                                        var closeSessionAjax = function() {
                                            var json = {"request": "${sUserMeeting.getStreamKey()}"};

                                            $.ajax({
                                                url: 'rest/meetingsession.json',
                                                data: JSON.stringify(json),
                                                type: "DELETE",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    closedSession();
                                                    //console.log(response);
                                                }
                                            });
                                        }

                                        var closedSession = function() {
                                            meeting_is_closed = true;
                                            $('#button-record-stop').hide();
                                            $('#button-volume').hide();
                                            $('#button-record').hide();
                                            <%-- abertranb - 20150611 - disabled archivebutton 
                                            $('#button-archive').hide();
                                            --%>
                                            $('#button-sendMessage').hide();
                                            <c:choose>
                                                <c:when test="${not empty url_notify_started_recording}" >
                                                    returnMeeting();
                                                    setFocusWindow(); 
                                                </c:when>
                                                <c:otherwise>
                                                    bootbox.alert("<spring:message code="message.meeting.closed"/>", function() {
                                                            returnMeeting();
                                                        });
                                                </c:otherwise>    
                                            </c:choose>
                                            
                                        }

                                        var lockSessionAjax = function() {
                                            var json = {"request": "${sUserMeeting.getStreamKey()}"};

                                            $.ajax({
                                                url: 'rest/meetingsession.json',
                                                data: JSON.stringify(json),
                                                type: "PUT",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                        }

                                        var lockSession = function() {
                                            if (meeting_is_locked) {
                                                $("#span-lock").removeClass("lock");
                                                $("#span-lock").addClass("unlock");
                                            } else {
                                                $("#span-lock").removeClass("unlock");
                                                $("#span-lock").addClass("lock");
                                            }
                                            meeting_is_locked = !meeting_is_locked;
                                        }

                                        var returnMeeting = function() {
                                            disconnectedUserAjax("${sUser.getUsername()}", false, goToSearchMeeting);

                                        }

                                        var goToSearchMeeting = function() {
                                            allowed_return = true;
                                            <c:choose>
                                                <c:when test="${not disable_back_button}" >
                                                    location.href = "searchMeeting.htm";
                                                </c:when>
                                            </c:choose>  

                                            
                                        }

                                        function removeByIndex(arr, index) {
                                            arr.splice(index, 1);
                                        }
                                        
                                        function disconnectedUserAjax(username, should_delete, callback) {
                                            var json = {"request": username, "extraParam": should_delete};

                                            $.ajax({
                                                url: 'rest/usermeeting.json',
                                                data: JSON.stringify(json),
                                                type: "DELETE",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                },
                                                complete: function() {
                                                    if (callback) {
                                                        callback();
                                                    }
                                                    <c:choose>
                                                        <c:when test="${not empty url_notify_ended_recording}" >
                                                            try {
                                                                var json = {};
                                                                $.ajax({
                                                                    url: 'rest/usermeeting_notify.json',
                                                                    data: JSON.stringify(json),
                                                                    type: "DELETE",
                                                                    beforeSend: function(xhr) {
                                                                        xhr.setRequestHeader("Accept", "application/json");
                                                                        xhr.setRequestHeader("Content-Type", "application/json");
                                                                    }                                                                
                                                                    }).done(function() {
                                                                    }).fail(function(e) {
                                                                            bootbox.alert("<spring:message code="error.closing.session"/>");
                                                                            //console.log(e);
                                                                      }).always (function () {

                                                                      });
                                                            } catch (e) {
                                                            }
                                                        </c:when>
                                                    </c:choose>

                                                }
                                            });
                                        }
                                        
                                        function loadedFlash(time) {
                                            setTimeout("setTranslations();", time);
                                          }
                                          function setTranslations() {
                                            var flash = swfobject.getObjectById("test-flash_id");
                                            var ok = false;
                                            if (flash!=null) {
                                                try{
                                                    flash.setTagFromJS("recorder", "<spring:message code="message.test.recoder"/>");
                                                    flash.setTagFromJS("player", "<spring:message code="message.test.player"/>");
                                                    flash.setTagFromJS("audio_ok", "<spring:message code="message.test.audio_ok"/>");
                                                    flash.setTagFromJS("video_ok", "<spring:message code="message.test.video_ok"/>");
                                                    flash.setTagFromJS("audio_ko", "<spring:message code="message.test.audio_ko"/>");
                                                    flash.setTagFromJS("video_ko", "<spring:message code="message.test.video_ko"/>");
                                                    flash.setTagFromJS("general", "<spring:message code="message.test.general"/>");
                                                    flash.setTagFromJS("fpsTextExplication", "<spring:message code="message.test.fps_explain"/>");
                                                    ok = true;
                                                }
                                                catch (e) {
                                                  console.log(e);
                                                }
                                            }
                                            if (!ok) {
                                                num_max_of_load_translations--;
                                                if (num_max_of_load_translations>0) {
                                                  loadedFlash(400);
                                                }
                                            }   
                                          }

                                        function connectUserAjax(username) {
                                            var json = {"request": username};

                                            $.ajax({
                                                url: 'rest/usermeeting.json',
                                                data: JSON.stringify(json),
                                                type: "POST",
                                                beforeSend: function(xhr) {
                                                    xhr.setRequestHeader("Accept", "application/json");
                                                    xhr.setRequestHeader("Content-Type", "application/json");
                                                },
                                                success: function(response) {
                                                    //console.log(response);
                                                }
                                            });
                                        }
                                        var user_disconnected_alert = Array();
                                        var disconnectedUser = function(info) {
                                            if (!meeting_is_closed) {
                                                var pos = returnPositionUser(info.userkey, array_streams);
                                                if (pos >= 0) {
                                                    var array_streams_temp = array_streams;
                                                    removeByIndex(array_streams_temp, pos);
                                                    restore_participants_div(<c:out value="${max_participants}"/>, "<c:out value="${concat_size_video}"/>");
                                                    array_streams = Array();
                                                    for (i = 0; i < array_streams_temp.length; i++) {
                                                        registeredUser(array_streams_temp[i]);
                                                    }

                                                    disconnectedUserAjax(info.userkey, false, false);

                                                }
                                                if (user_disconnected_alert.indexOf(info.username)==-1) {
                                                    var message = "<spring:message code="txt.m8.warning"/>";
                                                    user_disconnected_alert.push(info.username);
                                                    message = message.replace("%1", info.username);
                                                    bootbox.alert(message, function() {
                                                        if (user_disconnected_alert.indexOf(info.username)>=0) {
                                                            user_disconnected_alert.splice(user_disconnected_alert.indexOf(info.username), 1);
                                                        }
                                                    });
                                                }
                                            }

                                        }
                                        
                                        var showing_errorNoMicroFound = false;
                                        var showing_errorNotAllowedMicro = false;
                                        
                                        function errorNoMicroFound() {
                                            if (!swf_is_ready) {
                                                showFlashConfiguration();
                                                return false;
                                            }
                                            if (!showing_errorNoMicroFound) {
                                                showing_errorNoMicroFound = true;
                                                bootbox.alert("<spring:message code="message.no.micro"/>", function() {
                                                    showing_errorNoMicroFound = false;
                                                    showFlashConfiguration();
                                                    });
                                                } 
                                            }
                                        function errorNotAllowedMicro() {
                                            if (!swf_is_ready) {
                                                showFlashConfiguration();
                                                return false;
                                            }
                                            if (!showing_errorNotAllowedMicro) {
                                                showing_errorNotAllowedMicro = true;
                                                bootbox.alert("<spring:message code="message.no.allowed.camera.micro"/>", function() {
                                                    showing_errorNotAllowedMicro = false;
                                                    showFlashConfiguration();
                                                    });

                                            }
                                        }
                                            
                                        function errorNoCameraFound() {
                                            bootbox.alert("<spring:message code="message.no.camera"/>", function() {
                                                    showFlashConfiguration();                                                
                                                });
                                            }

                                        function errorConnectingToServerStreaming() {
                                            bootbox.alert("<spring:message code="message.error.connecting.server"/>", function() {
                                                });
                                            }
        </script>
    </body>
</html