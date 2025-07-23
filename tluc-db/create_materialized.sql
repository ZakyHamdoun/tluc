/*
** 5 - Create materialized view.
** Create the materialized view to retrieve last driver
** location quickly.
*/

-- Create a materialized view.
CREATE MATERIALIZED VIEW latest_driver_location AS
SELECT DISTINCT ON (driver_uuid)
  driver_uuid, timestamp, driver_status, location, speed
FROM tluc.live_updates
ORDER BY driver_uuid, timestamp DESC
WITH NO DATA;