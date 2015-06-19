<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%        int add_new_line = 2;
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
        int height_of_video = 146;
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

            <c:choose>
                <c:when test="${not disable_back_button}" >
                    <div class="row">
                        <div class="col-md-1">
                            <a href="searchMeeting.htm" class="videochat-btn-secundary"><span class="glyphicon glyphicon-arrow-left">&nbsp;<spring:message code="message.player.back"/></span></a>
                        </div>
                    </div>
                </c:when>
            </c:choose>  

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
                <%String fi = "fi";%> 
                <%String de = "de";%> 

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
            <div class="row" style="margin-top:10px;">
                <div class="col-md-2">
                    <div><strong>${meeting.getStart_record_date_txt()}</strong></div>
                    <div><strong>${meeting.getStart_record_time_txt()} - ${meeting.getEnd_record_time_txt()}</strong></div>
                    <strong><span id="currentTimeId">00:00</span></strong>
                </div>
            <c:choose>
                <c:when test="${not disable_back_button}" >                
                <div class="col-md-10 dashed">
                    <div><label style="width:20%; font-weight:normal"><spring:message code="label.topic"/>:</label><strong>${meeting.getTopic()}</strong></div>
                    <div><label style="width:20%; font-weight:normal"><spring:message code="label.description"/>:</label><strong>${meeting.getDescription()}</strong></div>
                </div>
                </c:when>
            </c:choose>    
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
                        <c:forEach var="i" begin="1" end="${max_participants}">
                            <c:if test="${i % add_new_line==1}">
                        <div class="row">
                            </c:if>
                            <div class="col-md-<c:out value="${size_of_video}"/> participant" id="user-<c:out value="${i}" />">
                                <div class="participant_content<c:out value="${concat_size_video}"/>">
                                    <div class='user-role'>  </div>
                                    <div id="nom-<c:out value="${i}" />">&nbsp;</div>
                                    <c:choose>
                                        <c:when test="${useJWplayer}">
                                            <div id="user-video-<c:out value="${i}" />"><img src="css/images/participant.png" class="participant_image_content<c:out value="${concat_size_video}"/>" alt="participant <c:out value="${i}" />"></div>
                                        </c:when>
                                        <c:otherwise>
                                            <video id="user-video-<c:out value="${i}" />" class="video-js vjs-default-skin"
                                                preload="auto" width="215" height="143"
                                                poster="css/images/participant.png" data-setup="{}" <c:if test="${(meeting.getParticipants().size()>=i)}">mediagroup="video2playgroup"</c:if>>
                                                <c:if test="${(meeting.getParticipants().size()>=i)}">
                                                    <source src="rtmp://${wowza_stream_server}:1935/${vod_application_player}${meeting.getParticipants().get(i-1).getStreamKeyRecorded()}" type="rtmp/mp4" />
                                                </c:if>
                                            </video>
                                        </c:otherwise>
                                    </c:choose>
                                    <div style="text-align:center" class="button_actions">
                                        <button class="btn btn-warning" type="button" id="button-solo-<c:out value="${i}" />" data-id="${i}">SOLO</button>
                                        <button class="btn btn-warning" type="button" id="button-mute-<c:out value="${i}" />" data-id="${i}">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${i % add_new_line==0}">
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
            <footer class="row"> 
                <div style="float: left; margin-top: 0pt; margin-left: 50px;text-align: justify; width: 600px;"><span style="font-size:9px;">This project has been funded with support from the Lifelong Learning Programme of the European Commission.  <br />
    This site reflects only the views of the authors, and the European Commission cannot be held responsible for any use which may be made of the information contained therein.</span>
    </div>
                     &nbsp; <img src="css/images/EU_flag.jpg" alt="" />
            </footer>
                            
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
        <script type="text/javascript" src="./js/synchronize.js"></script>
            </c:otherwise>
          </c:choose>
        <!-- jQuery -->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script> 
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
        <script type="text/javascript" src="js/print.jquery.js" ></script>

        <script>
            var currentVolume = 50;
            function hideButtons() {
                for (i = 1; i <= ${max_participants}; i++) {
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
                        jwplayer("user-video-" + pos).setMute(mute_unmute);
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
                muteJS(i, true);
            }
            function unmuteThis(i) {
                $("#button-mute-" + i).removeClass('active');
                muteJS(i, false);
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
                var participant = new StreamObject("${item.getPk().getUser().getUsername()}", "${item.getPk().getUser().getFullname()}", "${item.getStreamKeyRecorded()}","${item.getExtra_role()!=null?item.getExtra_role():""}");
                
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
                            playing=false;
                        <c:choose>
                            <c:when test="${useJWplayer}">
                            $(array_streams).each(function(p) {
                                playing=false;
                                pauseJS(p + 1);
                            });
                            </c:when>
                            <c:otherwise>
                                pauseJS(1);
                            </c:otherwise>
                        </c:choose>
                            $("#glyphicon-play").removeClass("glyphicon-pause");
                            $("#glyphicon-play").addClass("glyphicon-play");

                            $(array_streams).each(function(p) {
                                playing=false;
                                seekJS(p + 1, duration_video * (value / 100));
                            });
                            $(array_streams).each(function(p) {
                                playing=false;
                                pauseJS(p + 1);
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
            var total_seconds = 0;
            function registeredUser(info) {
                var pos = returnPositionUser(info.userkey, array_streams);
                streamObj = new StreamObject(info.userkey, info.username, info.publishName);
                if (array_streams.length < ${max_participants} || pos >= 0) {
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
                    // victor - Lets put the role on top of the admin name.
                    if (info.extra_role!=""){
                        var Role = "<span>"+info.extra_role+"</span>";
                    }
                    $("#user-" + pos + " .user-role ").html(Role);
                    <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).setup({
                            file: "rtmp://${wowza_stream_server}:1935/${vod_application_player}" + info.publishName,
                            image: "",
                            width: "<c:out value="${width_of_video}"/>",
                            height: <c:out value="${height_of_video}"/>,
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
                                }
                                <%-- Deprecated
                                ,onBufferChange: function(t) {
                                    // Buffer not exposed in RMTP playback
                                    var duration_video_temp = this.getDuration();
                                    if (duration_video < duration_video_temp) {
                                        duration_video = duration_video_temp;
                                        alert(duration_video);
                                        video_max_length = pos;
                                    }
                                },
                                onBeforePlay: function() {
                                    var duration_video_temp = this.getDuration();
                                    if (duration_video < duration_video_temp) {
                                        duration_video = duration_video_temp;
                                        video_max_length = pos;
                                    }
                                },
                                onTime: function(t) {
                                    if (duration_video > 0 && pos == video_max_length) {
                                        if (playing) {
                                            if (total_seconds!=t.position) {
                                                $("#sliderPlay").slider("value", (t.position / duration_video) * 100);
                                                $("#currentTimeId").html(secondsToHms(t.position));
                                            }
                                        }
                                    }
                                }--%>
                            }
                        });
                        if (pos == ${meeting.getParticipants().size()}) {
                            jwplayer("user-video-" + (pos)).onTime(function (t) {
                                set_max_duration();
                                if (duration_video > 0) {
                                    if (playing) {
                                        var current_position = parseInt(t.position, 10);
                                        if (total_seconds!=current_position) {
                                            total_seconds = current_position;
                                            $("#sliderPlay").slider("value", (t.position / duration_video) * 100);
                                            $("#currentTimeId").html(secondsToHms(current_position));
                                        }
                                    }
                                }
                            });
                        } 

                    </c:when>
                    <c:otherwise>
                        var videoObj = videojs("user-video-" + pos, {}, function() {
                                              });
                        videoObj.ready(function(){
                            if (++all_streams_are_ready === array_streams.length) {
                                /*currentVolumeOld = currentVolume;
                                currentVolume = 0;
                                playOrStopStreams();*/
                                $(document).on("sjs:allPlayersReady", function(event) {
                                    // All players have been successfully initialized - do something!
                                });
                                // 1. Synchronize via mediagroup
                                $.synchronizeVideos(0, "video2playgroup");
                            }
                                
                          }); 
                        /*  videoObj.on("loadedalldata", function() {
                              all_streams_are_ready_play++;
                              if (all_streams_are_ready_play === array_streams.length) {
                                currentVolume = currentVolumeOld;
                                playOrStopStreams();
                                syncAll(0, 0);
                                //$("#sliderPlay").slider("value", 0 * 100);
                              }
                          });
    */
                          videoObj.on("durationchange", function() {
                                var duration_video_temp = this.duration();
                                if (duration_video < duration_video_temp) {
                                    duration_video = duration_video_temp;
                                    video_max_length = pos;
                                }
                                
                             });
                            videoObj.on("timeupdate", function() {
                                if (duration_video > 0 && pos == video_max_length) {
                                    if (playing) {
                                        $("#sliderPlay").slider("value", (this.currentTime() / duration_video) * 100);
                                        $("#currentTimeId").text(secondsToHms(this.currentTime()));

                                    }
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
            
            
            function set_max_duration() {
                if (duration_video==0) {
                    for (var pos=1; pos<=array_streams.length; pos++) {
                        var d = jwplayer("user-video-" + (pos)).getDuration();
                        if (d>duration_video) {
                            duration_video = d;
                        }
                    }
                }
            }
            
            function pad_with_zeroes(number, length) {

                var my_string = '' + number;
                while (my_string.length < length) {
                    my_string = '0' + my_string;
                }

                return my_string;

            }

            function secondsToHms(d) {
                d = Number(d);
                var h = Math.floor(d / 3600);
                var m = pad_with_zeroes(Math.floor(d % 3600 / 60), 2);
                var s = pad_with_zeroes(Math.floor(d % 3600 % 60), 2);
                return ((h > 0 ? h + ":":"") + m + ":" + s); 
            }

            
            function playJS(pos) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).play(true);
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].play();
                    </c:otherwise>
                </c:choose>
            }
            function pauseJS(pos) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                        jwplayer("user-video-" + (pos)).pause(true);
                    </c:when>
                    <c:otherwise>
                        arrayVideos[pos-1].pause();
                    </c:otherwise>
                </c:choose>
            }
            function playOrStopStreams() {
                if (playing) {
                <c:choose>
                    <c:when test="${useJWplayer}">
                    $(array_streams).each(function(p) {
                        pauseJS(p + 1);
                    });
                    </c:when>
                    <c:otherwise>
                        pauseJS(1);
                    </c:otherwise>
                </c:choose>
                    $("#glyphicon-play").removeClass("glyphicon-pause");
                    $("#glyphicon-play").addClass("glyphicon-play");
                } else {
                        // if (p == 0){setTimeout()}
                <c:choose><c:when test="${useJWplayer}">
                    $(array_streams).each(function(p) {
                        playJS(p + 1);
                        volumeJS(p + 1, currentVolume);
                    });
                    </c:when>
                    <c:otherwise>
                        pauseJS(1);
                    </c:otherwise></c:choose> 
                    $("#glyphicon-play").removeClass("glyphicon-play");
                    $("#glyphicon-play").addClass("glyphicon-pause");
                }
                playing = !playing;
            }
           <c:if test="${!useJWplayer}"> 
           function syncAll(i, position) {
                $(array_streams).each(function(p) {
                    if ((p + 1) != i) {
                        seekJS(p+1, position);
                    }
                });
            }
            /*var videos = {
                a: Popcorn("#a"),    
                b: Popcorn("#b"), 
            },
            scrub = $("#scrub"),
            loadCount = 0, 
            events = "play pause timeupdate seeking".split(/\s+/g);

            // iterate both media sources
            Popcorn.forEach( videos, function( media, type ) {

                // when each is ready... 
                media.on( "canplayall", function() {

                    // trigger a custom "sync" event
                    this.emit("sync");

                // Listen for the custom sync event...    
                }).on( "sync", function() {

                    // Once both items are loaded, sync events
                    if ( ++loadCount == 2 ) {

                        // Iterate all events and trigger them on the video B
                        // whenever they occur on the video A
                        events.forEach(function( event ) {

                            videos.a.on( event, function() {

                                // Avoid overkill events, trigger timeupdate manually
                                if ( event === "timeupdate" ) {

                                    if ( !this.media.paused ) {
                                        return;
                                    } 
                                    videos.b.emit( "timeupdate" );

                                    // update scrubber
                                    scrub.val( this.currentTime() );

                                    return;
                                }

                                if ( event === "seeking" ) {
                                    videos.b.currentTime( this.currentTime() );
                                }

                                if ( event === "play" || event === "pause" ) {
                                    videos.b[ event ]();
                                }
                            });
                        });
                    }
                });
            });


            scrub.bind("change", function() {
                var val = this.value;
                videos.a.currentTime( val );
                videos.b.currentTime( val );
            });

            // With requestAnimationFrame, we can ensure that as 
            // frequently as the browser would allow, 
            // the video is resync'ed.
            function sync() {
                if (videos.b.media.readyState === 4 ) {
                    videos.b.currentTime( 
                        videos.a.currentTime()        
                    );        
                }
                requestAnimFrame( sync );
            }

            sync(); */    
            </c:if>

        </script>

    </body>
</html>
