package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

public class VertexNode<V> implements Vertex<V> {

    V element;
    ElementTypes elementTypes;
    String notes = "";

    public VertexNode(V element) {
        this.element = element;
        this.elementTypes = ElementTypes.CLASS;
    }

    public VertexNode(V element, ElementTypes elementTypes) {
        this.element = element;
        this.elementTypes = elementTypes;
    }

    public VertexNode(V element, ElementTypes elementTypes, String notes) {
        this(element, elementTypes);
        this.notes = notes;
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
    public String getNotes() {
        return this.notes;
    }

    @Override
    public String toString() {
        return "Vertex{" + element + '}';
    }
}
