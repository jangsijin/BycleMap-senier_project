<?php


	$num = $_POST["num"];
	$boardToken = $_POST["boardToken"];


	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{

	echo "성공!".mysql_error();;

	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 

	mysql_query("DELETE FROM board WHERE num = '$num'");
	mysql_query("DELETE FROM reply WHERE boardToken = '$boardToken'");

	}

	mysql_query("ALTER TABLE board AUTO_INCREMENT=1;");
	mysql_query("SET @COUNT = 0; ");
	mysql_query("UPDATE board SET board.num = @COUNT:=@COUNT+1;");

	echo "여기까지 수행";

	mysql_close($con);

?>
