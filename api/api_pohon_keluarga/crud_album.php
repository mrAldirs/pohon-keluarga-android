<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_album':
                $kd_keluarga = $_POST['kd_keluarga'];
                
                $sql = "SELECT nama_mc, kd_keluarga, kd_album, status_keluarga, foto_album, CONCAT('$http_img', foto_album) AS img_album
                    FROM album NATURAL JOIN keluarga NATURAL JOIN moderator WHERE kd_keluarga = '$kd_keluarga' ORDER BY kd_keluarga ASC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_album = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_album, $data);
                    }
                    echo json_encode($data_album);
                    exit();
                } else {
                    $data_album = array();
                    echo json_encode($data_album);
                }
                break;
            case 'insert':
                $query = mysqli_query($conn, "SELECT max(kd_album) AS id_terbesar FROM album");
                $data = mysqli_fetch_array($query);
                $kd_album = $data['id_terbesar'];

                $urut = (int) substr($kd_album, 3);
                $urut++;

                $depan = "ALB";
                $kode_album = $depan . sprintf("%06s", $urut);

                $kd_keluarga = $_POST['kd_keluarga'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "image/";

                $sql = "INSERT INTO album(kd_album, kd_keluarga, foto_album) VALUES('$kode_album','$kd_keluarga','$file')";
                $result = mysqli_query($conn,$sql);

                if ($result) {
                    if(file_put_contents($path.$file, base64_decode($imstr))==false){
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    }
                }
                break;
            case 'delete':
                $kd_album = $_POST['kd_album'];

                $sql = "DELETE FROM album WHERE kd_album = '$kd_album'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon'] = "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>