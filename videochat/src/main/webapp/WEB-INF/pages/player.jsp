<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Videochat - Recorder</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <!-- Optional theme -->
        <script type="text/javascript" src="js/videochat.js" ></script>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/general.css">
    </head> 
    <body>
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
            </header>
            <div class="row" style="margin-top:10px;">
                <div class="col-md-2">
                    <div><strong>${meeting.getStart_meeting_date_txt()}</strong></div>
                    <div><strong>${meeting.getStart_meeting_time_txt()} - ${meeting.getEnd_meeting_time_txt()}</strong></div>
                </div>
                <div class="col-md-10" style="border-left:1px dashed #ccc;">
                    <div><label style="width:20%; font-weight:normal">TOPIC:</label><strong>${meeting.getTopic()}</strong></div>
                    <div><label style="width:20%; font-weight:normal">DESCRIPTION:</label><strong>${meeting.getDescription()}</strong></div>
                </div>
            </div>
            <div class="row wrapper_recorder">
                <div class="col-md-8 player"><div class="my-inner">
                    <!-- TEST PLAY BUTTON -->
                    <button id="play-pause" type="button" class="btn btn-default">PLAY / PAUSE</button>
                </div></div>
                <div class="col-md-4"><div class="my-inner">audio</div></div>
            </div>
            <div class="row">
                <div class="col-md-8 participants">
                    <div class="my-inner">
                        <div class="row header_participants">
                            <h4 class="col-md-10 col-xs-6">Participants</h4>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-1">
                                <div class="participant_content">
                                    <div id="nom-1">&nbsp;</div>
                                    <div id="user-video-1"><img src="css/images/participant.png" alt="participant 4"></div>
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
                                    <div id="user-video-5"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-6">
                                <div class="participant_content">
                                    <div id="nom-6">&nbsp;</div>
                                    <div id="user-video-6"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 aside">
                    <div class="my-inner">
                        <div class="row header_chat">
                            <h4 class="col-md-9">Chat</h4>
                            <div class="btn-group col-md-3">
                                <button type="button" class="btn btn-warning"><span class="glyphicon glyphicon-print"></span></button>
                            </div>
                        </div>
                        <div class="wrapper_chat">
                            <c:forEach items="${meeting.getChat()}" var="item">
                                <p><b>${item.getChat_sent_time_txt()} - ${item.getUser().getFullname()}:</b> ${item.getChat_message()}</p>
                            </c:forEach>  
                        </div> 
                    </div>
                </div>
            </div>



            <!--div id="debug_display">
                <h4>Debug Data</h4>
                <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) :
                <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>)
            </div-->
        </div>  


        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

        <!-- VIDEO PLAYER JS -->
        <script type="text/javascript" src="./js/jwplayer/jwplayer.js"></script>
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 

        <script>

            var participants = new Array();
            $( document ).ready(function() {
                <c:forEach items="${meeting.getParticipants()}" var="item">
                var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKey()}");
                registeredUser(participant);
                </c:forEach>  
                });
            var playing = false;
            var array_streams = Array();
            function registeredUser(info) {
                var pos = returnPositionUser(info.userkey, array_streams);
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

                $("#play-pause").on("click", function(){
                    if (playing){
                        $(participants).each(function(p){
                            participants[p].pause();
                        });
                        playing = false;
                    } else {
                        $(participants).each(function(p){
                            // if (p == 0){setTimeout()}
                            participants[p].play();
                        });
                        playing = true;
                    }
                });

        </script>

    </body>
</html>
