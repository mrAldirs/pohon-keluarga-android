<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_anggota':
                $kd_keluarga = $_POST['kd_keluarga'];

                $sql = "SELECT kd_anggota, nama_anggota, parent_id, CONCAT('$http_img', foto) AS img, partner_id
                    FROM anggota_keluarga WHERE kd_keluarga = '$kd_keluarga'";
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
            case 'detail':
                $kd_anggota = $_POST['kd_anggota'];
    
                $sql = "SELECT nama_anggota, jenkel, status_anggota, tgl_lahir, status_hidup, kd_anggota, kd_keluarga, kd_profil, CONCAT('$http_img', foto) AS img, parent_id, partner_id
                    FROM anggota_keluarga NATURAL JOIN profil NATURAL JOIN keluarga WHERE kd_anggota = '$kd_anggota'";
                $result = mysqli_query($conn,$sql);
    
                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $profil = mysqli_fetch_assoc($result);
    
                    echo json_encode($profil);
                    exit();
                } else {
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'insert':
                // kd_anggota
                $query = mysqli_query($conn, "SELECT max(kd_anggota) AS id_terbesar FROM anggota_keluarga");
                $data = mysqli_fetch_array($query);
                $kd_anggota = $data['id_terbesar'];
                $urut = (int) substr($kd_anggota, 3);
                $urut++;
                $depan = "AG";
                $kode_anggota = $depan . sprintf("%06s", $urut);
    
                // kd_profil
                $query = mysqli_query($conn, "SELECT max(kd_profil) AS id_terbesar FROM profil");
                $data = mysqli_fetch_array($query);
                $kd_profil = $data['id_terbesar'];
                $urut = (int) substr($kd_profil, 3);
                $urut++;
                $depan = "PF";
                $kode_profil = $depan . sprintf("%06s", $urut);
    
                // kd_kontak
                $query = mysqli_query($conn, "SELECT max(kd_kontak) AS id_terbesar FROM kontak");
                $data = mysqli_fetch_array($query);
                $kd_kontak = $data['id_terbesar'];
                $urut = (int) substr($kd_kontak, 3);
                $urut++;
                $depan = "KN";
                $kode_kontak = $depan . sprintf("%06s", $urut);
    
                $kd_keluarga = $_POST['kd_keluarga'];
                $nama_anggota = $_POST['nama_anggota'];
                $jenkel = $_POST['jenkel'];
                $status_anggota = $_POST['status_anggota'];
                $parent_id = $_POST['parent_id'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "image/";
    
                if ($imstr == "") {
                    $sql = "INSERT INTO anggota_keluarga(kd_anggota, kd_keluarga, nama_anggota, jenkel, status_anggota, foto, parent_id)
                        VALUES('$kode_anggota','$kd_keluarga','$nama_anggota','$jenkel','$status_anggota','user.png', '$parent_id')";
                    $result = mysqli_query($conn,$sql);
    
                    $sql2 = "INSERT INTO profil(kd_profil, kd_anggota, status_hidup) VALUES('$kode_profil','$kode_anggota', 'Masih Hidup')";
                    $result2 = mysqli_query($conn,$sql2);
    
                    $sql3 = "INSERT INTO kontak(kd_kontak, kd_anggota) VALUES('$kode_kontak','$kode_anggota')";
                    $result3 = mysqli_query($conn,$sql3);
    
                    if ($result3) {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    }
                }
                else {
                    $sql = "INSERT INTO anggota_keluarga(kd_anggota, kd_keluarga, nama_anggota, jenkel, status_anggota, foto, parent_id)
                        VALUES('$kode_anggota','$kd_keluarga','$nama_anggota','$jenkel','$status_anggota','$file', '$parent_id')";
                    $result = mysqli_query($conn,$sql);
    
                    $sql2 = "INSERT INTO profil(kd_profil, kd_anggota, status_hidup) VALUES('$kode_profil','$kode_anggota', 'Masih Hidup')";
                    $result2 = mysqli_query($conn,$sql2);
    
                    $sql3 = "INSERT INTO kontak(kd_kontak, kd_anggota) VALUES('$kode_kontak','$kode_anggota')";
                    $result3 = mysqli_query($conn,$sql3);
    
                    if ($result3) {
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
                }
                break;
            case 'delete':
                $kd_anggota = $_POST['kd_anggota'];

                $query = mysqli_query($conn, "DELETE FROM anggota_keluarga WHERE kd_anggota = '$kd_anggota'");

                if ($query) {
                    $query2 = mysqli_query($conn, "DELETE FROM anggota_keluarga WHERE parent_id = '$kd_anggota'");
                    
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }

                break;
            case 'read_anggota':
                $kd_keluarga = $_POST['kd_keluarga'];
                $nama_anggota = $_POST['nama_anggota'];

                $sql = "SELECT kd_anggota, nama_anggota, status_anggota, CONCAT('$http_img', foto) AS img
                    FROM anggota_keluarga WHERE kd_keluarga = '$kd_keluarga' AND nama_anggota LIKE '%$nama_anggota%'";
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
            case 'get_anggota':
                $sql = "SELECT nama_anggota FROM anggota_keluarga ORDER BY nama_anggota ASC";
                $result = mysqli_query($conn,$sql);

                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-type: application/json; charset=UTF-8");

                    $respon = array();
                        while($nama_angg = mysqli_fetch_assoc($result)){
                            array_push($respon, $nama_angg);
                        }
                    echo json_encode($respon);
                } else {
                    $respon = array();
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>