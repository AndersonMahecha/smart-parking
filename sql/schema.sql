-- Parking database schema

-- Users table
-- identifier as uuid primary key autoincrement
-- first name as varchar(100)
-- last name as varchar(100)
-- username as varchar(50) unique not null
-- password as varchar(100)
-- role as varchar(50)

-- Parking slots table
-- identifier as uuid primary key autoincrement
-- number as varchar(10) unique not null
-- status as varchar(10) not null
-- Vehicle id as uuid

-- Cards table
-- identifier as uuid primary key autoincrement
-- card id as varchar(10) unique not null

-- Vehicles table
-- identifier as uuid primary key autoincrement
-- license plate as varchar(10) unique nullable
-- card id as FK to cards table nullable
-- short code as varchar(6) unique nullable
-- type as varchar(10) not null
-- entry date as timestamp not null
-- Parking slot id as FK to parking slots table nullable

-- Parking table
-- setting name as varchar(100) primary key
-- setting value as varchar(100)
-- setting description  as varchar(200)

-- History table
-- identifier as uuid primary key autoincrement
-- license plate as varchar(10) not null
-- type as varchar(10) not null
-- entry date as timestamp not null
-- exit date as timestamp not null
-- total as decimal(10,2) not null
-- parking slot id as FK to parking slots table nullable

-- Devices table
-- identifier as uuid primary key autoincrement
-- name as varchar(100) not null
-- type as varchar(100) not null
-- status as varchar(10) not null
-- parking slot id as FK to parking slots table nullable
-- host as varchar(100) not null
-- mac address as varchar(100) not null

CREATE TABLE IF NOT EXISTS users (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS parking_slots (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  number VARCHAR(10) UNIQUE NOT NULL,
  status VARCHAR(10) NOT NULL,
  vehicle_id UUID,
  FOREIGN KEY (vehicle_id) REFERENCES vehicles (identifier)
);

CREATE TABLE IF NOT EXISTS cards (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  card_id VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  license_plate VARCHAR(10) UNIQUE,
  card_id UUID,
  short_code VARCHAR(6) UNIQUE,
  type VARCHAR(10) NOT NULL,
  entry_date TIMESTAMP NOT NULL,
  parking_slot_id UUID,
  FOREIGN KEY (card_id) REFERENCES cards (identifier),
  FOREIGN KEY (parking_slot_id) REFERENCES parking_slots (identifier)
);

CREATE TABLE IF NOT EXISTS parking (
  setting_name VARCHAR(100) PRIMARY KEY,
  setting_value VARCHAR(100),
  setting_description VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS history (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  license_plate VARCHAR(10) NOT NULL,
  type VARCHAR(10) NOT NULL,
  entry_date TIMESTAMP NOT NULL,
  exit_date TIMESTAMP NOT NULL,
  total DECIMAL(10, 2) NOT NULL,
  parking_slot_id UUID,
  FOREIGN KEY (parking_slot_id) REFERENCES parking_slots (identifier)
);

CREATE TABLE IF NOT EXISTS devices (
  identifier UUID PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(100) NOT NULL,
  status VARCHAR(10) NOT NULL,
  parking_slot_id UUID,
  host VARCHAR(100) NOT NULL,
  mac_address VARCHAR(100) NOT NULL,
  FOREIGN KEY (parking_slot_id) REFERENCES parking_slots (identifier)
);
