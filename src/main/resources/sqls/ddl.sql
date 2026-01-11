CREATE DATABASE  IF NOT EXISTS `book_store` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `book_store`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: book_store
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `blurb` varchar(255) DEFAULT NULL,
  `edition` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `published` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `publisher_id` bigint DEFAULT NULL,
  `series_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgtvt7p649s4x80y6f4842pnfq` (`publisher_id`),
  KEY `FKqup0ss2jvfddhq6mt3mjm55t9` (`series_id`),
  CONSTRAINT `FKgtvt7p649s4x80y6f4842pnfq` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`),
  CONSTRAINT `FKqup0ss2jvfddhq6mt3mjm55t9` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_creators`
--

DROP TABLE IF EXISTS `book_creators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_creators` (
  `books_id` bigint NOT NULL,
  `creators_id` bigint NOT NULL,
  KEY `FKp24in9drdfrrxp8wqbnl2jsje` (`creators_id`),
  KEY `FKrgm62ne0i0wh88yca9pxq6ncf` (`books_id`),
  CONSTRAINT `FKp24in9drdfrrxp8wqbnl2jsje` FOREIGN KEY (`creators_id`) REFERENCES `creator` (`id`),
  CONSTRAINT `FKrgm62ne0i0wh88yca9pxq6ncf` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_detail`
--

DROP TABLE IF EXISTS `book_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `book_condition` varchar(255) DEFAULT NULL,
  `book_format` varchar(255) DEFAULT NULL,
  `dimensions` varchar(255) DEFAULT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `print_length` bigint DEFAULT NULL,
  `sale_price` bigint DEFAULT NULL,
  `stock` bigint DEFAULT NULL,
  `supply_price` bigint DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl1hmgccsvfwcxhem3qw6l7gpm` (`book_id`),
  CONSTRAINT `FKl1hmgccsvfwcxhem3qw6l7gpm` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_genres`
--

DROP TABLE IF EXISTS `book_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_genres` (
  `books_id` bigint NOT NULL,
  `genres_id` bigint NOT NULL,
  KEY `FKkemwddl6cxkebe21lsqa2ob4q` (`genres_id`),
  KEY `FKlbdkit5k0gr9g1w5l791qcamg` (`books_id`),
  CONSTRAINT `FKkemwddl6cxkebe21lsqa2ob4q` FOREIGN KEY (`genres_id`) REFERENCES `genre` (`id`),
  CONSTRAINT `FKlbdkit5k0gr9g1w5l791qcamg` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_tags`
--

DROP TABLE IF EXISTS `book_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_tags` (
  `books_id` bigint NOT NULL,
  `tags_id` bigint NOT NULL,
  KEY `FKsky6wumpk8q486i2lecduct0d` (`tags_id`),
  KEY `FKmolnjthi6racnguu0nhrwx5iw` (`books_id`),
  CONSTRAINT `FKmolnjthi6racnguu0nhrwx5iw` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKsky6wumpk8q486i2lecduct0d` FOREIGN KEY (`tags_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `campaign`
--

DROP TABLE IF EXISTS `campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `campaign_type` enum('FLAT_DISCOUNT','PERCENTAGE_DISCOUNT', 'PERCENTAGE_RECEIPT', 'PERCENTAGE_PRODUCT') DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `max_discount` double DEFAULT NULL,
  `min_total` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `percentage` double DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `campaign_detail`
--

DROP TABLE IF EXISTS `campaign_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `book_detail_id` bigint DEFAULT NULL,
  `campaign_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpjk8vymdghyebeq5wuko8cur` (`book_detail_id`),
  KEY `FKdy46wy7qheoeajmh3skxhixtf` (`campaign_id`),
  CONSTRAINT `FKdy46wy7qheoeajmh3skxhixtf` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
  CONSTRAINT `FKpjk8vymdghyebeq5wuko8cur` FOREIGN KEY (`book_detail_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cart_detail`
--

DROP TABLE IF EXISTS `cart_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `amount` bigint DEFAULT NULL,
  `book_detail_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjrcme6q8ajo40npg0xhkknho7` (`book_detail_id`),
  KEY `FKhqem60okkngoihvdljfclmw48` (`user_id`),
  CONSTRAINT `FKhqem60okkngoihvdljfclmw48` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKjrcme6q8ajo40npg0xhkknho7` FOREIGN KEY (`book_detail_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `creator`
--

DROP TABLE IF EXISTS `creator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `creator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genre_closure`
--

DROP TABLE IF EXISTS `genre_closure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre_closure` (
  `depth` bigint DEFAULT NULL,
  `descendant_id` bigint NOT NULL,
  `ancestor_id` bigint NOT NULL,
  PRIMARY KEY (`ancestor_id`,`descendant_id`),
  KEY `FK5cue3qxyxj6n7elg13k9xky4h` (`descendant_id`),
  CONSTRAINT `FK5cue3qxyxj6n7elg13k9xky4h` FOREIGN KEY (`descendant_id`) REFERENCES `genre` (`id`),
  CONSTRAINT `FKngo8stvvl123732t8tp3uru9a` FOREIGN KEY (`ancestor_id`) REFERENCES `genre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_detail`
--

DROP TABLE IF EXISTS `payment_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `payment_type` enum('CASH','TRANSFER') DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `receipt_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdyyskc81r8gy2lsa8qota6ciq` (`receipt_id`),
  CONSTRAINT `FKsda90npndrm2jqloujabmi2` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `publisher`
--

DROP TABLE IF EXISTS `publisher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publisher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `receipt`
--

DROP TABLE IF EXISTS `receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_address` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `grand_total` double DEFAULT NULL,
  `has_shipping` bit(1) DEFAULT NULL,
  `order_status` enum('AUTHORIZED','CANCELLED','FAILED','IN_TRANSIT','PAID','PENDING','REFUNDED','WAITING_REFUND_INFO') DEFAULT NULL,
  `order_type` enum('DIRECT','ONLINE') DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `service_cost` double DEFAULT NULL,
  `shipping_id` varchar(255) DEFAULT NULL,
  `shipping_service` varchar(255) DEFAULT NULL,
  `sub_total` double DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkfphmv967orgmkx5hkbpitf3q` (`customer_id`),
  KEY `FKjxj56bwfq337r1s3tai3cl1sl` (`employee_id`),
  CONSTRAINT `FKjxj56bwfq337r1s3tai3cl1sl` FOREIGN KEY (`employee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkfphmv967orgmkx5hkbpitf3q` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `receipt_detail`
--

DROP TABLE IF EXISTS `receipt_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `price_per_unit` double DEFAULT NULL,
  `quantity` bigint DEFAULT NULL,
  `book_copy_id` bigint DEFAULT NULL,
  `receipt_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbwohckwtlh3eyv1e01ystbtkq` (`book_copy_id`),
  KEY `FK4qgqei3yfmquqnhwutybgv0dj` (`receipt_id`),
  CONSTRAINT `FK4qgqei3yfmquqnhwutybgv0dj` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`id`),
  CONSTRAINT `FKbwohckwtlh3eyv1e01ystbtkq` FOREIGN KEY (`book_copy_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `book_id` bigint DEFAULT NULL,
  `reviewer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK70yrt09r4r54tcgkrwbeqenbs` (`book_id`),
  KEY `FKt58e9mdgxpl7j90ketlaosmx4` (`reviewer_id`),
  CONSTRAINT `FK70yrt09r4r54tcgkrwbeqenbs` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKt58e9mdgxpl7j90ketlaosmx4` FOREIGN KEY (`reviewer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `series` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_oauth2user` bit(1) DEFAULT NULL,
  `oauth2id` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `person_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-02  6:29:36
