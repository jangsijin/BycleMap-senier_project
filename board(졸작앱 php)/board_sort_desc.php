<?php

	$filename = "log.c";

	$title = $_POST["title"];
	$date = $_POST["date"];
	$id = $_POST["id"];
	$good = $_POST["good"];
	

//������ �ǹ̾���.



//����
	$con = mysql_connect("localhost","ehdntjr123","kjt46031");
	if (!$con){
	  die('Could not connect: ' . mysql_error());
	}else{

	}
//����Ϸ�

	mysql_select_db("ehdntjr123", $con);
	mysql_query("SET NAMES utf8"); 
//Auto_increment �ʱ�ȭ

	mysql_query($resetNum,$con);
//mysql ����
	$query = "SELECT * FROM board ORDER BY num desc";

//query�� ����


	$result = mysql_query($query,$con);
	$total_record = mysql_num_rows($result);

   // JSONArray �������� ����� ���ؼ�...

	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
//	echo "[";
 
   // ��ȯ�� �� ���ڵ庰�� JSONArray �������� �����.
   for ($i=0; $i < $total_record; $i++)                    
   {
      // ������ ���ڵ�� ��ġ(������) �̵�  
      mysql_data_seek($result, $i);       
        
      $row = mysql_fetch_array($result);
		echo "{\"num\":$row[num],\"title\":\"$row[title]\",\"date\":\"$row[date]\",\"id\":\"$row[id]\",\"good\":\"$row[good]\",\"memo\":\"$row[memo]\",\"goodcheck\":\"$row[goodcheck]\",\"boardToken\":\"$row[boardToken]\"}";
					 
   // ������ ���ڵ� ������ ,�� ���δ�. �׷��� ������ ������ �Ǵϱ�.  
   if($i<$total_record-1){
      echo ",";
   }
   }
   // JSONArray�� ������ �ݱ�
   echo "]}";
//  echo "]";


	mysql_close($con);
?>