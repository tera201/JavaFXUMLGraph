package org.tera201.umlgraph.graphview.edges;

import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graphview.StylableNode;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.GraphPanel;

/**
 * Used as a super-type for all different concrete edge implementations by
 * {@link GraphPanel}, e.g., line and curves.
 * <br>
 * An edge can have an attached arrow.
 * 
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 * 
 * @see DefaultArrow
 * @see Edge
 * @see GraphPanel
 * 
 * @author r.naryshkin99
 */
public interface EdgeBase<E, V> extends StylableNode {

    /**
     * Returns the underlying (stored reference) graph edge.
     *
     * @return edge reference
     *
     * @see GraphPanel
     */
    public Edge<E, V> getUnderlyingEdge();

    /**
     * Returns the attached arrow of the edge, for styling purposes.
     *
     * The arrows are only used with directed graphs.
     *
     * @return arrow reference; null if does not exist.
     */
    public StylableNode getStylableArrow();
    
    /**
     * Attaches a {@link DefaultArrow} to this edge, binding its position/rotation.
     * 
     * @param arrow     arrow to attach
     */
    public void attachArrow(DefaultArrow arrow);
    
    /**
     * Returns the attached {@link DefaultArrow}, if any.
     * 
     * @return      reference of the attached arrow; null if none.
     */
    public DefaultArrow getAttachedArrow();
    
}
