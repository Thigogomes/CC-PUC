<?php

require_once 'connect.php';

$nome  = $_POST['nome']  ?? '';
$email = $_POST['email'] ?? '';
$senha = $_POST['senha'] ?? '';

if ($nome === '' || $email === '' || $senha === '') {
    echo "Parametros faltando";
    exit;
}

$db = new Mydb();

$stmt = $db->con->prepare("INSERT INTO player (nome, email, senha) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $nome, $email, $senha);

if ($stmt->execute()) {
    echo "Usuario cadastrado!";
} else {
    echo "ERRO: " . $stmt->error;
}

$stmt->close();
$db->close();

?>