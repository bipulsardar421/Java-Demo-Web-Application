CREATE DATABASE  IF NOT EXISTS `bipul` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bipul`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: bipul
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `invoice_id` int NOT NULL AUTO_INCREMENT,
  `customer_name` char(3) NOT NULL,
  `customer_contact` varchar(20) NOT NULL,
  `invoice_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT '0.00',
  `tax` decimal(10,2) DEFAULT '0.00',
  `grand_total` decimal(10,2) NOT NULL,
  `payment_status` enum('Pending','Paid','Cancelled') DEFAULT 'Pending',
  `payment_method` char(4) NOT NULL,
  `notes` char(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'XYP','9876543210','2025-02-18 21:25:51',5000.00,200.00,300.00,5100.00,'Paid','AB12','A1B2C3D4E5','2025-02-18 15:55:51'),(2,'QWE','8765432109','2025-02-18 21:25:51',12000.00,500.00,900.00,12400.00,'Pending','ZX78','X8Y7Z6W5V4','2025-02-18 15:55:51'),(3,'RTM','7654321098','2025-02-18 21:25:51',8000.00,300.00,600.00,8300.00,'Paid','RT45','P4L3K2D1N0','2025-02-18 15:55:51'),(4,'LZY','6543210987','2025-02-18 21:25:51',15000.00,700.00,1200.00,15700.00,'Cancelled','BN23','M9N8O7R6S5','2025-02-18 15:55:51'),(5,'PLK','5432109876','2025-02-18 21:25:51',22000.00,800.00,1500.00,22700.00,'Paid','CX89','J1A2B3L4K5','2025-02-18 15:55:51');
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
INSERT INTO `invoice_item` VALUES (1,1,1,2,1000.00,2000.00),(2,1,3,1,1500.00,1500.00),(3,2,2,3,1200.00,3600.00),(4,2,4,2,1000.00,2000.00),(5,3,5,4,800.00,3200.00),(6,3,6,2,1000.00,2000.00),(7,4,7,3,500.00,1500.00),(8,4,8,2,600.00,1200.00),(9,5,9,5,1500.00,7500.00),(10,5,10,1,1700.00,1700.00);
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
  `status` enum('active','inactive') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (1,'b@c.c','abc','vendor','active'),(2,'bipul','fd5a7510428eb97f8d02a1f7875e21d79367e94809cf76e58eeb3366a478b6f4','vendor','active'),(3,'tt','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(4,'tt2','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(5,'tt23','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(6,'tt233','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(7,'null','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(8,'bipulsardar091@gmail.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','admin','active'),(11,'hanaso6986@lxheir.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','client','active'),(12,'bipulsardar421@gmail.com','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5','client','active'),(15,'mahanbipul420@gmail.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','admin','active');
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otpvalidation`
--

LOCK TABLES `otpvalidation` WRITE;
/*!40000 ALTER TABLE `otpvalidation` DISABLE KEYS */;
INSERT INTO `otpvalidation` VALUES (1,8,'669082','inactive'),(2,8,'170423','inactive'),(3,8,'943599','inactive'),(4,8,'457651','inactive'),(5,8,'101796','inactive'),(6,8,'144944','inactive'),(7,8,'491744','inactive'),(8,8,'711506','inactive'),(9,8,'241916','inactive'),(10,8,'508997','inactive'),(11,8,'711862','inactive'),(12,9,'636097','inactive'),(13,9,'938280','active'),(14,12,'349323','active'),(15,13,'171370','active'),(16,15,'201467','inactive'),(17,15,'710643','inactive'),(18,15,'345627','inactive'),(19,15,'126767','inactive'),(20,15,'890407','inactive'),(21,15,'705939','inactive'),(22,15,'892483','inactive'),(23,15,'598432','inactive'),(24,15,'587783','inactive'),(25,15,'524681','inactive'),(26,15,'769474','inactive'),(27,15,'108870','inactive'),(28,15,'330161','inactive'),(29,15,'324182','inactive'),(30,15,'915452','inactive'),(31,15,'454128','inactive'),(32,15,'508678','inactive'),(33,15,'184248','inactive'),(34,15,'894177','inactive'),(35,15,'452800','inactive'),(36,15,'529022','inactive'),(37,15,'187342','inactive'),(38,15,'689008','inactive');
/*!40000 ALTER TABLE `otpvalidation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signupvalidation`
--

DROP TABLE IF EXISTS `signupvalidation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `signupvalidation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(80) NOT NULL,
  `otp` varchar(6) NOT NULL,
  `status` enum('active','inactive') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signupvalidation`
--

LOCK TABLES `signupvalidation` WRITE;
/*!40000 ALTER TABLE `signupvalidation` DISABLE KEYS */;
INSERT INTO `signupvalidation` VALUES (1,'mahanbipul420@gmail.com','738863','inactive'),(2,'mahanbipul420@gmail.com','785118','inactive'),(3,'mahanbipul420@gmail.com','922024','inactive'),(4,'mahanbipul420@gmail.com','744701','inactive'),(5,'mahanbipul420@gmail.com','524131','inactive'),(6,'mahanbipul420@gmail.com','520792','inactive');
/*!40000 ALTER TABLE `signupvalidation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `rate` int NOT NULL,
  `recieved_date` date NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `status` enum('active','inactive') NOT NULL DEFAULT 'active',
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,'abc',12,12,'2025-02-12','http://trial.com','active','2025-02-13 06:47:55','2025-02-12 18:30:00'),(2,'abc',12,12,'2025-02-13','http://trial.com','active','2025-02-13 06:47:55','2025-02-12 18:30:00'),(3,'abc',12,12,'2025-02-21','http://trial.com','active','2025-02-13 10:01:47','2025-02-12 18:30:00'),(4,'Test',12,12,'2025-02-21','http://trial.com','active','2025-02-13 10:02:24','2025-02-14 04:19:53'),(5,'abc',12,12,'2025-02-21','http://trial.com','active','2025-02-13 11:30:45','2025-02-13 11:30:45'),(6,'abc',12,12,'2025-02-21','http://trial.com','active','2025-02-13 11:46:13','2025-02-13 11:46:13'),(7,'abc',12,12,'2025-02-21','No file uploaded!','active','2025-02-14 10:42:16','2025-02-14 10:42:16'),(8,'abc',12,12,'2025-02-21','http://localhost:8080/Java-Demo-Web-Application/images/products/1739529840233_me.jpg','active','2025-02-14 10:44:00','2025-02-14 10:44:00'),(9,'Test',12,12,'2025-02-21','http://localhost:8080/Java-Demo-Web-Application/images/products/1739784175809_me.jpg','active','2025-02-17 09:20:18','2025-02-17 09:22:56');
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
  `phone` int NOT NULL,
  `address` text NOT NULL,
  `image` varchar(255) NOT NULL,
  `status` varchar(9) NOT NULL DEFAULT 'active',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES (1,1,'Bipul',1234567890,'123 Main Street, NY','http://localhost:8080/Java-Demo-Web-Application/images/users/1739784583908_me.jpg','active','2025-02-14 06:32:42','2025-02-17 09:32:06'),(2,2,'Bipul',1234567890,'123 Main Street, NY','http://localhost:8080/Java-Demo-Web-Application/images/users/1739784583907_me.jpg','active','2025-02-17 09:29:43','2025-02-17 09:48:43');
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-24  1:02:41
