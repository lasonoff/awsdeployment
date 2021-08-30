CREATE TABLE `user`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT,
    `created`    datetime     DEFAULT NULL,
    `first_name` varchar(60)  DEFAULT NULL,
    `last_name`  varchar(60)  DEFAULT NULL,
    `login`      varchar(20) NOT NULL,
    `password`   varchar(255) DEFAULT NULL,
    `role`       varchar(20) NOT NULL,
    `status`     varchar(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_login` (`login`)
);