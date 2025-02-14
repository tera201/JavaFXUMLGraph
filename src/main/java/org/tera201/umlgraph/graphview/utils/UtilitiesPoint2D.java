package org.tera201.umlgraph.graphview.utils;

import javafx.geometry.Point2D;

/**
 * Class with utility methods for Point2D instances and force-based layout
 * computations.
 * 
 * @author r.naryshkin99
 */
public class UtilitiesPoint2D {
    
    /**
     * Rotate a point around a pivot point by a specific degrees amount
     * @param point point to rotate
     * @param pivot pivot point
     * @param angle_degrees rotation degrees
     * @return rotated point
     */
    public static Point2D rotate(final Point2D point, final Point2D pivot, double angle_degrees) {
        double angle = Math.toRadians(angle_degrees); //angle_degrees * (Math.PI/180); //to radians
        
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //translate to origin
        Point2D result = point.subtract(pivot);
        
        // rotate point
        Point2D rotatedOrigin = new Point2D(
                result.getX() * cos - result.getY() * sin,
                result.getX() * sin + result.getY() * cos);

        // translate point back
        result = rotatedOrigin.add(pivot);

        return result;
    }

}
