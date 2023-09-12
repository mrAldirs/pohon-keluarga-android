<?php 

    $server = "localhost";
    $username = "root";
    $password = "";
    $database = "pohon_keluarga";

    $conn = mysqli_connect($server, $username, $password, $database);

    $local = 'http://192.168.137.1/api_pohon_keluarga/';

    $http_img = $local . 'image/';
?>