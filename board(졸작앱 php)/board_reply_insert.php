<?php

	$filename = "log.c";

	$id = $_POST["id"];
	$time = $_POST["time"];
	$good = $_POST["good"];
	$text = $_POST["text"];
	$board_num = $_POST["board_num"];
	$title = $_POST["title"];
	$boardToken = $_POST["boardToken"];
	

	file_put_contents($filename, trim($title).PHP_EOL,FILE_APPEND);  
	file_put_contents($filename, trim($date).PHP_EOL,FILE_APPEND);  


	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{

	echo "¼º°ø!".mysql_error();;

	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 

	mysql_query("INSERT INTO reply (id,time,good,text,board_num,title,boardToken)
	VALUES ('$id', '$time','$good','$text','$board_num','$title','$boardToken')");

	}

	mysql_query("ALTER TABLE board AUTO_INCREMENT=1;");
	mysql_query("SET @COUNT = 0; ");
	mysql_query("UPDATE reply SET reply.num = @COUNT:=@COUNT+1;");

	mysql_close($con);

?>
