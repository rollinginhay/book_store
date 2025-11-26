-- book_store.campaign definition

CREATE TABLE `campaign`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `created_at`    datetime(6) NOT NULL,
    `enabled`       bit(1)       DEFAULT NULL,
    `note`          varchar(255) DEFAULT NULL,
    `updated_at`    datetime(6) DEFAULT NULL,
    `campaign_type` enum('FLAT_DISCOUNT','PERCENTAGE_DISCOUNT') DEFAULT NULL,
    `end_date`      date         DEFAULT NULL,
    `name`          varchar(255) DEFAULT NULL,
    `start_date`    date         DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.creator definition

CREATE TABLE `creator`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    `role`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.genre definition

CREATE TABLE `genre`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.publisher definition

CREATE TABLE `publisher`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.`role` definition

CREATE TABLE `role`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.series definition

CREATE TABLE `series`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.tag definition

CREATE TABLE `tag`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `enabled`    bit(1)       DEFAULT NULL,
    `note`       varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.test definition

CREATE TABLE `test`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.`user` definition

CREATE TABLE `user`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `created_at`    datetime(6) NOT NULL,
    `enabled`       bit(1)       DEFAULT NULL,
    `note`          varchar(255) DEFAULT NULL,
    `updated_at`    datetime(6) DEFAULT NULL,
    `address`       varchar(255) DEFAULT NULL,
    `email`         varchar(255) DEFAULT NULL,
    `is_oauth2user` bit(1)       DEFAULT NULL,
    `oauth2id`      varchar(255) DEFAULT NULL,
    `password`      varchar(255) DEFAULT NULL,
    `person_name`   varchar(255) DEFAULT NULL,
    `phone_number`  varchar(255) DEFAULT NULL,
    `username`      varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.book definition

