package com.rnar.umlgraph.graph;

/**
 * Error when using an invalid vertex in calls of methods in {@link Graph}
 * and {@link Digraph} implementations.
 * 
 * @see Graph
 * @see Digraph
 */
public class InvalidVertexException extends RuntimeException {

    public InvalidVertexException() {
        super("The vertex is invalid or does not belong to this graph.");
    }

    public InvalidVertexException(String string) {
        super(string);
    }
    
}
