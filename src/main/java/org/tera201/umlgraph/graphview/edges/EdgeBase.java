package org.tera201.umlgraph.graphview.edges;

import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.labels.LabelledNode;
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
 * @see LabelledNode
 * @see GraphPanel
 * 
 * @author r.naryshkin99
 */
public interface EdgeBase<E, V> extends Edge<E, V>, LabelledNode {
    
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
