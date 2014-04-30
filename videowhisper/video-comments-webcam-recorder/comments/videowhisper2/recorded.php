<?php

include_once("../../../../../wp-config.php");

if (!$recording=$_POST['recording']) exit;
if (!$stream=$_POST['stream']) exit;


$options = get_option('VWvideoComRecorderOptions');

$video_from = $options['directory'];

  include_once("incsan.php");
  sanV($stream);
  sanV($recording);
  
  // save file
  $fp=fopen("recordings/".$_POST['recording'].".vwr","w");
  if ($fp)
  {
    fwrite($fp, $_POST['stream'].";;;".time().";;;".$_POST['rectime']);
    fclose($fp);
  }
  if (file_exists("snapshots/".$_POST['stream'].".jpg"))  copy("snapshots/".$_POST['stream'].".jpg","snapshots/".$_POST['recording'].".jpg");
  
   //conversion
  $vid = $stream;
  
  if (file_exists($file = $video_from . $vid  . ".flv"))
	{
		$output_file= $video_from . $vid  . "-ip.mp4";

		$log_file = $video_from . $vid  . ".log";

		//mp4
		$cmd ="/usr/local/bin/ffmpeg -y -i $file -b 512k -vcodec libx264 -flags +loop+mv4 -cmp 256 -partitions +parti4x4+parti8x8+partp4x4+partp8x8 -subq 6 -trellis 0 -refs 5 -bf 0 -flags2 +mixed_refs -coder 0 -me_range 16 -g 250 -keyint_min 25 -sc_threshold 40 -i_qfactor 0.71 -qmin 10 -qmax 51 -qdiff 4 -acodec libfaac -ar 44100 -ab 96k -vpre baseline  $output_file >&$log_file &";
		exec($cmd, $output, $returnvalue);

		//ogv    
		$output_filew= $video_from . $vid  . ".ogv";
		$log_filew = $video_from . $vid  . "-ogv.log";
		$cmd = "/usr/local/bin/ffmpeg2theora $file -o $output_filew -V 512 -A 96 &>$log_filew &";
			
		exec($cmd, $output, $returnvalue);
	}
?>loadstatus=1