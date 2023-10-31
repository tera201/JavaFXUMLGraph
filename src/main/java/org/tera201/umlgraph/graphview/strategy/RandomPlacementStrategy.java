package org.tera201.umlgraph.graphview.strategy;

import java.util.Map;
import java.util.Random;

import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

/**
 * Scatters the vertices randomly.
 * 
 * @see PlacementStrategy
 * 
 * @author r.naryshkin99
 */
public class RandomPlacementStrategy implements PlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        
        Random rand = new Random();

        for (GraphVertex<V> vertex : vertexNodes.values()) {
            
            double x = rand.nextDouble() * width;
            double y = rand.nextDouble() * height;
                        
            vertex.setPosition(x, y);
          
        }
    }
    
}
