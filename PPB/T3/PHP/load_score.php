<?php

require_once 'connect.php';

$player_id = $_POST['player_id'];

$db = new Mydb();

$sql = "SELECT star_score, pineapple_score FROM score WHERE player_id = $player_id";
$result = $db->con->query($sql);
//echo $sql;

$scores = [];
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $scores[] = [
            "star_score" => (int)$row['star_score'],
            "pineapple_score" => (int)$row['pineapple_score']
        ];
    }
}

if (count($scores) === 0) {
    $scores[] = ["star_score" => 0, "pineapple_score" => 0];
}

echo json_encode(['scores' => $scores]);

$db->close();

?>