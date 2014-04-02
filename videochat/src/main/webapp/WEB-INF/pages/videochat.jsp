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
		<title>Videochat - Recorder</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <!-- Optional theme -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/general.css">
        <script type="text/javascript" src="js/swfobject.js" ></script>
        <!-- VIDEO PLAYER JS -->
        <script type="text/javascript" src="js/jwplayer/jwplayer.js"></script>
	</head>	
    <body>
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
        <!-- /.modal -->
        <!-- modal parar gravació-->
    	<div class="modal fade" id="Recorded">
            <div class="modal-dialog">
            	<div class="modal-content">
            		<div class="modal-header">
            			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            			<h4 class="modal-title">Important</h4>
            		</div>
            		<div class="modal-body">
            			<p><strong>This session is being recorded, what do you want to do?</strong></p>
                        <div>Cancel: This session continues recording<br/>
                        	 Stop: Stop this recording and reconnect to decide what to do, save or repeat</div>
                             <div>Save: Archive and close this session, it will be published on videochat</div>
                        
            		</div>
            		<div class="modal-footer">
            			<button type="button" class="btn btn-warning">Cancel</button>
                        <button type="button" class="btn btn-warning" onclick="stopRecordRequest()">Stop</button>
                        <button type="button" class="btn btn-warning" onclick="closeMeetingRequest()">Save and close</button>
            		</div>
            	</div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
        <!-- /.modal -->
        
    	<div class="container">
        	<header class="row">
            	<div class="col-md-4"><img src="css/images/logo.png" alt="videochat"/></div>
                <div id="idiomes" class="col-md-3 col-md-offset-4">
                    <select class="form-control">
                      <option>English</option>
                      <option>Catalan</option>
                      <option>Spanish</option>
                      <option>Polish</option>
                      <option>Dutch</option>
                      <option>Swedish</option>
                      <option>Irish</option>
                    </select>
                </div>
                <div id="close" class="col-md-1">
                    <span class="glyphicon glyphicon-remove"></span>
                </div>
            </header>
            <h3><c:out value="${sMeeting.getId_room().getLabel()}"/></h3>
            <div class="row wrapper_buttons">	
                <div class="col-md-3 col-xs-7">
                	<button type="button" id="button-record" class="btn btn-warning" data-toggle="modal" data-target="#record"><span class="glyphicon glyphicon-record"></span> RECORD</button>
                    <!-- modal del botó RECORD -->
                                <div class="modal fade" id="record">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                <h4 class="modal-title">Important</h4>
                                            </div>
                                            <div class="modal-body">
                                                <div><strong>Are you sure you want to start a new recorder?</strong></div>
                                                <div>If you continue the previously made recording will be deleted.</div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>
                                                <button type="button" data-dismiss="modal" class="btn btn-warning" onclick="startRecordRequest()">Continue</button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div>
        						<!-- END modal del botó RECORD -->
                	<button type="button" id="button-record-stop" class="btn btn-warning" data-toggle="modal" data-target="#record-stop"><span class="glyphicon glyphicon-stop"></span> STOP</button>
                    <!-- modal del botó RECORD -->
                                <div class="modal fade" id="record-stop">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                <h4 class="modal-title">Important</h4>
                                            </div>
                                            <div class="modal-body">
                                                <div><strong>Are you sure you want to stop a recorder?</strong></div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>
                                                <button type="button" data-dismiss="modal" class="btn btn-warning" onclick="stopRecordRequest()">Continue</button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div>
        						<!-- END modal del botó RECORD -->
                                                        
                     <button type="button" id="button-volume" class="btn btn-warning"><span class="glyphicon glyphicon-volume-up"></span></button>
                </div>
                <div class="col-md-2 col-md-offset-7 col-xs-5">
                	<button type="button" class="btn btn-warning" data-toggle="modal" id="button-archive" data-target="#archive"><span class="glyphicon glyphicon-save"></span> ARCHIVE & CLOSE</button>
                    <!-- modal del botó RECORD -->
                    <div class="modal fade" id="archive">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">Important</h4>
                                </div>
                                <div class="modal-body">
                                    <div><strong>Are you sure you want to archive and close the last recording made?</strong></div>
                                    <div>If you want to continue you will not be able to make more recordings within this session.</div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                    <button type="button" class="btn btn-warning" onclick="closeMeetingRequest()">Continue</button>
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
                            <h4 class="col-md-10 col-xs-6">Participants</h4>
                            <div class="btn-group col-md-2 col-xs-6">
                                <button type="button" class="btn btn-warning" id="button-reload"><span class="glyphicon glyphicon-repeat"></span></button>
                                <button type="button" class="btn btn-warning" id="button-configuration"><span class="glyphicon glyphicon-cog"></span></button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-1">
                                <div class="participant_content">
                                    <div id="nom-1"><c:out value="${sUser.getFullname()}"/></div>
                                    <div id="videochat_stream">
                                        <p>Alternative content</p>
                                        <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-2">
                                <div class="participant_content">
                                    <div id="nom-2">&nbsp;</div>
                                    <div id="user-video-2"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-3">
                                <div class="participant_content">
                                    <div id="nom-3">&nbsp;</div>
                                    <div id="user-video-3"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-4">
                            	<div class="participant_content">
                                    <div id="nom-4">&nbsp;</div>
                                    <div id="user-video-4"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-5">
                            	<div class="participant_content">
                                    <div id="nom-5">&nbsp;</div>
                                    <div id="user-video-5"><img src="css/images/participant.png" alt="participant 5"></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-6">
                            	<div class="participant_content">
                                    <div id="nom-6">&nbsp;</div>
                                    <div id="user-video-6"><img src="css/images/participant.png" alt="participant 6"></div>
                                </div>
                            </div>
                        </div>
                    </div>
              	</div>
                <div class="col-md-4 aside">
                	<div class="my-inner">
                    	<div class="header_chat row">
                			<h4>Chat</h4>
                    	</div>
                        <div class="wrapper_chat" id="chatContainer">
                        	
                        </div> 
                        <p>Enter your text here:</p>
                            <div class="row">
                                <div class="col-xs-10 col-md-9"><input type="text" class="form-control" id="messageTxt" maxlength="100" /></div>
                                <div class="col-xs-2 col-md-3"><button class="btn btn-warning" name="button-sendMessage" id="button-sendMessage">Send</button></div>
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
            var meeting_is_recorded = ${is_recorded};
            var meeting_is_closed = false;
            $( document ).ready(function() {
                
                $('#button-record-stop').hide();
                $('#button-volume').hide();
                $('#button-record').hide();
                $('#button-archive').hide();
                $('#button-sendMessage').hide();
                
                <c:forEach items="${participants}" var="item">
                    var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKey()}");
                    registeredUser(participant);
                 </c:forEach>  
                $("#button-sendMessage").click(
                   function() {
                        sendChatMessage();
                    }     
                );
                $('#messageTxt').keydown(function (e){
                    if(e.keyCode == 13){
                        sendChatMessage();
                    }
                });
                $("#button-reload").click(
                   function() {
                        location.reload();
                    }     
                );
                $("#button-configuration").click(
                   function() {
                        bootbox.alert("Manage configuration is not available yet", function() {});
                    }     
                );
            });
            function sendChatMessage() {
                var message = $("#messageTxt").val();
                if (message.length>0) {

                    var flash = swfobject.getObjectById("videochat_stream_id");
                    flash.sendChatMessage(message);
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
                id : "videochat_stream_id"
            };
            swfobject.embedSWF("FlashRTMPPlayer/recorder.swf", "videochat_stream", "215", "138", "9.0.0", "expressInstall.swf", flashvars, params, attributes);
        
            function setSWFIsReady() {
                swf_is_ready= true;
                $('#button-volume').show();
                $('#button-record').show();
                $('#button-archive').show();
                $('#button-sendMessage').show();
            }

            function stopRecordRequest(){
                var flash = swfobject.getObjectById("videochat_stream_id");
                flash.stopRecordFromJS();
            }
            
            function closeMeetingRequest(){
                if (meeting_is_recorded) {
                    if ($('#button-record-stop').is(":visible") ) {
                        stopRecordRequest();
                    }
                    var flash = swfobject.getObjectById("videochat_stream_id");
                    flash.closeMeetingFromJS();
                } else {
                    bootbox.alert("The meeting has to be recorded to close it");
                }
            }
            
            function startRecordRequest(){
                var flash = swfobject.getObjectById("videochat_stream_id");
                flash.startRecordFromJS();
            }
            var array_streams = Array();
            function StreamObject(userkey, username, publishName) {
                this.userkey = userkey;
                this.username = username;
                this.publishName = publishName;
            }
            function returnPositionUser(userkey){
                var pos = -1;
                for(i=0; i<array_streams.length; i++) {
                    if (array_streams[i].userkey===userkey) {
                        pos = i;
                        break;
                    }
                }
                return pos;
            }
            function registeredUser(info) {
                var pos = returnPositionUser(info.userkey);
                streamObj = new StreamObject(info.userkey, info.username, info.publishName);
                if (array_streams.length<6 || pos>=0) {
                    if (pos<0) {
                        array_streams.push(streamObj);
                        pos = array_streams.length+1;
                    } else {
                        //canviem
                        var array_streams_temp = array_streams;
                        array_streams_temp.splice(index, 1, streamObj);
                        array_streams = array_streams_temp;
                    }
                    $("#nom-"+pos).html(info.username);
                    jwplayer("user-video-"+(pos)).setup({
                                            file: "rtmp://${wowza_stream_server}:1935/videochat/"+info.publishName,
                                            image: "",
                                            width: 215,
                                            height: 138,
                                            controls: 'false',
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
                           jwplayer("user-video-"+(pos)).play();             
                  }
            }
            
            function currentUserAcceptConnection() {
                var json = { "request" : "${sUserMeeting.getStreamKey()}"};  

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
            
            function newChatMessage(info) {
                var str = "<p><b>"+info.username+":</b> "+info.message+"</p>";
                $("#chatContainer").append(str);  
                var height = $("#chatContainer").get(0).scrollHeight;
                $("#chatContainer").scrollTop(height);
                if (info.userkey=="${sUser.getUsername()}") {
                    $("#messageTxt").val("");
                    var json = { "request" : message};  

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
            
            function startedRecord() {
                $('#button-record').hide();
                meeting_is_recorded = true;
                var json = { "request" : "${sUserMeeting.getStreamKey()}"};  

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
            
            function stoppedRecord() {
                $('#button-record').show();
                $('#button-record-stop').hide();
            }
            
            function stoppedRecordAjax() {
                var json = { "request" : "${sUserMeeting.getStreamKey()}"};  

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
            
            function closeSessionAjax() {
                var json = { "request" : "${sUserMeeting.getStreamKey()}"};  

                $.ajax({  
                  url: 'rest/meeting.json',  
                  data: JSON.stringify(json),  
                  type: "DELETE",  

                  beforeSend: function(xhr) {  
                      xhr.setRequestHeader("Accept", "application/json");  
                      xhr.setRequestHeader("Content-Type", "application/json");  
                  },  
                  success: function(response) {  
                      console.log(response);
                  }  
              });
            }
            
            function closedSession() {
                meeting_is_closed = true;
                $('#button-record-stop').hide();
                $('#button-volume').hide();
                $('#button-record').hide();
                $('#button-archive').hide();
                $('#button-sendMessage').hide();
                bootbox.alert("The session has been closed", function() {
                    location.href = "searchMeeting.htm";
                });
            }
            
            function removeByIndex(arr, index) {
                arr.splice(index, 1);
            }
            function disconnectedUser(info) {
                if (!meeting_is_closed) {
                    var pos = returnPositionUser(info.userkey);
                    if (pos>=0) {
                       var array_streams_temp = array_streams;
                       removeByIndex(array_streams_temp, pos);
                       for( i=2; i<6; i++) {
                          $("#nom-"+i).html(''); 
                          $("#user-video-"+i).html('<img src="css/images/participant.png" alt="participant 4">'); 
                       }
                       array_streams = Array();
                       for( i=0; i<array_streams_temp.length; i++) {
                          registeredUser(array_streams_temp[i]);
                       }
                        var json = { "request" : info.userkey};  

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
                    bootbox.alert("User "+info.username+" has left the meeting", function() {});
                }
                
            }

		</script>
    </body>
</html>