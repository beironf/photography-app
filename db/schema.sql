CREATE TABLE images_exif (
    image_id        VARCHAR(255) NOT NULL PRIMARY KEY,
    camera_make     VARCHAR(255),
    camera_model    VARCHAR(255),
    lens            VARCHAR(255),
    focal_length    INTEGER,
    f_number        FLOAT,
    iso             INTEGER,
    exposure_time   VARCHAR(255),
    date            TIMESTAMP
);