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
                    <div><strong>28/02/2014</strong></div>
                    <div><strong>11:29:02 - 11:29:43</strong></div>
                </div>
                <div class="col-md-10" style="border-left:1px dashed #ccc;">
                    <div><label style="width:20%; font-weight:normal">TOPIC:</label><strong>test</strong></div>
                    <div><label style="width:20%; font-weight:normal">DESCRIPTION:</label><strong>test</strong></div>
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
                                    <div id="nom">Nom</div>
                                    
                                    <div id="vid1">
                                        <img src="css/images/participant.png" alt="participant 1">
                                    </div>

                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning active">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-2">
                                <div class="participant_content">
                                    <div id="nom">Nom</div>
                                    <div id="vid2">
                                        <img src="css/images/participant.png" alt="participant 2">
                                    </div>                                    
                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-3">
                                <div class="participant_content">
                                    <div id="nom">Nom</div>
                                    <div id="vid3">
                                        <img src="css/images/participant.png" alt="participant 3">
                                    </div>      
                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 participant" id="user-4">
                                <div class="participant_content">
                                    <div id="nom">Nom</div>
                                    <div id="vid4">
                                        <img src="css/images/participant.png" alt="participant 4">
                                    </div>      
                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-5">
                                <div class="participant_content">
                                    <div id="nom">Nom</div>
                                    <div id="vid5">
                                        <img src="css/images/participant.png" alt="participant 5">
                                    </div>      
                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 participant" id="user-6">
                                <div class="participant_content">
                                    <div id="nom">Nom</div>
                                    <div id="vid6">
                                        <img src="css/images/participant.png" alt="participant 6">
                                    </div>      
                                    <div class="button_actions" style="text-align:center">
                                        <button type="button" class="btn btn-warning">SOLO</button>
                                        <button type="button" class="btn btn-warning">MUTE</button>
                                    </div>
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
                            <p>15:16:42 -  Admin SpeakApps - -- Locked Room --</p>
                            <p>15:16:45 -  Admin SpeakApps - -- unLocked Room --</p>
                            <p>16:15:09 -  Admin SpeakApps - dadad</p>
                        </div> 
                        <p>Enter your text here:</p>
                        <textarea class="form-control" rows="2"></textarea> 
                    </div>
                </div>
            </div>



            <div id="debug_display">
                <h4>Debug Data</h4>
                <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) :
                <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>) : <span class="time"></span> (<span class="duration"></span>)
            </div>
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

            var streams = {
                "1": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording2.mp4",
                "2": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording1.mp4",
                "3": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording3.mp4",
                "4": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording4.mp4",
                "5": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording5.mp4",
                "6": "rtmp://184.73.205.58:1935/vod/mp4:webcamrecording6.mp4",
            }
            var participants = new Array();
            var playing = false;

            $(document).on("ready", function(){
                var vids = $("div[id^=vid]");
                vids.each(function(v){

                    participants[v] = jwplayer(vids[v]).setup({
                                        file: streams[v+1],
                                        image: "",
                                        width: 160,
                                        height: 110,
                                        controls: 'false',
                                        icons: 'false',
                                        flashplayer: "./js/jwplayer/jwplayer.flash.swf",
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
                                                if ($($("#debug_display .duration")[v]).html() == ""){$($("#debug_display .duration")[v]).html(t.duration)}
                                                $($("#debug_display .time")[v]).html(t.position);
                                            }
                                        }
                                    });                   
                });

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

            });
        </script>

    </body>
</html>
