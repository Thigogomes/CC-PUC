<?php
    class Mydb{
        public $com;
        const DB_CONN = "localhost";
        const DB_USER = "root";
        const DB_PASS = "";
        const DB_NAME = "jogo_backend";

        public function __construct(){
            $this->con = mysqli_connect(self::DB_CONN, self:: DB_USER, self::DB_PASS, self::DB_NAME);
            if(!$this->con){
                //echo "falha ao conectar: " . mysqli_connect_error();
            }else{
                //echo "Conectado com sucesso!";
            }
        }

        public function close(){
            $this->con->close();
        }

        public function query($sql){
            return $this->con->query($sql);
        }

        public function getId(){
            return $this->con->insert_id;
        }

        public function check_login($email, $senha){
            //echo "Email: $email Senha: $senha; ";
            $sql = "SELECT * FROM player WHERE email = '$email' AND senha = '$senha'";
            $result = $this->con->query($sql);
            if($result->num_rows > 0){
                $row = $result->fetch_assoc();
                return $row['id'];
            } else{
                return -1;
            }
        }
    }
?>