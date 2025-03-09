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
INSERT INTO `experience` VALUES (2, 'Breathtaking Views and Vibrant Atmosphere', 'Venice Beach oceanfront is a must-visit! The views of the Pacific are stunning, especially at sunset. The boardwalk is lively with street performers, unique shops, and plenty of food options. The beach itself is clean, great for a relaxing stroll or a game of volleyball. While it can get crowded, the energy is part of the charm. Perfect for people-watching, biking, or just soaking in the California vibes. Highly recommended for anyone looking to experience the heart of LA''s beach culture!', '2024-12-18', ST_PointFromText('POINT(33.986267981122 -118.473022732421)', 4326), '1701 Ocean Front Walk, Venice, CA 90291', 'ChIJ6WUUvrm6woARAwxatknJo3E', 'https://drupal-prod.visitcalifornia.com/sites/default/files/styles/fixed_300/public/VC_California101_VeniceBeach_Stock_RF_638340372_1280x640.jpg.webp?itok=vHd_tD-I', 0, 0, 3, '2024-12-18 21:51:05.000000', NULL, NULL);
INSERT INTO `experience` VALUES (3, 'A Hidden Gem for Seafood Lovers', 'This fish store in Tokyo is a paradise for seafood enthusiasts! The selection is incredibly fresh, with a wide variety of fish and shellfish, perfect for sushi or grilling. The staff is knowledgeable and helpful, offering great recommendations. The store is clean and well-organized, making shopping a pleasant experience. Whether you''re a local or a visitor looking to try top-quality seafood, this place is a must-visit. The flavors of Tokyo''s ocean bounty don''t get much better than this!', '2024-12-31', ST_PointFromText('POINT(35.5470864310433 139.720328437314)', 4326), '2 Chome-19-3 Minamirokugo, Ota City, Tokyo 144-0045, Japan', 'ChIJww9-v-9gGGARpcNwzMKncRg', 'https://www.japan-guide.com/g20/3754_01.jpg', 1, 5, 5, '2025-01-01 21:51:06.000000', '2025-01-21 21:51:07.000000', NULL);
INSERT INTO `experience` VALUES (4, 'Comida incrível, drinks deliciosos e ótima atmosfera! 🍹🎶', 'Este bar e restaurante no Brasil é simplesmente imperdível! O cardápio combina pratos tradicionais brasileiros com toques modernos, todos cheios de sabor. As caipirinhas são espetaculares! 🍋✨ O ambiente é super acolhedor, com música animada e um atendimento simpático. Seja para um jantar tranquilo ou uma noite divertida, este lugar tem tudo. Perfeito para quem quer aproveitar o melhor da gastronomia e da vida noturna brasileira! 🇧🇷🔥', '2025-01-06', ST_PointFromText('POINT(-7.22743565668937 -35.8960075852369)', 4326), 'R. José Elpídio da Costa Monteiro, 193 - São José, Campina Grande - PB, 58400-424, Brazil', 'ChIJzVeKKT8erAcR1d9qZMEJbi4', 'https://www.e-architect.com/wp-content/uploads/2023/10/bar-do-mussum-rio-de-janeiro-brazil-s261023-m4.jpg', 2, 7, 2, '2025-01-21 21:51:07.000000', '2025-02-02 21:51:07.000000', NULL);
INSERT INTO `experience` VALUES (5, 'A Serene and Spiritual Experience', 'This Hindu temple in India is a place of peace and devotion. The architecture is stunning, with intricate carvings and vibrant colors that reflect India’s rich cultural heritage. The atmosphere is serene, filled with the soothing sounds of prayers and bells. Visitors are welcomed warmly, and the spiritual energy is truly uplifting. Whether you seek blessings, meditation, or simply wish to admire the beauty of the temple, this sacred place is a must-visit for a deeply enriching experience. 🕉️🙏', '2025-01-15', ST_PointFromText('POINT(13.0538388897379 80.2769524362226)', 4326), 'Car St, Narayana Krishnaraja Puram, Triplicane, Chennai, Tamil Nadu 600005, India', 'ElFDYXIgU3QsIE5hcmF5YW5hIEtyaXNobmFyYWphIFB1cmFtLCBUcmlwbGljYW5lLCBDaGVubmFpLCBUYW1pbCBOYWR1IDYwMDAwNSwgSW5kaWEiLiosChQKEgmXl8ninWhSOhHdkEp0ZJV0-RIUChIJMRvDDJ5oUjoRLNs_rqqSgZ4', 'https://upload.wikimedia.org/wikipedia/commons/0/0f/Tiruvallikeni1.jpg', 3, 14, 4, '2025-02-02 21:51:07.000000', NULL, NULL);
INSERT INTO `experience` VALUES (6, 'Stunning Coastal Views and Peaceful Escape', 'Point Dume in Malibu is a breathtaking spot that offers stunning ocean views, rugged cliffs, and a serene atmosphere. The short hike to the top is well worth it, rewarding you with panoramic vistas of the Pacific, dolphins, and even migrating whales during the season. The beach below is perfect for a peaceful stroll or sunbathing away from crowds. Whether you''re into hiking, photography, or just relaxing by the sea, Point Dume is a must-visit gem along the California coast!', '2023-06-15', ST_PointFromText('POINT(34.001297776493786 -118.80645701320736)', 4326), 'Cliffside Dr &, Birdview Ave, Malibu, CA 90265', 'ChIJCVaxHgQZ6IARtqUavCkgOSM', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/View_north_from_Point_Dume_State_Beach.jpg/1280px-View_north_from_Point_Dume_State_Beach.jpg', 199, 954, 9, '2023-06-18 21:41:05.5320000', NULL, NULL);
INSERT INTO `experience` VALUES (7, 'A Must-Visit for Art and Architecture Lovers', 'The Getty Center in Los Angeles is an incredible blend of art, architecture, and breathtaking views. The museum houses an impressive collection of paintings, sculptures, and decorative arts, with masterpieces from Van Gogh, Monet, and Rembrandt. The modern architecture and beautifully landscaped gardens add to the experience. The hilltop location provides stunning panoramic views of the city. Best of all, admission is free. Whether you''re an art enthusiast or just looking for a unique cultural experience, the Getty Center is a must-visit.', '2024-10-21', ST_PointFromText('POINT(34.07739637188053 -118.47327143111012)', 4326), '1200 Getty Center Dr, Los Angeles, CA 90049', 'ChIJbzYnQte8woARJaqqFVpKeNo', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/26/Getty_Center_patio.jpg/1280px-Getty_Center_patio.jpg', 235, 1097, 4, '2024-11-05 11:21:05.040000', NULL, NULL);
INSERT INTO `experience` VALUES (8, 'A Fun and Educational Experience for All Ages', 'The Los Angeles Zoo is a great place to explore wildlife and enjoy a day surrounded by nature. Home to a diverse range of animals, from majestic lions to playful primates, the zoo offers well-maintained exhibits and informative displays. The botanical gardens add an extra touch of beauty, and the layout makes it easy to navigate. While some areas can get crowded, it’s a fantastic spot for families, animal lovers, and anyone looking for an enjoyable and educational experience in LA.', '2024-05-26', ST_PointFromText('POINT(34.14904672627733 -118.28826874228422)', 4326), '5333 Zoo Dr, Los Angeles, CA 90027', 'ChIJ1cdRl2XAwoAR_WUoKRXLQTY', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Jaguaratthezoo.jpg/1129px-Jaguaratthezoo.jpg', 5, 23, 5, '2024-06-10 01:31:25.000000', NULL, NULL);
INSERT INTO `experience` VALUES (9, 'Breathtaking Scenic Drive and Outdoor Adventure', 'Angeles Crest Highway offers one of the most stunning scenic drives in Los Angeles, winding through the San Gabriel Mountains with breathtaking views at every turn. Whether you''re into hiking, camping, or simply enjoying a peaceful escape from the city, this route has something for everyone. There are plenty of trails, picnic spots, and overlooks to stop and take in the beauty. The air is fresh, the roads are well-maintained, and during winter, you might even see snow. A must-visit for nature lovers and adventure seekers.', '2025-02-27', ST_PointFromText('POINT(34.26464271746633 -118.174113929381)', 4326), '701 Angeles Crest Scenic Bywy, Tujunga, CA 91042', 'ChIJLXxIxf7pwoARdmE__Amp6eo', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Angeles_Crest_Highway_2019.jpg/1280px-Angeles_Crest_Highway_2019.jpg', 1, 5, 3, '2025-03-01 13:26:13.000000', NULL, NULL);
INSERT INTO `experience` VALUES (10, 'Historic Park with Urban Energy', 'MacArthur Park in Los Angeles is a unique blend of history, culture, and city life. The park features a large lake, walking paths, and plenty of green space for relaxation. It has a rich history and has long been a gathering place for the local community. While the area can be busy and somewhat gritty, it offers a glimpse into LA’s diverse urban culture. Street vendors, musicians, and public art add to its character. It’s worth a visit for those interested in the city’s history and vibrant local scene.', '2024-12-20', ST_PointFromText('POINT(34.05943707945064 -118.27710002214133)', 4326), '2230 W 6th St, Los Angeles, CA 90057', 'ChIJ96JdGuAzw4AR5ltZ-RCykY8', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/MacArthur_Park_soccer_fields_from_west_2015-10-18.jpg/1280px-MacArthur_Park_soccer_fields_from_west_2015-10-18.jpg', 12, 34, 5, '2024-12-23 09:42:02.000000', NULL, NULL);
-- INSERT INTO `experience` VALUES (0, 'title', 'desc', 'event_date', ST_PointFromText('POINT(lat lng)', 4326), 'address', 'placeid', 'image', rtngcnt, rtngsum, userid, '2024-12-18 21:51:05.000000', NULL, NULL);

-- ----------------------------
-- Records of user
-- ----------------------------
-- All passwords are 'password'.
INSERT INTO `user` VALUES (1, 'Emma Carter', 'emmaC_art', '{noop}password', '2024-02-04 10:15:30.000000', NULL);
INSERT INTO `user` VALUES (2, 'Liam Rodriguez', 'LiamR_99', '{bcrypt}$2a$10$r.5nrugrtMFGWVxfXSom2enSMi4/MBrntK21m9ypSr.39691b8SYq', '2024-03-15 10:16:45.000000', NULL);
INSERT INTO `user` VALUES (3, 'Sophia Bennett', 'SophBennettXO', '{MD5}5f4dcc3b5aa765d61d8327deb882cf99', '2024-03-24 10:17:12.000000', NULL);
INSERT INTO `user` VALUES (4, 'Noah Patel', 'NoahP_23', '{SHA-1}5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', '2024-05-04 10:18:20.000000', NULL);
INSERT INTO `user` VALUES (5, 'Ava Thompson', 'AvaT_creates', '{SHA-256}5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', '2024-05-16 10:19:05.000000', NULL);
INSERT INTO `user` VALUES (6, 'Ethan Kim', 'EthanKimOfficial', '{bcrypt}$2a$10$r.5nrugrtMFGWVxfXSom2enSMi4/MBrntK21m9ypSr.39691b8SYq', '2024-06-01 10:20:38.000000', NULL);
INSERT INTO `user` VALUES (7, 'Olivia Martinez', 'LivMartinez_7', '{noop}password', '2024-06-26 10:21:50.000000', NULL);
INSERT INTO `user` VALUES (8, 'Mason Nguyen', 'MasonN_Playz', '{SHA-256}5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', '2024-07-03 10:22:33.000000', NULL);
INSERT INTO `user` VALUES (9, 'Isabella Rivera', 'Bella_Riv23', '{MD5}5f4dcc3b5aa765d61d8327deb882cf99', '2024-08-16 10:23:10.000000', NULL);
INSERT INTO `user` VALUES (10, 'Lucas Anderson', 'LucasAnders0n', '{SHA-1}5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', '2024-12-30 10:24:47.000000', NULL);

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
