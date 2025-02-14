package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.Collection;

/**
 * A graph is made up of a set of vertices connected by edges, where the edges 
 * have no direction associated with them, i.e., they establish a two-way connection.
 *
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * 
 * @see Edge
 * @see Vertex
 */
public interface Graph<V, E> {

    /**
     * Returns the total number of vertices of the graph.
     * 
     * @return      total number of vertices
     */
    public int numVertices();

    /**
     * Returns the total number of edges of the graph.
     * 
     * @return      total number of vertices
     */
    public int numEdges();

    /**
     * Returns the vertices of the graph as a collection.
     * 
     * If there are no vertices, returns an empty collection.
     * 
     * @return      collection of vertices
     */
    public Collection<Vertex<V>> vertices();

    /**
     * Returns the edges of the graph as a collection.
     * 
     * If there are no edges, returns an empty collection.
     * 
     * @return      collection of edges
     */
    public Collection<Edge<E, V>> edges();

    /**
     * Returns a vertex's <i>incident</i> edges as a collection.
     * 
     * Incident edges are all edges that are connected to vertex <code>v</code>.
     * If there are no incident edges, e.g., an isolated vertex, 
     * returns an empty collection.
     * 
     * @param v     vertex for which to obtain the incident edges
     * 
     * @return      collection of edges
     */
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v)
            throws InvalidVertexException;

    /**
     * Given vertex <code>v</code>, return the opposite vertex at the other end
     * of edge <code>e</code>.
     * 
     * If both <code>v</code> and <code>e</code> are valid, but <code>e</code>
     * is not connected to <code>v</code>, returns <i>null</i>.
     * 
     * @param v         vertex on one end of <code>e</code>
     * @param e         edge connected to <code>v</code>
     * @return          opposite vertex along <code>e</code>
     * 
     * @exception InvalidVertexException    if the vertex is invalid for the graph
     * @exception InvalidEdgeException      if the edge is invalid for the graph
     */
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e)
            throws InvalidVertexException, InvalidEdgeException;

    /**
     * Inserts a new vertex with a given element, returning its reference.
     * 
     * @param vElement      the element to store at the vertex
     * 
     * @return              the reference of the newly created vertex
     * 
     * @exception InvalidVertexException    if there already exists a vertex
     *                                      containing <code>vElement</code>
     *                                      according to the equality of
     *                                      {@link Object#equals(java.lang.Object) }
     *                                      method.
     *                                  
     */
    public Vertex<V> insertVertex(V vElement)
            throws InvalidVertexException;

    public Vertex<V> insertVertex(V vElement, ElementTypes elementTypes)
            throws InvalidVertexException;

    public Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label)
            throws InvalidVertexException;
    public Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label, String notes)
            throws InvalidVertexException;

    /**
     * Inserts a new edge with a given element between two existing vertices and
     * return its (the edge's) reference.
     *
     * @param u           a vertex
     * @param v           another vertex
     * @param edgeElement the element to store in the new edge
     * @param arrowTypes
     * @return the reference for the newly created edge
     * @throws InvalidVertexException if <code>u</code> or <code>v</code>
     *                                are invalid vertices for the graph
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>edgeElement</code>
     *                                according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    public default Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement)
            throws InvalidVertexException, InvalidEdgeException {
        return insertEdge(u, v, edgeElement, ArrowTypes.ASSOCIATION);
    }

    /**
     * Inserts a new edge with a given element between two existing vertices and
     * return its (the edge's) reference.
     *
     * @param u           a vertex
     * @param v           another vertex
     * @param edgeElement the element to store in the new edge
     * @param arrowTypes
     * @return the reference for the newly created edge
     * @throws InvalidVertexException if <code>u</code> or <code>v</code>
     *                                are invalid vertices for the graph
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>edgeElement</code>
     *                                according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement, ArrowTypes arrowTypes)
            throws InvalidVertexException, InvalidEdgeException;

}
