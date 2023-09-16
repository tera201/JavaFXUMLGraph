package umlgraph.graphview.strategy;

import java.util.Collection;
import java.util.Map;

import umlgraph.graph.Graph;
import umlgraph.graph.Vertex;
import umlgraph.graphview.vertices.GraphVertex;
import umlgraph.graphview.vertices.GraphVertexPaneNode;

/**
 * Contains the method that should be implemented when creating new vertex placement
 * strategies.
 * 
 * @author r.naryshkin99
 */
public interface PlacementStrategy {

    /**
     * Implementations of placement strategies must implement this interface.
     * 
     * Should use the {@link GraphVertex#setPosition(double, double) }
     *                  method to place individual vertices.
     * 
     * 
     * @param <V>       Generic type for element stored at vertices.
     * @param <E>       Generic type for element stored at edges.
     * @param width     Width of the area in which to place the vertices.
     * @param height    Height of the area in which to place the vertices.
     * @param theGraph  Reference to the {@link Graph} containing the graph model.
     *                  Can use methods to check for additional information
     *                  pertaining the model.
     * 
     * @param vertices  Collection of {@link GraphVertex} to place.
     *                  
     */
    public <V,E> void place(double width, double height, Graph<V,E> theGraph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes);
}
