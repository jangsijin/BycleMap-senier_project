<?php
if(count($_POST)){
    $boardToken ='';
    if(isset($_POST['boardToken'])){
        $boardToken = $_POST['boardToken'];
    if ($boardToken==null || !$boardToken)
         echo 'boardToken is null';


   }

 }




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
	$query = "SELECT * FROM reply WHERE boardToken = '$boardToken' ORDER BY num DESC";

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
   echo "{\"num\":$row[num],\"id\":\"$row[id]\",\"time\":\"$row[time]\",\"good\":\"$row[good]\",\"text\":\"$row[text]\",\"board_num\":\"$row[board_num]\",\"title\":\"$row[title]\",\"boardToken\":\"$row[boardToken]\"}";
 
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