package umlgraph.graphview.edges;

import umlgraph.graph.Vertex;
import umlgraph.graphview.GraphPanel;
import umlgraph.graphview.StylableNode;

/**
 * A graph edge visually connects two {@link Vertex} of type <code>V</code>.
 * <br>
 * Concrete edge implementations used by {@link GraphPanel} should
 * implement this interface as this type is the only one exposed to the user.
 * 
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 *
 * @see Vertex
 * @see GraphPanel
 * 
 * @author r.naryshkin99
 */
public interface Edge<E, V> extends StylableNode {
    
     /**
     * Returns the underlying (stored reference) graph edge.
     * 
     * @return edge reference 
     * 
     * @see GraphPanel
     */
    public umlgraph.graph.Edge<E, V> getUnderlyingEdge();
    
    /**
     * Returns the attached arrow of the edge, for styling purposes.
     * 
     * The arrows are only used with directed graphs.
     * 
     * @return arrow reference; null if does not exist.
     */
    public StylableNode getStylableArrow();
    
    /**
     * Returns the label node for further styling.
     * 
     * @return the label node.
     */
    public StylableNode getStylableLabel();
}
