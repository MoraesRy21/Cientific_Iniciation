/*
 * Author Iarley Moraes
 * Date: 06/02/2021
 */
 
 CREATE DATABASE IF NOT EXISTS `sslfb`;
 
 USE `sslfb`;

/*Table structure for table `autenticacao` */

DROP TABLE IF EXISTS `autenticacao`;

CREATE TABLE `autenticacao` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `versao` VARCHAR(8) NOT NULL,
  `descricao` TEXT,
  `data` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `autenticacao` (`id`, `versao`, `descricao`, `data`) VALUES('1','2.1.0','Modification','2020-09-14 19:51:20');

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(255) DEFAULT NULL,
  `senha` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `login` VALUES(DEFAULT, 'admin', 'admin');

/*Table structure for table `pessoa` */

DROP TABLE IF EXISTS `pessoa`;

CREATE TABLE `pessoa` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `sobrenome` VARCHAR(45) NOT NULL,
  `nascimento` DATE DEFAULT NULL,
  `email` VARCHAR(150) DEFAULT NULL,
  `telefone` VARCHAR(25) DEFAULT NULL,
  `CPF` VARCHAR(11) NOT NULL,
  `RG` VARCHAR(10) DEFAULT NULL,
  `alunoMatricula` VARCHAR(15) DEFAULT NULL,
  `alunoCurso` VARCHAR(255) DEFAULT NULL,
  `alunoAnoMatricula` INT(11) DEFAULT NULL,
  `alunoAnoSaida` INT(11) DEFAULT NULL,
  `funcionarioCodigo` VARCHAR(15) DEFAULT NULL,
  `funcionarioDataAdmissao` DATE DEFAULT NULL,
  `funcionarioDataDemissao` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CPF` (`CPF`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `log` */

DROP TABLE IF EXISTS `log`;

CREATE TABLE `log` (
  `id_pessoa` INT(11) NOT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dataEntrada` DATETIME NOT NULL,
  `dataSaida` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_pessoa` (`id_pessoa`),
  CONSTRAINT `log_ibfk_1` FOREIGN KEY (`id_pessoa`) REFERENCES `pessoa` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE DATABASE IF NOT EXISTS`sslfb_data`;

USE `sslfb_data`;

/*Table structure for table `facial_data` */

DROP TABLE IF EXISTS `facial_data`;

CREATE TABLE `facial_data` (
  `id_pessoa` INT(11) NOT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `arquivo` MEDIUMBLOB,
  `tipoObjeto` VARCHAR(255) NOT NULL,
  `dataRegistro` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_pessoa` (`id_pessoa`),
  CONSTRAINT `facial_data_ibfk_1` FOREIGN KEY (`id_pessoa`) REFERENCES `sslfb`.`pessoa` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/* Trigger structure for table `facial_data` */

DELIMITER $$

DROP TRIGGER IF EXISTS `trigger_facial_data_registro` $$

CREATE DEFINER = 'root'@'localhost' TRIGGER `trigger_facial_data_registro` 
	BEFORE INSERT ON `facial_data` FOR EACH ROW SET new.`dataRegistro` = NOW() $$

DELIMITER ;
