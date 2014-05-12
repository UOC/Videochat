<?php

//include("inc.php");
include_once("../../../../../wp-config.php");

$options = get_option('VWvideoRecorderOptions');

$rtmp_server = $options['rtmp_server'];
$camWidth = $options['camWidth'];
$camHeigth = $options['camHeigth'];
$camFps = $options['camFps'];
$micRate = $options['micRate'];
$camBandwidth = $options['camBandwidth'];
$camMaxBandwidth = $options['camMaxBandwidth'];
$showCamSettings = $options['showCamSettings'];
$advancedCamSettings = $options['advancedCamSettings'];
$disablePreview = $options['disablePreview'];
$layoutCode = $options['layoutCode'];
$fillWindow = $options['fillWindow'];
$recordLimit = $options['recordLimit'];
$videowhisper = $options['videowhisper'];
$directory = $options['directory'];

$loggedin=0;
global $current_user;
get_currentuserinfo();
if ($current_user->user_nicename) $username=urlencode($current_user->user_nicename);
		
$msg="";
		if ($username) $loggedin=1;
		else $msg=urlencode("<a href=\"/\">Please login first or register an account if you don't have one! Click here to return to website.</a>");

//suffix to attach to $username and obtain recording filename
//$recordingId="-".base_convert(time(),10,36); //latest versions automatically add time stamp

$recordingId="";

$layoutCode=<<<layoutEND
id=0&label=Video&x=346&y=10&width=326&height=298; id=1&label=Camcorder&x=10&y=10&width=326&height=298
layoutEND;

?>server=<?=$rtmp_server?>&serverAMF=<?=$rtmp_amf?>&username=<?=$username?>&recordingId=<?=$recordingId?>&msg=<?=$msg?>&loggedin=<?=$loggedin?>&camWidth=<?=$camWidth?>&camHeight=<?=$camHeight?>&camFPS=<?=$camFPS?>&camBandwidth=<?=$camBandwidth?>&showCamSettings=<?=$showCamSettings?>&camMaxBandwidth=<?=$camMaxBandwidth?>&micRate=<?=$micRate?>&advancedCamSettings=<?=$advancedCamSettings?>&recordLimit=<?=$recordLimit?>&bufferLive=900&bufferFull=900&bufferLivePlayback=0.2&bufferFullPlayback=10&layoutCode=<?=urlencode($layoutCode)?>&fillWindow=<?=$fillWindow?>&disablePreview=<?=$disablePreview?>&loadstatus=1