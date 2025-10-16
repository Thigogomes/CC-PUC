<?php

require_once 'connect.php';

$player_id = $_POST['player_id'];
$star_score = $_POST['star_score'];
$pineapple_score = $_POST['pineapple_score'];

$sql = "INSERT INTO score (player_id, star_score, pineapple_score) VALUES ('$player_id', '$star_score', '$pineapple_score')";
echo $sql;

$db = new Mydb();

if($db->query($sql) == TRUE){
    echo "Saved Score!!! ";
} else{
    echo "Erro!!!";
}

?>