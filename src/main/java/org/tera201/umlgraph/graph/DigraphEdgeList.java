package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.*;

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

            if (((Edge<E, V>) edge).getInbound() == inbound) {
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

            if (((Edge<E, V>) edge).getOutbound() == outbound) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement, ArrowTypes arrowTypes) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            return edges.get(edgeElement);
//            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        Vertex<V> outVertex = checkVertex(outbound);
        Vertex<V> inVertex = checkVertex(inbound);

        outVertex.addChild(inVertex);

        Edge<E, V> newEdge = new Edge<>(edgeElement, outVertex, inVertex, arrowTypes);

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
        Edge<E, V> edge = checkEdge(e);

        if (!edge.contains(v)) {
            return null; /* this edge does not connect vertex v */
        }

        if (edge.vertices()[0] == v) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }

    }

    public synchronized Vertex<V> getVertex(V vElement) {
        if (!vertices.containsKey(vElement)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }
        return vertices.get(vElement);
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        Vertex<V> newVertex = new Vertex<>(vElement);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        Vertex<V> newVertex = new Vertex<>(vElement, elementTypes);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        Vertex<V> newVertex = new Vertex<>(vElement, elementTypes, label);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement, ElementTypes elementTypes, String label, String notes) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        Vertex<V> newVertex = new Vertex<>(vElement, elementTypes, label, notes);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    private Vertex<V> vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.getElement().equals(vElement)) {
                return (Vertex<V>) v;
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
    private Vertex<V> checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        Vertex<V> vertex;
        try {
            vertex = (Vertex<V>) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private Edge<E, V> checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        Edge<E, V> edge;
        try {
            edge = (Edge<E, V>) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an adge.");
        }

        if (!edges.containsKey(edge.element)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }
    
}
