-- TLUC TimescaleDB + PostGIS Initialization Script

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;

CREATE SCHEMA IF NOT EXISTS tluc;

CREATE TABLE IF NOT EXISTS tluc.live_updates (
    timestamp     TIMESTAMPTZ     NOT NULL,
    driver_uuid   UUID            NOT NULL,
    driver_status TEXT            NOT NULL,
    location      GEOGRAPHY(POINT, 4326) NOT NULL,
    speed         DOUBLE PRECISION NOT NULL
);

-- Hypertable etc after ... I still need to test if everything works .....