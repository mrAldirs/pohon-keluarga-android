package com.example.pohon_keluarga.Pohon

data class Node(
    val id: String,
    val nama: String,
    val status: String,
    val img: String,
    val id_parent: String?
)