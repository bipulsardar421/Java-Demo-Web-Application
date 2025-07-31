CREATE DATABASE  IF NOT EXISTS `bipul` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bipul`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: bipul
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `client_id` int NOT NULL AUTO_INCREMENT,
  `client_name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'Subbaya Gari Hotel','Mano Sri Harsha','222416 Bukka street town kotha road','7749879798','2025-03-21 11:21:52'),(2,'Apple Inc.','Tim Cooks','222416 Bukka street town kotha road','1234567890','2025-03-21 11:34:44'),(3,'Meta Inc.','Mark Zuckerberg','332254 Meta India, Vizag','1234567897','2025-03-21 11:35:37');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `invoice_id` int NOT NULL AUTO_INCREMENT,
  `client_id` int NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `customer_contact` varchar(12) NOT NULL,
  `invoice_date` date DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT '0.00',
  `tax` decimal(10,2) DEFAULT '0.00',
  `grand_total` decimal(10,2) NOT NULL,
  `payment_status` enum('Pending','Paid','Cancelled') DEFAULT 'Pending',
  `payment_method` varchar(40) NOT NULL,
  `notes` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,1,'Subbaya Gari Hotel','7749879798','2025-03-21',600.00,2.00,18.00,693.84,'Paid','Card','Hii','2025-03-21 05:54:33'),(2,1,'Subbaya Gari Hotel','7749879798','2025-03-21',56400.00,2.00,18.00,65220.96,'Paid','Card','Test','2025-03-21 06:00:51'),(3,3,'Meta Inc.','1234567897','2025-03-21',200.00,2.00,18.00,231.28,'Paid','Card','Hello','2025-03-21 06:06:34'),(4,1,'Subbaya Gari Hotel','7749879798','2025-03-21',20.00,2.00,18.00,23.13,'Paid','Card','rrr','2025-03-21 06:20:49'),(5,3,'Meta Inc.','1234567897','2025-03-21',320.00,2.00,18.00,370.05,'Paid','Card','Hii','2025-03-21 06:23:49'),(6,1,'Subbaya Gari Hotel','7749879798','2025-03-22',400.00,2.00,18.00,462.56,'Paid','Card','hii','2025-03-21 06:38:42'),(7,3,'Meta Inc.','1234567897','2025-04-01',740736.00,2.00,18.00,856587.11,'Paid','Card','hii','2025-04-01 10:59:38'),(8,1,'Subbaya Gari Hotel','7749879798','2025-04-01',60.00,2.00,18.00,69.38,'Paid','Card','','2025-04-01 11:32:14'),(9,2,'Apple Inc.','1234567890','2025-04-15',200.00,2.00,18.00,231.28,'Paid','Card','87987','2025-04-15 06:56:27');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_item`
--

DROP TABLE IF EXISTS `invoice_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_item` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_item`
--

