-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2023 at 02:26 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pohon_keluarga`
--

-- --------------------------------------------------------

--
-- Table structure for table `akses_silsilah`
--

CREATE TABLE `akses_silsilah` (
  `kd_akses` varchar(15) NOT NULL,
  `kd_pengguna` varchar(15) DEFAULT NULL,
  `kd_keluarga` varchar(15) DEFAULT NULL,
  `tgl_akses` date DEFAULT NULL,
  `pengguna_parent` varchar(15) DEFAULT NULL,
  `status_akses` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `akses_silsilah`
--

INSERT INTO `akses_silsilah` (`kd_akses`, `kd_pengguna`, `kd_keluarga`, `tgl_akses`, `pengguna_parent`, `status_akses`) VALUES
('AKS000001', 'AK000001', 'KG000002', '2023-07-18', 'AK000002', 'acc'),
('AKS000002', 'AK000002', 'KG000003', '2023-07-18', 'AK000001', 'acc'),
('AKS000003', 'AK000002', 'KG000004', '2023-07-19', 'AK000001', 'acc'),
('AKS000004', 'AK000002', 'KG000003', '2023-07-19', 'AK000003', 'acc'),
('AKS000005', 'AK000003', 'KG000005', '2023-07-19', 'AK000001', 'acc');

-- --------------------------------------------------------

--
-- Table structure for table `album`
--

CREATE TABLE `album` (
  `kd_album` varchar(15) NOT NULL,
  `kd_keluarga` varchar(15) DEFAULT NULL,
  `foto_album` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `anggota_keluarga`
--

CREATE TABLE `anggota_keluarga` (
  `kd_anggota` varchar(15) NOT NULL,
  `kd_keluarga` varchar(15) DEFAULT NULL,
  `nama_anggota` varchar(50) DEFAULT NULL,
  `jenkel` varchar(15) DEFAULT NULL,
  `status_anggota` varchar(30) DEFAULT NULL,
  `foto` varchar(50) DEFAULT NULL,
  `parent_id` varchar(15) DEFAULT NULL,
  `partner_id` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `anggota_keluarga`
--

INSERT INTO `anggota_keluarga` (`kd_anggota`, `kd_keluarga`, `nama_anggota`, `jenkel`, `status_anggota`, `foto`, `parent_id`, `partner_id`) VALUES
('AG000001', 'KG000001', 'Namikaze Minato', 'Laki-laki', 'Orang Tua', 'user.png', 'null', 'AG000007'),
('AG000002', 'KG000001', 'Uzumaki Naruto', 'Laki-laki', 'Anak dari Namikaze Minato', 'user.png', 'AG000001', 'AG000006'),
('AG000003', 'KG000001', 'Uzumaki Boruto', 'Laki-laki', 'Anak dari Uzumaki Naruto', 'user.png', 'AG000002', NULL),
('AG000004', 'KG000001', 'Uzumaki Himawari', 'Perempuan', 'Anak dari Uzumaki Naruto', 'user.png', 'AG000002', NULL),
('AG000005', 'KG000001', 'Uzumaki Kawaki', 'Laki-laki', 'Anak dari Uzumaki Naruto', 'user.png', 'AG000002', NULL),
('AG000006', 'KG000003', 'Uciha Bapaknya Sasuke', 'Laki-laki', 'Orang Tua', 'user.png', 'null', NULL),
('AG000007', 'KG000005', 'Fabri', 'Laki-laki', 'Orang Tua', 'user.png', 'null', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `keluarga`
--

CREATE TABLE `keluarga` (
  `kd_keluarga` varchar(15) NOT NULL,
  `nama_mc` varchar(50) DEFAULT NULL,
  `nama_keluarga` varchar(50) DEFAULT NULL,
  `status_keluarga` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `keluarga`
--

INSERT INTO `keluarga` (`kd_keluarga`, `nama_mc`, `nama_keluarga`, `status_keluarga`) VALUES
('KG000001', 'Uzumaki Naruto', 'Namikaze Minato', 'Privat'),
('KG000002', 'Uzumaki Naruto', 'Keluarga Paiwan', 'Umum'),
('KG000003', 'Uchiha Sasuke', 'Keluarga Uciha', 'Umum'),
('KG000004', 'Uchiha Sasuke', 'Keluarga Paijah', 'Umum'),
('KG000005', 'Klan 3B Skuad', 'Keluarga 3B', 'Umum'),
('KG000006', 'Klan 3B Skuad', 'Silsilah Privat', 'Umum');

-- --------------------------------------------------------

--
-- Table structure for table `kontak`
--

CREATE TABLE `kontak` (
  `kd_kontak` varchar(15) NOT NULL,
  `kd_anggota` varchar(15) DEFAULT NULL,
  `email` varchar(35) DEFAULT NULL,
  `nohp_kontak` varchar(15) DEFAULT NULL,
  `alamat` mediumtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kontak`
