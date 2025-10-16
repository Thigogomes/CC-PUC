<?php

require_once 'connect.php';

$player_id = $_POST['player_id'];
$star_score = $_POST['star_score'];
$pineapple_score = $_POST['pineapple_score'];

$db = new Mydb();

$sql = "SELECT id FROM score WHERE player_id = $player_id";
$result = $db->con->query($sql);

if ($result->num_rows > 0) {
    echo "1";
} else {
    echo "0";
}

$db->close();

?>