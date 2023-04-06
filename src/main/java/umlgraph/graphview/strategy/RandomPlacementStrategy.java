package umlgraph.graphview.strategy;

import java.util.Collection;
import java.util.Random;

import umlgraph.graph.Graph;
import umlgraph.graphview.vertices.GraphVertex;

/**
 * Scatters the vertices randomly.
 * 
 * @see PlacementStrategy
 * 
 * @author r.naryshkin99
 */
public class RandomPlacementStrategy implements PlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<? extends GraphVertex<V>> vertices) {
        
        Random rand = new Random();

        for (GraphVertex<V> vertex : vertices) {
            
            double x = rand.nextDouble() * width;
            double y = rand.nextDouble() * height;
                        
            vertex.setPosition(x, y);
          
        }
    }
    
}
