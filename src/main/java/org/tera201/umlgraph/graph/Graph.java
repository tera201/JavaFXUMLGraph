package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Graph<V, E> {
    private final Map<V, Vertex<V>> vertices;
    private final Map<E, Edge<E, V>> edges;

    public Graph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }

    public Collection<Edge<E, V>> GetIncidentEdges(Vertex<V> inbound) throws GraphException {
        checkVertex(inbound);
        return edges.values().stream().filter((edge) -> (edge.getInbound() == inbound)).toList();
    }

    public Collection<Edge<E, V>> GetOutboundEdges(Vertex<V> outbound) throws GraphException {
        checkVertex(outbound);
        return edges.values().stream().filter((edge) -> (edge.getOutbound() == outbound)).toList();
    }

    public Edge<E, V> getOrCreateEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement, ArrowTypes arrowTypes) throws GraphException {
        if (existsEdgeWith(edgeElement)) {
            return edges.get(edgeElement);
        }

        checkVertex(outbound);
        checkVertex(inbound);
        outbound.addChild(inbound);
        Edge<E, V> newEdge = new Edge<>(edgeElement, outbound, inbound, arrowTypes);
        edges.put(edgeElement, newEdge);
        return newEdge;
    }

    public int numVertices() {
        return vertices.size();
    }

    public int numEdges() {
        return edges.size();
    }

    public Collection<Vertex<V>> getVertices() {
        return vertices.values();
    }

    public Collection<Edge<E, V>> getEdges() {
        return edges.values();
    }

    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws GraphException {
        checkVertex(v);
        checkEdge(e, v);
        return (Objects.equals(e.vertices()[0], v)) ? e.vertices()[1] : e.vertices()[0];
    }

    public Vertex<V> getVertex(V vElement) {
        if (!existsVertexWith(vElement)) {
            throw new GraphException("Vertex does not belong to this graph.");
        }
        return vertices.get(vElement);
    }

    public Vertex<V> getOrCreateVertex(V vElement) throws GraphException {
        return getOrCreateVertex(vElement, null);
    }

    public Vertex<V> getOrCreateVertex(V vElement, ElementTypes elementTypes) throws GraphException {
        return getOrCreateVertex(vElement, elementTypes, vElement.toString());
    }

    public Vertex<V> getOrCreateVertex(V vElement, ElementTypes elementTypes, String label) throws GraphException {
        return getOrCreateVertex(vElement, elementTypes, label, "");
    }

    public Vertex<V> getOrCreateVertex(V vElement, ElementTypes elementTypes, String label, String notes) throws GraphException {
        if (existsVertexWith(vElement)) {
            return vertices.get(vElement);
        }

        Vertex<V> newVertex = new Vertex<>(vElement, elementTypes, label, notes);
        vertices.put(vElement, newVertex);
        return newVertex;
    }

    private boolean existsVertexWith(V vElement) {
        return vertices.containsKey(vElement);
    }

    private boolean existsEdgeWith(E edgeElement) {
        return edges.containsKey(edgeElement);
    }

    private void checkVertex(Vertex<V> v) throws GraphException {
        Objects.requireNonNull(v, "Vertex cannot be null");
    }

    private void checkEdge(Edge<E, V> e, Vertex<V> v) throws GraphException {
        Objects.requireNonNull(e, "Edge cannot be null");
        if (v != null && !e.contains(v)) throw new GraphException("Vertex is not incident to this edge.");
    }

    @Override
    public String toString() {
        return "Graph with " + numVertices() + " vertices and " + numEdges() + " edges:\n"
                + "--- Vertices: \n" + vertices.values().stream().map(Vertex::toString).collect(Collectors.joining("\n"))
                + "\n--- Edges: \n" + edges.values().stream().map(Edge::toString).collect(Collectors.joining("\n"));
    }
}
