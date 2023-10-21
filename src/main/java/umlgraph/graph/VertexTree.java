package umlgraph.graph;

import umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.List;

public interface VertexTree<V>  extends Vertex<V> {


    /**
     * Returns the element stored in the vertex.
     *
     * @return      stored element
     */

    public V element();

    public ElementTypes getType();

    public String getNotes();

    public List<VertexTree<V>> getChilds();

    void addChild(VertexTree<V> child);

    void setParent(VertexTree<V> parent);

    VertexTree<V> getParent();
    void incDepth();
    public int getDepth();
}
