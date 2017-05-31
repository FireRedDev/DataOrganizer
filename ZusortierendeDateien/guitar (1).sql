-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 16. Mai 2017 um 09:59
-- Server-Version: 10.1.16-MariaDB
-- PHP-Version: 7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `guitar`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `guitar`
--

CREATE TABLE `guitar` (
  `id` tinyint(2) NOT NULL,
  `path` tinytext NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `guitar`
--

INSERT INTO `guitar` (`id`, `path`, `date`) VALUES
(3, 'upload/natur.jpg', '2017-05-15 09:40:23'),
(4, 'upload/man.jpg', '2017-05-15 21:02:52'),
(6, 'upload/girl-1488518_1920.jpg', '2017-05-15 21:33:32'),
(7, 'upload/countryside-1851309_1920.jpg', '2017-05-16 09:58:57');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `guitar`
--
ALTER TABLE `guitar`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `guitar`
--
ALTER TABLE `guitar`
  MODIFY `id` tinyint(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
