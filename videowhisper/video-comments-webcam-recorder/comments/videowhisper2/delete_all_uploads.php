<?php

//can setup a password below like 'pass' instead of '' to only allow access like delete_all_uploads.php?password=pass
if ($_GET['password']!='') exit;

function deltree($path) {
	  if (is_dir($path)) {
		  if (version_compare(PHP_VERSION, '5.0.0') < 0) {
			$entries = array();
		  if ($handle = opendir($path)) {
			while (false !== ($file = readdir($handle))) $entries[] = $file;

			closedir($handle);
		  }
		  } else {
			$entries = @scandir($path);
			if ($entries === false) $entries = array(); // just in case scandir fail...
		  }

		foreach ($entries as $entry) {
		  if ($entry != '.' && $entry != '..') {
			deltree($path.'/'.$entry);
		  }
		}

		return @rmdir($path);
	  } else {
		  return @unlink($path);
	  }
	}
	
	function cleanUp($dir, $exceptions = array())
	{
	echo "<BR>Deleting all files from '$dir'...";
	$k=0;
	if (!file_exists($dir)) 
	{
	echo "n/a";
	return;
	}
	$handle=opendir($dir);
		while (($file = readdir($handle))!==false) 
		{
			if (($file != ".") && ($file != ".."))
			{
				if (is_dir("$dir/" . $file)) deltree($dir."/".$file);
				else if (!in_array($exceptions, $file)) @unlink("$dir/" . $file);
				$k++;
				
				if ($k%50==0)
				{
				echo " ."; 
				flush();
				}
			}
		}
	closedir($handle); 
	echo "Cleaned up $k files or folders.";
	}

	cleanUp('recordings');
	cleanUp('snapshots', array ('no_video.png'));
	cleanUp('flv');
	cleanUp('streams');
	cleanUp('red5streams');
	cleanUp('red5archive');
	cleanUp('wowzastreams');
?>