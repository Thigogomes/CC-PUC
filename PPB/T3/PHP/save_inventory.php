<?php
require_once 'connect.php'; 

if (isset($_POST['player_id']) && isset($_POST['item_id'])) {

    $player_id = (int) $_POST['player_id'];
    $item_id = (int) $_POST['item_id'];

    $db = new Mydb();

    $sql = "INSERT INTO inventario (player_id, item_id) VALUES ($player_id, $item_id)";

    if ($db->query($sql) === TRUE) {
        echo "Item adicionado.";
    } else {
        echo "Erro no SQL: " . $db->error;
    }

    $db->close();
} else {
    echo "Erro: player_id ou item_id não enviados.";
}
?>
