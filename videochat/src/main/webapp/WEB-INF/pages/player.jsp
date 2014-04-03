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
            <div class="row">
                <div class="col-md-1">
                    <a href="searchMeeting.htm" class="videochat-btn-secundary"><span class="glyphicon glyphicon-arrow-left">&nbsp;Back</span></a>
                </div>
                
            </div>
            <header class="row">
                <div class="col-md-4">
                    <img src="css/images/logo.png" alt="videochat"/></div>
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
                    <div><label style="width:20%; font-weight:normal"><spring:message code="label.topic"/></label><strong>${meeting.getTopic()}</strong></div>
                    <div><label style="width:20%; font-weight:normal">DESCRIPTION:</label><strong>${meeting.getDescription()}</strong></div>
                </div>
            </div>
            <div class="row wrapper_recorder">
                <div class="col-md-8 player"><div class="my-inner">
                    <!-- TEST PLAY BUTTON -->
                    <button id="play-rewind" type="button" class="btn btn-warning" title="Rewind"><span class="glyphicon glyphicon-backward" ></button> ${item.getTotal_time_txt()}
                    <button id="play-pause" type="button" class="btn btn-warning" title="Play"><span class="glyphicon glyphicon-play" id="glyphicon-play"></button> ${item.getTotal_time_txt()}
                    <div id="sliderPlay"></div>  
                </div></div>
                <div class="col-md-4">
                        <section>   
                            <span class="tooltip"></span>   
                            <div id="slider"></div>  
                            <span class="volume"></span>  
                        </section>  
                    </div>
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
                                    <div style="text-align:center" class="button_actions">
                                        <button class="btn btn-warning" type="button" id="button-solo-1" data-id="1">SOLO</button>
                                        <button class="btn btn-warning" type="button" id="button-mute-1" data-id="1">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-2">
                                <div class="participant_content">
                                    <div id="nom-2">&nbsp;</div>
                                    <div id="user-video-2"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                                    <div style="text-align:center" class="button_actions">
                                        <button class="btn btn-warning" type="button" id="button-solo-2" data-id="2">SOLO</button>
                                        <button class="btn btn-warning" type="button" id="button-mute-2" data-id="2">MUTE</button>
                                    </div>
                            </div>
                            <div class="col-md-4 participant" id="user-3">
                                <div class="participant_content">
                                    <div id="nom-3">&nbsp;</div>
                                    <div id="user-video-3"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                                    <div style="text-align:center" class="button_actions">
                                        <button class="btn btn-warning" type="button" id="button-solo-3" data-id="3">SOLO</button>
                                        <button class="btn btn-warning" type="button" id="button-mute-3" data-id="3">MUTE</button>
                                    </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-4">
                                <div class="participant_content">
                                    <div id="nom-4">&nbsp;</div>
                                    <div id="user-video-4"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                                <div style="text-align:center" class="button_actions">
                                    <button class="btn btn-warning" type="button" id="button-solo-4" data-id="4">SOLO</button>
                                    <button class="btn btn-warning" type="button" id="button-mute-4" data-id="4">MUTE</button>
                                </div>
                                
                            </div>
                            <div class="col-md-4 participant" id="user-5">
                                <div class="participant_content">
                                    <div id="nom-5">&nbsp;</div>
                                    <div id="user-video-5"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                                <div style="text-align:center" class="button_actions">
                                    <button class="btn btn-warning" type="button" id="button-solo-5" data-id="5">SOLO</button>
                                    <button class="btn btn-warning" type="button" id="button-mute-5" data-id="5">MUTE</button>
                                </div>
                                
                            </div>
                            <div class="col-md-4 participant" id="user-6">
                                <div class="participant_content">
                                    <div id="nom-6">&nbsp;</div>
                                    <div id="user-video-6"><img src="css/images/participant.png" alt="participant 4"></div>
                                </div>
                                <div class="button_actions">
                                    <button class="btn btn-warning" type="button" id="button-solo-6" data-id="6">SOLO</button>
                                    <button class="btn btn-warning" type="button" id="button-mute-6" data-id="6">MUTE</button>
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
                                <button type="button" class="btn btn-warning" id="button-print"><span class="glyphicon glyphicon-print"></span></button>
                            </div>
                        </div>
                        <div class="wrapper_chat" id="printable_chat">
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
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

        <script>
            var currentVolume = 50;
            function hideButtons() {
                for (i=1; i<=6; i++) {
                 $("#button-solo-"+i).hide();   
                 $("#button-solo-"+i).on("click", function(){
                     var id = this.attributes['data-id'].value;
                     if (!$("#button-solo-"+id).hasClass('active')) {
                         muteAll(id);
                     } else {
                        $("#button-solo-"+id).removeClass('active');
                        unmuteAll();
                     }
                 });
                 $("#button-mute-"+i).hide();   
                 $("#button-mute-"+i).on("click", function(){
                     var id = this.attributes['data-id'].value;
                     if (!$("#button-mute-"+id).hasClass('active')) {
                        $("#button-mute-"+id).addClass('active');
                        $("#button-solo-"+id).removeClass('active');
                        muteThis(id);
                     } else {
                        $("#button-mute-"+id).removeClass('active');
                        unmuteThis(id);
                     }
                 });
                }
                
            }
            function muteAll(i) {
                $(array_streams).each(function(p){
                    if ((p+1)!=i) {
                        $("#button-mute-"+(p+1)).addClass('active');
                        $("#button-solo-"+(p+1)).removeClass('active');
                    } else {
                        $("#button-solo-"+(p+1)).addClass('active');
                        $("#button-mute-"+(p+1)).removeClass('active');
                    }
                    jwplayer("user-video-"+(p+1)).setMute((p+1)!=i);
                });
            }
            function unmuteAll() {
                $(array_streams).each(function(p){
                    $("#button-mute-"+p+1).removeClass('active');
                    jwplayer("user-video-"+(p+1)).setMute(false);
                });
            }
            function muteThis(i) {
                $("#button-mute-"+i).addClass('active');
                jwplayer("user-video-"+i).setMute(true);
            }
            function unmuteThis(i) {
                $("#button-mute-"+i).removeClass('active');
                jwplayer("user-video-"+i).setMute(false);
            }

            $( document ).ready(function() {
                hideButtons();
                <c:forEach items="${meeting.getParticipants()}" var="item">
                var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKeyRecorded()}");
                registeredUser(participant);
                </c:forEach> 
                $("#button-print").on("click", function(){
                    $("#printable_chat").print();
                });
                    
                $("#play-rewind").on("click", function(){
                    $(array_streams).each(function(p){
                        jwplayer("user-video-"+(p+1)).seek(0);
                    });
                });
                var slider = $('#slider'),  
                    tooltip = $('.tooltip');  

                tooltip.hide();  

                slider.slider({  
                    range: "min",  
                    min: 0,  
                    step: 5,  
                    max: 100,  
                    value: currentVolume,  

                    start: function(event,ui) {  
                      tooltip.fadeIn('fast');  
                    },  

                    slide: function(event, ui) {  

                        var value = slider.slider('value'),  
                            volume = $('.volume');  
                            if (value<=5) {
                                value = 0;
                            }
                            currentVolume = value;
                            $(array_streams).each(function(p){
                                console.log(currentVolume);
                                jwplayer("user-video-"+(p+1)).setVolume(currentVolume);    
                            });

                        tooltip.css('left', value).text(ui.value);  

                        if(value <= 5) {   
                            volume.css('background-position', '0 0');  
                        }   
                        else if (value <= 25) {  
                            volume.css('background-position', '0 -25px');  
                        }   
                        else if (value <= 75) {  
                            volume.css('background-position', '0 -50px');  
                        }   
                        else {  
                            volume.css('background-position', '0 -75px');  
                        };  

                    },  

                    stop: function(event,ui) {  
                      tooltip.fadeOut('fast');  
                    },  
                });  
                var sliderPlay = $('#sliderPlay');
                    sliderPlay.slider({  
                        range: "min",  
                        min: 0,  
                        max: 100,  
                        value: 0,  
                        slide: function(event, ui) {  
                            if (all_streams_are_ready===array_streams.length) {
                                if (!playing) {
                                    playOrStopStreams();
                                }
                                var value = sliderPlay.slider('value');
                                $(array_streams).each(function(p){
                                    jwplayer("user-video-"+(p+1)).seek(duration_video*(value/100));    
                                });
                            } else {
                                sliderPlay.slider('value', 0)
                            }
                        },  

                    });
                               
                });
            var duration_video = 0;    
            var playing = false;
            var array_streams = Array();
            var video_max_length = 0;
            var all_streams_are_ready = 0;
            function registeredUser(info) {
                var pos = returnPositionUser(info.userkey, array_streams);
                streamObj = new StreamObject(info.userkey, info.username, info.publishName);
                if (array_streams.length<6 || pos>=0) {
                    if (pos<0) {
                        array_streams.push(streamObj);
                        pos = array_streams.length;
                    } else {
                        //canviem
                        var array_streams_temp = array_streams;
                        array_streams_temp.splice(index, 1, streamObj);
                        array_streams = array_streams_temp;
                    }
                    $("#button-solo-"+pos).show();   
                    $("#button-mute-"+pos).show();   
                    $("#nom-"+pos).html(info.username);
                    jwplayer("user-video-"+(pos)).setup({
                                            file: "rtmp://${wowza_stream_server}:1935/vod/mp4:"+info.publishName,
                                            image: "",
                                            width: 215,
                                            height: 138,
                                            controls: 'false',
                                            preload: 'auto',
                                            icons: 'false',
                                            modes: [
                                                { type: "html5" },
                                                { type: "flash", src: "./js/jwplayer/jwplayer.flash.swf" }
                                              ],
                                            //flashplayer: "./js/jwplayer/jwplayer.flash.swf",
                                            events:{
                                                onReady: function(e){
                                                    all_streams_are_ready++;
                                                },
                                                onBuffer: function(t){
                                                    // Buffer not exposed in RMTP playback
                                                    var duration_video_temp = this.getDuration();            
                                                    if (duration_video < duration_video_temp) {
                                                        duration_video = duration_video_temp;
                                                        video_max_length = pos;
                                                    }
                                                },
                                                onPlay: function() {
                                                    var duration_video_temp = this.getDuration();            
                                                    if (duration_video < duration_video_temp) {
                                                        duration_video = duration_video_temp;
                                                        video_max_length = pos;
                                                    }                                                    
                                                },
                                                onTime: function(t) {
                                                    if (duration_video>0 && pos == video_max_length) {
                                                     $("#sliderPlay").slider( "value", (t.position/duration_video)*100 );
                                                    }
                                                }
                                            }
                                        });           
                  }
            }

                $("#play-pause").on("click", function(){
                    playOrStopStreams();
                });
                
                function playOrStopStreams() {
                    if (playing){
                        $(array_streams).each(function(p){
                            jwplayer("user-video-"+(p+1)).pause();
                        });
                        playing = false;
                        $("#glyphicon-play").removeClass("glyphicon-pause");
                        $("#glyphicon-play").addClass("glyphicon-play");
                    } else {
                        $(array_streams).each(function(p){
                            // if (p == 0){setTimeout()}
                            jwplayer("user-video-"+(p+1)).play();
                            jwplayer("user-video-"+(p+1)).setVolume(currentVolume);
                        });
                        $("#glyphicon-play").removeClass("glyphicon-play");
                        $("#glyphicon-play").addClass("glyphicon-pause");
                    }
                }

        </script>

    </body>
</html>
