/*
** 1 - Create all extensions.
** Create all the necessary extensions needed
** for the realtime database to function correctly.
*/

-- Enable PostGIS for geospatial queries.
CREATE EXTENSION IF NOT EXISTS postgis;

-- Enable TimescaleDB for time-series partitioning.
CREATE EXTENSION IF NOT EXISTS timescaledb;