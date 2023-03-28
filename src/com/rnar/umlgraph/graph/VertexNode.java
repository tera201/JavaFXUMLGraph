package com.rnar.umlgraph.graph;

import com.rnar.umlgraph.graphview.vertices.elements.ElementTypes;

public class VertexNode<V> implements Vertex<V> {

    V element;
    ElementTypes elementTypes;

    public VertexNode(V element) {
        this.element = element;
        this.elementTypes = ElementTypes.CLASS;
    }

    public VertexNode(V element, ElementTypes elementTypes) {
        this.element = element;
        this.elementTypes = elementTypes;
    }

    @Override
    public V element() {
        return this.element;
    }

    @Override
    public ElementTypes getType() {
        return this.elementTypes;
    }

    @Override
    public String toString() {
        return "Vertex{" + element + '}';
    }
}
