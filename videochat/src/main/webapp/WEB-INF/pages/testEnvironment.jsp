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
        <link rel="stylesheet" href="css/general.css">
        <script type="text/javascript" src="js/swfobject.js" ></script>
        <script type="text/javascript" src="js/videochat.js" ></script>
        <!-- VIDEO PLAYER JS -->
        <script type="text/javascript" src="js/jwplayer/jwplayer.js"></script>
        
    </head>	
    <body>
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


        <div class="container">
            <header class="row">
                <div class="col-md-4"><img src="css/images/logo.png" alt="videochat"/></div>
                <div id="idiomes" class="col-md-3 col-md-offset-4">
                    <form:form  name="lang_form" action="testEnvironment.htm" commandName="testEnvironment" modelAttribute="course" method="POST">
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
            <h3><spring:message code="txt.title.test"/></h3>
            
            <div class="row wrapper_content">
                <div id="test-flash">
                    <p><spring:message code="message.grabavideoconferencia.flash"/></p>
                    <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
                </div>
            </div>
        </div>  
        <footer class="row"> 
            <div style="float: left; margin-top: 0pt; margin-left: 50px;text-align: justify; width: 600px;"><span style="font-size:9px;">This project has been funded with support from the Lifelong Learning Programme of the European Commission.  <br />
This site reflects only the views of the authors, and the European Commission cannot be held responsible for any use which may be made of the information contained therein.</span>
</div>
		 &nbsp;	<img src="css/images/EU_flag.jpg" alt="" />
        </footer>
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootbox.min.js"></script>
        <script>
                                        
            $(document).ready(function() {

                var flashvars = {
                    rmtpServer: "rtmp://${wowza_stream_server}/videochat",
                };
                var params = {
                };
                var attributes = {
                    id: "test-flash_id"
                };
                swfobject.embedSWF("FlashRTMPPlayer/recorder_tester.swf", "test-flash", "577", "330", "11.1.0", "expressInstall.swf", flashvars, params, attributes);                                                        
                setTranslations();
            });
            var showing_errorNoMicroFound = false;
            var showing_errorNotAllowedMicro = false;
            
            var num_max_of_load_translations = 5;
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
            function errorNoMicroFound() {
                if (!swf_is_ready) {
                    return false;
                }
                if (!showing_errorNoMicroFound) {
                    showing_errorNoMicroFound = true;
                    bootbox.alert("<spring:message code="message.no.micro"/>", function() {
                        showing_errorNoMicroFound = false;
                        });
                    } 
                }
            function errorNotAllowedMicro() {
                if (!swf_is_ready) {
                    return false;
                }
                if (!showing_errorNotAllowedMicro) {
                    showing_errorNotAllowedMicro = true;
                    bootbox.alert("<spring:message code="message.no.allowed.camera.micro"/>", function() {
                        showing_errorNotAllowedMicro = false;
                        });
                }
            }

            function errorNoCameraFound() {
                bootbox.alert("<spring:message code="message.no.camera"/>", function() {
                    });
                }

            function errorConnectingToServerStreaming() {
                bootbox.alert("<spring:message code="message.error.connecting.server"/>", function() {
                    });
                }

        </script>
    </body>
</html>
