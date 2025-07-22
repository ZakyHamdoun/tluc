-- The ORM will take care of creating the necessary tables and relationships.

-- Function to get nearby drivers within a specified distance ( lontitude, latitude, distance in meters )
CREATE OR REPLACE FUNCTION get_nearby_drivers(
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,
    distance_meters DOUBLE PRECISION
)
RETURNS TABLE (
    driver_id VARCHAR(255),
    distance DOUBLE PRECISION,
    timestamp TIMESTAMP,
    speed DOUBLE PRECISION,
    status VARCHAR(50)
) AS $$
BEGIN
    -- Todo : Implement the logic to find nearby drivers
END;
$$ LANGUAGE plpgsql; 