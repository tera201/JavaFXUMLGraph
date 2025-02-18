package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.Collection;

/**
 * A directed graph (or digraph) is a graph that is made up of a set of vertices 
 * connected by edges, where the edges have a direction associated with them.
 * <br>
 * An directed-edge leaves the <i>outbound VertexNode</i>
 * towards the <i>inbound VertexNode</i> and this changes the reasoning behind some
 * methods of the {@link Graph} interface, which are overridden in this interface
 * so as to provide different documentation of expected behavior.
 * 
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 *
 * @see Edge
 * @see Vertex
 */
public interface Digraph<V, E> {
    
    /**
     * Returns a vertex's <i>incident</i> edges as a collection.
     * 
     * Incident edges are all edges that have vertex <code>inbound</code> as the
     * <i>inbound VertexNode</i>, i.e., the edges "entering" vertex <code>inbound</code>.
     * If there are no incident edges, e.g., an isolated vertex, 
     * returns an empty collection.
     */
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound)
            throws InvalidVertexException;

    /**
     * Returns a vertex's <i>outbound</i> edges as a collection.
     */
    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound)
            throws InvalidVertexException;


    Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement, ArrowTypes arrowTypes)
            throws InvalidVertexException, InvalidEdgeException;
    int numVertices();
    int numEdges();

    Collection<Vertex<V>> vertices();
    Collection<Edge<E, V>> edges();
    Vertex<V> opposite(Vertex<V> v, Edge<E, V> e)
            throws InvalidVertexException, InvalidEdgeException;

    Vertex<V> insertVertex(V vElement)
            throws InvalidVertexException;

    Vertex<V> insertVertex(V vElement, ElementTypes elementTypes)
            throws InvalidVertexException;

    Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label)
            throws InvalidVertexException;
    Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label, String notes)
            throws InvalidVertexException;

    default Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement)
            throws InvalidVertexException, InvalidEdgeException {
        return insertEdge(u, v, edgeElement, ArrowTypes.ASSOCIATION);
    }




}
