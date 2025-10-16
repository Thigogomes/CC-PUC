<?php
require_once 'connect.php';

if (isset($_POST['id'], $_POST['pineapple_score'], $_POST['star_score'])) {

    $id = (int) $_POST['id'];
    $pineapple_score = (int) $_POST['pineapple_score'];
    $star_score = (int) $_POST['star_score'];

    $db = new Mydb();

    $sql = "SELECT preco, tipo_moeda FROM produto WHERE id = $id";
    $result = $db->con->query($sql);

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $preco = (int)$row['preco'];
        $tipo_moeda = $row['tipo_moeda'];

        if($tipo_moeda == "pineapple"){
            echo ($pineapple_score >= $preco) ? -$preco : "0";
        }
        else if($tipo_moeda == "star"){
            echo ($star_score >= $preco) ? $preco : "0";
        } else {
            echo "0"; // caso o tipo_moeda seja inválido
        }

    } else {
        echo "0"; // produto não encontrado
    }

    $db->close();

} else {
    echo "Erro: id, pineapple_score ou star_score não enviados.";
}
?>
