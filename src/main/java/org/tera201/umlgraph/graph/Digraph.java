package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;

import java.util.Collection;

/**
 * A directed graph (or digraph) is a graph that is made up of a set of vertices 
 * connected by edges, where the edges have a direction associated with them.
 * <br>
 * An directed-edge leaves the <i>outbound vertex</i>
 * towards the <i>inbound vertex</i> and this changes the reasoning behind some
 * methods of the {@link Graph} interface, which are overridden in this interface
 * so as to provide different documentation of expected behavior.
 * 
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * 
 * @see Graph
 * @see Edge
 * @see Vertex
 */
public interface Digraph<V, E> extends Graph<V, E> {
    
    /**
     * Returns a vertex's <i>incident</i> edges as a collection.
     * 
     * Incident edges are all edges that have vertex <code>inbound</code> as the
     * <i>inbound vertex</i>, i.e., the edges "entering" vertex <code>inbound</code>.
     * If there are no incident edges, e.g., an isolated vertex, 
     * returns an empty collection.
     * 
     * @param inbound     vertex for which to obtain the incident edges
     * 
     * @return            collection of edges
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound)
            throws InvalidVertexException;

    /**
     * Returns a vertex's <i>outbound</i> edges as a collection.
     * 
     * Incident edges are all edges that have vertex <code>outbound</code> as the
     * <i>outbound vertex</i>, i.e., the edges "leaving" vertex <code>outbound</code>.
     * If there are no outbound edges, e.g., an isolated vertex, 
     * returns an empty collection.
     * 
     * @param outbound     vertex for which to obtain the outbound edges
     * 
     * @return            collection of edges
     */
    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound)
            throws InvalidVertexException;

    /**
     * Inserts a new edge with a given element between two existing vertices and
     * return its (the edge's) reference.
     *
     * @param outbound    outbound vertex
     * @param inbound     inbound vertex
     * @param edgeElement the element to store in the new edge
     * @param arrowTypes
     * @return the reference for the newly created edge
     * @throws InvalidVertexException if <code>outbound</code> or
     *                                <code>inbound</code>
     *                                are invalid vertices for the graph
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>edgeElement</code>
     *                                according to the equality of
     *                                {@link Object#equals(java.lang.Object)}
     *                                method.
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement, ArrowTypes arrowTypes)
            throws InvalidVertexException, InvalidEdgeException;


    
    
}
