<?php		
include_once("../../../../wp-config.php");
$home = home_url();
$options = get_option('VWvideoRecorderOptions');

$video_from = $options['directory'];

$recording = $_POST['recs'];;

if ( $recording ) 
{


//var_dump($recording);

include_once("../../../../wp-config.php");
$home = home_url();
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
	

	$streamname = $recording;
	
	 $vid = $streamname;
  

    // conversie  
	if (!file_exists($output_file = $videosPath . $streamname  . "-ip.mp4"))
	{

		if (file_exists($file = $videosPath . $vid  . ".flv"))
		{
		    //echo "<BR>";
			$output_file= $videosPath  . $vid  . "-ip.mp4";
			//echo "<BR>";
			$log_file = $videosPath  . $vid  . ".log";

			//mp4
			$cmd ="/usr/local/bin/ffmpeg -y -i $file -b 512k -vcodec libx264 -flags +loop+mv4 -cmp 256 -partitions +parti4x4+parti8x8+partp4x4+partp8x8 -subq 6 -trellis 0 -refs 5 -bf 0 -flags2 +mixed_refs -coder 0 -me_range 16 -g 250 -keyint_min 25 -sc_threshold 40 -i_qfactor 0.71 -qmin 10 -qmax 51 -qdiff 4 -acodec libfaac -ar 44100 -ab 96k -vpre baseline  $output_file >&$log_file &";
			exec($cmd, $output, $returnvalue);

			//ogv   
			//echo "<BR>";			
			 $output_filew= $videosPath . $vid  . ".ogv";
			//echo "<BR>";
			$log_filew = $videosPath . $vid  . "-ogv.log";
			$cmd = "/usr/local/bin/ffmpeg2theora $file -o $output_filew -V 512 -A 96 &>$log_filew &";
				
			exec($cmd, $output, $returnvalue);
		}
	}
	if($embedmode == 0)
	{
	
	$home = home_url();
	//echo $player;
	
	switch($player)
	{
		case 'vwplayer':
		$playercode = <<<EOD
<u>$streamname</u><div  style='width:${embedWidth}px; height:${embedHeight}px'><object height=\"100%\" width=\"100%\"><param name=\"movie\" value=\" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=\"><param name=\"scale\" value=\"noscale\"><param name=\"salign\" value=\"lt\"><param name=\"base\" value=\"$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"allowscriptaccess\" value=\"always\"><embed base=\"$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/\"  scale=\"noscale\" salign=\"lt\" src=\" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" height=\"${embedHeight}px\" width=\"${embedWidth}px\"></object></div>$poweredby
EOD;
	
		break;
		case 'jwplayer':
		$image = file_exists("videowhisper/snapshots/$streamname.jpg")?$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/$streamname.jpg":$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/no_video.png";
		$playercode = <<<EOD
<u>$streamname</u><div id='jwplayer1' style='width: ${embedWidth}px; height: ${embedHeight}px'><scr"+"ipt type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js'></scr"+"ipt><scr"+"ipt type='text/javascript'>var flashvars = { file: '$streamname', streamer: '$rtmp_server', autostart: '$autoplay',width: '${embedWidth}px', height: '${embedHeight}px', type: 'rtmp', image: '$image' }; swfobject.embedSWF('$home/wp-content/uploads/jw-player-plugin-for-wordpress/player/player.swf','jwplayer1','$embedWidth px','$embedHeight px','9','false', flashvars,  {allowfullscreen:'true',allowscriptaccess:'always'},   {id:'jwplayer',name:'jwplayer'}  );</scr"+"ipt></div>$poweredby
EOD;
		break;
		
		case 'ffmpeg':
		
			$play_url = $streams_url . $streamname  . "-ip.mp4";
			$play_urlw = $streams_url . $streamname  . ".ogv";
			$playercode = <<<EOD
<video width='$embedWidth px' height='$embedHeight px'  autobuffer autoplay controls='controls'><source src='$play_url' type='video/mp4'><source src='$play_urlw' type='video/ogg'>You must have an HTML5 capable browser.</video>$poweredby
EOD;
		break;
		
	}
} else
{
	$playercode = "[videowhisper stream=\"$streamname\"]";
	$playercode = addslashes($playercode);
}

?>
<script type="text/javascript" src="../../../../wp-includes/js/tinymce/tiny_mce_popup.js"></script>
<script> 
var ImportDialog = {
	init : function(ed) {
	    tinyMCEPopup.execCommand('mceInsertContent', false, "<?php echo $playercode;?>");
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(ImportDialog.init, ImportDialog);
  </script>
  
 <?
 

}

function format_bytes($a_bytes)
{
    if ($a_bytes < 1024) {
        return $a_bytes .' B';
    } elseif ($a_bytes < 1048576) {
        return round($a_bytes / 1024, 2) .' KB';
    } elseif ($a_bytes < 1073741824) {
        return round($a_bytes / 1048576, 2) . ' MB';
    } 
}
		global $wpdb;
			$table_name = $wpdb->prefix."vw_videorecordings";
			$items =  $wpdb->get_results("SELECT * FROM `$table_name` ORDER BY `id` DESC");
			echo "<table>";
			?>
			<h2>Video Recordings list</h2> <BR>
			<form id = "myForm" name = "form" action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post">
			<?
			
			if ($items)	foreach ($items as $item) 
			{
			echo "<tr>";
				echo "<td valign='middle'>";
				?>
				<input type="radio" id="recs" name="recs" value="<?php echo $item->streamname; ?>"> </td>
				<?php
				echo "<td><a href= ".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplay.php?vid='.$item->streamname." target='_blank'>";
				
				if( file_exists($file = 'videowhisper/snapshots/'.$item->streamname.'.jpg') )
				{
					echo "<img src=".$file.">";
				}
				else
				{
					echo "<img src=".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/no_video.png'.">";
					
				}
				echo "</a>";
				echo "</td>";
				echo "<td>";
				echo "<B>".$item->streamname."</B>";
				echo "<BR><BR> ";
				echo "<a href= ".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplay.php?vid='.$item->streamname.'&postid='.$item->postId." target='_blank'><B>Preview</B></a>";
				
				$size = filesize($video_from.$item->streamname.".flv");
				echo " <BR> ";
				echo date("D M j G:i:s T Y",$item->time);
				echo " <BR> ";
				echo format_bytes($size);
				echo " <BR> ";
				echo "User id: ".$item->userId;
			
				echo "</td>";
			echo "</tr>";
			}
			echo "</table>";
			?>
			<INPUT   type="submit" value="Import" id="import" name="import">
			  </form>
			<?php
			
			?>