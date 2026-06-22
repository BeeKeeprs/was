-- MySQL dump 10.13  Distrib 8.0.44, for macos15 (arm64)
-- Host: ec2-43-201-218-24.ap-northeast-2.compute.amazonaws.com    Database: webee
-- ------------------------------------------------------
-- Server version	9.6.0

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



-- Table structure for table `bee_diagnosis`

DROP TABLE IF EXISTS `bee_diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bee_diagnosis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `confidence` double NOT NULL,
  `crop_name` varchar(255) NOT NULL,
  `cultivation_address` varchar(255) NOT NULL,
  `cultivation_type` tinyint NOT NULL,
  `details` varchar(255) NOT NULL,
  `disease_type` enum('ADULT_NORMAL','ADULT_VARROA_MITE','ADULT_WING_DEFORMITY_VIRUS','LARVA_FOULBROOD','LARVA_NORMAL','LARVA_SACBROOD','LARVA_VARROA_MITE') DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `situation_analysis` varchar(255) NOT NULL,
  `solutions` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkksa5peshgvonnr4lj0o1mu1v` (`user_id`),
  CONSTRAINT `FKkksa5peshgvonnr4lj0o1mu1v` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `bee_diagnosis_chk_1` CHECK ((`cultivation_type` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `bee_recommendation`

DROP TABLE IF EXISTS `bee_recommendation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bee_recommendation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `bee_type` enum('BUMBLEBEE','HONEYBEE','MASON_BEE') NOT NULL,
  `caution` text NOT NULL,
  `characteristics` text NOT NULL,
  `crop_name` varchar(255) NOT NULL,
  `cultivation_address` varchar(255) NOT NULL,
  `cultivation_type` enum('CONTROLLED','OPEN_FIELD') NOT NULL,
  `input_end_date` date NOT NULL,
  `input_start_date` date NOT NULL,
  `usage_tip` text NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhplg6r4v4dfnrhoucqqyw0u4o` (`user_id`),
  CONSTRAINT `FKhplg6r4v4dfnrhoucqqyw0u4o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `business`

DROP TABLE IF EXISTS `business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `business` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `business_cert_image_url` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `commencement_date` date NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `kakao_chat_url` varchar(255) DEFAULT NULL,
  `online_store_url` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) NOT NULL,
  `registration_number` varchar(255) NOT NULL,
  `representative_name` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjkjjimeu5p0gv4orhue734xni` (`user_id`),
  CONSTRAINT `FKjkjjimeu5p0gv4orhue734xni` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `fcm_token`

DROP TABLE IF EXISTS `fcm_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fcm_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_info` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8u9xsmd3agc2nn80tb16ouph4` (`user_id`),
  CONSTRAINT `FK8u9xsmd3agc2nn80tb16ouph4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive`

DROP TABLE IF EXISTS `hive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `location` varchar(255) NOT NULL,
  `memo` text,
  `name` varchar(255) NOT NULL,
  `region` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `is_connected` bit(1) NOT NULL,
  `last_connected_at` datetime(6) DEFAULT NULL,
  `mac_address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5b3xq151h1f6w6cxkhm9px4st` (`user_id`),
  CONSTRAINT `FK5b3xq151h1f6w6cxkhm9px4st` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=221 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive_bee_count`

DROP TABLE IF EXISTS `hive_bee_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive_bee_count` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int NOT NULL,
  `recorded_at` datetime(6) NOT NULL,
  `hive_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrdqocjbxo0qpu7wdv51wu7gv3` (`hive_id`),
  CONSTRAINT `FKrdqocjbxo0qpu7wdv51wu7gv3` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive_control`

DROP TABLE IF EXISTS `hive_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive_control` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `auto_enabled` bit(1) NOT NULL,
  `manual_enabled` bit(1) NOT NULL,
  `type` enum('CO2','DOOR','FAN','HEATER','HUMIDITY','TEMPERATURE') NOT NULL,
  `hive_id` bigint NOT NULL,
  `is_on` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKap9gehvba7jke6m9a1udpamao` (`hive_id`),
  CONSTRAINT `FKap9gehvba7jke6m9a1udpamao` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive_control_schedule`

DROP TABLE IF EXISTS `hive_control_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive_control_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `end_time` time(6) NOT NULL,
  `start_time` time(6) NOT NULL,
  `hive_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4o31j16o6dcpgu0yn410rvs` (`hive_id`),
  CONSTRAINT `FKh4o31j16o6dcpgu0yn410rvs` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive_replacement_history`

DROP TABLE IF EXISTS `hive_replacement_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive_replacement_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `replaced_at` date NOT NULL,
  `usage_days` bigint DEFAULT NULL,
  `hive_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrsqblvpssxu4l9qj9l2eil36e` (`hive_id`),
  CONSTRAINT `FKrsqblvpssxu4l9qj9l2eil36e` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `hive_telemetry`

DROP TABLE IF EXISTS `hive_telemetry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hive_telemetry` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `co2` double DEFAULT NULL,
  `external_humidity` double DEFAULT NULL,
  `external_temperature` double DEFAULT NULL,
  `internal_humidity` double DEFAULT NULL,
  `internal_temperature` double DEFAULT NULL,
  `recorded_at` datetime(6) NOT NULL,
  `hive_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7reeg9kav31y1r5044yyx011f` (`hive_id`),
  CONSTRAINT `FK7reeg9kav31y1r5044yyx011f` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `interest_market`

DROP TABLE IF EXISTS `interest_market`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interest_market` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `crop_major_code` varchar(255) DEFAULT NULL,
  `crop_mid_name` varchar(255) DEFAULT NULL,
  `crop_minor_name` varchar(255) DEFAULT NULL,
  `market_code` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhd81dlp89ro83k60nyn8i8f8q` (`user_id`),
  CONSTRAINT `FKhd81dlp89ro83k60nyn8i8f8q` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `interest_news_keyword`

DROP TABLE IF EXISTS `interest_news_keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interest_news_keyword` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmdju4tseib4pjo97yx9tuge40` (`user_id`),
  CONSTRAINT `FKmdju4tseib4pjo97yx9tuge40` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `notification`

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` enum('HIVE_ALERT') NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb0yvoep4h4k92ipon31wmdf7e` (`user_id`),
  CONSTRAINT `FKb0yvoep4h4k92ipon31wmdf7e` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1401 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `oauth`

DROP TABLE IF EXISTS `oauth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `platform` enum('KAKAO','NAVER') DEFAULT NULL,
  `platform_id` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfy142i6qiv079490g6fkdh21w` (`user_id`),
  CONSTRAINT `FK7a23c0j3h2lvkrqjy7hkpx4hi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `post`

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`),
  CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `post_comment`

DROP TABLE IF EXISTS `post_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKna4y825fdc5hw8aow65ijexm0` (`post_id`),
  KEY `FKtc1fl97yq74q7j8i08ds731s1` (`user_id`),
  CONSTRAINT `FKna4y825fdc5hw8aow65ijexm0` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKtc1fl97yq74q7j8i08ds731s1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `post_like`

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj7iy0k7n3d0vkh8o7ibjna884` (`post_id`),
  KEY `FKhuh7nn7libqf645su27ytx21m` (`user_id`),
  CONSTRAINT `FKhuh7nn7libqf645su27ytx21m` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj7iy0k7n3d0vkh8o7ibjna884` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `product`

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `bee_type` enum('BUMBLEBEE','HONEYBEE','MASON_BEE') NOT NULL,
  `content` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `origin` enum('DOMESTIC','IMPORTED') NOT NULL,
  `price` int NOT NULL,
  `transaction_method` enum('OFFLINE','ONLINE') NOT NULL,
  `transaction_type` enum('PURCHASE','RENTAL') NOT NULL,
  `business_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbxgk89jvyti6iaqnuevrlwt6r` (`business_id`),
  CONSTRAINT `FKbxgk89jvyti6iaqnuevrlwt6r` FOREIGN KEY (`business_id`) REFERENCES `business` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `product_image`

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6oo0cvcdtb6qmwsga468uuukk` (`product_id`),
  CONSTRAINT `FK6oo0cvcdtb6qmwsga468uuukk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `product_review`

DROP TABLE IF EXISTS `product_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkaqmhakwt05p3n0px81b9pdya` (`product_id`),
  KEY `FK78cdr7qgrm9sp9igada7vk4xp` (`user_id`),
  CONSTRAINT `FK78cdr7qgrm9sp9igada7vk4xp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkaqmhakwt05p3n0px81b9pdya` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `user`

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `business_registered` bit(1) NOT NULL DEFAULT b'0',
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `user_crop`

DROP TABLE IF EXISTS `user_crop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_crop` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `cultivation_area` int NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `cultivation_type` enum('CONTROLLED','OPEN_FIELD') NOT NULL,
  `name` varchar(255) NOT NULL,
  `planting_date` date DEFAULT NULL,
  `variety` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `harvest_end_date` date DEFAULT NULL,
  `harvest_start_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcp900wnw74bkkyb1gfybg9r9n` (`user_id`),
  CONSTRAINT `FKcp900wnw74bkkyb1gfybg9r9n` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-22 17:30:02
