/*
** 4 - Create the different indexes for fast lookup.
** Create the different indexes on different tables.
*/

-- Create the index on location.
CREATE INDEX IF NOT EXISTS idx_live_location_gist ON tluc.live_updates USING GIST (location);

-- Create the index on driver time.
CREATE INDEX IF NOT EXISTS idx_live_driver_time ON tluc.live_updates (driver_uuid, timestamp DESC);

-- Create the index on driver status.
CREATE INDEX IF NOT EXISTS idx_live_status_time ON tluc.live_updates (driver_status, timestamp DESC);