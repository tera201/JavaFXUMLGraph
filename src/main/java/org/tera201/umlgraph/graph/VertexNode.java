package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class VertexNode<V> implements Vertex<V>{

    V element;
    ElementTypes elementTypes;
    String notes = "";
    List<Vertex<V>> childs;
    Vertex<V> parent;
    int depth;

    public VertexNode(V element) {
        this(element, ElementTypes.CLASS);
    }

    public VertexNode(V element, ElementTypes elementTypes) {
        this.element = element;
        this.elementTypes = elementTypes;
        this.childs = new ArrayList<>();
        this.depth = 0;
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
    public List<Vertex<V>> getChilds() {
        return childs;
    }
    @Override
    public void addChild(Vertex<V> child) {
        if (parent != child && this != child) {
            this.childs.add(child);
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

    @Override
    public void setParent(Vertex<V> parent) {
        this.parent = parent;
    }

    @Override
    public Vertex<V> getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return "Vertex{" + element + '}';
    }

    @Override
    public void incDepth() {
        this.depth += 1;
        if (this.parent != null) {
            this.parent.incDepth();
        }

    }
    @Override
    public int getDepth() {
        return this.depth;
    }
}