-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: mediconnect_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_date` date NOT NULL,
  `appointment_reference` varchar(255) DEFAULT NULL,
  `appointment_time` time(6) NOT NULL,
  `cancellation_reason` varchar(255) DEFAULT NULL,
  `consultation_type` enum('IN_PERSON','VIDEO') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `fee` double DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `reschedule_reason` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','CONFIRMED','NO_SHOW','PENDING','RESCHEDULED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo690gyordobh8dp3ea367v871` (`doctor_id`),
  KEY `FKopb2h9yhin1rb4dqote8bws6w` (`patient_id`),
  CONSTRAINT `FKo690gyordobh8dp3ea367v871` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`),
  CONSTRAINT `FKopb2h9yhin1rb4dqote8bws6w` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,'2026-06-15','MC-1779879892254','10:30:00.000000','Personal Emergency','IN_PERSON','2026-05-27 16:34:52.254663',800,'Regular checkup',NULL,'CANCELLED','2026-05-27 16:36:27.913280',1,1),(2,'2026-06-20','MC-1780211316488','10:00:00.000000',NULL,'VIDEO','2026-05-31 12:38:36.488528',800,'Video consultation test',NULL,'PENDING','2026-05-31 12:38:36.488528',1,1),(3,'2026-07-01','MC-1780221436344','11:00:00.000000',NULL,'IN_PERSON','2026-05-31 15:27:16.344969',800,'Follow up',NULL,'PENDING','2026-05-31 15:27:16.344969',1,1),(4,'2026-07-01','MC-1780328765799','11:15:00.000000',NULL,'IN_PERSON','2026-06-01 21:16:05.799966',800,'Follow up',NULL,'PENDING','2026-06-01 21:16:05.799966',1,1);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `entity_id` bigint DEFAULT NULL,
  `entity_type` varchar(255) NOT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `user_role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultations`
--

DROP TABLE IF EXISTS `consultations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `diagnosis` varchar(1000) DEFAULT NULL,
  `doctor_notes` varchar(2000) DEFAULT NULL,
  `duration_minutes` bigint DEFAULT NULL,
  `follow_up_date` varchar(255) DEFAULT NULL,
  `session_end_time` datetime(6) DEFAULT NULL,
  `session_start_time` datetime(6) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','IN_PROGRESS','NO_SHOW','SCHEDULED') NOT NULL,
  `symptoms` varchar(1000) DEFAULT NULL,
  `treatment_plan` varchar(1000) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `video_session_id` varchar(255) DEFAULT NULL,
  `appointment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp0pg0r434dp34iesx6lj69b8m` (`appointment_id`),
  CONSTRAINT `FKp77tpwkqp4e3fxdi9d7eo44cx` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultations`
--

LOCK TABLES `consultations` WRITE;
/*!40000 ALTER TABLE `consultations` DISABLE KEYS */;
INSERT INTO `consultations` VALUES (1,'2026-05-31 12:39:47.501807','Mild angina','Patient advised to reduce salt intake',1,'2026-07-01','2026-05-31 12:42:40.870379','2026-05-31 12:41:10.510930','IN_PROGRESS','Chest pain, shortness of breath','Rest, avoid stress, medication prescribed','2026-05-31 12:42:40.885699','mediconnect-94f438e5',2);
/*!40000 ALTER TABLE `consultations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delegations`
--

DROP TABLE IF EXISTS `delegations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delegations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `end_date` date NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `start_date` date NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `coordinator_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg4c2dtu1orr6k7dda4h9w0h34` (`coordinator_id`),
  KEY `FK8w9j4xbd580ddhs5mipvepolu` (`doctor_id`),
  CONSTRAINT `FK8w9j4xbd580ddhs5mipvepolu` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`),
  CONSTRAINT `FKg4c2dtu1orr6k7dda4h9w0h34` FOREIGN KEY (`coordinator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delegations`
--

LOCK TABLES `delegations` WRITE;
/*!40000 ALTER TABLE `delegations` DISABLE KEYS */;
INSERT INTO `delegations` VALUES (1,_binary '','2026-06-04 11:40:56.795213','2026-06-20','Medical conference','2026-06-10','2026-06-04 11:40:56.795213',3,1);
/*!40000 ALTER TABLE `delegations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispensing_records`
--

DROP TABLE IF EXISTS `dispensing_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispensing_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `status` enum('DISPENSED','OUT_OF_STOCK','PARTIALLY_DISPENSED','PENDING','REJECTED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  `pharmacy_user_id` bigint NOT NULL,
  `prescription_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg557wwtq6k0pprif08aqwhgae` (`patient_id`),
  KEY `FK2tx9fjnxl9mdyrg05b76bcshi` (`pharmacy_user_id`),
  KEY `FKeqoa03dbv66sp6pbg2r29chv1` (`prescription_id`),
  CONSTRAINT `FK2tx9fjnxl9mdyrg05b76bcshi` FOREIGN KEY (`pharmacy_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKeqoa03dbv66sp6pbg2r29chv1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `FKg557wwtq6k0pprif08aqwhgae` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispensing_records`
--

LOCK TABLES `dispensing_records` WRITE;
/*!40000 ALTER TABLE `dispensing_records` DISABLE KEYS */;
INSERT INTO `dispensing_records` VALUES (1,'2026-06-02 12:17:07.283815','All medicines dispensed successfully',NULL,'PENDING','2026-06-02 12:17:07.283815',1,3,1);
/*!40000 ALTER TABLE `dispensing_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_profiles`
--

DROP TABLE IF EXISTS `doctor_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `accepting_new_patients` bit(1) NOT NULL,
  `approved` bit(1) NOT NULL,
  `average_rating` double DEFAULT NULL,
  `bio` varchar(1000) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `clinic_address` varchar(255) DEFAULT NULL,
  `clinic_name` varchar(255) DEFAULT NULL,
  `consultation_fee` double DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `experience_years` int DEFAULT NULL,
  `languages_spoken` varchar(255) DEFAULT NULL,
  `license_number` varchar(255) NOT NULL,
  `profile_photo_url` varchar(255) DEFAULT NULL,
  `qualification` varchar(255) NOT NULL,
  `specialization` enum('CARDIOLOGIST','DENTIST','DERMATOLOGIST','ENDOCRINOLOGIST','ENT_SPECIALIST','GENERAL_PHYSICIAN','GYNECOLOGIST','NEUROLOGIST','ONCOLOGIST','OPHTHALMOLOGIST','ORTHOPEDIC','PEDIATRICIAN','PSYCHIATRIST','RADIOLOGIST','UROLOGIST') NOT NULL,
  `total_ratings` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlfhrhro6r3sajly4jo7sfmsvw` (`license_number`),
  UNIQUE KEY `UKf2ac4saatw7tnup2kqa53oqkl` (`user_id`),
  CONSTRAINT `FKhrpk2q09sjwf9en18301dioyr` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_profiles`
--

LOCK TABLES `doctor_profiles` WRITE;
/*!40000 ALTER TABLE `doctor_profiles` DISABLE KEYS */;
INSERT INTO `doctor_profiles` VALUES (1,_binary '',_binary '',0,'Experienced cardiologist with 10 years of practice.','Pune','123 MG Road','Heart Care Clinic',800,'2026-05-27 12:07:38.974588',10,'English, Hindi, Marathi','MH-12345',NULL,'MBBS, MD Cardiology','CARDIOLOGIST',0,'2026-05-27 12:07:38.974588',2);
/*!40000 ALTER TABLE `doctor_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_schedules`
--

DROP TABLE IF EXISTS `doctor_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_schedules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `buffer_time_minutes` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `daily_patient_cap` int DEFAULT NULL,
  `day_of_week` enum('FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') NOT NULL,
  `end_time` time(6) NOT NULL,
  `slot_duration_minutes` int NOT NULL,
  `start_time` time(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhd5567m3o90306cy8mv9sge8s` (`doctor_id`),
  CONSTRAINT `FKhd5567m3o90306cy8mv9sge8s` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_schedules`
--

LOCK TABLES `doctor_schedules` WRITE;
/*!40000 ALTER TABLE `doctor_schedules` DISABLE KEYS */;
INSERT INTO `doctor_schedules` VALUES (1,_binary '',5,'2026-05-27 16:51:14.872019',15,'MONDAY','17:00:00.000000',30,'09:00:00.000000','2026-05-27 16:51:14.872019',1);
/*!40000 ALTER TABLE `doctor_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_vitals`
--

DROP TABLE IF EXISTS `health_vitals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_vitals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `abnormal` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `recorded_at` datetime(6) NOT NULL,
  `unit` varchar(255) NOT NULL,
  `value` double NOT NULL,
  `vital_type` enum('BLOOD_GLUCOSE','BLOOD_PRESSURE_DIASTOLIC','BLOOD_PRESSURE_SYSTOLIC','HEART_RATE','SPO2','TEMPERATURE','WEIGHT') NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnontel910nu87do44ef4h7b3k` (`patient_id`),
  CONSTRAINT `FKnontel910nu87do44ef4h7b3k` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_vitals`
--

LOCK TABLES `health_vitals` WRITE;
/*!40000 ALTER TABLE `health_vitals` DISABLE KEYS */;
INSERT INTO `health_vitals` VALUES (1,_binary '\0','2026-06-04 11:38:24.671445','After morning walk','2026-06-01 08:00:00.000000','bpm',72,'HEART_RATE',1);
/*!40000 ALTER TABLE `health_vitals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_blocks`
--

DROP TABLE IF EXISTS `leave_blocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_blocks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `leave_date` date NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6006jcfqeixubpaeiox7gg7ed` (`doctor_id`),
  CONSTRAINT `FK6006jcfqeixubpaeiox7gg7ed` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_blocks`
--

LOCK TABLES `leave_blocks` WRITE;
/*!40000 ALTER TABLE `leave_blocks` DISABLE KEYS */;
INSERT INTO `leave_blocks` VALUES (1,'2026-05-27 16:53:20.304728','2026-06-10','Personal leave',1);
/*!40000 ALTER TABLE `leave_blocks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_records`
--

DROP TABLE IF EXISTS `medical_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` enum('DISCHARGE_SUMMARY','INSURANCE','LAB_REPORT','OTHER','PRESCRIPTION','RADIOLOGY','VACCINATION') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `doctor_name` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `file_size` bigint NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `lab_name` varchar(255) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `original_file_name` varchar(255) NOT NULL,
  `record_date` date DEFAULT NULL,
  `share_token` varchar(255) DEFAULT NULL,
  `share_token_expiry` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe3g9v0pbec2843wd1rxeb0is3` (`patient_id`),
  CONSTRAINT `FKe3g9v0pbec2843wd1rxeb0is3` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_records`
--

LOCK TABLES `medical_records` WRITE;
/*!40000 ALTER TABLE `medical_records` DISABLE KEYS */;
INSERT INTO `medical_records` VALUES (1,'LAB_REPORT','2026-05-31 14:26:45.305242',_binary '\0',NULL,'Dr. Anil Sharma','e24d98a1-c5dd-4286-8cf1-e0dc9b4ebad5','C:\\Users\\KIIT\\Desktop\\MediConnect\\mediconnect-backend\\uploads\\medical-records\\1\\968e870c-8bde-4126-bb69-766ec4f2cb98_IMG-20250103-WA0003.jpg',63301,'image/jpeg','City Lab','Blood test Results','IMG-20250103-WA0003.jpg','2026-05-01','e7c67def-46d5-453b-9115-880e7099ebbe','2026-06-02 14:28:29.879491','2026-05-31 14:28:29.881087',1);
/*!40000 ALTER TABLE `medical_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `generic_name` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deep_link` varchar(255) DEFAULT NULL,
  `message` varchar(500) NOT NULL,
  `is_read` bit(1) NOT NULL DEFAULT b'0',
  `title` varchar(255) NOT NULL,
  `type` varchar(50) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'2026-06-01 21:16:05.857316','/appointments/4','Your appointment with Dr. Anil Sharma on 2026-07-01 at 11:15 is confirmed.',_binary '','Appointment Confirmed','APPOINTMENT_BOOKED',1),(2,'2026-06-01 21:16:05.865031','/appointments/4','New appointment booked by Yash Shah on 2026-07-01 at 11:15',_binary '\0','New Appointment','APPOINTMENT_BOOKED',2),(3,'2026-06-02 11:51:30.787160','/payments/1','Your payment of INR 991.2 for appointment MC-1780221436344 was successful.',_binary '\0','Payment Successful','PAYMENT_SUCCESS',1),(4,'2026-06-02 12:17:07.295169','/prescriptions/1','Your prescription RX-1780212760693 is ready for pickup.',_binary '\0','Prescription Update','PRESCRIPTION_READY',1);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_profiles`
--

DROP TABLE IF EXISTS `patient_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `allergies` varchar(500) DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `chronic_conditions` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `emergency_contact_name` varchar(255) DEFAULT NULL,
  `emergency_contact_phone` varchar(255) DEFAULT NULL,
  `emergency_contact_relation` varchar(255) DEFAULT NULL,
  `family_history` varchar(500) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER','PREFER_NOT_TO_SAY') DEFAULT NULL,
  `height_cm` double DEFAULT NULL,
  `past_surgeries` varchar(500) DEFAULT NULL,
  `profile_photo_url` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `weight_kg` double DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm1vq601k5agscsnei45j7bcv1` (`user_id`),
  CONSTRAINT `FK48bdvcabhgaa1bqphn9jijwn2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_profiles`
--

LOCK TABLES `patient_profiles` WRITE;
/*!40000 ALTER TABLE `patient_profiles` DISABLE KEYS */;
INSERT INTO `patient_profiles` VALUES (1,'Penicillin','B+','None','2026-05-25 21:25:45.861062','2000-05-15','Raj Shah','9876543211','Father','Diabetes','MALE',175,'None',NULL,'2026-05-25 21:25:45.861062',70.5,1);
/*!40000 ALTER TABLE `patient_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `gateway_order_id` varchar(255) DEFAULT NULL,
  `gateway_payment_id` varchar(255) DEFAULT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `payment_method` enum('CREDIT_CARD','DEBIT_CARD','NET_BANKING','UPI','WALLET') NOT NULL,
  `refund_amount` double DEFAULT NULL,
  `refund_reason` varchar(255) DEFAULT NULL,
  `refunded_at` datetime(6) DEFAULT NULL,
  `status` enum('FAILED','PARTIALLY_REFUNDED','PENDING','REFUNDED','SUCCESS') NOT NULL,
  `transaction_id` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `appointment_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlryndveuwa4k5qthti0pkmtlx` (`transaction_id`),
  UNIQUE KEY `UK2kxb37oip0md9ggekjbjmana4` (`appointment_id`),
  KEY `FK8wv0vpol7w56lno7dvlh3k782` (`patient_id`),
  CONSTRAINT `FK8wv0vpol7w56lno7dvlh3k782` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9a0odew03qao7nlbdsesrux5u` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,991.2,'2026-06-02 11:50:58.249069','INR',NULL,'ORD-6255BB8A','PAY-2B53FCE5','2026-06-02 11:51:30.752357','UPI',NULL,NULL,NULL,'SUCCESS','TXN-1780381258249','2026-06-02 11:51:30.769877',3,1);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacy_inventory`
--

DROP TABLE IF EXISTS `pharmacy_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacy_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `generic_name` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `medicine_name` varchar(255) NOT NULL,
  `reorder_threshold` int NOT NULL,
  `stock_quantity` int NOT NULL,
  `unit_price` double DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `pharmacy_user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK303xfk1wuhabejbhw32roso7q` (`pharmacy_user_id`),
  CONSTRAINT `FK303xfk1wuhabejbhw32roso7q` FOREIGN KEY (`pharmacy_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_inventory`
--

LOCK TABLES `pharmacy_inventory` WRITE;
/*!40000 ALTER TABLE `pharmacy_inventory` DISABLE KEYS */;
INSERT INTO `pharmacy_inventory` VALUES (1,_binary '','2026-06-02 12:16:10.455699','Acetylsalicylic acid','Bayer','Aspirin',20,100,5.5,'2026-06-02 12:16:10.455699',3);
/*!40000 ALTER TABLE `pharmacy_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_config`
--

DROP TABLE IF EXISTS `platform_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `config_key` varchar(255) NOT NULL,
  `config_value` varchar(1000) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj74hnmt8154yqsshcnrdhus5j` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_config`
--

LOCK TABLES `platform_config` WRITE;
/*!40000 ALTER TABLE `platform_config` DISABLE KEYS */;
INSERT INTO `platform_config` VALUES (1,'PAYMENT','platform.commission.percent','5.0','2026-06-03 12:50:36.985292','Platform commission percentage per consultation','2026-06-03 12:50:36.985292');
/*!40000 ALTER TABLE `platform_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_items`
--

DROP TABLE IF EXISTS `prescription_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `allergy_flagged` bit(1) NOT NULL,
  `dosage` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `frequency` varchar(255) DEFAULT NULL,
  `instructions` varchar(500) DEFAULT NULL,
  `medicine_name` varchar(255) NOT NULL,
  `prescription_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6uh7tdy2lv6sx34u1365acqsf` (`prescription_id`),
  CONSTRAINT `FK6uh7tdy2lv6sx34u1365acqsf` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_items`
--

LOCK TABLES `prescription_items` WRITE;
/*!40000 ALTER TABLE `prescription_items` DISABLE KEYS */;
INSERT INTO `prescription_items` VALUES (1,_binary '\0','75mg','30 days','Once daily','Take after food','Aspirin',1),(2,_binary '','500mg','7 days','Twice daily','Take with water','Penicillin',1);
/*!40000 ALTER TABLE `prescription_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `additional_instructions` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `prescription_number` varchar(255) DEFAULT NULL,
  `repeat_prescription` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `valid_until` varchar(255) DEFAULT NULL,
  `consultation_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj6varr98psv2onkoxks6jin14` (`consultation_id`),
  KEY `FKvvyaody4khe49e79fl0mvdld` (`doctor_id`),
  KEY `FK7sia9wnwh9j5hwrta9k8q0rbq` (`patient_id`),
  CONSTRAINT `FK7sia9wnwh9j5hwrta9k8q0rbq` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj6varr98psv2onkoxks6jin14` FOREIGN KEY (`consultation_id`) REFERENCES `consultations` (`id`),
  CONSTRAINT `FKvvyaody4khe49e79fl0mvdld` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,'Avoid alcohol','2026-05-31 13:02:40.693038','RX-1780212760693',_binary '\0','2026-05-31 13:02:40.918651',NULL,1,1,1);
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `doctor_response` varchar(500) DEFAULT NULL,
  `flagged` bit(1) NOT NULL,
  `hidden` bit(1) NOT NULL,
  `rating` int NOT NULL,
  `review_text` varchar(500) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `appointment_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKvroos1rdslok15k6q2go3p15` (`appointment_id`),
  KEY `FKatvl959pkpd9p36sa7pi92684` (`doctor_id`),
  KEY `FKp6ff3lit060ehcuyc5artangi` (`patient_id`),
  CONSTRAINT `FKatvl959pkpd9p36sa7pi92684` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`),
  CONSTRAINT `FKfhaj6kqx2pjpn6eambt0pa1nm` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `FKp6ff3lit060ehcuyc5artangi` FOREIGN KEY (`patient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_locked` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verification_token` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `failed_login_attempts` int NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `lock_time` datetime(6) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `role` enum('ADMIN','DOCTOR','PATIENT','PHARMACY') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '\0','2026-05-25 16:12:13.411851','yash@gmail.com','9022c8c6-9959-4107-8f85-d4f0d94675a4',_binary '',0,'Yash Shah',NULL,'$2a$10$oy8l308lT5jVDFvc0MiYUu5G4DhQHd3kZb.i1t/jyHkMG5QJLo/Di','9876543210','PATIENT','2026-06-02 12:51:12.891574'),(2,_binary '\0','2026-05-27 12:06:37.662045','anil@gmail.com','b87169b8-1477-42a4-ba3d-c2f3c45a0d67',_binary '',0,'Dr. Anil Sharma',NULL,'$2a$10$mUGhag1Og9uFq8iFDuTOFO6RguUYoPj4FimiAr9yp75AC975r2/9i','9876543212','DOCTOR','2026-05-27 12:06:37.662045'),(3,_binary '\0','2026-06-02 12:15:27.677433','citypharmacy@gmail.com','7152dc0d-3091-4906-b354-d6fed90e6149',_binary '',0,'CityMed Pharmacy',NULL,'$2a$10$iVxDbBYMQVqGS5B8AwBVD.duui/BiE9xWqZqTSWfhM6pWYC5g8DHG','9876543213','PHARMACY','2026-06-02 12:15:27.677433');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-04 11:51:54
