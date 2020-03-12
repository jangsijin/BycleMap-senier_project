<?php


	$id = $_POST['id'];
	$password = $_POST['password'];
	$nickname =$_POST['nickname'];
	$email = $_POST['email'];



	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{



	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 


	mysql_query("INSERT INTO login (id,password,nickname,email)
	VALUES ('$id', '$password','$nickname','$email')");
	}


	mysql_close($con);

?>
