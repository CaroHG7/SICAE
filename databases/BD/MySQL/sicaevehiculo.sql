
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marca` (
  `idMarca` int NOT NULL AUTO_INCREMENT,
  `marca` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estatus` bit(1) NOT NULL,
  PRIMARY KEY (`idMarca`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `marca` WRITE;
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
INSERT INTO `marca` VALUES (1,'Toyota',_binary ''),(2,'Honda',_binary ''),(3,'Nissan',_binary ''),(4,'Chevrolet',_binary ''),(5,'Ford',_binary ''),(6,'Volkswagen',_binary ''),(7,'Mazda',_binary ''),(8,'Kia',_binary ''),(9,'Hyundai',_binary ''),(10,'BMW',_binary ''),(11,'Mercedes-Benz',_binary ''),(12,'Audi',_binary ''),(13,'Suzuki',_binary ''),(14,'Yamaha',_binary ''),(15,'Italika',_binary '');
/*!40000 ALTER TABLE `marca` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `modelo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modelo` (
  `idModelo` int NOT NULL AUTO_INCREMENT,
  `idMarca` int NOT NULL,
  `modelo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `estatus` bit(1) NOT NULL,
  PRIMARY KEY (`idModelo`) USING BTREE,
  KEY `idMarca` (`idMarca`) USING BTREE,
  CONSTRAINT `modelo_ibfk_1` FOREIGN KEY (`idMarca`) REFERENCES `marca` (`idMarca`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `modelo` WRITE;
/*!40000 ALTER TABLE `modelo` DISABLE KEYS */;
INSERT INTO `modelo` VALUES (1,1,'Corolla',_binary ''),(2,1,'Yaris',_binary ''),(3,1,'Hilux',_binary ''),(4,2,'Civic',_binary ''),(5,2,'Accord',_binary ''),(6,2,'CR-V',_binary ''),(7,3,'Sentra',_binary ''),(8,3,'Versa',_binary ''),(9,3,'NP300',_binary ''),(10,4,'Aveo',_binary ''),(11,4,'Onix',_binary ''),(12,4,'Tracker',_binary ''),(13,5,'Fiesta',_binary ''),(14,5,'Focus',_binary ''),(15,5,'Ranger',_binary ''),(16,6,'Jetta',_binary ''),(17,6,'Golf',_binary ''),(18,6,'Vento',_binary ''),(19,7,'Mazda 2',_binary ''),(20,7,'Mazda 3',_binary ''),(21,7,'CX-5',_binary ''),(22,8,'Rio',_binary ''),(23,8,'Forte',_binary ''),(24,8,'Sportage',_binary ''),(25,9,'Accent',_binary ''),(26,9,'Elantra',_binary ''),(27,9,'Tucson',_binary ''),(28,10,'Serie 3',_binary ''),(29,10,'X3',_binary ''),(30,11,'Clase C',_binary ''),(31,11,'GLA',_binary ''),(32,12,'A3',_binary ''),(33,12,'Q5',_binary ''),(34,13,'Swift',_binary ''),(35,13,'Vitara',_binary ''),(36,14,'FZ',_binary ''),(37,14,'R15',_binary ''),(38,15,'FT150',_binary ''),(39,15,'DM200',_binary '');
/*!40000 ALTER TABLE `modelo` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo` (
  `idVehiculo` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `claveVehiculo` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `idModelo` int NOT NULL,
  `placa` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `anio` int NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `estatus` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`idVehiculo`) USING BTREE,
  KEY `idModelo` (`idModelo`) USING BTREE,
  CONSTRAINT `vehiculo_ibfk_1` FOREIGN KEY (`idModelo`) REFERENCES `modelo` (`idModelo`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `vehiculo` WRITE;
/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;
INSERT INTO `vehiculo` VALUES (1,2,'VEH001',1,'ABC1234','Negro',2020,'Vehiculo de prueba',_binary '');
/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `vehiculofullinfo`;
/*!50001 DROP VIEW IF EXISTS `vehiculofullinfo`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vehiculofullinfo` AS SELECT 
 1 AS `idVehiculo`,
 1 AS `idUsuario`,
 1 AS `claveVehiculo`,
 1 AS `idMarca`,
 1 AS `marca`,
 1 AS `idModelo`,
 1 AS `modelo`,
 1 AS `placa`,
 1 AS `color`,
 1 AS `anio`,
 1 AS `descripcion`,
 1 AS `estatus`*/;
SET character_set_client = @saved_cs_client;
/*!50001 DROP VIEW IF EXISTS `vehiculofullinfo`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vehiculofullinfo` AS select `v`.`idVehiculo` AS `idVehiculo`,`v`.`idUsuario` AS `idUsuario`,`v`.`claveVehiculo` AS `claveVehiculo`,`m1`.`idMarca` AS `idMarca`,`m1`.`marca` AS `marca`,`v`.`idModelo` AS `idModelo`,`m`.`modelo` AS `modelo`,`v`.`placa` AS `placa`,`v`.`color` AS `color`,`v`.`anio` AS `anio`,`v`.`descripcion` AS `descripcion`,`v`.`estatus` AS `estatus` from ((`vehiculo` `v` join `modelo` `m` on((`m`.`idModelo` = `v`.`idModelo`))) join `marca` `m1` on((`m1`.`idMarca` = `m`.`idMarca`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

