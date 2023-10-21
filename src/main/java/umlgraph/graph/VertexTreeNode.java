package umlgraph.graph;

import umlgraph.graph.VertexTree;
import umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class VertexTreeNode<V> implements VertexTree<V> {

    V element;
    ElementTypes elementTypes;
    String notes = "";
    List<VertexTree<V>> childs;
    VertexTree<V> parent;
    int depth;

    public VertexTreeNode(V element) {
        this(element, ElementTypes.CLASS);
    }

    public VertexTreeNode(V element, ElementTypes elementTypes) {
        this.element = element;
        this.elementTypes = elementTypes;
        this.childs = new ArrayList<>();
        this.depth = 0;
    }

    public VertexTreeNode(V element, ElementTypes elementTypes, String notes) {
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
    public List<VertexTree<V>> getChilds() {
        return childs;
    }
    @Override
    public void addChild(VertexTree<V> child) {
        if (parent != child && this != child) {
//            System.out.println("PASS " + parent + "  " + this + "  " + child);
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
    public void setParent(VertexTree<V> parent) {
        this.parent = parent;
    }

    @Override
    public VertexTree<V> getParent() {
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
//            System.out.println(this.parent.element());
//            System.out.println(this + "  " + this.childs);
            this.parent.incDepth();
        }

    }
    @Override
    public int getDepth() {
        return this.depth;
    }
}
