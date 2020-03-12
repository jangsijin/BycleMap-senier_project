<?php
session_start();

if(count($_POST)){
    $id ='';
    if(isset($_POST['id'])){
        $id = $_POST['id'];
    if ($id==null || !$id)
         echo 'id is null';
	}
 }


//연결
	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{

	}
//연결완료


	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 

	$query = "SELECT Token FROM `login` where nickname = '$id'";

	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);

	for ($i=0; $i < $total_record; $i++)                    
	{

		mysql_data_seek($result, $i);      
		  
		$row = mysql_fetch_array($result);

		echo $row[Token];

	}

	mysql_close($con);
?>

