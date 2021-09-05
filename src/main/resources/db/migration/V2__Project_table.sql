CREATE TABLE `project`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `name`        varchar(200) DEFAULT NULL,
    `description` varchar(200) DEFAULT NULL,
    `status`      varchar(60) NOT NULL,
    'path'        varchar(200) DEFAULT NULL,
    `url`         varchar(200) DEFAULT NULL,
    PRIMARY KEY (`id`)
);