SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
    `user_id` bigint NOT NULL AUTO_INCREMENT,
    `full_name` varchar(255) NOT NULL,
    `username` varchar(50) NOT NULL UNIQUE,
    `password` varchar(255) NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `experience`;
CREATE TABLE `experience`  (
    `experience_id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `event_date` date NOT NULL,
    -- 4326 = Spatial Reference ID for surface of Earth (WGS 84)
    `location` point NOT NULL SRID 4326,
    `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    -- id used to identify locations in Google Maps Places API
    `place_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
    -- text because URL technically doesn't have max length
    `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
    -- number of ratings for this experience
    `rating_cnt` int NOT NULL,
    -- sum of all rating scores, divide by count to get average rating
    -- cache these values to avoid expensive aggregate queries each time an experience's rating is accessed
    `rating_sum` int NOT NULL,
    `user_id` bigint NOT NULL,
    -- all date/time values are implicitly UTC, database should be UTC
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NULL DEFAULT NULL,
    -- soft-delete system, entries aren't physically deleted from table
    `deleted_at` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`experience_id`) USING BTREE,
    -- index to enable efficient geospatial searches, such as finding Experiences near some reference point
    SPATIAL INDEX `location_spt_idx`(`location`),
    -- index to enable keyword search
    FULLTEXT INDEX `title_description_address_ft_idx`(`title`, `description`, `address`),
    INDEX `user_id_fk_idx`(`user_id`) USING BTREE,
    CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `trip`;
CREATE TABLE `trip` (
    `trip_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `trip_title` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    `deleted_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`trip_id`) USING BTREE,
    CONSTRAINT `user_id_fk_trip` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- join table
DROP TABLE IF EXISTS `trip_experience`;
CREATE TABLE `trip_experience` (
    `trip_id` BIGINT NOT NULL,
    `experience_id` BIGINT NOT NULL,
    PRIMARY KEY (`trip_id`, `experience_id`) USING BTREE,
    CONSTRAINT `trip_fk` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`trip_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
