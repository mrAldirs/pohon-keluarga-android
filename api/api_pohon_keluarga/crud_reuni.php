<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'cek_akun':
                $username = $_POST['username'];

                $query = mysqli_query($conn, "SELECT * FROM pengguna WHERE username = '$username'");
                if(mysqli_num_rows($query)>0){
                    $respon['username'] = $username;
                    $respon['respon'] = "1";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['username'] = "0";
                    $respon['respon'] = "2";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'show_data_reuni':
                $kd_pengguna = $_POST['kd_pengguna'];

                $query = mysqli_query($conn, "SELECT username FROM pengguna WHERE kd_pengguna = '$kd_pengguna'");
                $data = mysqli_fetch_assoc($query);
                $username = $data['username'];

                $query = mysqli_query($conn, "SELECT kd_reuni, nama_mc, tgl_reuni, pesan, status_reuni, CONCAT('$http_img', foto_mc) AS img
                    FROM pengguna NATURAL JOIN moderator NATURAL JOIN reuni WHERE pengguna_dikirim = '$username' GROUP BY kd_reuni ORDER BY tgl_reuni DESC");
                
                if(mysqli_num_rows($query)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_list = array();
                    while ($data = mysqli_fetch_assoc($query)) {
                        array_push($data_list, $data);
                    }
                    echo json_encode($data_list);
                    exit();
                } else {
                    $data_list = array();
                    echo json_encode($data_list);
                }
                break;
            case 'show_undangan_terkirim':
                $query = mysqli_query($conn, "SELECT * FROM reuni WHERE kd_reuni = '$kd_reuni'");

                if(mysqli_num_rows($query)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_list = array();
                    while ($data = mysqli_fetch_assoc($query)) {
                        array_push($data_list, $data);
                    }
                    echo json_encode($data_list);
                    exit();
                } else {
                    $data_list = array();
                    echo json_encode($data_list);
                }
                break;
            case 'lihat_undangan_baru':
                $kd_reuni = $_POST['kd_reuni'];

                $query = mysqli_query($conn, "SELECT * FROM reuni WHERE kd_reuni = '$kd_reuni' AND status_reuni = 'baru'");
                if(mysqli_num_rows($query)>0){
                    $query = mysqli_query($conn, "UPDATE reuni SET status_reuni = 'done' WHERE kd_reuni = '$kd_reuni'");

                    $respon['respon'] = "1";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon'] = "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'detail':
                $kd_reuni = $_POST['kd_reuni'];
    
                $sql = "SELECT * FROM reuni NATURAL JOIN pengguna NATURAL JOIN moderator WHERE kd_reuni = '$kd_reuni'";
                $result = mysqli_query($conn,$sql);
    
                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);
    
                    echo json_encode($data);
                    exit();
                } else {
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'show_data_terkirim':
                $kd_pengguna = $_POST['kd_pengguna'];
                
                $queryA = mysqli_query($conn, "SELECT tempat, jam_reuni, tgl_reuni, pesan FROM reuni WHERE kd_pengirim = '$kd_pengguna' GROUP BY jam_reuni, tgl_reuni");
                $data_result = array();
                
                while ($data = mysqli_fetch_array($queryA)) {
                    $tempat = $data['tempat'];
                    $jam_reuni = $data['jam_reuni'];
                    $tgl_reuni = $data['tgl_reuni'];
                    $pesan = $data['pesan'];

                    $queryB = mysqli_query($conn, "SELECT COUNT(pengguna_dikirim) AS `value` FROM reuni WHERE kd_pengirim = '$kd_pengguna' GROUP BY jam_reuni, tgl_reuni");
                    $data = mysqli_fetch_array($queryB);
                    $value = $data['value'];
                    
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $result = array();

                    $result['tempat'] = $tempat;
                    $result['jam_reuni'] = $jam_reuni;
                    $result['tgl_reuni'] = $tgl_reuni;
                    $result['pesan'] = $pesan;
                    $result['value'] = $value;

                    $data_result[] = $result;
                }

                header("Access-Control-Allow-Origin: *");
                header("Content-Type: application/json");

                echo json_encode($data_result);
                exit();
                break;
        }
    }

?>