package com.rnar.umlgraph.graphview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.geometry.Point2D;
import com.rnar.umlgraph.graph.Graph;

/**
 * Places vertices around a circle, ordered by the underlying
 * vertices {@code element.toString() value}.
 * 
 * @see SmartPlacementStrategy
 * 
 * @author r.naryshkin99
 */
public class SmartCircularSortedPlacementStrategy implements SmartPlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<? extends SmartGraphVertex<V>> vertices) {
        Point2D center = new Point2D(width / 2, height / 2);
        int N = vertices.size();
        double angleIncrement = -360f / N;
        
        //place first vertice north position, others in clockwise manner
        boolean first = true;
        Point2D p = null;
        for (SmartGraphVertex<V> vertex : sort(vertices)) {
            
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
    
    protected <V> Collection<SmartGraphVertex<V>> sort(Collection<? extends SmartGraphVertex<V>> vertices) {
        
        List<SmartGraphVertex<V>> list = new ArrayList<>();
        list.addAll(vertices);
        
        Collections.sort(list, new Comparator<SmartGraphVertex<V>>() {
            @Override
            public int compare(SmartGraphVertex<V> t, SmartGraphVertex<V> t1) {
                return t.getUnderlyingVertex().element().toString().compareToIgnoreCase(t1.getUnderlyingVertex().element().toString());
            }
        });
        
        return list;
    }
}
