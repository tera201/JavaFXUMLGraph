package com.rnar.umlgraph.graph;

import com.rnar.umlgraph.graphview.vertices.elements.ElementTypes;
import com.rnar.umlgraph.graphview.arrows.ArrowTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ADT Graph implementation that stores a collection of edges (and vertices) and
 * where each edge contains the references for the vertices it connects.
 * <br>
 * Does not allow duplicates of stored elements through <b>equals</b> criteria.
 *
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * 
 * @author r.naryshkin99
 */
public class GraphEdgeList<V, E> implements Graph<V, E> {

    /* inner classes are defined at the end of the class, so are the auxiliary methods 
     */
    private Map<V, Vertex<V>> vertices;
    private Map<E, Edge<E, V>> edges;

    /**
     * Creates a empty graph.
     */
    public GraphEdgeList() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        List<Vertex<V>> list = new ArrayList<>();
        for (Vertex<V> v : vertices.values()) {
            list.add(v);
        }
        return list;
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> list = new ArrayList<>();
        for (Edge<E, V> e : edges.values()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {

        checkVertex(v);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((EdgeNode<E, V>) edge).contains(v)) {
                /* edge.vertices()[0] == v || edge.vertices()[1] == v */
                incidentEdges.add(edge);
            }

        }

        return incidentEdges;
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        EdgeNode<E, V> edge = checkEdge(e);

        if (!edge.contains(v)) {
            return null; /* this edge does not connect vertex v */
        }

        if (edge.vertices()[0] == v) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }

    }

    @Override
    public synchronized boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        //we allow loops, so we do not check if u == v
        checkVertex(v);
        checkVertex(u);

        /* find and edge that contains both u and v */
        for (Edge<E, V> edge : edges.values()) {
            if (((EdgeNode<E, V>) edge).contains(u) && ((EdgeNode<E, V>) edge).contains(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> newVertex = new VertexNode<V>(vElement);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> newVertex = new VertexNode<V>(vElement, elementTypes);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement, ArrowTypes arrowTypes)
            throws InvalidVertexException, InvalidEdgeException {

        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        VertexNode<V> outVertex = checkVertex(u);
        VertexNode<V> inVertex = checkVertex(v);

        EdgeNode<E, V> newEdge = new EdgeNode<E, V>(edgeElement, outVertex, inVertex, arrowTypes);

        edges.put(edgeElement, newEdge);

        return newEdge;

    }

    @Override
    public synchronized Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement, ArrowTypes arrowTypes)
            throws InvalidVertexException, InvalidEdgeException {
        
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        if (!existsVertexWith(vElement1)) {
            throw new InvalidVertexException("No vertex contains " + vElement1);
        }
        if (!existsVertexWith(vElement2)) {
            throw new InvalidVertexException("No vertex contains " + vElement2);
        }

        VertexNode<V> outVertex = vertexOf(vElement1);
        VertexNode<V> inVertex = vertexOf(vElement2);

        EdgeNode<E, V> newEdge = new EdgeNode<E, V>(edgeElement, outVertex, inVertex, arrowTypes);

        edges.put(edgeElement, newEdge);

        return newEdge;

    }

    @Override
    public synchronized V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);

        V element = v.element();

        //remove incident edges
        Iterable<Edge<E, V>> incidentEdges = incidentEdges(v);
        for (Edge<E, V> edge : incidentEdges) {
            edges.remove(edge.element());
        }

        vertices.remove(v.element());

        return element;
    }

    @Override
    public synchronized E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        checkEdge(e);

        E element = e.element();
        edges.remove(e.element());

        return element;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (existsVertexWith(newElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> vertex = checkVertex(v);

        V oldElement = vertex.element;
        vertex.element = newElement;

        return oldElement;
    }

    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        if (existsEdgeWith(newElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        EdgeNode<E, V> edge = checkEdge(e);

        E oldElement = edge.element;
        edge.element = newElement;

        return oldElement;
    }

    private VertexNode<V> vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return (VertexNode<V>) v;
            }
        }
        return null;
    }

    private boolean existsVertexWith(V vElement) {
        return vertices.containsKey(vElement);
    }

    private boolean existsEdgeWith(E edgeElement) {
        return edges.containsKey(edgeElement);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Graph with %d vertices and %d edges:\n", numVertices(), numEdges())
        );

        sb.append("--- Vertices: \n");
        for (Vertex<V> v : vertices.values()) {
            sb.append("\t").append(v.toString()).append("\n");
        }
        sb.append("\n--- Edges: \n");
        for (Edge<E, V> e : edges.values()) {
            sb.append("\t").append(e.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Checks whether a given vertex is valid and belongs to this graph
     *
     * @param v
     * @return
     * @throws InvalidVertexException
     */
    private VertexNode<V> checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");
        
        VertexNode<V> vertex;
        try {
            vertex = (VertexNode<V>) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private EdgeNode<E, V> checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");
        
        EdgeNode<E, V> edge;
        try {
            edge = (EdgeNode<E, V>) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an adge.");
        }

        if (!edges.containsKey(edge.element)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }
}
