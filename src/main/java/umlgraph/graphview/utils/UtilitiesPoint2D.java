package umlgraph.graphview.utils;

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
    
    /**
     * Computes the vector of the attractive force between two nodes.
     *
     * @param from              Coordinates of the first node
     * @param to                Coordinates of the second node
     * @param globalCount       Global number of nodes
     * @param force             Force factor to be used
     * @param scale             Scale factor to be used
     * 
     * @return                  Computed force vector
     */
    public static Point2D attractiveForce(Point2D from, Point2D to, int globalCount, double force, double scale) {

        double distance = from.distance(to); 

        Point2D vec = to.subtract(from).normalize();
        
        double factor = attractiveFunction(distance, globalCount, force, scale);
        return vec.multiply(factor);              
    }

    /**
     * Computes the value of the scalar attractive force function based on
     * the given distance of a group of nodes.
     *
     * @param distance          Distance between two nodes
     * @param globalCount       Global number of nodes
     * @return                  Computed attractive force
     */
    static double attractiveFunction(double distance, int globalCount, double force, double scale) {
        if (distance < 1) {
            distance = 1;
        }

        //the attractive strenght grows logarithmically with distance 
        
        //return force * Math.log(distance / scale) * (1.0 / (1.0 * numVertices));
        //return force * Math.log(distance / numVertices) * (1 / scale);
        
        return force * Math.log(distance / scale) * 0.1;// * (1.0 / (1.0 * numVertices));        
    }

    /**
     * Computes the vector of the repelling force between two node.
     *
     * @param from              Coordinates of the first node
     * @param to                Coordinates of the second node
     * @param scale             Scale factor to be used
     * @return                  Computed force vector
     */
    public static Point2D repellingForce(Point2D from, Point2D to, double scale) {
        double distance = from.distance(to); 

        Point2D vec = to.subtract(from).normalize();
        
        double factor = -repellingFunction(distance, scale);
        
        return vec.multiply(factor);       
    }

    /**
     * Computes the value of the scalar repelling force function based on
     * the given distance of two nodes.
     *
     * @param distance          Distance between two nodes
     * @return                  Computed repelling force
     */
    static double repellingFunction(double distance, double scale) {
        if (distance < 1) {
            distance = 1;
        }
        return scale / (distance*distance);
    }

}
