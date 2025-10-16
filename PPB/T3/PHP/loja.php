<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loja</title>

    <!-- (opcional) adicionar Bootstrap para a tabela ficar estilizada -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>

<body>

    <h1>LOJA</h1>

    <?php
        require_once 'connect.php';
        $db = new Mydb(); 

        $sql = "SELECT * FROM produto";

        $result = $db->query($sql);

        if($result->num_rows == 0){
            echo "nao ha registros";
            die();
        }
    ?>

    <table class="table table-striped">
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Preço</th>
        </tr>

        <?php foreach($result as $row): ?>

        <tr>
            <td><?= $row['id']; ?></td>
            <td><?= $row['nome']; ?></td>
            <td><?= $row['preco']; ?></td>
        </tr>

        <?php endforeach; ?>
    </table>

</body>
</html>

