package com.rnar.umlgraph.graphview;

import java.util.Collection;
import java.util.Random;
import com.rnar.umlgraph.graph.Graph;

/**
 * Scatters the vertices randomly.
 * 
 * @see SmartPlacementStrategy
 * 
 * @author r.naryshkin99
 */
public class SmartRandomPlacementStrategy implements SmartPlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<? extends SmartGraphVertex<V>> vertices) {
        
        Random rand = new Random();

        for (SmartGraphVertex<V> vertex : vertices) {
            
            double x = rand.nextDouble() * width;
            double y = rand.nextDouble() * height;
                        
            vertex.setPosition(x, y);
          
        }
    }
    
}
