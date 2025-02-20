package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;

public class Vertex<V> {

    V element;
    String label;
    ElementTypes elementTypes;
    String notes = "";
    List<Vertex<V>> children;
    Vertex<V> parent;
    int depth;

    public Vertex(V element) {
        this(element, null);
    }

    public Vertex(V element, ElementTypes elementTypes) {
        this(element, elementTypes, element.toString());
    }


    public Vertex(V element, ElementTypes elementTypes, String label) {
        this(element, elementTypes, label, "");
    }

    public Vertex(V element, ElementTypes elementTypes, String label, String notes) {
        this.element = element;
        this.elementTypes = Objects.requireNonNullElse(elementTypes, ElementTypes.CLASS);
        this.label = label;
        this.children = new ArrayList<>();
        this.depth = 0;
        this.notes = notes;
    }

    public V getElement() {
        return this.element;
    }

    public String getLabel() {
        return this.label;
    }

    public ElementTypes getType() {
        return this.elementTypes;
    }

    public String getNotes() {
        return this.notes;
    }

    public List<Vertex<V>> getChildren() {
        return children;
    }

    public void addChild(Vertex<V> child) {
        if (parent != child && this != child) {
            this.children.add(child);
            child.setParent(this);
            if (depth == 0) {
                if (this.parent != null) {
                    this.parent.incDepth();
                }
                this.depth = 1;
            }
            this.depth = max(depth,1);
        }
    }

    public void setParent(Vertex<V> parent) {
        this.parent = parent;
    }

    public Vertex<V> getParent() {
        return this.parent;
    }

    public String toString() {
        return "Vertex{" + element + '}';
    }

    public void incDepth() {
        this.depth += 1;
        if (this.parent != null) {
            this.parent.incDepth();
        }

    }
    public int getDepth() {
        return this.depth;
    }
}