package com.rnar.umlgraph.graphview.vertices;


import com.rnar.umlgraph.graphview.GraphPanel;
import com.rnar.umlgraph.graphview.StylableNode;
import com.rnar.umlgraph.labels.LabelledNode;
import javafx.geometry.Point2D;

import java.util.Collection;

/**
 * Abstracts the internal representation and behavior of a visualized graph vertex.
 * 
 * @param <V> Type stored in the underlying vertex
 * 
 * @see GraphPanel
 * 
 * @author r.naryshkin99
 */
public interface GraphVertexInterface<V> extends GraphVertex<V>, LabelledNode {



    /**
     * Adds a vertex to the internal list of adjacent vertices.
     *
     * @param v vertex to add
     */
    public void addAdjacentVertex(GraphVertexInterface<V> v);

    /**
     * Removes a vertex from the internal list of adjacent vertices.
     *
     * @param v vertex to remove
     * @return true if <code>v</code> existed; false otherwise.
     */
    public boolean removeAdjacentVertex(GraphVertexInterface<V> v);

    /**
     * Removes a collection of vertices from the internal list of adjacent
     * vertices.
     *
     * @param col collection of vertices
     * @return true if any vertex was effectively removed
     */
    public boolean removeAdjacentVertices(Collection<GraphVertexInterface<V>> col);

    /**
     * Checks whether <code>v</code> is adjacent this instance.
     *
     * @param v vertex to check
     * @return true if adjacent; false otherwise
     */
    public boolean isAdjacentTo(GraphVertexInterface<V> v);

    /**
     * Returns the current position of the instance in pixels.
     *
     * @return the x,y coordinates in pixels
     */
    public Point2D getPosition();

    /**
     * Resets the current computed external force vector.
     *
     */
    public void resetForces();

    /**
     * Adds the vector represented by <code>(x,y)</code> to the current external
     * force vector.
     *
     * @param x x-component of the force vector
     * @param y y-component of the force vector
     *
     */
    public void addForceVector(double x, double y);

    /**
     * Returns the current external force vector.
     *
     * @return force vector
     */
    public Point2D getForceVector();

    /**
     * Returns the future position of the vertex.
     *
     * @return future position
     */
    public Point2D getUpdatedPosition();

    /**
     * Updates the future position according to the current internal force
     * vector.
     *
     * @see GraphPanel#updateForces()
     */
    public void updateDelta();

    /**
     * Moves the vertex position to the computed future position.
     * <p>
     * Moves are constrained within the parent pane dimensions.
     *
     * @see GraphPanel#applyForces()
     */
    public void moveFromForces();


    public StylableNode getStylableLabel();
}
