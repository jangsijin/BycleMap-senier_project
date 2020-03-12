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


	$queryg = "SELECT good FROM board where num = '$num'";
	$result = mysql_query($queryg,$con);
	$row = mysql_fetch_array($result);
	echo $row[good];

	$good = $row[good];


	$query = "UPDATE board b
	SET goodcheck = concat(
	(SELECT goodcheck FROM (SELECT * FROM board) as t where b.num = t.num),
	'|$id')
	where num = '$num'";
	mysql_query($query,$con);


	$queryc = "UPDATE board
	SET good = ('$good' + 1)
	where num = '$num'";
	mysql_query($queryc,$con);

	mysql_close($con);
?>


