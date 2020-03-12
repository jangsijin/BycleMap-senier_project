<?php


$cnt = $_POST['count'];

for ($i=0; $i < $cnt; $i++){

	if(count($_POST)){
		$word[] ='';

		if(isset($_POST['word'.$i])){
			$word[$i] = $_POST['word'.$i];


		if ($word==null || !$word)
			 echo 'word is null';

	   }

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

	$total_record_count = 0;
	for ($j=0; $j < $cnt; $j++){
		
		$query = "SELECT * FROM board where title like '%$word[$j]%'";
		$result = mysql_query($query,$con);
		$total_record[j] = mysql_num_rows($result);

		if($total_record[j] == 0){

			echo 0;

		}

		
		$total_record_count = $total_record_count+$total_record[j];


	}

	if($total_record_count != 0){

		echo "{\"status\":\"OK\",\"num_results\":\"$total_record_count\",\"results\":[";
		for ($j=0; $j < $cnt; $j++){
			
			$query = "SELECT * FROM board where title like '%$word[$j]%'";
			$result = mysql_query($query,$con);
			$total_record = mysql_num_rows($result);

			if($total_record != 0){

				// JSONArray 형식으로 만들기 위해서...



				// 반환된 각 레코드별로 JSONArray 형식으로 만들기.
				for ($i=0; $i < $total_record; $i++){

					 // 가져올 레코드로 위치(포인터) 이동  
					mysql_data_seek($result, $i);       
							
					$row = mysql_fetch_array($result);
					echo "{\"num\":$row[num],\"title\":\"$row[title]\",\"date\":\"$row[date]\",\"id\":\"$row[id]\",\"good\":\"$row[good]\",\"memo\":\"$row[memo]\",\"goodcheck\":\"$row[goodcheck]\",\"boardToken\":\"$row[boardToken]\"}";
					 
					// 마지막 레코드 이전엔 ,를 붙인다. 그래야 데이터 구분이 되니깐.  
					if($i<$total_record-1){
					echo ",";

					}
				}

			
			}

			if($j<$cnt-1){
				echo ",";

			}
		}

		echo "]}";
	}

	mysql_close($con);
?>

