/*
** 3 - Create the different tables and hypertables.
** Create all tables and hypertables used for live update
** operations.
*/

-- Create the "live_updates" table.
CREATE TABLE IF NOT EXISTS tluc.live_updates (
    timestamp     TIMESTAMPTZ     NOT NULL,
    driver_uuid   UUID            NOT NULL,
    driver_status TEXT            NOT NULL,
    location      GEOGRAPHY(POINT, 4326) NOT NULL,
    speed         DOUBLE PRECISION NOT NULL
);

-- Create a hypertable on the live_updates table, with chunks of 1 day.
SELECT create_hypertable('tluc.live_updates', 'timestamp', chunk_time_interval => interval '1 day');