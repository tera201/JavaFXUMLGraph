package org.tera201.umlgraph.graphview.strategy;

import java.util.*;

import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.utils.UtilitiesPoint2D;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import javafx.geometry.Point2D;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

/**
 * Places vertices around a circle, ordered by the underlying
 * vertices {@code element.toString() value}.
 * 
 * @see PlacementStrategy
 * 
 * @author r.naryshkin99
 */
public class CircularSortedPlacementStrategy implements PlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        Point2D center = new Point2D(width / 2, height / 2);
        int N = vertexNodes.values().size();
        double angleIncrement = -360f / N;
        
        //place first vertice north position, others in clockwise manner
        boolean first = true;
        Point2D p = null;
        for (GraphVertex<V> vertex : sort(vertexNodes.values())) {
            
            if (first) {
                //verifiy smaller width and height.
                if(width > height)
                    p = new Point2D(center.getX(),
                            center.getY() - height / 2 + vertex.getRadius() * 2);
                else
                    p = new Point2D(center.getX(),
                            center.getY() - width / 2 + vertex.getRadius() * 2);
                
        
                first = false;
            } else {
                p = UtilitiesPoint2D.rotate(p, center, angleIncrement);
            }

            vertex.setPosition(p.getX(), p.getY());
            
        }
    }
    
    protected <V> Collection<GraphVertex<V>> sort(Collection<? extends GraphVertex<V>> vertices) {
        
        List<GraphVertex<V>> list = new ArrayList<>();
        list.addAll(vertices);
        
        Collections.sort(list, new Comparator<GraphVertex<V>>() {
            @Override
            public int compare(GraphVertex<V> t, GraphVertex<V> t1) {
                return t.getUnderlyingVertex().element().toString().compareToIgnoreCase(t1.getUnderlyingVertex().element().toString());
            }
        });
        
        return list;
    }
}
