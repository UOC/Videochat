<?php
/*
Plugin Name: Video Posts Webcam Recorder
Plugin URI: http://www.videowhisper.com/?p=WordPress+Video+Recorder+Posts+Comments
Description: Video Posts Webcam Recorder
Version: 1.55
Author: VideoWhisper.com
Author URI: http://www.videowhisper.com/
Contributors: videowhisper, VideoWhisper.com
*/

function videoposts_addbuttons() {
   // Don't bother doing this stuff if the current user lacks permissions
   if ( ! current_user_can('edit_posts') && ! current_user_can('edit_pages') )
     return;
 
   // Add only in Rich Editor mode
   if ( get_user_option('rich_editing') == 'true') {
     add_filter("mce_external_plugins", "add_videoposts_tinymce_plugin");
     add_filter('mce_buttons', 'register_videoposts_button'); 

   }
}
 
function register_videoposts_button($buttons) {
   array_push($buttons, "separator", "recorder");
   array_push($buttons, "separator", "import");
   return $buttons;
}
 
// Load the TinyMCE plugin : editor_plugin.js (wp2.5)
function add_videoposts_tinymce_plugin($plugin_array) {
   $plugin_array['recorder'] = home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/editor_plugin.js';
   $plugin_array['import'] = home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/editor_plugin_i.js';
   return $plugin_array;
}
 
// init process for button control
add_action('init', 'videoposts_addbuttons');


