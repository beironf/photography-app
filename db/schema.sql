CREATE TABLE images_exif (
    image_id        VARCHAR(255) PRIMARY KEY,
    camera_make     VARCHAR(255),
    camera_model    VARCHAR(255),
    lens            VARCHAR(255),
    focal_length    INTEGER,
    f_number        FLOAT,
    iso             INTEGER,
    exposure_time   VARCHAR(10),
    date            TIMESTAMP
);

CREATE TABLE locations (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    region          VARCHAR(255) NOT NULL,
    country         VARCHAR(255) NOT NULL,
    longitude       FLOAT NOT NULL,
    latitude        FLOAT NOT NULL
);

CREATE TABLE judgements (
    id              SERIAL PRIMARY KEY,
    rating          INTEGER NOT NULL,
    is_showroom     BOOLEAN NOT NULL
);

CREATE TABLE photos (
    image_id        VARCHAR(255) PRIMARY KEY,
    title           TEXT NOT NULL,
    description     TEXT,
    photographer    VARCHAR(255) NOT NULL,
    group           VARCHAR(255),
    locations_id    INTEGER NOT NULL REFERENCES locations (id),
    taken           DATE NOT NULL,
    camera          VARCHAR(255) NOT NULL,
    lens            VARCHAR(255) NOT NULL,
    camera_settings JSON NOT NULL,
    metadata        JSON NOT NULL,
    judgements_id   INTEGER NOT NULL REFERENCES judgements (id)
);