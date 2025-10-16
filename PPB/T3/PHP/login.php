<?php

require_once 'connect.php';

$email = $_POST['email'] ?? '';
$senha = $_POST['senha'] ?? '';

$db = new Mydb();
echo $db->check_login($email, $senha);

?>