CREATE TABLE `book`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6) NOT NULL,
    `enabled`      bit(1)       DEFAULT NULL,
    `note`         varchar(255) DEFAULT NULL,
    `updated_at`   datetime(6) DEFAULT NULL,
    `blurb`        varchar(255) DEFAULT NULL,
    `edition`      varchar(255) DEFAULT NULL,
    `image_url`    varchar(255) DEFAULT NULL,
    `language`     varchar(255) DEFAULT NULL,
    `published`    datetime(6) DEFAULT NULL,
    `title`        varchar(255) DEFAULT NULL,
    `publisher_id` bigint       DEFAULT NULL,
    `series_id`    bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY            `FKgtvt7p649s4x80y6f4842pnfq` (`publisher_id`),
    KEY            `FKqup0ss2jvfddhq6mt3mjm55t9` (`series_id`),
    CONSTRAINT `FKgtvt7p649s4x80y6f4842pnfq` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`),
    CONSTRAINT `FKqup0ss2jvfddhq6mt3mjm55t9` FOREIGN KEY (`series_id`) REFERENCES `series` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.book_creators definition

CREATE TABLE `book_creators`
(
    `books_id`    bigint NOT NULL,
    `creators_id` bigint NOT NULL,
    KEY           `FKp24in9drdfrrxp8wqbnl2jsje` (`creators_id`),
    KEY           `FKrgm62ne0i0wh88yca9pxq6ncf` (`books_id`),
    CONSTRAINT `FKp24in9drdfrrxp8wqbnl2jsje` FOREIGN KEY (`creators_id`) REFERENCES `creator` (`id`),
    CONSTRAINT `FKrgm62ne0i0wh88yca9pxq6ncf` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.book_detail definition

CREATE TABLE `book_detail`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `created_at`     datetime(6) NOT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `note`           varchar(255) DEFAULT NULL,
    `updated_at`     datetime(6) DEFAULT NULL,
    `book_condition` varchar(255) DEFAULT NULL,
    `book_format`    varchar(255) DEFAULT NULL,
    `dimensions`     varchar(255) DEFAULT NULL,
    `isbn`           varchar(255) DEFAULT NULL,
    `print_length`   bigint       DEFAULT NULL,
    `sale_price`     bigint       DEFAULT NULL,
    `stock`          bigint       DEFAULT NULL,
    `supply_price`   bigint       DEFAULT NULL,
    `book_id`        bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `FKl1hmgccsvfwcxhem3qw6l7gpm` (`book_id`),
    CONSTRAINT `FKl1hmgccsvfwcxhem3qw6l7gpm` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.book_genres definition

CREATE TABLE `book_genres`
(
    `books_id`  bigint NOT NULL,
    `genres_id` bigint NOT NULL,
    KEY         `FKkemwddl6cxkebe21lsqa2ob4q` (`genres_id`),
    KEY         `FKlbdkit5k0gr9g1w5l791qcamg` (`books_id`),
    CONSTRAINT `FKkemwddl6cxkebe21lsqa2ob4q` FOREIGN KEY (`genres_id`) REFERENCES `genre` (`id`),
    CONSTRAINT `FKlbdkit5k0gr9g1w5l791qcamg` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.book_tags definition

CREATE TABLE `book_tags`
(
    `books_id` bigint NOT NULL,
    `tags_id`  bigint NOT NULL,
    KEY        `FKsky6wumpk8q486i2lecduct0d` (`tags_id`),
    KEY        `FKmolnjthi6racnguu0nhrwx5iw` (`books_id`),
    CONSTRAINT `FKmolnjthi6racnguu0nhrwx5iw` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`),
    CONSTRAINT `FKsky6wumpk8q486i2lecduct0d` FOREIGN KEY (`tags_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.campaign_detail definition

CREATE TABLE `campaign_detail`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `created_at`     datetime(6) NOT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `note`           varchar(255) DEFAULT NULL,
    `updated_at`     datetime(6) DEFAULT NULL,
    `value` double DEFAULT NULL,
    `book_detail_id` bigint       DEFAULT NULL,
    `campaign_id`    bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `FKpjk8vymdghyebeq5wuko8cur` (`book_detail_id`),
    KEY              `FKdy46wy7qheoeajmh3skxhixtf` (`campaign_id`),
    CONSTRAINT `FKdy46wy7qheoeajmh3skxhixtf` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
    CONSTRAINT `FKpjk8vymdghyebeq5wuko8cur` FOREIGN KEY (`book_detail_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.cart_detail definition

CREATE TABLE `cart_detail`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `created_at`     datetime(6) NOT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `note`           varchar(255) DEFAULT NULL,
    `updated_at`     datetime(6) DEFAULT NULL,
    `amount`         bigint       DEFAULT NULL,
    `book_detail_id` bigint       DEFAULT NULL,
    `user_id`        bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `FKjrcme6q8ajo40npg0xhkknho7` (`book_detail_id`),
    KEY              `FKhqem60okkngoihvdljfclmw48` (`user_id`),
    CONSTRAINT `FKhqem60okkngoihvdljfclmw48` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKjrcme6q8ajo40npg0xhkknho7` FOREIGN KEY (`book_detail_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.genre_closure definition

CREATE TABLE `genre_closure`
(
    `depth`         bigint DEFAULT NULL,
    `descendant_id` bigint NOT NULL,
    `ancestor_id`   bigint NOT NULL,
    PRIMARY KEY (`ancestor_id`, `descendant_id`),
    KEY             `FK5cue3qxyxj6n7elg13k9xky4h` (`descendant_id`),
    CONSTRAINT `FK5cue3qxyxj6n7elg13k9xky4h` FOREIGN KEY (`descendant_id`) REFERENCES `genre` (`id`),
    CONSTRAINT `FKngo8stvvl123732t8tp3uru9a` FOREIGN KEY (`ancestor_id`) REFERENCES `genre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.receipt definition

CREATE TABLE `receipt`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `created_at`       datetime(6) NOT NULL,
    `enabled`          bit(1)       DEFAULT NULL,
    `note`             varchar(255) DEFAULT NULL,
    `updated_at`       datetime(6) DEFAULT NULL,
    `customer_address` varchar(255) DEFAULT NULL,
    `customer_name`    varchar(255) DEFAULT NULL,
    `customer_phone`   varchar(255) DEFAULT NULL,
    `discount` double DEFAULT NULL,
    `grand_total` double DEFAULT NULL,
    `has_shipping`     bit(1)       DEFAULT NULL,
    `order_status`     enum('AUTHORIZED','CANCELLED','FAILED','IN_TRANSIT','PAID','PENDING','REFUNDED') DEFAULT NULL,
    `order_type`       enum('DIRECT','ONLINE') DEFAULT NULL,
    `payment_date`     datetime(6) DEFAULT NULL,
    `service_cost` double DEFAULT NULL,
    `shipping_id`      varchar(255) DEFAULT NULL,
    `shipping_service` varchar(255) DEFAULT NULL,
    `sub_total` double DEFAULT NULL,
    `tax` double DEFAULT NULL,
    `customer_id`      bigint       DEFAULT NULL,
    `employee_id`      bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                `FKkfphmv967orgmkx5hkbpitf3q` (`customer_id`),
    KEY                `FKjxj56bwfq337r1s3tai3cl1sl` (`employee_id`),
    CONSTRAINT `FKjxj56bwfq337r1s3tai3cl1sl` FOREIGN KEY (`employee_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKkfphmv967orgmkx5hkbpitf3q` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.receipt_detail definition

CREATE TABLE `receipt_detail`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6) NOT NULL,
    `enabled`      bit(1)       DEFAULT NULL,
    `note`         varchar(255) DEFAULT NULL,
    `updated_at`   datetime(6) DEFAULT NULL,
    `price_per_unit` double DEFAULT NULL,
    `quantity`     bigint       DEFAULT NULL,
    `book_copy_id` bigint       DEFAULT NULL,
    `receipt_id`   bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY            `FKbwohckwtlh3eyv1e01ystbtkq` (`book_copy_id`),
    KEY            `FK4qgqei3yfmquqnhwutybgv0dj` (`receipt_id`),
    CONSTRAINT `FK4qgqei3yfmquqnhwutybgv0dj` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`id`),
    CONSTRAINT `FKbwohckwtlh3eyv1e01ystbtkq` FOREIGN KEY (`book_copy_id`) REFERENCES `book_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.review definition

CREATE TABLE `review`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `created_at`  datetime(6) NOT NULL,
    `enabled`     bit(1)       DEFAULT NULL,
    `note`        varchar(255) DEFAULT NULL,
    `updated_at`  datetime(6) DEFAULT NULL,
    `comment`     varchar(255) DEFAULT NULL,
    `rating`      int          DEFAULT NULL,
    `book_id`     bigint       DEFAULT NULL,
    `reviewer_id` bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY           `FK70yrt09r4r54tcgkrwbeqenbs` (`book_id`),
    KEY           `FKt58e9mdgxpl7j90ketlaosmx4` (`reviewer_id`),
    CONSTRAINT `FK70yrt09r4r54tcgkrwbeqenbs` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
    CONSTRAINT `FKt58e9mdgxpl7j90ketlaosmx4` FOREIGN KEY (`reviewer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.user_roles definition

CREATE TABLE `user_roles`
(
    `user_id`  bigint NOT NULL,
    `roles_id` bigint NOT NULL,
    KEY        `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
    KEY        `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
    CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- book_store.payment_detail definition

CREATE TABLE `payment_detail`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6) NOT NULL,
    `enabled`      bit(1)       DEFAULT NULL,
    `note`         varchar(255) DEFAULT NULL,
    `updated_at`   datetime(6) DEFAULT NULL,
    `amount` double DEFAULT NULL,
    `payment_type` enum('CASH','TRANSFER') DEFAULT NULL,
    `provider`     varchar(255) DEFAULT NULL,
    `provider_id`  varchar(255) DEFAULT NULL,
    `receipt_id`   bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKdyyskc81r8gy2lsa8qota6ciq` (`receipt_id`),
    CONSTRAINT `FKsda90npndrm2jqloujabmi2` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;