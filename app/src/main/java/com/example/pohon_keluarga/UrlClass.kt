package com.example.pohon_keluarga

class UrlClass {

    val local = "http://192.168.137.1/api_pohon_keluarga/"

    // crud_validasi.php
    val validasi = local + "login_service.php"

    // crud_moderator.php
    val urlModerator = local + "crud_moderator.php"

    // crud_keluarga.php
    val urlKeluarga = local + "crud_keluarga.php"

    // crud_anggota.php
    val urlAnggota = local + "crud_anggota.php"

    // crud_album.php
    val urlAlbum = local + "crud_album.php"

    // crud_profil.php
    val urlProfil = local + "crud_profil.php"

    // crud_akses.php
    val urlAkses = local + "crud_akses.php"

    // inser_reuni.php
    val insertReuni = local + "insert_reuni.php"

    // crud_reuni.php
    val urlReuni = local + "crud_reuni.php"
}