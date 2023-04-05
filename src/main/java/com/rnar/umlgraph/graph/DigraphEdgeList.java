package com.rnar.umlgraph.graph;

import com.rnar.umlgraph.graphview.vertices.elements.ElementTypes;
import com.rnar.umlgraph.graphview.arrows.ArrowTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a digraph that adheres to the {@link Digraph} interface.
 * <br>
 * Does not allow duplicates of stored elements through <b>equals</b> criteria.
 * <br>
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * 
 * @author r.naryshkin99
 */
public class DigraphEdgeList<V, E> implements Digraph<V, E> {

     /* inner classes are defined at the end of the class, so are the auxiliary methods 
     */
    private final Map<V, Vertex<V>> vertices;
    private final Map<E, Edge<E, V>> edges;

    public DigraphEdgeList() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }
    
    
    @Override
    public synchronized Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        checkVertex(inbound);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((EdgeNode<E, V>) edge).getInbound() == inbound) {
                incidentEdges.add(edge);
            }
        }
        return incidentEdges;
    }
    
    @Override
    public synchronized Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        checkVertex(outbound);

        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((EdgeNode<E, V>) edge).getOutbound() == outbound) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
    }

    @Override
    public boolean areAdjacent(Vertex<V> outbound, Vertex<V> inbound) throws InvalidVertexException {
        //we allow loops, so we do not check if outbound == inbound
        checkVertex(outbound);
        checkVertex(inbound);

        /* find and edge that goes outbound ---> inbound */
        for (Edge<E, V> edge : edges.values()) {
            if (((EdgeNode<E, V>) edge).getOutbound() == outbound && ((EdgeNode<E, V>) edge).getInbound() == inbound) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement, ArrowTypes arrowTypes) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        VertexNode<V> outVertex = checkVertex(outbound);
        VertexNode<V> inVertex = checkVertex(inbound);

        EdgeNode<E, V> newEdge = new EdgeNode<>(edgeElement, outVertex, inVertex, arrowTypes);

        edges.put(edgeElement, newEdge);

        return newEdge;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(V outboundElement, V inboundElement, E edgeElement, ArrowTypes arrowTypes) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        if (!existsVertexWith(outboundElement)) {
            throw new InvalidVertexException("No vertex contains " + outboundElement);
        }
        if (!existsVertexWith(inboundElement)) {
            throw new InvalidVertexException("No vertex contains " + inboundElement);
        }

        VertexNode<V> outVertex = vertexOf(outboundElement);
        VertexNode<V> inVertex = vertexOf(inboundElement);

        EdgeNode<E, V> newEdge = new EdgeNode<>(edgeElement, outVertex, inVertex, arrowTypes);

        edges.put(edgeElement, newEdge);

        return newEdge;
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
    public synchronized Collection<Vertex<V>> vertices() {
        List<Vertex<V>> list = new ArrayList<>();
        vertices.values().forEach((v) -> {
            list.add(v);
        });
        return list;
    }

    @Override
    public synchronized Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> list = new ArrayList<>();
        edges.values().forEach((e) -> {
            list.add(e);
        });
        return list;
    }

    @Override
    public synchronized Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
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
    public synchronized Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> newVertex = new VertexNode<>(vElement);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> newVertex = new VertexNode<>(vElement, elementTypes);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String notes) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        VertexNode<V> newVertex = new VertexNode<>(vElement, elementTypes, notes);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);

        V element = v.element();

        //remove incident edges
        Collection<Edge<E, V>> inOutEdges = incidentEdges(v);
        inOutEdges.addAll(outboundEdges(v));
        
        for (Edge<E, V> edge : inOutEdges) {
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
