<?php
session_start();

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
    $password ='';
    if(isset($_POST['password'])){
        $password = $_POST['password'];
    if ($password==null || !$password)
         echo 'password is null';

     //echo $password;
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


	$query = "SELECT * FROM login where id = '$id'";
	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);

	if($total_record == 0){

			echo "0";
			echo "||";
			echo "0";
			echo "||";
			echo "0";
			echo "||";
			echo "0";

			if(!isset($_SESSION['id']) || !isset($_SESSION['password'])) {

			$_SESSION['id'] = $id;
			$_SESSION['password'] = $password;
			}else{

			$id = $_SESSION['id'];
			$password = $_SESSION['password'];

			}


	}
	
	
	else if($total_record != 0) {


		$query_password = "SELECT * FROM `login` where id = '$id' and password ='$password'";
		$result_password = mysql_query($query_password,$con);
		$total_record_password = mysql_num_rows($result_password);

		if($total_record_password == 0){
		
		echo "1";
		
		}else{



			if(!isset($_SESSION['id']) || !isset($_SESSION['password'])) {
			session_start();
			$_SESSION['id'] = $id;
			$_SESSION['password'] = $password;
			}else{
			session_start();
			$id = $_SESSION['id'];
			$password = $_SESSION['password'];

			}

			echo "2";





		}

		echo "||";


		$query_parser = "SELECT id,nickname,email FROM `login` where id = '$id'";

		$result_parser = mysql_query($query_parser,$con);
		$total_record_parser = mysql_num_rows($result_parser);

		for ($i=0; $i < $total_record_parser; $i++)                    
		{

		  mysql_data_seek($result_parser, $i);      
		  
		  $row = mysql_fetch_array($result_parser);

		  echo $row[id];
		  echo "||";
		  echo $row[nickname];
		  echo "||";
		  echo $row[email];

		}

	}

	

	mysql_close($con);
?>

