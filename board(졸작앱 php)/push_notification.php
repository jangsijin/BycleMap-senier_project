<?php 
	
	function send_notification ($tokens, $message)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';
		$fields = array(
			 'registration_ids' => $tokens,
			 'data' => $message
			);

		$headers = array(
			'Authorization:key = AIzaSyAsfmpymkfKlY4dFdS7BYrhkolQwd5LwMI' . GOOGLE_API_KEY,
			'Content-Type: application/json'
			);

	   $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
	}

	if(count($_POST)){
		$b_num ='';
		if(isset($_POST['b_num'])){
			$b_num = $_POST['b_num'];
		if ($b_num==null || !$b_num)
			 echo 'b_num is null';

		}
	}

	if(count($_POST)){
		$title ='';
		if(isset($_POST['title'])){
			$title = $_POST['title'];
		if ($title==null || !$title)
			 echo 'title is null';

		}
	}


	//데이터베이스에 접속해서 토큰들을 가져와서 FCM에 발신요청
	include_once 'config.php';
	$conn = mysqli_connect("localhost","ehdntjr123","kjt46031","ehdntjr123");

	//$sql = "Select Token From login where id = 'a'";


	$sql = "SELECT Token FROM login WHERE nickname = 
						(SELECT id FROM board WHERE num = '$b_num')";
	
	
	
	$result = mysqli_query($conn,$sql);// or die(mysqli_error($conn));
	$tokens = array();

	if(mysqli_num_rows($result) > 0 ){

		while ($row = mysqli_fetch_assoc($result)) {
			$tokens[] = $row["Token"];
		}
	}




	mysqli_close($conn);
	
    $myMessage = $_POST['message']; //폼에서 입력한 메세지를 받음
	
	if ($myMessage == ""){
		$myMessage = "No.".$b_num." 게시물 제목 : [".$title."]에 새로운 댓글이 등록되었습니다.";
	}

	$message = array("message" => $myMessage);
	$message_status = send_notification($tokens, $message);
	echo $message_status;

 ?>
