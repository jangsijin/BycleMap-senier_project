<?php

if(count($_POST)){
    $num ='';
    if(isset($_POST['num'])){
        $num = $_POST['num'];
    if ($num==null || !$num)
         echo 'num is null';
   }

 }

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
	}
//연결완료

	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 


	$query = "SELECT num,goodcheck FROM board where num = '$num'";
	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);

    $row = mysql_fetch_array($result);
	$ex_id = $row[goodcheck];
	
	$id_explode = array();
	$id_explode = explode('|',$ex_id);


	
	for($i=0;$i< count($id_explode) ;$i++){

		if($id_explode[$i] == $id){

			$a = 0;
			break;

		}else{

			$a = 1;

		}
	
	}

	echo $a;


	mysql_close($con);
?>


