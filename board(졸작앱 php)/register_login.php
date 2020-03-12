<?php
if(count($_POST)){
    $id ='';
    if(isset($_POST['id'])){
        $id = $_POST['id'];
    if ($id==null || !$id)
         echo 'id is null';

     //echo $id;
   }

 }

 if(count($_POST)){
    $token ='';
    if(isset($_POST['Token'])){
        $token = $_POST['Token'];
    if ($token==null || !$token)
         echo 'Token is null';

     //echo $token;
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


	$query = "UPDATE login SET Token = '$token' where id = '$id'";
	$result = mysql_query($query,$con);
	

	mysql_close($con);
?>

