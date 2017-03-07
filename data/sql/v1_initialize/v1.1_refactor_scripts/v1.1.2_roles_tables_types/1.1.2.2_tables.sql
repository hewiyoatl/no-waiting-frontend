CREATE TABLE hewiyoat_no_waiting.restaurant (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE hewiyoat_no_waiting.sucursal (
  id                SERIAL PRIMARY KEY,
  address_1         VARCHAR(500),
  address_2         VARCHAR(500),
  zip_code          VARCHAR(5),
  state             VARCHAR(2),
  city              VARCHAR(100),
  country           VARCHAR(10),
  phone_number      VARCHAR(20),
  restaurant_id     BIGINT UNSIGNED NOT NULL,
  latitude          FLOAT           NOT NULL,
  longitud          FLOAT           NOT NULL,
  created_timestamp TIMESTAMP,
  deleted           BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (restaurant_id) REFERENCES hewiyoat_no_waiting.restaurant (id)
);

CREATE TABLE hewiyoat_no_waiting.service_time (
  id          SERIAL PRIMARY KEY,
  sucursal_id BIGINT UNSIGNED,
  day         INT,
  start_time  TIMESTAMP,
  end_time    TIMESTAMP,
  FOREIGN KEY (sucursal_id) REFERENCES hewiyoat_no_waiting.sucursal (id)
);

CREATE TABLE hewiyoat_no_waiting.rest_user (
  id                SERIAL PRIMARY KEY,
  sucursal_id       BIGINT UNSIGNED,
  first_name        VARCHAR(100),
  last_name         VARCHAR(100),
  phone_number      VARCHAR(20),
  user_name         VARCHAR(50),
  password          VARCHAR(64),
  created_timestamp TIMESTAMP,
  deleted           BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (sucursal_id) REFERENCES hewiyoat_no_waiting.sucursal (id)
);

CREATE TABLE hewiyoat_no_waiting.client (
  id                SERIAL PRIMARY KEY,
  phone_number      VARCHAR(20) UNIQUE,
  first_name        VARCHAR(100),
  last_name         VARCHAR(100),
  email             VARCHAR(300),
  user_name         VARCHAR(100),
  password          VARCHAR(64),
  created_timestamp TIMESTAMP,
  deleted           BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE hewiyoat_no_waiting.reservation (
  id                SERIAL PRIMARY KEY,
  user_id           BIGINT UNSIGNED,
  user_type         BIGINT UNSIGNED,
  sucursal_id       BIGINT UNSIGNED,
  status            INT,
  created_timestamp TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES hewiyoat_no_waiting.client (id),
  FOREIGN KEY (sucursal_id) REFERENCES hewiyoat_no_waiting.sucursal (id)
);

CREATE TABLE hewiyoat_no_waiting.reservation_event (
  id                SERIAL PRIMARY KEY,
  user_id           BIGINT UNSIGNED,
  user_type         BIGINT UNSIGNED,
  reservation_id    BIGINT UNSIGNED,
  status            INT,
  created_timestamp TIMESTAMP,
  FOREIGN KEY (reservation_id) REFERENCES hewiyoat_no_waiting.reservation (id)
);

