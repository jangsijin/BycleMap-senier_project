<?php


	$id = $_POST['id'];
	$nickname =$_POST['nickname'];


	$filename = "log.c";
	
	file_put_contents($filename, trim($id).PHP_EOL,FILE_APPEND);  
	file_put_contents($filename, trim($nickname).PHP_EOL,FILE_APPEND);  


	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{



	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 

	$query = "SELECT id FROM `login`";


	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);


	for ($i=0; $i < $total_record; $i++)                    
	{
	  mysql_data_seek($result, $i);      
	  
	  $row = mysql_fetch_array($result);
	  if(($row[id]==$id)){

		echo 1;

	  }

	}


	$query_nick = "SELECT nickname FROM `login`";

	$result_nick = mysql_query($query_nick,$con);
	$total_record = mysql_num_rows($result_nick);


	for ($i=0; $i < $total_record; $i++)                    
	{
	  mysql_data_seek($result_nick, $i);      
	  
	  $row = mysql_fetch_array($result_nick);
	  if(($row[nickname]==$nickname)){

		echo 2;

	  }else{

	  }

	}


	echo 3;


//	mysql_query("INSERT INTO login (id,password,nickname,email)
//	VALUES ('$id', '$password','$nickname','$email')");
	}


	mysql_close($con);

?>
