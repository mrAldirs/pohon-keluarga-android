<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_bagikan':
                $kd_pengguna = $_POST['kd_pengguna'];

                $queryA = mysqli_query($conn, "SELECT * FROM keluarga NATURAL JOIN moderator WHERE kd_pengguna = '$kd_pengguna'");

                $data_result = array();
                while ($data1 = mysqli_fetch_array($queryA)) {
                    $kd_keluarga = $data1['kd_keluarga'];
                    $nama_keluarga = $data1['nama_keluarga'];

                    $queryB = mysqli_query($conn, "SELECT COUNT(status_akses) AS `value` FROM akses_silsilah WHERE kd_keluarga = '$kd_keluarga' AND status_akses = 'acc'");
                    $data2 = mysqli_fetch_array($queryB);
                    $value = $data2['value'];

                    $data = array();
                    $data['kd_keluarga'] = $kd_keluarga;
                    $data['nama_keluarga'] = $nama_keluarga;
                    $data['value'] = $value;

                    $data_result[] = $data;
                }

                header("Access-Control-Allow-Origin: *");
                header("Content-Type: application/json");

                echo json_encode($data_result);
                break;
            case 'show_akses_silsilah':
                $penggunaParent = $_POST['kd_pengguna'];

                $sql = "SELECT kd_akses, kd_pengguna, username, kd_keluarga, nama_keluarga, pengguna_parent, status_akses, tgl_akses, CONCAT('$http_img', foto_mc) AS img
                    FROM akses_silsilah NATURAL JOIN pengguna NATURAL JOIN keluarga NATURAL JOIN moderator WHERE pengguna_parent = '$penggunaParent' ORDER BY tgl_akses DESC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_keluarga = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_keluarga, $data);
                    }
                    echo json_encode($data_keluarga);
                    exit();
                } else {
                    $data_keluarga = array();
                    echo json_encode($data_keluarga);
                }
                break;
            case 'insert_akses':
                $query = mysqli_query($conn, "SELECT max(kd_akses) AS id_terbesar FROM akses_silsilah");
                $data = mysqli_fetch_array($query);
                $kd_akses = $data['id_terbesar'];

                $urut = (int) substr($kd_akses, 3);
                $urut++;

                $depan = "AKS";
                $kode_akses = $depan . sprintf("%06s", $urut);
                
                $kd_pengguna = $_POST['kd_pengguna'];
                $kd_keluarga = $_POST['kd_keluarga'];
                $username = $_POST['username'];
                $nohp_mc = $_POST['nohp_mc'];

                $query = mysqli_query($conn, "SELECT * FROM keluarga WHERE kd_keluarga = '$kd_keluarga'");
                $data = mysqli_fetch_array($query);
                $status = $data['status_keluarga'];

                if($status == 'Privat'){ 
                    $respon['respon'] = "3";
                    echo json_encode($respon);
                    exit();
                } else {
                    $query = mysqli_query($conn, "SELECT * FROM pengguna NATURAL JOIN moderator WHERE username = '$username' AND nohp_mc = '$nohp_mc'");

                    if(mysqli_num_rows($query)>0){
                        $data = mysqli_fetch_array($query);
                        $penggunaParent = $data['kd_pengguna'];

                        $query = mysqli_query($conn, "SELECT * FROM akses_silsilah WHERE pengguna_parent = '$penggunaParent' AND kd_keluarga = '$kd_keluarga'");

                        if(mysqli_num_rows($query)>0){ 
                            $respon['respon'] = "2";
                            echo json_encode($respon);
                            exit();
                        } else {
                            $query = mysqli_query($conn, "INSERT INTO akses_silsilah(kd_akses, kd_pengguna, kd_keluarga, tgl_akses, pengguna_parent, status_akses)
                                VALUES('$kode_akses','$kd_pengguna','$kd_keluarga',NOW(),'$penggunaParent','baru')");

                            $respon['respon'] = "1";
                            echo json_encode($respon);
                            exit();
                        }
                    } else {
                        $respon['respon'] = "0";
                        echo json_encode($respon);
                        exit();
                    }
                }
                break;
            case 'acc_akses':
                $kd_akses = $_POST['kd_akses'];
                $status_akses = $_POST['status_akses'];

                $query = mysqli_query($conn, "UPDATE akses_silsilah SET status_akses = '$status_akses' WHERE kd_akses = '$kd_akses'");

                if ($status_akses == 'acc') {
                    $respon['respon'] = "1";
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