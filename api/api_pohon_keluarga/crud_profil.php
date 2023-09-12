<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'detail_profil':
                $kd_anggota = $_POST['kd_anggota'];

                $sql = "SELECT nama_mc, nama_anggota, jenkel, status_anggota, tgl_lahir, status_hidup, kd_anggota, kd_keluarga, kd_profil, CONCAT('$http_img', foto) AS img, parent_id
                FROM anggota_keluarga NATURAL JOIN profil NATURAL JOIN keluarga NATURAL JOIN moderator WHERE kd_anggota = '$kd_anggota' GROUP BY nama_anggota";
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
            case 'detail_kontak':
                $kd_anggota = $_POST['kd_anggota'];

                $sql = "SELECT nama_mc, email, nohp_kontak, alamat, kd_anggota, kd_kontak
                    FROM kontak NATURAL JOIN anggota_keluarga NATURAL JOIN keluarga NATURAL JOIN moderator WHERE kd_anggota = '$kd_anggota' GROUP BY nama_anggota";
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
            case 'detail_lainnya':
                $kd_anggota = $_POST['kd_anggota'];

                $sql = "SELECT nama_mc, pendidikan, pekerjaan, status_kawin, golongan_darah, kd_profil, kd_anggota
                    FROM profil NATURAL JOIN anggota_keluarga NATURAL JOIN keluarga NATURAL JOIN moderator WHERE kd_anggota = '$kd_anggota' GROUP BY nama_anggota";
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
            case 'edit_profil':
                $kd_anggota = $_POST['kd_anggota'];
                $nama_anggota = $_POST['nama_anggota'];
                $jenkel = $_POST['jenkel'];
                $tgl_lahir = $_POST['tgl_lahir'];
                $status_hidup = $_POST['status_hidup'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "image/";

                if ($imstr == "") {
                    $sql = "UPDATE anggota_keluarga SET nama_anggota = '$nama_anggota', jenkel = '$jenkel'
                        WHERE kd_anggota = '$kd_anggota'";
                    $result = mysqli_query($conn,$sql);

                    $sql2 = "UPDATE profil SET tgl_lahir = '$tgl_lahir', status_hidup = '$status_hidup' WHERE kd_anggota = '$kd_anggota'";
                    $result2 = mysqli_query($conn,$sql2);

                    if ($result2) {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    }
                } else {
                    $sql = "UPDATE anggota_keluarga SET nama_anggota = '$nama_anggota', jenkel = '$jenkel', foto = '$file'
                        WHERE kd_anggota = '$kd_anggota'";
                    $result = mysqli_query($conn,$sql);
    
                    $sql2 = "UPDATE profil SET tgl_lahir = '$tgl_lahir', status_hidup = '$status_hidup' WHERE kd_anggota = '$kd_anggota'";
                    $result2 = mysqli_query($conn,$sql2);
    
                    if ($result2) {
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
            case 'edit_kontak':
                $kd_anggota = $_POST['kd_anggota'];
                $email = $_POST['email'];
                $nohp_kontak = $_POST['nohp_kontak'];
                $alamat = $_POST['alamat'];

                $sql = "UPDATE kontak SET email = '$email', nohp_kontak = '$nohp_kontak', alamat = '$alamat' WHERE kd_anggota = '$kd_anggota'";
                $result = mysqli_query($conn,$sql);

                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'edit_lainnya':
                $kd_anggota = $_POST['kd_anggota'];
                $pendidikan = $_POST['pendidikan'];
                $pekerjaan = $_POST['pekerjaan'];
                $status_kawin = $_POST['status_kawin'];
                $golongan_darah = $_POST['golongan_darah'];

                $sql = "UPDATE profil SET pendidikan = '$pendidikan', pekerjaan = '$pekerjaan', status_kawin = '$status_kawin' , golongan_darah = '$golongan_darah'
                    WHERE kd_anggota = '$kd_anggota'";
                $result = mysqli_query($conn,$sql);

                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
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