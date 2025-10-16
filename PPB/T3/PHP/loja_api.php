<?php 
    header('Content-Type: application/json');

    require_once 'connect.php';
    $db = new Mydb(); 

    $sql = "SELECT * FROM produto";

    $result = $db->query($sql);

    if($result->num_rows == 0){
        $json = [
            "status" => "500",
            "data" => ""
        ];
    }else{
        $json = [
            "status" => "200",
            "data" => $result->fetch_all(MYSQLI_ASSOC)
        ];
    }

    echo json_encode($json);
?>