--

INSERT INTO `kontak` (`kd_kontak`, `kd_anggota`, `email`, `nohp_kontak`, `alamat`) VALUES
('KN000001', 'AG000001', NULL, NULL, NULL),
('KN000002', 'AG000002', NULL, NULL, NULL),
('KN000003', 'AG000003', NULL, NULL, NULL),
('KN000004', 'AG000004', NULL, NULL, NULL),
('KN000005', 'AG000005', NULL, NULL, NULL),
('KN000006', 'AG000006', NULL, NULL, NULL),
('KN000007', 'AG000007', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `moderator`
--

CREATE TABLE `moderator` (
  `nama_mc` varchar(50) NOT NULL,
  `kd_pengguna` varchar(15) DEFAULT NULL,
  `nohp_mc` varchar(15) DEFAULT NULL,
  `email_mc` varchar(35) DEFAULT NULL,
  `foto_mc` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `moderator`
--

INSERT INTO `moderator` (`nama_mc`, `kd_pengguna`, `nohp_mc`, `email_mc`, `foto_mc`) VALUES
('Klan 3B Skuad', 'AK000003', '0854456251', '', 'IMG20230718225739.jpg'),
('Uchiha Sasuke', 'AK000002', '085664536445', '', 'IMG20230718000215.jpg'),
('Uzumaki Naruto', 'AK000001', '081249710599', '', 'IMG20230717165239.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `kd_pengguna` varchar(15) NOT NULL,
  `username` varchar(35) DEFAULT NULL,
  `password` varchar(35) DEFAULT NULL,
  `level` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`kd_pengguna`, `username`, `password`, `level`) VALUES
('AK000001', 'naruto', 'naruto', 'User'),
('AK000002', 'sasuke', 'sasuke', 'User'),
('AK000003', 'klan3b', 'klan3b', 'User');

-- --------------------------------------------------------

--
-- Table structure for table `profil`
--

CREATE TABLE `profil` (
  `kd_profil` varchar(15) NOT NULL,
  `kd_anggota` varchar(15) DEFAULT NULL,
  `tgl_lahir` date DEFAULT NULL,
  `status_hidup` varchar(25) DEFAULT NULL,
  `pendidikan` varchar(35) DEFAULT NULL,
  `pekerjaan` varchar(35) DEFAULT NULL,
  `status_kawin` varchar(25) DEFAULT NULL,
  `golongan_darah` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profil`
--

INSERT INTO `profil` (`kd_profil`, `kd_anggota`, `tgl_lahir`, `status_hidup`, `pendidikan`, `pekerjaan`, `status_kawin`, `golongan_darah`) VALUES
('PF000001', 'AG000001', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000002', 'AG000002', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000003', 'AG000003', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000004', 'AG000004', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000005', 'AG000005', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000006', 'AG000006', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL),
('PF000007', 'AG000007', NULL, 'Masih Hidup', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `reuni`
--

CREATE TABLE `reuni` (
  `kd_reuni` int(15) NOT NULL,
  `pesan` text DEFAULT NULL,
  `jam_reuni` varchar(10) DEFAULT NULL,
  `tgl_reuni` date DEFAULT NULL,
  `tempat` varchar(50) DEFAULT NULL,
  `status_reuni` varchar(15) DEFAULT NULL,
  `pengguna_dikirim` varchar(35) DEFAULT NULL,
  `kd_pengirim` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reuni`
--

INSERT INTO `reuni` (`kd_reuni`, `pesan`, `jam_reuni`, `tgl_reuni`, `tempat`, `status_reuni`, `pengguna_dikirim`, `kd_pengirim`) VALUES
(5, 'Undangan pesan', '12:00', '2023-07-18', 'Kediri', 'done', 'naruto', 'AK000003'),
(6, 'Undangan pesan', '12:00', '2023-07-18', 'Kediri', 'done', 'sasuke', 'AK000003'),
(7, 'Ini adalah contoh undangan reuni keluarga', '07:30', '2023-07-29', 'Hotel Kediri', 'done', 'naruto', 'AK000003'),
(8, 'Ini adalah contoh undangan reuni keluarga', '07:30', '2023-07-29', 'Hotel Kediri', 'done', 'sasuke', 'AK000003');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `akses_silsilah`
--
ALTER TABLE `akses_silsilah`
  ADD PRIMARY KEY (`kd_akses`),
  ADD KEY `kd_pengguna` (`kd_pengguna`),
  ADD KEY `kd_keluarga` (`kd_keluarga`);

--
-- Indexes for table `album`
--
ALTER TABLE `album`
  ADD PRIMARY KEY (`kd_album`),
  ADD UNIQUE KEY `album_pk` (`kd_album`),
  ADD KEY `relationship_9` (`kd_keluarga`);

--
-- Indexes for table `anggota_keluarga`
--
ALTER TABLE `anggota_keluarga`
  ADD PRIMARY KEY (`kd_anggota`),
  ADD UNIQUE KEY `anggota_keluarga_pk` (`kd_anggota`),
  ADD KEY `relationship_7_fk` (`kd_keluarga`);

--
-- Indexes for table `keluarga`
--
ALTER TABLE `keluarga`
  ADD PRIMARY KEY (`kd_keluarga`),
  ADD UNIQUE KEY `keluarga_pk` (`kd_keluarga`),
  ADD KEY `relationship_6_fk` (`nama_mc`);

--
-- Indexes for table `kontak`
--
ALTER TABLE `kontak`
  ADD PRIMARY KEY (`kd_kontak`),
  ADD UNIQUE KEY `kontak_pk` (`kd_kontak`),
  ADD KEY `relationship_4_fk` (`kd_anggota`);

--
-- Indexes for table `moderator`
--
ALTER TABLE `moderator`
  ADD PRIMARY KEY (`nama_mc`),
  ADD UNIQUE KEY `moderator_pk` (`nama_mc`),
  ADD KEY `relationship_1_fk` (`kd_pengguna`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`kd_pengguna`),
  ADD UNIQUE KEY `pengguna_pk` (`kd_pengguna`);

--
-- Indexes for table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`kd_profil`),
  ADD UNIQUE KEY `profil_pk` (`kd_profil`),
  ADD KEY `relationship_3_fk` (`kd_anggota`);

--
-- Indexes for table `reuni`
--
ALTER TABLE `reuni`
  ADD PRIMARY KEY (`kd_reuni`),
  ADD KEY `kd_pengirim` (`kd_pengirim`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `reuni`
--
ALTER TABLE `reuni`
  MODIFY `kd_reuni` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `akses_silsilah`
--
ALTER TABLE `akses_silsilah`
  ADD CONSTRAINT `akses_silsilah_ibfk_1` FOREIGN KEY (`kd_pengguna`) REFERENCES `pengguna` (`kd_pengguna`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `akses_silsilah_ibfk_2` FOREIGN KEY (`kd_keluarga`) REFERENCES `keluarga` (`kd_keluarga`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `album`
--
ALTER TABLE `album`
  ADD CONSTRAINT `fk_album_relathionship_keluarga` FOREIGN KEY (`kd_keluarga`) REFERENCES `keluarga` (`kd_keluarga`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `anggota_keluarga`
--
ALTER TABLE `anggota_keluarga`
  ADD CONSTRAINT `fk_anggota__relations_keluarga` FOREIGN KEY (`kd_keluarga`) REFERENCES `keluarga` (`kd_keluarga`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `keluarga`
--
ALTER TABLE `keluarga`
  ADD CONSTRAINT `fk_keluarga_relations_moderato` FOREIGN KEY (`nama_mc`) REFERENCES `moderator` (`nama_mc`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kontak`
--
ALTER TABLE `kontak`
  ADD CONSTRAINT `fk_kontak_relations_anggota_` FOREIGN KEY (`kd_anggota`) REFERENCES `anggota_keluarga` (`kd_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `moderator`
--
ALTER TABLE `moderator`
  ADD CONSTRAINT `fk_moderato_relations_pengguna` FOREIGN KEY (`kd_pengguna`) REFERENCES `pengguna` (`kd_pengguna`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `profil`
--
ALTER TABLE `profil`
  ADD CONSTRAINT `fk_profil_relations_anggota_` FOREIGN KEY (`kd_anggota`) REFERENCES `anggota_keluarga` (`kd_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reuni`
--
ALTER TABLE `reuni`
  ADD CONSTRAINT `reuni_ibfk_2` FOREIGN KEY (`kd_pengirim`) REFERENCES `pengguna` (`kd_pengguna`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
