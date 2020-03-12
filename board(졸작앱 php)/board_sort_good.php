<?php

	$filename = "log.c";

	$title = $_POST["title"];
	$date = $_POST["date"];
	$id = $_POST["id"];
	$good = $_POST["good"];
	

//위에는 의미없음.



//연결
	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{

	}
//연결완료

	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 
//Auto_increment 초기화

	mysql_query($resetNum,$con);
//mysql 설정
	$query = "SELECT * FROM board ORDER BY good asc";

//query를 저장


	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);

   // JSONArray 형식으로 만들기 위해서...

	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
//	echo "[";
 
   // 반환된 각 레코드별로 JSONArray 형식으로 만들기.
   for ($i=0; $i < $total_record; $i++)                    
   {
      // 가져올 레코드로 위치(포인터) 이동  
      mysql_data_seek($result, $i);       
        
      $row = mysql_fetch_array($result);
	echo "{\"num\":$row[num],\"title\":\"$row[title]\",\"date\":\"$row[date]\",\"id\":\"$row[id]\",\"good\":\"$row[good]\",\"memo\":\"$row[memo]\",\"goodcheck\":\"$row[goodcheck]\",\"boardToken\":\"$row[boardToken]\"}";
					 
   // 마지막 레코드 이전엔 ,를 붙인다. 그래야 데이터 구분이 되니깐.  
   if($i<$total_record-1){
      echo ",";
   }
   }
   // JSONArray의 마지막 닫기
   echo "]}";
//  echo "]";


	mysql_close($con);
?>