LOCK TABLES `invoice_item` WRITE;
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
INSERT INTO `invoice_item` VALUES (1,1,1,6,100.00,600.00),(2,2,1,564,100.00,56400.00),(3,3,1,2,100.00,200.00),(4,4,2,2,10.00,20.00),(5,5,2,2,10.00,20.00),(6,5,1,3,100.00,300.00),(7,6,1,4,100.00,400.00),(8,7,3,6,123456.00,740736.00),(9,8,2,6,10.00,60.00),(10,9,1,2,100.00,200.00);
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL,
  `isNew` tinyint(1) DEFAULT '1',
  `status` enum('active','inactive') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (1,'bipulsardar421@gmail.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','admin',0,'active'),(2,'vamsigontudad@gmail.com','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225','nuser',0,'active'),(3,'bipulsardar091@gmail.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','nuser',0,'active'),(4,'abc@gmail.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','admin',0,'active'),(5,'test@example.us','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','nuser',0,'active');
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otpvalidation`
--

DROP TABLE IF EXISTS `otpvalidation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otpvalidation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `otp` varchar(6) NOT NULL,
  `status` enum('active','inactive') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otpvalidation`
--

LOCK TABLES `otpvalidation` WRITE;
/*!40000 ALTER TABLE `otpvalidation` DISABLE KEYS */;
INSERT INTO `otpvalidation` VALUES (1,1,'881364','inactive'),(2,1,'335651','inactive'),(3,1,'957272','inactive'),(4,2,'669861','inactive'),(5,1,'854787','inactive'),(6,3,'503712','inactive'),(7,1,'354153','inactive'),(8,1,'560432','inactive'),(9,1,'243777','inactive'),(10,1,'240793','inactive'),(11,3,'420883','inactive'),(12,4,'276231','inactive'),(13,5,'436003','inactive'),(14,1,'433768','inactive');
/*!40000 ALTER TABLE `otpvalidation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `vendor_id` int NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `rate` decimal(10,3) NOT NULL,
  `recieved_date` date NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `status` enum('active','inactive') NOT NULL DEFAULT 'active',
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,1,'Bat Mobile',416,100.000,'2025-03-07','http://localhost:8080/Java-Demo-Web-Application/images/products/1742536136106_1742407164016_moonboat_e38uu2at.jpg','active','2025-03-21 05:48:56','2025-03-21 05:48:56'),(2,1,'Apple',90,10.000,'2025-03-21','http://localhost:8080/Java-Demo-Web-Application/images/products/1742537927716_1742493459336_2download.jpg','active','2025-03-21 06:18:47','2025-03-21 06:18:47'),(3,2,'Jupiter',1228,123456.000,'2025-02-01','http://localhost:8080/Java-Demo-Web-Application/images/products/1743505115524_1742453902161_abc.jpg','active','2025-04-01 10:58:35','2025-04-01 10:58:35');
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` text NOT NULL,
  `image` varchar(255) NOT NULL,
  `status` varchar(9) NOT NULL DEFAULT 'active',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES (1,1,'Bipul Sardar','7749879755','222416 Bukka street town kotha road','http://localhost:8080/Java-Demo-Web-Application/images/users/1742535609179_IMG_20240523_144909.jpg','active','2025-03-21 05:40:09','2025-03-21 05:40:09'),(2,2,'Vamsi','7578787578','222416 Bukka street town kotha road','http://localhost:8080/Java-Demo-Web-Application/images/users/1742537455668_1740669897547_Screenshot (61).png','active','2025-03-21 05:56:59','2025-03-21 06:10:56'),(3,3,'Bipul Sardar','1234567890','222416 Bukka street town kotha road','http://localhost:8080/Java-Demo-Web-Application/images/users/1742538803390_abc.jpg','active','2025-03-21 06:33:23','2025-03-21 06:33:23'),(4,4,'Abc','1234587890','Abc ','http://localhost:8080/Java-Demo-Web-Application/images/users/1744699458748_IMG_20240523_144909.jpg','active','2025-04-15 06:44:18','2025-04-15 06:44:19'),(5,5,'Bipul Sardar','7749879750','222416 Bukka street town kotha road','http://localhost:8080/Java-Demo-Web-Application/images/users/1744699658751_14kb.jpeg','active','2025-04-15 06:47:38','2025-04-15 06:47:39');
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendors`
--

DROP TABLE IF EXISTS `vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendors` (
  `vendor_id` int NOT NULL AUTO_INCREMENT,
  `vendor_name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`vendor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendors`
--

LOCK TABLES `vendors` WRITE;
/*!40000 ALTER TABLE `vendors` DISABLE KEYS */;
INSERT INTO `vendors` VALUES (1,'Gontu Industry','Vamsi Gontu','Srikakulam','7894561230','2025-03-21 11:16:26'),(2,'TVS Motors','Harsha','Vizag','1234567890','2025-03-21 12:00:37');
/*!40000 ALTER TABLE `vendors` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-31 14:21:18
