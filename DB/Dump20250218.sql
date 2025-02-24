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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (1,'b@c.c','abc','admin','active'),(2,'bipul','fd5a7510428eb97f8d02a1f7875e21d79367e94809cf76e58eeb3366a478b6f4','admin','active'),(3,'tt','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(4,'tt2','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(5,'tt23','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(6,'tt233','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(7,'null','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','admin','active'),(8,'bipulsardar091@gmail.com','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5','admin','active');
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otpvalidation`
--

LOCK TABLES `otpvalidation` WRITE;
/*!40000 ALTER TABLE `otpvalidation` DISABLE KEYS */;
INSERT INTO `otpvalidation` VALUES (1,8,'570896','inactive'),(2,8,'961654','inactive'),(3,8,'289997','inactive'),(4,8,'406834','inactive'),(5,8,'139035','inactive'),(6,8,'249734','inactive'),(7,8,'133437','inactive'),(8,8,'263853','inactive'),(9,8,'553122','inactive'),(10,8,'208011','inactive'),(11,8,'182331','inactive'),(12,8,'217116','active');
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
INSERT INTO `user_details` VALUES (1,1,'Bipul',1234567890,'123 Main Street, NY','http://localhost:8080/Java-Demo-Web-Application/images/users/1739784583908_me','active','2025-02-14 06:32:42','2025-02-17 09:32:06'),(2,2,'Bipul',1234567890,'123 Main Street, NY','http://localhost:8080/Java-Demo-Web-Application/images/users/1739784583907_me.jpg','active','2025-02-17 09:29:43','2025-02-17 09:48:43');
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

-- Dump completed on 2025-02-18 18:28:40
