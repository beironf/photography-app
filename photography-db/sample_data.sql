INSERT INTO images_exif
    (image_id, camera_make, camera_model, lens, focal_length, f_number, iso, exposure_time, date, width, height)
VALUES
    ('DJI_0309.jpg', 'DJI', 'FC3582', '6.7 mm f/1.7', 6.72, 1.7, 100, '1/400', '2022-11-18 14:09:57', 4687, 7031),
    ('DSCF2581.jpg', 'FUJIFILM', 'X100F', NULL, 23, 5.6, 200, '1/140', '2021-01-18 09:19:54', 6000, 4000),
    ('DSCF2867.jpg', 'FUJIFILM', 'X100F', NULL, 23, 8, 200, '1/80', '2021-10-22 12:58:14', 5575, 3717),
    ('DSCF2911.jpg', 'FUJIFILM', 'X100F', NULL, 23, 5.6, 200, '1/160', '2021-12-03 08:24:44', 4000, 6000),
    ('DSCF2950.jpg', 'FUJIFILM', 'X100F', NULL, 23, 8, 200, '7.5', '2021-12-03 16:57:04', 6000, 4000),
    ('DSCF2968.jpg', 'FUJIFILM', 'X100F', NULL, 23, 8, 200, '1/160', '2022-03-13 19:40:12', 5920, 3947),
    ('DSCF3023.jpg', 'FUJIFILM', 'X100F', NULL, 23, 5.6, 400, '1/900', '2022-04-15 18:49:59', 5336, 3557),
    ('IMG_4419.jpg', 'Canon', 'Canon EOS 5D Mark II', 'EF24-70mm f/2.8L USM', 70, 8, 800, '1/200', '2017-06-21 04:02:05', 3266, 4899),
    ('IMG_4814.jpg', 'Canon', 'Canon EOS 5D Mark II', 'EF24-70mm f/2.8L USM', 25, 7.1, 1600, '0.3', '2017-06-26 23:13:41', 3744, 5392),
    ('IMG_4815_pano.jpg', 'Canon', 'Canon EOS 5D Mark II', 'EF24-70mm f/2.8L USM', 24, 11, 100, '1/160', '2017-06-27 22:23:03', 10190, 5693),
    ('IMG_4891.jpg', 'Canon', 'Canon EOS 5D Mark II', 'EF24-70mm f/2.8L USM', 24, 11, 100, '1.3', '2017-06-28 05:07:59', 5562, 3708),
    ('IMG_7149.jpg', 'Canon', 'Canon EOS 5D Mark II', 'EF24-70mm f/2.8L USM', 24, 8, 100, '1/30', '2020-07-30 15:18:55', 3744, 5616),
    ('output2.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1080, 608);

INSERT INTO locations
    (id, name, country, longitude, latitude)
VALUES
    (1, 'Maspalomas', 'Spain', -15.5761, 27.7443),
    (2, 'Lofsdalen', 'Sweden', 13.3156, 62.1279),
    (3, 'Prague', 'Czechia', 14.3994, 50.0873),
    (4, 'Falkenberg', 'Sweden', 12.5093, 56.8798),
    (5, 'Njupeskär', 'Sweden', 12.6843, 61.634),
    (6, 'Sannegården', 'Sweden', 11.9245, 57.7078),
    (7, 'Eriksberg', 'Sweden', 11.9233, 57.7029),
    (8, 'Zion National Park', 'USA', -112.95, 37.3062),
    (9, 'Arizona', 'USA', -111.511, 36.8791),
    (10, 'Cape Tribulation', 'Australia', 145.468, -16.074),
    (11, 'Yosemite', 'USA', -119.596, 37.7487),
    (12, 'Grand Canyon South Rim', 'USA', -112.116, 36.0648);

INSERT INTO judgements
    (id, rating, in_showroom)
VALUES
    (1, 4, false),
    (2, 4, true),
    (3, 2, false),
    (4, 3, false),
    (5, 4, true),
    (6, 2, false),
    (7, 3, false),
    (8, 5, true),
    (9, 5, true),
    (10, 5, true),
    (11, 5, true),
    (12, 5, true);

INSERT INTO photos
    (image_id, title, description, photographer, "group", locations_id, taken, camera, lens, camera_settings, metadata, judgements_id)
VALUES
    ('DJI_0309.jpg', 'Rainforest vs Great Barrier Reef', NULL, 'Fredrik Beiron', 'Australia', 10, '2022-11-18 14:09:57', 'DJIMini3', 'DJI6_7F1_7', '{"iso": "ISO 100",  "aperture": "f/1.7",  "focalLength": "6.72mm",  "exposureTime": "1/400s"}', '{"tags": ["rainforest",  "great-barrier-reef",  "beach",  "ocean"],  "category": "Nature",  "cameraTechniques": ["Aerial"]}', 10),
    ('DSCF2581.jpg', 'Lonely Skier', NULL, 'Fredrik Beiron', NULL, 2, '2021-01-18 09:19:54', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 200",  "aperture": "f/5.6",  "focalLength": "23mm",  "exposureTime": "1/140s"}', '{"tags": ["snow",  "mountains",  "sunset"],  "category": "Landscape",  "cameraTechniques": []}', 2),
    ('DSCF2867.jpg', 'Old Wall', NULL, 'Fredrik Beiron', NULL, 3, '2021-10-22 12:58:14', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 200",  "aperture": "f/8",  "focalLength": "23mm",  "exposureTime": "1/80s"}', '{"tags": ["green",  "red",  "colour",  "branches"],  "category": "CityAndArchitecture",  "cameraTechniques": []}', 3),
    ('DSCF2911.jpg', 'Balcony Sunset', NULL, 'Fredrik Beiron', NULL, 6, '2021-12-03 08:24:44', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 200",  "aperture": "f/5.6",  "focalLength": "23mm",  "exposureTime": "1/160s"}', '{"tags": ["snow",  "winter",  "balcony",  "sunset",  "red"],  "category": "CityAndArchitecture",  "cameraTechniques": []}', 6),
    ('DSCF2950.jpg', 'Snowy Eriksberg', NULL, 'Fredrik Beiron', NULL, 7, '2021-12-03 16:57:04', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 200",  "aperture": "f/8",  "focalLength": "23mm",  "exposureTime": "7.5s"}', '{"tags": ["snow",  "city",  "smoke",  "winter",  "bridge"],  "category": "CityAndArchitecture",  "cameraTechniques": ["LongExposure"]}', 7),
    ('DSCF2968.jpg', 'Dune', NULL, 'Fredrik Beiron', NULL, 1, '2022-03-13 19:40:12', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 200",  "aperture": "f/8",  "focalLength": "23mm",  "exposureTime": "1/160s"}', '{"tags": ["dunes",  "sand",  "sunny",  "desert"],  "category": "Landscape",  "cameraTechniques": []}', 1),
    ('DSCF3023.jpg', 'Evening Silhouettes', NULL, 'Fredrik Beiron', NULL, 4, '2022-04-15 18:49:59', 'FujifilmX100F', 'Fujinon35', '{"iso": "ISO 400",  "aperture": "f/5.6",  "focalLength": "23mm",  "exposureTime": "1/900s"}', '{"tags": ["people",  "grass",  "silhouette",  "sunset",  "group",  "hill"],  "category": "People",  "cameraTechniques": []}', 4),
    ('IMG_4419.jpg', 'Yosemite Falls', NULL, 'Fredrik Beiron', 'USA', 11, '2017-06-21 04:02:05', 'CanonEOS5DMarkII', 'Canon24_70L', '{"iso": "ISO 800",  "aperture": "f/8",  "focalLength": "70mm",  "exposureTime": "1/200s"}', '{"tags": ["yosemite",  "mountains",  "forest",  "waterfall"],  "category": "Landscape",  "cameraTechniques": []}', 11),
    ('IMG_4814.jpg', 'The Narrows', NULL, 'Fredrik Beiron', 'USA', 8, '2017-06-26 23:13:41', 'CanonEOS5DMarkII', 'Canon24_70L', '{"iso": "ISO 1600",  "aperture": "f/7.1",  "focalLength": "25mm",  "exposureTime": "0.3s"}', '{"tags": ["river",  "water",  "walls",  "mountain",  "narrow",  "red"],  "category": "Nature",  "cameraTechniques": ["LongExposure"]}', 8),
    ('IMG_4815_pano.jpg', 'The Horseshoe Bend', NULL, 'Fredrik Beiron', 'USA', 9, '2017-06-27 22:23:03', 'CanonEOS5DMarkII', 'Canon24_70L', '{"iso": "ISO 100",  "aperture": "f/11",  "focalLength": "24mm",  "exposureTime": "1/160s"}', '{"tags": ["grand-canyon",  "river",  "horseshoe",  "panorama"],  "category": "Landscape",  "cameraTechniques": ["Panorama"]}', 9),
    ('IMG_4891.jpg', 'Grand Canyon', NULL, 'Fredrik Beiron', 'USA', 12, '2017-06-28 05:07:59', 'CanonEOS5DMarkII', 'Canon24_70L', '{"iso": "ISO 100",  "aperture": "f/11",  "focalLength": "24mm",  "exposureTime": "1.3s"}', '{"tags": ["canyon",  "red",  "blue",  "dawn"],  "category": "Landscape",  "cameraTechniques": []}', 12),
    ('IMG_7149.jpg', 'Njupeskär Waterfall', NULL, 'Fredrik Beiron', NULL, 5, '2020-07-30 15:18:55', 'CanonEOS5DMarkII', 'Canon24_70L', '{"iso": "ISO 100",  "aperture": "f/8",  "focalLength": "24mm",  "exposureTime": "1/30s"}', '{"tags": ["waterfall",  "stones",  "water",  "rocks",  "stream"],  "category": "Nature",  "cameraTechniques": ["LongExposure"]}', 5);
