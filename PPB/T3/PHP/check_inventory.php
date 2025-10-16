<?php
require_once 'connect.php';

if (isset($_POST['player_id'], $_POST['item_id'])) {

    $player_id = (int) $_POST['player_id'];
    $item_id = (int) $_POST['item_id'];

    $db = new Mydb();

    $sql = "SELECT id FROM inventario WHERE player_id = $player_id AND item_id = $item_id";
    $result = $db->con->query($sql);

    if ($result && $result->num_rows > 0) {
        echo "1";
    } else {
        echo "0"; 
    }

    $db->close();

} else {
    echo "Erro: id, pineapple_score ou star_score não enviados.";
}
?>