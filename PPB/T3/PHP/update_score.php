<?php

require_once 'connect.php';

$player_id = $_POST['player_id'];
$star_score = $_POST['star_score'];
$pineapple_score = $_POST['pineapple_score'];

$db = new Mydb();

$sql = $sql = "UPDATE score SET star_score = $star_score, pineapple_score = $pineapple_score WHERE player_id = $player_id";
$result = $db->con->query($sql);

if ($result->num_rows > 0) {
    echo "updeitadooo";
} else {
    echo "nao deu update :-(";
}

$db->close();

?>