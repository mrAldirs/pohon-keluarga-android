<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_keluarga':
                $kd_pengguna = $_POST['kd_pengguna'];

                $sql = "SELECT kd_keluarga, nama_keluarga, status_keluarga, nama_mc
                    FROM keluarga NATURAL JOIN moderator WHERE kd_pengguna = '$kd_pengguna'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_anggota = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_anggota, $data);
                    }
                    echo json_encode($data_anggota);
                    exit();
                } else {
                    $data_anggota = array();
                    echo json_encode($data_anggota);
                }
                break;
            case 'show_data_keluarga_umum':
                $sql = "SELECT kd_keluarga, nama_keluarga, status_keluarga, nama_mc
                    FROM keluarga NATURAL JOIN moderator WHERE status_keluarga = 'Umum'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_anggota = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_anggota, $data);
                    }
                    echo json_encode($data_anggota);
                    exit();
                } else {
                    $data_anggota = array();
                    echo json_encode($data_anggota);
                }
                break;
            case 'insert':
                $query = mysqli_query($conn, "SELECT max(kd_keluarga) AS id_terbesar FROM keluarga");
                $data = mysqli_fetch_array($query);
                $kd_keluarga = $data['id_terbesar'];

                $urut = (int) substr($kd_keluarga, 3);
                $urut++;

                $depan = "KG";
                $kode_keluarga = $depan . sprintf("%06s", $urut);

                $nama_mc = $_POST['nama_mc'];
                $nama_keluarga = $_POST['nama_keluarga'];
                $status_keluarga = $_POST['status_keluarga'];

                $sql = "INSERT INTO keluarga(kd_keluarga, nama_mc, nama_keluarga, status_keluarga)
                    VALUES('$kode_keluarga','$nama_mc','$nama_keluarga','$status_keluarga')";
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
            case 'jumlah_keluarga':
                $nama_mc = $_POST['nama_mc'];

                $sql = "SELECT COUNT(*) AS jumlah FROM moderator NATURAL JOIN keluarga WHERE nama_mc = '$nama_mc'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $jumlah = mysqli_fetch_assoc($result);

                    echo json_encode($jumlah);
                    exit();
                } else {
                    $respon['kode']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'delete':
                $kd_keluarga = $_POST['kd_keluarga'];

                $sql = "DELETE FROM keluarga WHERE kd_keluarga = '$kd_keluarga'";
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
            case 'ubah_status':
                $kd_keluarga = $_POST['kd_keluarga'];
                $status_keluarga = $_POST['status_keluarga'];

                $sql = "UPDATE keluarga SET status_keluarga = '$status_keluarga' WHERE kd_keluarga = '$kd_keluarga'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'coba':
                $query = mysqli_query($conn, "SELECT max(kd_keluarga) AS id_terbesar FROM keluarga");
                $data = mysqli_fetch_array($query);
                $kd_keluarga = $data['id_terbesar'];

                $urut = (int) substr($kd_keluarga, 3);
                $urut++;

                $depan = "KD-";
                $kode_keluarga = $depan . sprintf("%05s", $urut);

                echo json_encode($kode_keluarga);
                break;
        }
    }

?>