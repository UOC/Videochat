<?php
include("header.php");
include("inc.php");


include_once("../../../../../wp-config.php");
$options = get_option('VWvideoComRecorderOptions');
$delete_from = $options['directory'];
$rtmp_server = $options['rtmp_server'];
echo $rtmp_server;

$vid = $_GET['vid'];
$postid = $_GET['postid'];
$commentid = $_GET['commentid'];
$vwplayer = $_GET['vwplayer'];
$jwplayer = $_GET['jwplayer'];
?>
<div align="center" style="width:320px;height:240px" class="info">
<?
if ($jwplayer&&!$vwplayer)
{
$path_only = implode("/", (explode('/', $_SERVER["SCRIPT_URI"], -1)));	
?>
<script type="text/javascript"
src="http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js">
</script>
<p id="container1">Please install the Flash Plugin</p>
<script type="text/javascript">
var flashvars = { file: "<?php echo urlencode($vid); ?>", streamer: "<?php echo $rtmp_server; ?>", autostart: "true", type: "rtmp", image: "<?php echo  dirname($_SERVER['PHP_SELF']) . "/" . (file_exists("snapshots/$vid.jpg")?"snapshots/$vid.jpg":"snapshots/no_video.png"); ?>" };
  
  swfobject.embedSWF('jwplayer/player.swf','container1','320','240','9','false', flashvars,   {allowfullscreen:'true',allowscriptaccess:'always'},   {id:'jwplayer',name:'jwplayer'}  );
</script>
<p><a href="streamplay.php?vwplayer=1&vid=<?php echo urlencode($vid); ?>">Play using VideoWhisper Stream Player</a></p>		

<?php
}
else
{
$swfurl="streamplayer.swf?streamName=".urlencode($vid)."&serverRTMP=".urlencode($rtmp_server)."&templateURL=";

?>
<object width="100%" height="100%">
<param name="movie" value="<?=$swfurl?>"></param><param name="scale" value="noscale" /><param name="salign" value="lt"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed width="100%" height="100%" scale="noscale" salign="lt" src="<?=$swfurl?>" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true"></embed>
</object>
<?php
}
	if ($delete_from) 
	{
	?>
<p><a href="recorded_videos.php?delete=<?php echo urlencode($vid); ?>&postid=<?php echo $postid;?>&commentid=<?php echo $commentid;?>" target='_blank'>Delete this Recording (<?php echo $vid; ?>)</a></p>		
	<?php
	}
 ?>

</div>