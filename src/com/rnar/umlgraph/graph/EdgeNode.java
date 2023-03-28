package com.rnar.umlgraph.graph;

import com.rnar.umlgraph.arrows.ArrowTypes;

public class EdgeNode<E, V>  implements Edge<E, V> {

    E element;
    Vertex<V> vertexOutbound;
    Vertex<V> vertexInbound;

    ArrowTypes arrowType;

    public EdgeNode(E element, Vertex<V> vertexOutbound, Vertex<V> vertexInbound, ArrowTypes arrType) {
        this.element = element;
        this.vertexOutbound = vertexOutbound;
        this.vertexInbound = vertexInbound;
        this.arrowType = arrType;
    }

    @Override
    public E element() {
        return this.element;
    }

    @Override
    public ArrowTypes getArrowsType() {
        return this.arrowType;
    }

    public boolean contains(Vertex<V> v) {
        return (vertexOutbound == v || vertexInbound == v);
    }

    @Override
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
