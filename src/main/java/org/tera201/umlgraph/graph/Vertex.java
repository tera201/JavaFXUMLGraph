package org.tera201.umlgraph.graph;

import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

import java.util.List;

/**
 * A vertex contains an element of type <code>V</code> and is used both in
 * graphs and digraphs.
 * 
 * @param <V> Type of value stored in the vertex.
 * 
 * @see Graph
 * @see Digraph
 */

public interface Vertex<V> {
    
    /**
     * Returns the element stored in the vertex.
     * 
     * @return      stored element
     */

    public V element();

    String getLabel();

    public ElementTypes getType();

    public String getNotes();

    public List<Vertex<V>> getChilds();

    void addChild(Vertex<V> child);

    void setParent(Vertex<V> parent);

    Vertex<V> getParent();
    void incDepth();
    public int getDepth();
}
