package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.Objects;

public class Edge<E, V> {

    E element;
    Vertex<V> vertexOutbound;
    Vertex<V> vertexInbound;

    ArrowTypes arrowType;

    public Edge(E element, Vertex<V> vertexOutbound, Vertex<V> vertexInbound, ArrowTypes arrType) {
        this.element = element;
        this.vertexOutbound = vertexOutbound;
        this.vertexInbound = vertexInbound;
        this.arrowType = Objects.requireNonNullElse(arrType, ArrowTypes.AGGREGATION);;
    }

    public E element() {
        return this.element;
    }

    public ArrowTypes getArrowsType() {
        return this.arrowType;
    }

    public boolean contains(Vertex<V> v) {
        return (vertexOutbound == v || vertexInbound == v);
    }

    public Vertex<V>[] vertices() {
        Vertex[] vertices = new Vertex[2];
        vertices[0] = vertexOutbound;
        vertices[1] = vertexInbound;

        return vertices;
    }

    @Override
    public String toString() {
        return "Edge{{" + element + "}, vertexOutbound=" + vertexOutbound.toString()
                + ", vertexInbound=" + vertexInbound.toString() + '}';
    }

    public Vertex<V> getOutbound() {
        return vertexOutbound;
    }

    public Vertex<V> getInbound() {
        return vertexInbound;
    }
}
