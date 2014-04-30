<?

include_once("../../../../../wp-config.php");
$options = get_option('VWvideoRecorderOptions');

$rtmp_server = urlencode($options['rtmp_server']);
$videowhisper = $options['videowhisper'];
$player = $options['selectPlayer'];
$embedmode = $options['embedMode'];
$embedWidth = $options['embedWidth'];
$embedHeight = $options['embedHeight'];
$autoplay = $options['autoplay'];
$streams_url = $options['videos_url'];
$videosPath = $options['directory'];

	$state = 'block' ;
	if (!$videowhisper) $state = 'none';
	
	$poweredby = '<div style=\"display: ' . $state . ';\"><i><small>Powered by <a href=\"http://www.videowhisper.com\"  target=\'_blank\'>VideoWhisper</a>,<a href=\"http://www.videowhisper.com/?p=Video+Recorder\"  target=\'_blank\'> Video Recorder</a>.</small></i></div>';
	
	$postId = 0;

	$streamname = $_GET['stream'];

	global $current_user;
	$current_user = wp_get_current_user();
	$userId = $current_user->ID;
	
	global $wpdb;
	$table_name = $wpdb->prefix."vw_videorecordings";
	$wpdb->insert( $table_name, array( 'time' => time(), 'streamname' => $streamname,'userId' => $userId, 'postId' => $postId) );

	setcookie("recIdCookie", $wpdb->insert_id, time()+3600*24,'/');
	if($embedmode == 0)
	{
	
	$home = home_url();
	
	switch($player)
	{
		case 'vwplayer':
		$playercode = <<<EOD
<u>$streamname</u><div  style='width:${embedWidth}px; height:${embedHeight}px'><object height=\"100%\" width=\"100%\"><param name=\"movie\" value=\" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=\"><param name=\"scale\" value=\"noscale\"><param name=\"salign\" value=\"lt\"><param name=\"base\" value=\"$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"allowscriptaccess\" value=\"always\"><embed base=\"$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/\"  scale=\"noscale\" salign=\"lt\" src=\" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" height=\"${embedHeight}px\" width=\"${embedWidth}px\"></object></div>$poweredby
EOD;
	
		break;
		case 'jwplayer':
		$image = file_exists("snapshots/$streamname.jpg")?$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/$streamname.jpg":$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/no_video.png";
		$playercode = <<<EOD
<u>$streamname</u><div id='jwplayer1' style='width: ${embedWidth}px; height: ${embedHeight}px'><scr"+"ipt type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js'></scr"+"ipt><scr"+"ipt type='text/javascript'>var flashvars = { file: '$streamname', streamer: '$rtmp_server', autostart: '$autoplay',width: '${embedWidth}px', height: '${embedHeight}px', type: 'rtmp', image: '$image' }; swfobject.embedSWF('$home/wp-content/uploads/jw-player-plugin-for-wordpress/player/player.swf','jwplayer1','$embedWidth px','$embedHeight px','9','false', flashvars,  {allowfullscreen:'true',allowscriptaccess:'always'},   {id:'jwplayer',name:'jwplayer'}  );</scr"+"ipt></div>$poweredby
EOD;
		break;
		
		case 'ffmpeg':
		//echo "finished".$videosPath . $streamname  . "-ip.mp4";
		if (file_exists($output_file = $videosPath . $streamname  . "-ip.mp4"))
		{
			$play_url = $streams_url . $streamname  . "-ip.mp4";
			$play_urlw = $streams_url . $streamname  . ".ogv";
			$playercode = <<<EOD
<video width='$embedWidth px' height='$embedHeight px'  autobuffer autoplay controls='controls'><source src='$play_url' type='video/mp4'><source src='$play_urlw' type='video/ogg'>You must have an HTML5 capable browser.</div></video>$poweredby
EOD;
		}
		break;
		
	}
} else
{
	$playercode = "[videowhisper stream=\"$streamname\"]";
	$playercode = addslashes($playercode);
}
?>
<script type="text/javascript" src="../../../../../wp-includes/js/tinymce/tiny_mce_popup.js"></script>
<script> 
var RecordDialog = {
	init : function(ed) {
	    tinyMCEPopup.execCommand('mceInsertContent', false, "<?php echo $playercode;?>");
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(RecordDialog.init, RecordDialog);
  </script>
  
