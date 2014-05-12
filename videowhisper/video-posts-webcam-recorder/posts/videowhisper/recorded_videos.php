<?php
include("header.php");
include("inc.php");
include_once("../../../../../wp-config.php");

$options = get_option('VWvideoRecorderOptions');
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
	if (($rec=$_GET['delete'])&&$delete_from)
	{
		$postid = $_GET['postid'];
		global $wpdb;
		$table_name = $wpdb->prefix."vw_videorecordings";
		echo $rec;
		echo "<BR>";

		echo "<BR>";
		$wpdb->query("DELETE FROM $table_name WHERE streamname = '$rec' ");
	
		echo "Deleting $rec ...";
		echo "<BR>";
		echo "<BR>";
		
		if (file_exists($file = $delete_from . $rec  . ".flv"))
		{
			unlink($file);
			echo "The file $file was successfully deleted!";
			echo "<BR><BR>";
			echo "This deletes only the video recording. You will have to  manually edit the post to remove embed code and references. Click on the link below to go to your post!";
			echo "<BR>";
			echo "<p align = 'center'><a href=".home_url().'?p='.$postid." target='_blank'><B> View Post </B></a></p>";
		}
		if (file_exists($file = $delete_from . $rec  . ".key")) 
		{
			unlink($file);
			echo "The file $file was successfully deleted!";
			echo "<BR><BR>";
			echo "This deletes only the video recording. You will have to  manually edit the post to remove embed code and references. Click on the link below to go to your post!";
			echo "<BR>";
			echo "<p align = 'center'><a href=".home_url().'?p='.$postid." target='_blank'><B> View Post </B></a></p>";
		}
		if (file_exists($file = $delete_from . $rec  . ".meta")) 
		{
			unlink($file);
			echo "The file $file was successfully deleted!";
			echo "<BR><BR>";
			echo "This deletes only the video recording. You will have to  manually edit the post to remove embed code and references. Click on the link below to go to your post!";
			echo "<BR>";
			echo "<p align = 'center'><a href=".home_url().'?p='.$postid." target='_blank'><B> View Post </B></a></p>";
		}
		
		if (file_exists($file = "recordings/" . $rec  . ".vwr")) unlink($file);
		
		if (file_exists($file = $delete_from . $rec  . "-ip.mp4")) unlink($file);
		if (file_exists($file = $delete_from . $rec  . ".log")) unlink($file);
		if (file_exists($file = $delete_from . $rec  . ".ogv")) unlink($file);
		if (file_exists($file = $delete_from . $rec  . "-ogv.log")) unlink($file);
		
		if (file_exists($file = "snapshots/" . $rec  . ".jpg")) unlink($file);
		
	}
}?>
	
