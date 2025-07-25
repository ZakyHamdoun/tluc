package com.taxinopli.tluc.geometry;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {
    private GeometryUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);
}
