package com.rnar.umlgraph.graph;

/**
 * Error when using an invalid edge in calls of methods in {@link Graph}
 * and {@link Digraph} implementations.
 * 
 * @see Graph
 * @see Digraph
 */
public class InvalidEdgeException extends RuntimeException {

    public InvalidEdgeException() {
        super("The edge is invalid or does not belong to this graph.");
    }

    public InvalidEdgeException(String string) {
        super(string);
    }
    
}
