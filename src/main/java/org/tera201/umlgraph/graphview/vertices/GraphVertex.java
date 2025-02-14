package org.tera201.umlgraph.graphview.vertices;

import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.GraphPanel;
import org.tera201.umlgraph.graphview.StylableNode;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import javafx.beans.property.DoubleProperty;

/**
 * Abstracts the internal representation and behavior of a visualized graph vertex.
 * 
 * @param <V> Type stored in the underlying vertex
 * 
 * @see GraphPanel
 * 
 * @author r.naryshkin99
 */
public interface GraphVertex<V> extends StylableNode {
    
    /**
     * Returns the underlying (stored reference) graph vertex.
     * 
     * @return vertex reference 
     * 
     * @see Graph
     */
    public Vertex<V> getUnderlyingVertex();
    
    /**
     * Sets the position of this vertex in panel coordinates. 
     * 
     * Apart from its usage in the {@link GraphPanel}, this method
     * should only be called when implementing {@link PlacementStrategy}.
     * 
     * @param x     x-coordinate for the vertex
     * @param y     y-coordinate for the vertex
     */
    public void setPosition(double x, double y);
    
    /**
     * Return the center x-coordinate of this vertex in panel coordinates.
     * 
     * @return     x-coordinate of the vertex 
     */
    public double getPositionCenterX();
    
    /**
     * Return the center y-coordinate of this vertex in panel coordinates.
     * 
     * @return     y-coordinate of the vertex 
     */
    public double getPositionCenterY();
    
    /**
     * Returns the circle radius used to represent this vertex.
     * 
     * @return      circle radius
     */
    public double getRadius();

    /**
     * Returns the label node for further styling.
     *
     * @return the label node.
     */
    public DoubleProperty centerXProperty();

    public DoubleProperty centerYProperty();

    public double getCenterX();

    public double getCenterY();

    /**
     * Returns the label node for further styling.
     *
     * @return the label node.
     */
    public StylableNode getStylableLabel();
}
