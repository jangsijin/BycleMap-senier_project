<?php

	if(isset($_POST["Token"])){

		$token = $_POST["Token"];
		//데이터베이스에 접속해서 토큰을 저장
		include_once 'config.php';
		$conn = mysqli_connect("localhost","ehdntjr123","kjt46031","ehdntjr123");
		$query = "INSERT INTO notlogin(Token) Values ('$token') ON DUPLICATE KEY UPDATE Token = '$token'; ";
		mysqli_query($conn, $query);

		mysqli_close($conn);
	}

?>

