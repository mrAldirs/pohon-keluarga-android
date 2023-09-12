<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_profil':
                $kd_pengguna = $_POST['kd_pengguna'];

                $sql = "SELECT nama_mc, nohp_mc, email_mc, CONCAT('$http_img', foto_mc) AS foto
                    FROM pengguna NATURAL JOIN moderator
                    WHERE kd_pengguna = '$kd_pengguna'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);
                    
                    echo json_encode($data); 
                    exit();
                }else{
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'validasi':
                $kd_keluarga = $_POST['kd_keluarga'];

                $sql = "SELECT kd_keluarga, nama_mc FROM keluarga NATURAL JOIN moderator WHERE kd_keluarga = '$kd_keluarga'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $keluarga = mysqli_fetch_assoc($result);

                    echo json_encode($keluarga);
                    exit();
                } else {
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                
                break;
        }
    }

?>