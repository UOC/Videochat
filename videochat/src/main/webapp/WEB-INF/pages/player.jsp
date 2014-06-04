<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><spring:message code="header.videochat.player"/></title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <!-- Optional theme -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <script type="text/javascript" src="js/videochat.js" ></script>
        <link rel="stylesheet" href="css/general.css">
    </head> 
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-1">
                    <a href="searchMeeting.htm" class="videochat-btn-secundary"><span class="glyphicon glyphicon-arrow-left">&nbsp;<spring:message code="message.player.back"/></span></a>
                </div>
                <% String locale = request.getParameter("lang"); %>

                <%String cat = "ca";%>       
                <%String es = "es";%> 
                <%String en = "en";%>
                <%String fr = "fr";%> 
                <%String ir = "ir";%> 
                <%String nl = "nl";%> 
                <%String pl = "pl";%> 
                <%String sv = "sv";%> 

            </div>
            <header class="row"> 
                <div class="col-md-4">
                    <img src="css/images/logo.png" alt="videochat"/></div>
                <div id="idiomes" class="col-md-3 col-md-offset-4">
                    <form:form  name="lang_form" action="player.htm" commandName="player" modelAttribute="course" method="POST">
                        <form:select onchange="changeLanguage(this.value)" path="lang" itemValue="path" itemLabel="path" class="form-control">
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
                                <c:when test="<%=ir.equalsIgnoreCase(locale)%>" >
                                    <form:option value="ir" selected="true"><spring:message code="message.lang.irish"/></form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="ir"><spring:message code="message.lang.irish"/></form:option>
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

                        </form:select>
                    </form:form>


                </div> 
            </header>
            <div class="row" style="margin-top:10px;">
                <div class="col-md-2">
                    <div><strong>${meeting.getStart_record_date_txt()}</strong></div>
                    <div><strong>${meeting.getStart_record_time_txt()} - ${meeting.getEnd_record_time_txt()}</strong></div>
                </div>
                <div class="col-md-10" style="border-left:1px dashed #ccc;">
                    <div><label style="width:20%; font-weight:normal"><spring:message code="label.topic"/>:</label><strong>${meeting.getTopic()}</strong></div>
                    <div><label style="width:20%; font-weight:normal"><spring:message code="label.description"/>:</label><strong>${meeting.getDescription()}</strong></div>
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
                            <h4 class="col-md-10 col-xs-6"><spring:message code="message.participants"/></h4>
                        </div>
                        <c:forEach var="i" begin="1" end="6">
                            <c:if test="${((i mod 3)==1)}">
                        <div class="row">
                            </c:if>
                            <div class="col-md-4 participant" id="user-<c:out value="${i}" />">
                                <div class="participant_content">
                                    <div id="nom-<c:out value="${i}" />">&nbsp;</div>
                                    <c:choose>
                                        <c:when test="${useJWplayer}">
                                            <div id="user-video-<c:out value="${i}" />"><img src="css/images/participant.png" alt="participant <c:out value="${i}" />"></div>
                                        </c:when>
                                        <c:otherwise>
                                            <video id="user-video-<c:out value="${i}" />" class="video-js vjs-default-skin"
                                                preload="auto" width="215" height="143"
                                                poster="css/images/participant.png" data-setup="{}">
                                                <c:if test="${(meeting.getParticipants().size()>=i)}">
                                                    <source src="rtmp://${wowza_stream_server}:1935/vod/&mp4:${meeting.getParticipants().get(i-1).getStreamKeyRecorded()}" type="rtmp/mp4" />
                                                </c:if>
                                            </video>
                                        </c:otherwise>
                                    </c:choose>
                                    <div style="text-align:center" class="button_actions">
                                        <button class="btn btn-warning" type="button" id="button-solo-<c:out value="${i}" />" data-id="1">SOLO</button>
                                        <button class="btn btn-warning" type="button" id="button-mute-<c:out value="${i}" />" data-id="1">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${((i mod 3)==0)}">
                        </div>
                            </c:if>
                        </c:forEach>    
                        
                    </div>
                </div>
                <div class="col-md-4 aside">
                    <div class="my-inner">
                        <div class="row header_chat">
                            <h4 class="col-md-9"><spring:message code="txt.chat.title"/></h4>
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
        </div>  


        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <c:choose>
            <c:when test="${useJWplayer}">
        <!-- VIDEO PLAYER JS -->
        <script type="text/javascript" src="./js/jwplayer/jwplayer.js"></script>
            </c:when>
            <c:otherwise>
        <link href="//vjs.zencdn.net/4.6/video-js.css" rel="stylesheet">
        <script src="//vjs.zencdn.net/4.6/video.js"></script>
            </c:otherwise>
          </c:choose>
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
        <script type="text/javascript" src="js/print.jquery.js" ></script>

        <script>
            var currentVolume = 50;
            function hideButtons() {
                for (i = 1; i <= 6; i++) {
                    $("#button-solo-" + i).hide();
                    $("#button-solo-" + i).on("click", function() {
                        var id = this.attributes['data-id'].value;
                        if (!$("#button-solo-" + id).hasClass('active')) {
                            muteAll(id);
                        } else {
                            $("#button-solo-" + id).removeClass('active');
                            unmuteAll();
                        }
                    });
                    $("#button-mute-" + i).hide();
                    $("#button-mute-" + i).on("click", function() {
                        var id = this.attributes['data-id'].value;
                        if (!$("#button-mute-" + id).hasClass('active')) {
                            $("#button-mute-" + id).addClass('active');
                            $("#button-solo-" + id).removeClass('active');
                            muteThis(id);
                        } else {
                            $("#button-mute-" + id).removeClass('active');
                            unmuteThis(id);
                        }
                    });
                }

            }
            function muteJS(pos, mute_unmute) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).setMute(mute_unmute);
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].muted(mute_unmute);
                    </c:otherwise>
                </c:choose>
            }
            function muteAll(i) {
                $(array_streams).each(function(p) {
                    if ((p + 1) != i) {
                        $("#button-mute-" + (p + 1)).addClass('active');
                        $("#button-solo-" + (p + 1)).removeClass('active');
                    } else {
                        $("#button-solo-" + (p + 1)).addClass('active');
                        $("#button-mute-" + (p + 1)).removeClass('active');
                    }
                    muteJS(p + 1, (p + 1) != i);
                });
            }
            function unmuteAll() {
                $(array_streams).each(function(p) {
                    $("#button-mute-" + p + 1).removeClass('active');
                    muteJS(p + 1, false);
                });
            }
            function muteThis(i) {
                $("#button-mute-" + i).addClass('active');
                muteJS(1, true);
            }
            function unmuteThis(i) {
                $("#button-mute-" + i).removeClass('active');
                muteJS(1, false);
            }
            
            function seekJS(pos, position) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).seek(position);
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].currentTime(position);
                    </c:otherwise>
                </c:choose>
            }
            function volumeJS(pos, volume) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).setVolume(volume);
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].volume(volume/100); //Betwen 0.0 and 1.0
                    </c:otherwise>
                </c:choose>
            }
            $(document).ready(function() {
                hideButtons();
            <c:forEach items="${meeting.getParticipants()}" var="item">
                var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKeyRecorded()}");
                registeredUser(participant);
            </c:forEach>
                $("#button-print").on("click", function() {
                    $("#printable_chat").print();
                });

                $("#play-rewind").on("click", function() {
                    $(array_streams).each(function(p) {
                        seekJS(p + 1, 0);
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
                    start: function(event, ui) {
                        tooltip.fadeIn('fast');
                    },
                    slide: function(event, ui) {

                        var value = slider.slider('value'),
                                volume = $('.volume');
                        if (value <= 5) {
                            value = 0;
                        }
                        currentVolume = value;
                        $(array_streams).each(function(p) {
                            volumeJS(p + 1, currentVolume);
                        });

                        tooltip.css('left', value).text(ui.value);

                        if (value <= 5) {
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
                        }
                        ;

                    },
                    stop: function(event, ui) {
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
                        if (all_streams_are_ready === array_streams.length) {
                            if (!playing) {
                                playOrStopStreams();
                            }
                            var value = sliderPlay.slider('value');
                            $(array_streams).each(function(p) {
                                seekJS(p + 1, duration_video * (value / 100));
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
            var all_streams_are_ready_play = 0;
            var currentVolumeOld = 0;
            var arrayVideos = Array(); //only for videojs
            function registeredUser(info) {
                var pos = returnPositionUser(info.userkey, array_streams);
                streamObj = new StreamObject(info.userkey, info.username, info.publishName);
                if (array_streams.length < 6 || pos >= 0) {
                    if (pos < 0) {
                        array_streams.push(streamObj);
                        pos = array_streams.length;
                    } else {
                        //canviem
                        var array_streams_temp = array_streams;
                        array_streams_temp.splice(index, 1, streamObj);
                        array_streams = array_streams_temp;
                    }
                    $("#button-solo-" + pos).show();
                    $("#button-mute-" + pos).show();
                    $("#nom-" + pos).html(info.username);
                    <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).setup({
                            file: "rtmp://${wowza_stream_server}:1935/vod/mp4:" + info.publishName,
                            image: "",
                            width: 215,
                            height: 138,
                            controls: 'false',
                            preload: 'auto',
                            icons: 'false',
                            modes: [
                                {type: "html5"},
                                {type: "flash", src: "./js/jwplayer/jwplayer.flash.swf"}
                            ],
                            //flashplayer: "./js/jwplayer/jwplayer.flash.swf",
                            events: {
                                onReady: function(e) {
                                    all_streams_are_ready++;
                                },
                                onBuffer: function(t) {
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
                                    if (duration_video > 0 && pos == video_max_length) {
                                        $("#sliderPlay").slider("value", (t.position / duration_video) * 100);
                                    }
                                }
                            }
                        });

                    </c:when>
                    <c:otherwise>
                        var videoObj = videojs("user-video-" + pos, {}, function() {
                                              });
                        videoObj.ready(function(){
                            all_streams_are_ready++;
                            if (all_streams_are_ready === array_streams.length) {
                                currentVolumeOld = currentVolume;
                                currentVolume = 0;
                                playOrStopStreams();
                            }
                                
                          }); 
                          videoObj.on("loadedalldata", function() {
                              all_streams_are_ready_play++;
                              if (all_streams_are_ready_play === array_streams.length) {
                                currentVolume = currentVolumeOld;
                                playOrStopStreams();
                                this.currentTime(0);
                                //$("#sliderPlay").slider("value", 0 * 100);
                              }
                          });
                          videoObj.on("durationchange", function() {
                                var duration_video_temp = this.duration();
                                console.log("current "+pos+" and duration "+duration_video_temp);
                                if (duration_video < duration_video_temp) {
                                    duration_video = duration_video_temp;
                                    video_max_length = pos;
                                    console.log("video_max_length "+pos+" and duration "+duration_video_temp);
                                }
                                
                             });
                            videoObj.on("timeupdate", function() {
                                if (duration_video > 0 && pos == video_max_length) {
                                    $("#sliderPlay").slider("value", (this.currentTime() / duration_video) * 100);
                                }
                            });
                                

                          arrayVideos.push(videoObj);
                    </c:otherwise>
                </c:choose>

                }
            }

            $("#play-pause").on("click", function() {
                playOrStopStreams();
            });
            
            function playJS(pos) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).play();
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].play();
                    </c:otherwise>
                </c:choose>
            }
            function pauseJS(pos) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).pause();
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].pause();
                    </c:otherwise>
                </c:choose>
            }
            function playOrStopStreams() {
                if (playing) {
                    $(array_streams).each(function(p) {
                        pauseJS(p + 1);
                    });
                    $("#glyphicon-play").removeClass("glyphicon-pause");
                    $("#glyphicon-play").addClass("glyphicon-play");
                } else {
                    $(array_streams).each(function(p) {
                        // if (p == 0){setTimeout()}
                        playJS(p + 1);
                        volumeJS(p + 1, currentVolume);
                    });
                    $("#glyphicon-play").removeClass("glyphicon-play");
                    $("#glyphicon-play").addClass("glyphicon-pause");
                }
                playing = !playing;
            }

        </script>

    </body>
</html>
