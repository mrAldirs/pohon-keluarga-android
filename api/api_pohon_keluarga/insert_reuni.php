<?php 

    include "koneksi.php";

    $pengguna_array = explode(",", $_POST['pengguna']);
    $pesan = $_POST['pesan'];
    $jam_reuni = $_POST['jam_reuni'];
    $tgl_reuni = $_POST['tgl_reuni'];
    $tempat = $_POST['tempat'];
    $kd_pengirim = $_POST['kd_pengirim'];

    // Loop melalui setiap kd_pengguna dan masukkan undangan ke dalam tabel reuni
    foreach ($pengguna_array as $pg) {
        $query = mysqli_query($conn, "INSERT INTO reuni(pesan, jam_reuni, tgl_reuni, tempat, status_reuni, pengguna_dikirim, kd_pengirim)
            VALUES('$pesan','$jam_reuni','$tgl_reuni','$tempat','baru','$pg','$kd_pengirim')");
    }

    $respon['respon'] = "1";
    echo json_encode($respon);
    exit();

?>
