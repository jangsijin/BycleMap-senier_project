<?php

	$title = $_POST["title"];
	$date = $_POST["date"];
	$id = $_POST["id"];
	$good = $_POST["good"];
	$memo = $_POST["memo"];
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

	mysql_query("INSERT INTO board (title,date,id,good,memo,boardToken)
	VALUES ('$title', '$date','$id','$good','$memo','$boardToken')");
	}

	mysql_query("ALTER TABLE board AUTO_INCREMENT=1;");
	mysql_query("SET @COUNT = 0; ");
	mysql_query("UPDATE board SET board.num = @COUNT:=@COUNT+1;");

	mysql_close($con);

?>
