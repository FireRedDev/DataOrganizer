-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 28. Mai 2017 um 14:11
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

DROP TABLE IF EXISTS `guitar`;
CREATE TABLE `guitar` (
  `id` tinyint(2) NOT NULL,
  `path` tinytext NOT NULL,
  `date` datetime NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `guitar`
--

INSERT INTO `guitar` (`id`, `path`, `date`, `user_id`) VALUES
(3, 'upload/natur.jpg', '2017-05-15 09:40:23', 9),
(4, 'upload/man.jpg', '2017-05-15 21:02:52', 9),
(6, 'upload/girl-1488518_1920.jpg', '2017-05-15 21:33:32', 9),
(7, 'upload/countryside-1851309_1920.jpg', '2017-05-16 09:58:57', 9),
(15, 'upload/guitar-player-2176688_1920.jpg', '2017-05-28 11:38:52', 9);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `guitar`
--
ALTER TABLE `guitar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `guitar`
--
ALTER TABLE `guitar`
  MODIFY `id` tinyint(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `guitar`
--
ALTER TABLE `guitar`
  ADD CONSTRAINT `guitar_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `guitar_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
