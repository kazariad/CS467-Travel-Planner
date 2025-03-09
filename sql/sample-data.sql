SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of experience
-- ----------------------------
-- NOTE: mysql-dump doesn't generate correct insert statements for geospatial fields, must correct manually
-- Should be in format ST_PointFromText('POINT(latitude longitude)', 4326)
-- eg: INSERT INTO `experience` VALUES (NULL, 'Title', 'Description', '2025-01-01', ST_PointFromText('POINT(13.0538388897379 80.2769524362226)', 4326), 'Address', 'Place ID', 'https://image.url', 0, 0, 1, '2025-01-01 00:00:00.000000', NULL, NULL);
-- Place ID lookup:
-- https://developers.google.com/maps/documentation/places/web-service/place-id#find-id
--
INSERT INTO `experience` VALUES (1, 'WIP Title', NULL, '2024-11-30', ST_PointFromText('POINT(0 0)', 4326), NULL, NULL, NULL, 0, 0, 1, '2024-12-01 02:38:38.000000', NULL, NULL);
INSERT INTO `experience` VALUES (2, 'Breathtaking Views and Vibrant Atmosphere', 'Venice Beach oceanfront is a must-visit! The views of the Pacific are stunning, especially at sunset. The boardwalk is lively with street performers, unique shops, and plenty of food options. The beach itself is clean, great for a relaxing stroll or a game of volleyball. While it can get crowded, the energy is part of the charm. Perfect for people-watching, biking, or just soaking in the California vibes. Highly recommended for anyone looking to experience the heart of LA’s beach culture!', '2024-12-18', ST_PointFromText('POINT(33.986267981122 -118.473022732421)', 4326), '1701 Ocean Front Walk, Venice, CA 90291', 'ChIJ6WUUvrm6woARAwxatknJo3E', 'https://drupal-prod.visitcalifornia.com/sites/default/files/styles/fixed_300/public/VC_California101_VeniceBeach_Stock_RF_638340372_1280x640.jpg.webp?itok=vHd_tD-I', 1, 5, 1, '2024-12-18 21:51:05.000000', NULL, NULL);
INSERT INTO `experience` VALUES (3, 'A Hidden Gem for Seafood Lovers', 'This fish store in Tokyo is a paradise for seafood enthusiasts! The selection is incredibly fresh, with a wide variety of fish and shellfish, perfect for sushi or grilling. The staff is knowledgeable and helpful, offering great recommendations. The store is clean and well-organized, making shopping a pleasant experience. Whether you\'re a local or a visitor looking to try top-quality seafood, this place is a must-visit. The flavors of Tokyo’s ocean bounty don’t get much better than this!', '2024-12-31', ST_PointFromText('POINT(35.5470864310433 139.720328437314)', 4326), '2 Chome-19-3 Minamirokugo, Ota City, Tokyo 144-0045, Japan', 'ChIJww9-v-9gGGARpcNwzMKncRg', 'https://www.japan-guide.com/g20/3754_01.jpg', 9, 44, 1, '2025-01-01 21:51:06.000000', '2025-01-21 21:51:07.000000', NULL);
INSERT INTO `experience` VALUES (4, 'Comida incrível, drinks deliciosos e ótima atmosfera! 🍹🎶', 'Este bar e restaurante no Brasil é simplesmente imperdível! O cardápio combina pratos tradicionais brasileiros com toques modernos, todos cheios de sabor. As caipirinhas são espetaculares! 🍋✨ O ambiente é super acolhedor, com música animada e um atendimento simpático. Seja para um jantar tranquilo ou uma noite divertida, este lugar tem tudo. Perfeito para quem quer aproveitar o melhor da gastronomia e da vida noturna brasileira! 🇧🇷🔥', '2025-01-06', ST_PointFromText('POINT(-7.22743565668937 -35.8960075852369)', 4326), 'R. José Elpídio da Costa Monteiro, 193 - São José, Campina Grande - PB, 58400-424, Brazil', 'ChIJzVeKKT8erAcR1d9qZMEJbi4', 'https://www.e-architect.com/wp-content/uploads/2023/10/bar-do-mussum-rio-de-janeiro-brazil-s261023-m4.jpg', 99, 457, 2, '2025-01-21 21:51:07.000000', '2025-02-02 21:51:07.000000', NULL);
INSERT INTO `experience` VALUES (5, 'A Serene and Spiritual Experience', 'This Hindu temple in India is a place of peace and devotion. The architecture is stunning, with intricate carvings and vibrant colors that reflect India’s rich cultural heritage. The atmosphere is serene, filled with the soothing sounds of prayers and bells. Visitors are welcomed warmly, and the spiritual energy is truly uplifting. Whether you seek blessings, meditation, or simply wish to admire the beauty of the temple, this sacred place is a must-visit for a deeply enriching experience. 🕉️🙏', '2025-01-15', ST_PointFromText('POINT(13.0538388897379 80.2769524362226)', 4326), 'Car St, Narayana Krishnaraja Puram, Triplicane, Chennai, Tamil Nadu 600005, India', 'ElFDYXIgU3QsIE5hcmF5YW5hIEtyaXNobmFyYWphIFB1cmFtLCBUcmlwbGljYW5lLCBDaGVubmFpLCBUYW1pbCBOYWR1IDYwMDAwNSwgSW5kaWEiLiosChQKEgmXl8ninWhSOhHdkEp0ZJV0-RIUChIJMRvDDJ5oUjoRLNs_rqqSgZ4', 'https://upload.wikimedia.org/wikipedia/commons/0/0f/Tiruvallikeni1.jpg', 999, 4967, 2, '2025-02-02 21:51:07.000000', NULL, NULL);

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Joe Smith', 'jsmith57', '{noop}password', '2024-12-30 02:03:52.000000', NULL);
INSERT INTO `user` VALUES (2, 'Luís Roberto Barroso', 'luisbrasil', '{bcrypt}$2a$10$r.5nrugrtMFGWVxfXSom2enSMi4/MBrntK21m9ypSr.39691b8SYq', '2025-01-20 13:59:20.000000', NULL);

-- ----------------------------
-- Records of trips
-- ----------------------------
INSERT INTO `trip` (`trip_id`, `user_id`, `trip_title`, `start_date`, `end_date`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 1, 'California Adventure', '2024-12-01', '2024-12-18', '2024-11-30 00:00:00', NULL, NULL),
(2, 1, 'Seafood Tour in Tokyo', '2024-12-31', '2025-01-06', '2024-12-30 00:00:00', NULL, NULL),
(3, 2, 'Brazilian Nights', '2025-01-06', '2025-01-21', '2025-01-05 00:00:00', NULL, NULL),
(4, 2, 'Spiritual Retreat in India', '2025-01-15', '2025-01-20', '2025-01-14 00:00:00', NULL, NULL);
INSERT INTO `trip_experience` (`trip_id`, `experience_id`) VALUES
-- California Adventure
(1, 1),
(1, 2),
-- Seafood Tour in Tokyo
(2, 3),
-- Brazilian Nights
(3, 4),
-- Spiritual Retreat in India
(4, 5);

SET FOREIGN_KEY_CHECKS = 1;
