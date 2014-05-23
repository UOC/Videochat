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
        <%String pl = "pl";%> 
        <% /*
             <!-- modal que apareix al carregar la pàgina-->
             <div class="modal fade" id="camera">
             <div class="modal-dialog">
             <div class="modal-content">
             <div class="modal-header">
             <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
             <h4 class="modal-title">Camera</h4>
             </div>
             <div class="modal-body">
             <p>This is your private webcam signal.<br/><strong>Click on activate camera</strong> when you are ready to dend it to this session.</p>
             </div>
             <div class="modal-footer">
             <button type="button" class="btn btn-warning">Activate camera</button>
             </div>
             </div><!-- /.modal-content -->
             </div><!-- /.modal-dialog -->
             </div>
             <!-- /.modal -->*/%>

        <!-- modal form gravació-->
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
        <!-- /.modal -->
        <div class="container">
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

                            <form:option value="de"><spring:message code="message.lang.dutch"/></form:option>
                            <form:option value="se"><spring:message code="message.lang.swedish"/></form:option>
                            <form:option value="ir"><spring:message code="message.lang.irish"/></form:option>

                        </form:select>
                    </form:form>
                </div>
                <div id="close" class="col-md-1">
                    <span class="glyphicon glyphicon-remove btn-exit-videochat" title="<spring:message code="btn.title.exit"/>" id="button-exit"></span>
                </div>
            </header>
            <h3><c:out value="${sMeeting.getId_room().getLabel()}"/></h3>
            <div class="row wrapper_buttons">	
                <div class="col-md-3 col-xs-7">
                    <button type="button" id="button-record" class="btn btn-warning" data-toggle="modal" data-target="#record" title="<spring:message code="btn.record"/>"><span class="glyphicon glyphicon-record"></span> <spring:message code="btn.record"/></button>
                    <!-- modal del botó RECORD -->
                    <div class="modal fade" id="record">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title"><spring:message code="txt.m1.important"/></h4>
                                </div>
                                <div class="modal-body">
                                    <div><strong><spring:message code="txt.m1.confirm"/></strong></div>
                                    <div><spring:message code="txt.m1.warning"/></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" data-dismiss="modal" data-dismiss="modal" class="btn btn-default"><spring:message code="btn.canceltxt"/></button>
                                    <button type="button" data-dismiss="modal" data-dismiss="modal" class="btn btn-warning" onclick="startRecordRequest()"><spring:message code="btn.continue"/></button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div>
                    <!-- END modal del botó RECORD -->
                    <button type="button" id="button-record-stop" class="btn btn-warning" data-toggle="modal" data-target="#Recorded" title="<spring:message code="btn.stop"/>"><span class="glyphicon glyphicon-stop"></span> <spring:message code="btn.stop"/></button>
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
                                    <div><strong><spring:message code="txt.m1.important"/></strong>: <spring:message code="txt.m3.warning1.2"/><br/>
                                        <spring:message code="txt.m3.warning2.1"/>: <spring:message code="txt.m3.confirm"/></div>
                                    <div><strong><spring:message code="txt.m3.warning3.1"/></strong>: <spring:message code="txt.m3.warning3.2"/></div>

                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-warning" data-dismiss="modal"><spring:message code="txt.m3.warning1.1"/></button>
                                    <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="stopRecordRequest()"><spring:message code="txt.m3.warning2.1"/></button>
                                    <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="showFormCloseMeetingRequest()"><spring:message code="btn.saveclose"/></button>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div>
                    <!-- /.modal -->
                    <!-- END modal del botó RECORD -->

                    <button type="button" id="button-volume" class="btn btn-warning" title="<spring:message code="btn.title.mute"/>"><span class="glyphicon glyphicon-volume-up" id="span-volume"/></span></button>
                </div>
                <div class="col-md-2 col-md-offset-7 col-xs-5">
                    <button type="button" class="btn btn-warning" data-toggle="modal" id="button-archive" data-target="#archive" disabled="true"><span class="glyphicon glyphicon-save" title="<spring:message code="btn.arch.close"/>"></span> <spring:message code="btn.arch.close"/></button>
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
                            <div class="col-md-4 participant" id="user-1">
                                <div class="participant_content">
                                    <div id="nom-1"><c:out value="${sUser.getFullname()}"/>&nbsp;<button type="button" class="btn btn-warning btn-mini-videochat" id="button-config" title="<spring:message code="btn.title.settings"/>"><span class="glyphicon glyphicon-cog"></span></button></div>
                                    <div id="videochat_stream">
                                        <p><spring:message code="message.grabavideoconferencia.flash"/></p>
                                        <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-2">
                                <div class="participant_content">
                                    <div id="nom-2">&nbsp;</div>
                                    <div id="main-user-video-2"><div id="user-video-2"><img src="css/images/participant.png" alt="participant 4"></div></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-3">
                                <div class="participant_content">
                                    <div id="nom-3">&nbsp;</div>
                                    <div id="main-user-video-3"><div id="user-video-3"><img src="css/images/participant.png" alt="participant 4"></div></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-4">
                                <div class="participant_content">
                                    <div id="nom-4">&nbsp;</div>
                                    <div id="main-user-video-4"><div id="user-video-4"><img src="css/images/participant.png" alt="participant 4"></div></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-5">
                                <div class="participant_content">
                                    <div id="nom-5">&nbsp;</div>
                                    <div id="main-user-video-5"><div id="user-video-5"><img src="css/images/participant.png" alt="participant 5"></div></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-6">
                                <div class="participant_content">
                                    <div id="nom-6">&nbsp;</div>
                                    <div id="main-user-video-6"><div id="user-video-6"><img src="css/images/participant.png" alt="participant 6"></div></div>
                                </div>
                            </div>
                        </div>
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



        </div>  
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootbox.min.js"></script>
        <script>
                                        var array_messages = Array();
                                        array_messages ['--vc-system-start-record--'] = "<spring:message code="system_chat_message_1"/>";
                                        array_messages ['--vc-system-stop-record--'] = "<spring:message code="system_chat_message_2"/>";
                                        array_messages ['--vc-system-close-session--'] = "<spring:message code="system_chat_message_3"/>";
                                        array_messages ['--vc-system-lock-session--'] = "<spring:message code="system.lock.session"/>";
                                        array_messages ['--vc-system-unlock-session--'] = "<spring:message code="system.unlock.session"/>";
                                        var meeting_is_recorded = ${is_recorded};
                                        var meeting_is_closed = false;
                                        var micro_is_muted = false;
                                        var meeting_is_locked = false;
                                        
                                        $(document).ready(function() {

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

                                            $('#button-record-stop').hide();
                                            $('#button-volume').hide();
                                            $('#button-record').hide();
                                            $('#button-archive').hide();
                                            $('#button-sendMessage').hide();
                                            $('#button-lock').hide();

            <c:forEach items="${participants}" var="item">
                                            var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKey()}");
                                            registeredUser(participant);
            </c:forEach>
                                            $("#button-sendMessage").click(
                                                    function() {
                                                        sendChatMessage();
                                                    }
                                            );
                                            $("#button-lock").click(
                                                    function() {
                                                        lockMeetingRequest();
                                                    }
                                            );
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
                                                        var flash = swfobject.getObjectById("videochat_stream_id");
                                                        flash.showSettingsFromJS();
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
                                        var sendChatMessage = function() {
                                            var message = $("#messageTxt").val();
                                            if (message.length > 0) {

                                                var flash = swfobject.getObjectById("videochat_stream_id");
                                                flash.sendChatMessage(message);
                                                $("#messageTxt").val("");
                                            }
                                        }
                                        var swf_is_ready = false;
                                        var flashvars = {
                                            debug: "0",
                                            publishName: "${fn:replace(sUserMeeting.getStreamKey(), ":", ".")}",
                                            rmtpServer: "rtmp://${wowza_stream_server}/videochat",
                                            userkey: "${sUser.getUsername()}",
                                            username: "${sUser.getFullname()}",
                                            roomID: "${sMeeting.getPath()}",
                                        };
                                        var params = {
                                        };
                                        var attributes = {
                                            id: "videochat_stream_id"
                                        };
                                        swfobject.embedSWF("FlashRTMPPlayer/recorder.swf", "videochat_stream", "215", "138", "9.0.0", "expressInstall.swf", flashvars, params, attributes);

                                        var setSWFIsReady = function() {
                                            swf_is_ready = true;
                                            $('#button-volume').show();
                                            $('#button-record').show();
                                            $('#button-archive').show();
                                            $('#button-sendMessage').show();
                                            $('#button-lock').show();
                                        }

                                        var stopRecordRequest = function() {
                                            var flash = swfobject.getObjectById("videochat_stream_id");
                                            flash.stopRecordFromJS();
                                        }

                                        var showFormCloseMeetingRequest = function() {
                                            if ($('#button-record-stop').is(":visible")) {
                                                stopRecordRequest();
                                            }
                                            $('#formModal').modal('show');
                                        }

                                        var closeMeetingRequest = function() {
                                            if (meeting_is_recorded) {
                                                
                                                //Save the topic and description
                                                var topic = $('#topic_meeting').val();
                                                var description = $('#description_meeting').val();
                                                if (topic.length > 0) {
                                                    saveTopicDescription(topic, description);
                                                    var flash = swfobject.getObjectById("videochat_stream_id");
                                                    flash.closeMeetingFromJS();
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
                                            var pos = returnPositionUser(info.userkey, array_streams);
                                            streamObj = new StreamObject(info.userkey, info.username, info.publishName);
                                            if (array_streams.length < 6 || pos >= 0) {
                                                if (pos < 0) {
                                                    array_streams.push(streamObj);
                                                    pos = array_streams.length + 1;
                                                } else {
                                                    //canviem
                                                    var array_streams_temp = array_streams;
                                                    array_streams_temp.splice(index, 1, streamObj);
                                                    array_streams = array_streams_temp;
                                                }
                                                $("#nom-" + pos).html(info.username);
            <% /*
                         jwplayer("user-video-"+(pos)).setup({
                         file: "rtmp://${wowza_stream_server}:1935/videochat/"+info.publishName,
                         image: "",
                         width: 215,
                         height: 138,
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
                                                };
                                                var params = {
                                                };
                                                var attributes = {
                                                    id: "play_stream_id_" + (pos)
                                                };
                                                swfobject.embedSWF("FlashRTMPPlayer/player.swf", "user-video-" + (pos), "215", "138", "9.0.0", "expressInstall.swf", flashvars, params, attributes);

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
                                                    console.log(response);
                                                }
                                            });
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
                                                    console.log(response);
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
                                                        console.log(response);
                                                    }
                                                });
                                            }
                                        }

                                        var startedRecord = function() {
                                            $('#button-record').hide();
                                            meeting_is_recorded = true;
                                            $('#button-archive').attr('disabled', false);

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
                                                    console.log(response);
                                                }
                                            });
                                            $('#button-record-stop').show();
                                        }

                                        var stoppedRecord = function() {
                                            $('#button-record').show();
                                            $('#button-record-stop').hide();
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
                                                    console.log(response);
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
                                                    console.log(response);
                                                }
                                            });
                                        }

                                        var closedSession = function() {
                                            meeting_is_closed = true;
                                            $('#button-record-stop').hide();
                                            $('#button-volume').hide();
                                            $('#button-record').hide();
                                            $('#button-archive').hide();
                                            $('#button-sendMessage').hide();
                                            bootbox.alert("<spring:message code="message.meeting.closed"/>", function() {
                                                returnMeeting();
                                            });
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
                                                    console.log(response);
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
                                            location.href = "searchMeeting.htm";
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
                                                    console.log(response);
                                                },
                                                complete: function() {
                                                    if (callback) {
                                                        callback();
                                                    }
                                                }
                                            });
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
                                                    console.log(response);
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
                                                    for (i = 2; i < 6; i++) {
                                                        $("#nom-" + i).html('&nbsp;');
                                                        $("#main-user-video-" + i).html('<div id="user-video-' + i + '"><img src="css/images/participant.png" alt="participant ' + i + '"></div>');
                                                    }
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
                                            if (!showing_errorNoMicroFound) {
                                                showing_errorNoMicroFound = true;
                                                bootbox.alert("<spring:message code="message.no.micro"/>", function() {
                                                    showing_errorNoMicroFound = false;
                                                    });
                                                } 
                                            }
                                        function errorNotAllowedMicro() {
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
