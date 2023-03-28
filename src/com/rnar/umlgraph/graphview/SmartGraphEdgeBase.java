package com.rnar.umlgraph.graphview;

import com.rnar.umlgraph.arrows.DefaultArrow;

/**
 * Used as a super-type for all different concrete edge implementations by
 * {@link SmartGraphPanel}, e.g., line and curves.
 * <br>
 * An edge can have an attached arrow.
 * 
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 * 
 * @see SmartArrow
 * @see SmartGraphEdge
 * @see SmartLabelledNode
 * @see SmartGraphPanel
 * 
 * @author r.naryshkin99
 */
public interface SmartGraphEdgeBase<E, V> extends SmartGraphEdge<E, V>, SmartLabelledNode {
    
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