if (!class_exists("VWvideoPosts")) 
{
    class VWvideoPosts {
        
	function VWvideoPosts() { //constructor

    }
	function settings_link($links) {
	  $settings_link = '<a href="options-general.php?page=videoposts.php&mod=settings">'.__("Settings").'</a>';
	  array_unshift($links, $settings_link);
	  return $links;
	}
	function recordings_link($links) {
	  $recordings_link = '<a href="options-general.php?page=videoposts.php&mod=recordings">'.__("Recordings").'</a>';
	  array_unshift($links, $recordings_link);
	  return $links;
	}
	
	function wpse_hold_global_post_number( $post_id, $post ) {

	global $post_ID;
	if($post_ID) $post_id = $post_ID;
	$recCookie = $_COOKIE["recIdCookie"];
   
	global $wpdb;
	$table_name = $wpdb->prefix."vw_videorecordings";
	$sql="UPDATE $table_name SET postId = '$post_id' WHERE id = '$recCookie' AND postId = '0'";
	$wpdb->query($sql);
	}
	function post_shortcode($content)
	{
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
		
		$poweredby = '<div style=\'display: ' . $state . ';\'><i><small>Powered by <a href=\'http://www.videowhisper.com\'  target=\'_blank\'>VideoWhisper</a>,<a href=\'http://www.videowhisper.com/?p=Video+Recorder\'  target=\'_blank\'> Video Recorder</a>.</small></i></div>';
		
		preg_match_all("/\[videowhisper stream=\"([a-zA-Z0-9_\-\s]*)\"\]/i",$content,$matches);
		//var_dump($matches);
		$result = $content;
		//echo $player;
		
		for( $i=0; $i<count($matches[0]);$i++)
		{
			//echo $player;
			$home = home_url();
			$streamname = $matches[1][$i];
			switch($player)
			{
				case 'vwplayer':
				$playercode = <<<EOD
<div id='vwplayer' style='width:$embedWidth px; height:$embedHeight px'><object height="100%" width="100%"><param name="movie" value=" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=\"><param name="scale" value="noscale"><param name="salign" value="lt"><param name="base" value="$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/"><param name="allowFullScreen" value="true"><param name="allowscriptaccess" value="always"><embed base="$home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/"  scale="noscale" salign="lt" src=" $home/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplayer.swf?streamName=$streamname&amp;serverRTMP=$rtmp_server&amp;templateURL=" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" height="$embedHeight px" width="$embedWidth px"></object></div>$poweredby
EOD;
	
				break;
				case 'jwplayer':
				$image = file_exists("wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/$streamname.jpg")?$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/$streamname.jpg":$home."/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/no_video.png";
				$playercode = <<<EOD
<div id='jwplayer_$streamname' style='width: ${embedWidth}px; height: ${embedHeight}px'><script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js'></script><script type='text/javascript'>var flashvars = { file: '$streamname', streamer: '$rtmp_server', autostart: '$autoplay',width: '${embedWidth}px', height: '${embedHeight}px', type: 'rtmp', image: '$image' }; swfobject.embedSWF('$home/wp-content/uploads/jw-player-plugin-for-wordpress/player/player.swf','jwplayer_$streamname','$embedWidth px','$embedHeight px','9','false', flashvars,  {allowfullscreen:'true',allowscriptaccess:'always'},   {id:'jwplayer',name:'jwplayer'}  );</script></div>$poweredby
EOD;
				break;
				
				case 'ffmpeg':
				if (file_exists($output_file = $videosPath . $streamname  . "-ip.mp4"))
				{
					//echo $videosPath.$streams_url;
				$play_url = $streams_url . $streamname  . "-ip.mp4";
				$play_urlw = $streams_url . $streamname  . ".ogv";
				$playercode = <<<EOD
<video width='$embedWidth px' height='$embedHeight px'  autobuffer autoplay controls='controls'><source src='$play_url' type='video/mp4'><source src='$play_urlw' type='video/ogg'>You must have an HTML5 capable browser.</video>$poweredby
EOD;
				}
				break;
				
			}
			$result = str_replace($matches[0][$i],$playercode,$result);
		}	
		return $result;	
	}
	
function init()
	{
		$plugin = plugin_basename(__FILE__);
		add_filter("plugin_action_links_$plugin",  array('VWvideoPosts','settings_link') );
		add_filter("plugin_action_links_$plugin",  array('VWvideoPosts','recordings_link') );
		add_filter("the_content",array('VWvideoPosts','post_shortcode'));
		
		add_action( 'save_post', array('VWvideoPosts','wpse_hold_global_post_number'), null, 2 );
		// global post ID
	
		wp_register_sidebar_widget('videopostsWidget','VideoWhisper Video Posts', array('VWvideoPosts', 'widget') );
	  
	    //check db
	  	$vw_recorder_version = "1.2";

		global $wpdb;
		$table_name = $wpdb->prefix."vw_videorecordings";
			
		$installed_ver = get_option( "vw_recorder_version" );

		if( $installed_ver != $vw_recorder_version ) 
		{
		$wpdb->flush();
		
		$sql = "DROP TABLE IF EXISTS `$table_name`;
		CREATE TABLE `$table_name` (
		  `id` int(11) NOT NULL auto_increment,
		  `userId` int(12)  NOT NULL,
		  `postId` int(12)  NOT NULL,
		  `streamname` varchar(64) NOT NULL,
		  `time` int(12)  NOT NULL,
		  PRIMARY KEY  (`id`)
		) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Video Whisper: Sessions - 2009@videowhisper.com' AUTO_INCREMENT=1;
		
		";

			require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
			dbDelta($sql);

			if (!$installed_ver) add_option("vw_recorder_version", $vw_recorder_version);
			else update_option( "vw_recorder_version", $vw_recorder_version );
			
		$wpdb->flush();
		}
			

	}
	function menu() {
	  add_options_page('Video Posts Webcam Recorder Options', 'Video Posts', 9, basename(__FILE__), array('VWvideoPosts', 'options'));
	}
	
	function getAdminOptions() {
				
				$adminOptions = array(
				'embedMode' => 1,
				'embedWidth' => 320,
				'embedHeight' => 240,
				'autoplay' => true,
				'rtmp_server' => 'rtmp://localhost/videowhisper',
				'selectPlayer' => 'vwplayer',
				'camWidth' => 320,
				'camHeigth' => 240,
				'embedWidth' => 320,
				'embedHeight' => 240,
				'camFps' => 15,
				'micRate' => 22,
				'camBandwidth' => 49158,
				'camMaxBandwidth' => 131072,
				'showCamSettings' => 1,
				'advancedCamSettings' => 1,
				'disablePreview' => 0,
				'layoutCode' => '',
				'fillWindow' => 0,
				'recordLimit' => 600,
				'directory' => '/home/youraccount/public_html/streams/',
				'videos_url' => 'http://yourserver.com/streams/',
				'videowhisper' => 0
				);
				
				$options = get_option('VWvideoRecorderOptions');
				if (!empty($options)) {
					foreach ($options as $key => $option)
						$adminOptions[$key] = $option;
				}            
				update_option('VWvideoRecorderOptions', $adminOptions);
				return $adminOptions;
	}

	function options() 
	{
		$mod = $_GET['mod'];
		$model = $_GET['model'];
		if ($mod == '') $mod = 'settings';
		if($mod == 'settings')
		{
		$options = VWvideoPosts::getAdminOptions();

		if (isset($_POST['updateSettings'])) 
		{
				if (isset($_POST['rtmp_server'])) $options['rtmp_server'] = $_POST['rtmp_server'];
				if (isset($_POST['embedMode'])) $options['embedMode'] = $_POST['embedMode'];
				if (isset($_POST['embedWidth'])) $options['embedWidth'] = $_POST['embedWidth'];
				if (isset($_POST['embedHeight'])) $options['embedHeight'] = $_POST['embedHeight'];
				if (isset($_POST['autoplay'])) $options['autoplay'] = $_POST['autoplay'];
				if (isset($_POST['selectPlayer'])) $options['selectPlayer'] = $_POST['selectPlayer'];
				if (isset($_POST['camWidth'])) $options['camWidth'] = $_POST['camWidth'];
				if (isset($_POST['camHeigth'])) $options['camHeigth'] = $_POST['camHeigth'];
				if (isset($_POST['camFps'])) $options['camFps'] = $_POST['camFps'];
				if (isset($_POST['micRate'])) $options['micRate'] = $_POST['micRate'];
				if (isset($_POST['camBandwidth'])) $options['camBandwidth'] = $_POST['camBandwidth'];
				if (isset($_POST['camMaxBandwidth'])) $options['camMaxBandwidth'] = $_POST['camMaxBandwidth'];
				if (isset($_POST['showCamSettings'])) $options['showCamSettings'] = $_POST['showCamSettings'];
				if (isset($_POST['advancedCamSettings'])) $options['advancedCamSettings'] = $_POST['advancedCamSettings'];
				if (isset($_POST['disablePreview'])) $options['disablePreview'] = $_POST['disablePreview'];
				if (isset($_POST['layoutCode'])) $options['layoutCode'] = $_POST['layoutCode'];
				if (isset($_POST['fillWindow'])) $options['fillWindow'] = $_POST['fillWindow'];
				if (isset($_POST['recordLimit'])) $options['recordLimit'] = $_POST['recordLimit'];
				if (isset($_POST['directory'])) $options['directory'] = $_POST['directory'];
				if (isset($_POST['videos_url'])) $options['videos_url'] = $_POST['videos_url'];
				if (isset($_POST['videowhisper'])) $options['videowhisper'] = $_POST['videowhisper'];
				update_option('VWvideoRecorderOptions', $options);
		}
	  ?>
<div class="wrap">
<div id="icon-options-general" class="icon32"><br></div>
<h2>Video Posts Webcam Recorder Settings</h2>
</div>

<p><H3>&gt; Settings | 
<a href = "<?php echo home_url();?>/wp-admin/options-general.php?page=videoposts.php&mod=recordings">Recordings List </a></H3> </p>

<form method="post" action="<?php echo $_SERVER["REQUEST_URI"]; ?>">

<h3>General Settings</h3>
<?php

$detectedp[jwplayer] = 0;

$cmd ="/usr/local/bin/ffmpeg -codecs";
exec($cmd, $output, $returnvalue); 
if ($returnvalue == 127) $ffmpegdetected = 0;
	else $ffmpegdetected = 1;


if (is_plugin_active('jw-player-plugin-for-wordpress/jwplayermodule.php')) $detectedp[jwplayer] = 1;


?>
	<h5>Select Player</h5>
	<select name='selectPlayer' id='selectPlayer'> 
	<option value='vwplayer' <?=$options['selectPlayer'] == 'vwplayer'?"":"selected"?>>Video Whisper</option>
	<?php
 	if ($detectedp[jwplayer]) {
	?>
	<option value='jwplayer' <?=$options['selectPlayer'] == 'jwplayer'?"selected":""?>>Jw Player</option> 
	<?php
	}
	if ($ffmpegdetected == 1)
 {
	?>
	<option value='ffmpeg' <?=$options['selectPlayer'] == 'ffmpeg'?"selected":""?>>HTML5</option> 
	<?php
	}

  ?>
	</select>
	<p> Jw Player: 
		<?php
 	if ($detectedp[jwplayer]) {
	    echo "Detected";
	}
	else{
	    echo"<a href='http://wordpress.org/extend/plugins/jw-player-plugin-for-wordpress/' target='_blank'> Not Detected </a>";
	}
	?></p>
	
	<?php

	
	echo "<p><h5> Conversion tools for HTML5 playback: </h5></p>";


	echo "<table><tr><td> ffmpeg: </td>";
	$cmd ="/usr/local/bin/ffmpeg -codecs";
	exec($cmd, $output, $returnvalue); 
	if ($returnvalue == 127)  echo "<td><font color='red'> &nbsp &nbsp &nbsp &nbsp  Not detected: $cmd</font></td></tr>"; else echo "<td><font color='green'> &nbsp &nbsp &nbsp &nbsp  Detected</font></td></tr>";

	//detect codecs
	if ($output) if (count($output)) 
	foreach (array('h264','faac','speex', 'nellymoser') as $cod) 
	{
	$det=0; $outd="";
	echo "<tr><td> $cod codec: </td>";
	foreach ($output as $outp) if (strstr($outp,$cod)) { $det=1; $outd=$outp; };
	if ($det) echo "<td><font color='green'> &nbsp &nbsp &nbsp &nbsp  Detected ($outd)</font></td></tr>"; else echo "<td><font color='red'> &nbsp &nbsp &nbsp &nbsp  Missing: please configure and install ffmpeg with lib$cod</font></td></tr>";
	}

	echo "<tr><td> ffmpeg2theora: </td> ";
	$cmd ="/usr/local/bin/ffmpeg2theora";
	echo exec($cmd, $output, $returnvalue); 
	if ($returnvalue == 127)  echo "<td><font color='red'> &nbsp &nbsp &nbsp &nbsp  Not detected: $cmd</font></td></tr>"; else echo "<td><font color='green'> &nbsp &nbsp  &nbsp &nbsp Detected</font></td></tr>";
	echo "</table>";
	
	?>

<h5>Embed Mode</h5>
<select name="embedMode" id="embedMode">
  <option value="0" <?=$options['embedMode']?"":"selected"?>>Direct mode</option>
  <option value="1" <?=$options['embedMode']?"selected":""?>>Shortcode mode</option>
</select>

<h5>Embed Width</h5>
<input name="embedWidth" type="text" id="embedWidth" size="5" maxlength="5" value="<?=$options['embedWidth']?>"/>

<h5>Embed Height</h5>
<input name="embedHeight" type="text" id="embedHeight" size="5" maxlength="5" value="<?=$options['embedHeight']?>"/>

<h5>Autoplay</h5>
<select name="autoplay" id="autoplay">
  <option value="false" <?=$options['autoplay']?"":"selected"?>>Off</option>
  <option value="true" <?=$options['autoplay']?"selected":""?>>On</option>
</select>

<h5>RTMP Address</h5>
<p>To run this, make sure your hosting environment meets all <a href="http://www.videowhisper.com/?p=Requirements" target="_blank">requirements</a>.  If you don't have a videowhisper rtmp address yet (from a managed rtmp host), go to <a href="http://www.videowhisper.com/?p=RTMP+Applications" target="_blank">RTMP Application   Setup</a> for  installation details.</p>
<input name="rtmp_server" type="text" id="rtmp_server" size="80" maxlength="256" value="<?=$options['rtmp_server']?>"/>

<h5>Cam Width</h5>
<input name="camWidth" type="text" id="camWidth" size="5" maxlength="5" value="<?=$options['camWidth']?>"/>

<h5>Cam Heigth</h5>
<input name="camHeigth" type="text" id="camHeigth" size="5" maxlength="5" value="<?=$options['camHeigth']?>"/>

<h5>Cam Fps</h5>
<input name="camFps" type="text" id="camFps" size="5" maxlength="5" value="<?=$options['camFps']?>"/>

<h5>Mic Rate</h5>
<input name="micRate" type="text" id="micRate" size="5" maxlength="5" value="<?=$options['micRate']?>"/>

<h5>Cam Bandwidth</h5>
<input name="camBandwidth" type="text" id="camBandwidth" size="8" maxlength="8" value="<?=$options['camBandwidth']?>"/>

<h5>Cam Max Bandwidth</h5>
<input name="camMaxBandwidth" type="text" id="camMaxBandwidth" size="8" maxlength="8" value="<?=$options['camMaxBandwidth']?>"/>

<h5>Show Cam Settings</h5>
<select name="showCamSettings" id="showCamSettings">
  <option value="0" <?=$options['showCamSettings']?"":"selected"?>>No</option>
  <option value="1" <?=$options['showCamSettings']?"selected":""?>>Yes</option>
</select>

<h5>Advanced Cam Settings</h5>
<select name="advancedCamSettings" id="advancedCamSettings">
  <option value="0" <?=$options['advancedCamSettings']?"":"selected"?>>No</option>
  <option value="1" <?=$options['advancedCamSettings']?"selected":""?>>Yes</option>
</select>

<h5>Disable Preview</h5>
<select name="disablePreview" id="disablePreview">
  <option value="0" <?=$options['disablePreview']?"":"selected"?>>No</option>
  <option value="1" <?=$options['disablePreview']?"selected":""?>>Yes</option>
</select>

<h5>Layout Code</h5>
<textarea name="layoutCode" type="textarea" cols="50" rows="3" id="layoutCode">
<?echo $options['layoutCode'];?>
</textarea>

<h5>Fill Window</h5>
<select name="fillWindow" id="fillWindow">
  <option value="0" <?=$options['fillWindow']?"":"selected"?>>No</option>
  <option value="1" <?=$options['fillWindow']?"selected":""?>>Yes</option>
</select>

<h5>Record Limit</h5>
<input name="recordLimit" type="text" id="recordLimit" size="5" maxlength="5" value="<?=$options['recordLimit']?>"/>
<h5>Videos directory</h5>
<input name="directory" type="text" id="directory" size="80" maxlength="256" value="<?=$options['directory']?>"/>
<BR>

Example: /home/youraccount/public_html/streams/ 
<BR>
(Ending in / .)

<h5>Videos url</h5>
<input name="videos_url" type="text" id="videos_url" size="80" maxlength="256" value="<?=$options['videos_url']?>"/>
<BR>
Example: http://yourserver.com/streams/ 
<BR>
(Ending in / .)

<h5>Show VideoWhisper Powered by</h5>
<select name="videowhisper" id="videowhisper">
  <option value="0" <?=$options['videowhisper']?"":"selected"?>>No</option>
  <option value="1" <?=$options['videowhisper']?"selected":""?>>Yes</option>
</select>

<div class="submit">
  <input type="submit" name="updateSettings" id="updateSettings" value="<?php _e('Update Settings', 'VWvideoPosts') ?>" />
</div>

</form>
	 <?
	}
		if($mod == 'recordings')
		{ 
			if($model == "delete")
			{
				// sterg alea si afisez un msg
				$recs = $_POST['recs'];
				
				//$options = get_option('VWvideoRecorderOptions');
				$delete_from = $options['directory'];

				$loggedin=0;
				global $current_user;
				get_currentuserinfo();
				if ($current_user->user_nicename) $username=urlencode($current_user->user_nicename);
						
					if ($username) $loggedin=1;
					else 
					{
						echo "<BR>";
						echo "<p aling='center'><H3>Only admin can access this page!</H3></p>";
					}
				if($loggedin == 1)
				{
					global $wpdb;
					$table_name = $wpdb->prefix."vw_videorecordings";
					
					if($recs){
						foreach ($recs as $rec)
						{
							$wpdb->query($sql = "DELETE FROM $table_name WHERE streamname = '$rec' ");
							//echo $sql;
						
							if (file_exists($file = $delete_from . $rec  . ".flv"))	unlink($file);
						
							if (file_exists($file = $delete_from . $rec  . ".key")) unlink($file);
								
							if (file_exists($file = $delete_from . $rec  . ".meta")) unlink($file);

							if (file_exists($file = "recordings/" . $rec  . ".vwr")) unlink($file);
							
							if (file_exists($file = $delete_from . $rec  . "-ip.mp4")) unlink($file);
							if (file_exists($file = $delete_from . $rec  . ".log")) unlink($file);
							if (file_exists($file = $delete_from . $rec  . ".ogv")) unlink($file);
							if (file_exists($file = $delete_from . $rec  . "-ogv.log")) unlink($file);
							
							if (file_exists($file = "snapshots/" . $rec  . ".jpg")) unlink($file);
							
						}
						echo "<BR><BR>";
						echo "The files were successfully deleted!";
						
						echo "This deletes only the video recordings. You will have to  manually edit the post to remove embed code and references. ";
						echo "<BR><BR>";
					}
				}
				//var_dump($recs);
			}
			?>
			
			<div class="wrap">
			<div id="icon-options-general" class="icon32"><br></div>
			<h2>Video Posts Recordings list</h2>
			<p><H3><a href = "<?php echo home_url();?>/wp-admin/options-general.php?page=videoposts.php&mod=settings">Settings</a> | 
 &gt; Recordings List</H3> </p>
			</div>

			<?php
			
			$streamname = $_GET['stream'];

			global $wpdb;
			$table_name = $wpdb->prefix."vw_videorecordings";
			$items =  $wpdb->get_results("SELECT * FROM `$table_name` ORDER BY `id` DESC");
			
			?>
			
					
			<script language="javascript">
			function fncDelete()
			{
				if(confirm('Are you sure you want to delete videos?')==true)
				{
					//window.location = 'page1.cgi';
					// $.cookie('deleted', deleted );
					//form.inputdelete.value = 1;
					form.submit();
				}
			}
			</script>
		
			
			<script language="JavaScript">
			function checkUncheck(form, setTo) {
				var c = document.getElementById(form).getElementsByTagName('input');
				for (var i = 0; i < c.length; i++) {
					if (c[i].type == 'checkbox') {
						c[i].checked = setTo;
					}
				}
			}

			</script>
			
			<form id = "myForm" name = "form" action="<?php echo home_url(); ?>/wp-admin/options-general.php?page=videoposts.php&mod=recordings&model=delete" method="post">
			<input type='button' onclick="checkUncheck('myForm', true);" value='Check All'>&nbsp;&nbsp;
			<input type='button' onclick="checkUncheck('myForm', false);" value='Uncheck All'><br><br><?php
			
			echo "<table>";
			if ($items)	foreach ($items as $item) 
			{
			echo "<tr>";
				echo "<td valign='center'>";
				?>
				<input type="checkbox" id="recs[]" name="recs[]" value="<?php echo $item->streamname; ?>">
				<?php
				echo "</td><td>";
				echo "<a href= ".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplay.php?vid='.$item->streamname." target='_blank'>";

				if(file_exists('../'.$file = 'wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/'.$item->streamname.'.jpg')) 
				{
					echo "<img src=".home_url().'/'.$file.">";
				}
				else
				{
					echo "<img src=".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/snapshots/no_video.png'.">";
				}
				echo "</a>";
				echo "</td>";
				echo "<td>";
				echo "<a href= ".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/streamplay.php?vid='.$item->streamname.'&postid='.$item->postId." target='_blank'><B>".$item->streamname."</B></a>";
				echo " <BR><BR> ";
				echo "<a href=".home_url().'?p='.$item->postId." target='_blank'><B> View Post </B></a>";
				//echo " <BR> ";
				//echo "<a href=".home_url().'/wp-content/plugins/video-posts-webcam-recorder/posts/videowhisper/recorded_videos.php?delete='. urlencode($item->streamname).'&postid='.$item->postId." target='_blank'><B> Delete this Recording </B></a>";
				echo " <BR> ";
				echo date("D M j G:i:s T Y",$item->time);
				echo " <BR> ";
				echo "User id: ".$item->userId;
				echo "</td>";
			echo "</tr>";
			}
			echo "</table>";
			?>
			<INPUT onClick="JavaScript:fncDelete(this.form);"  type="button" value="Delete" id="delete" name="delete">
			  </form>
			<?php
			
		}
	}
}
} 
//instantiate
if (class_exists("VWvideoPosts")) {
        $videoPosts = new VWvideoPosts();
}

//Actions and Filters   
if (isset($videoPosts)) {
	add_action("plugins_loaded", array(&$videoPosts, 'init'));
	add_action('admin_menu', array(&$videoPosts, 'menu'));
	}
?>