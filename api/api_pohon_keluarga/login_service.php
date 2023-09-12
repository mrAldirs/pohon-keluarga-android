<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'login':
                $username = $_POST['username'];
                $password = $_POST['password'];

                $sql = "SELECT * FROM pengguna NATURAL JOIN moderator WHERE username = '$username' AND `password` = '$password'";

                $result = mysqli_query($conn, $sql);
                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);

                    echo json_encode($data);
                    exit();
                }else {
                    $respon['nama_mc'] = "0";
                    $respon['username'] = "0";
                    $respon['password'] = "0";
                    $respon['level'] = "0";
                    $respon['kd_pengguna'] = "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'regis':
                // kd_pengguna
                $query = mysqli_query($conn, "SELECT max(kd_pengguna) AS id_terbesar FROM pengguna");
                $data = mysqli_fetch_array($query);
                $kd_pengguna = $data['id_terbesar'];
                $urut = (int) substr($kd_pengguna, 3);
                $urut++;
                $depan = "AK";
                $kode_pengguna = $depan . sprintf("%06s", $urut);

                $username = $_POST['username'];
                $password = $_POST['password'];
                $nohp_mc = $_POST['nohp_mc'];
                $nama_mc = $_POST['nama_mc'];
                $email_mc = $_POST['email_mc'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "image/";

                $sql = "SELECT * FROM pengguna WHERE username = '$username'";
                $result = mysqli_query($conn, $sql);
                if (mysqli_num_rows($result)>0) {
                    $respon['respon'] = '0';
                    echo json_encode($respon);
                    exit();
                } else {
                    $sql = "SELECT * FROM moderator WHERE nohp_mc = '$nohp_mc'";
                    $result = mysqli_query($conn, $sql);
                    if (mysqli_num_rows($result)>0) {
                        $respon['respon'] = '1';
                        echo json_encode($respon);
                        exit();
                    } else {
                        $sql = "SELECT * FROM moderator WHERE nama_mc = '$nama_mc'";
                        $result = mysqli_query($conn, $sql);
                        if (mysqli_num_rows($result)>0) {
                            $respon['respon'] = '2';
                            echo json_encode($respon);
                            exit();
                        } else {
                            $sql = "INSERT INTO pengguna(kd_pengguna, username, `password`, `level`)
                                VALUES('$kode_pengguna','$username','$password','User')";
                            $result = mysqli_query($conn, $sql);

                            $sql2 = "INSERT INTO moderator(nama_mc, kd_pengguna, nohp_mc, email_mc, foto_mc)
                                VALUES('$nama_mc','$kode_pengguna','$nohp_mc','$email_mc','$file')";
                            $result2 = mysqli_query($conn, $sql2);
                            if ($result2) {
                                if(file_put_contents($path.$file, base64_decode($imstr))==false){
                                    echo json_encode($respon);
                                    exit();
                                } else {
                                    $respon['respon']= "3";
                                    echo json_encode($respon);
                                    exit();
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

?>