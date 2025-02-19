package org.tera201.umlgraph.graphview.edges;

import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.tera201.umlgraph.graph.Edge;


/**
 * Implementation of a straight line edge.
 * 
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 * 
 * @author r.naryshkin99
 */
public class EdgeLine<E, V> extends Line implements EdgeLineElement<E, V> {

    private final Edge<E, V> underlyingEdge;

    private final GraphVertex<V> inbound;
    private final GraphVertex<V> outbound;

    private DefaultArrow attachedArrow = null;

    public EdgeLine(Edge<E, V> edge, GraphVertex<V> inbound, GraphVertex<V> outbound) {
        if( inbound == null || outbound == null) {
            throw new IllegalArgumentException("Cannot connect null vertices.");
        }

        this.inbound = inbound;
        this.outbound = outbound;
        
        this.underlyingEdge = edge;
        
        switch (edge.getArrowsType()) {
            case DEPENDENCY, REALIZATION:
                addStyleClass("edge-dash");
                break;
            default: addStyleClass("edge");
        }
        
        this.startXProperty().bind(outbound.centerXProperty());
        this.startYProperty().bind(outbound.centerYProperty());
        this.endXProperty().bind(inbound.centerXProperty());
        this.endYProperty().bind(inbound.centerYProperty());
    }

    @Override
    public void setStyleClass(String cssClass) {
        cleanStyleClass();
        addStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return this.getStyleClass().remove(cssClass);
    }

    @Override
    public void cleanStyleClass() {
        this.getStyleClass().clear();
    }

    @Override
    public Edge<E, V> getUnderlyingEdge() {
        return underlyingEdge;
    }

    @Override
    public void attachArrow(DefaultArrow arrow) {
        this.attachedArrow = arrow;
        
        /* attach arrow to line's endpoint */
        arrow.translateXProperty().bind(endXProperty());
        arrow.translateYProperty().bind(endYProperty());
        
        /* rotate arrow around itself based on this line's angle */
        Rotate rotation = new Rotate();
        rotation.pivotXProperty().bind(translateXProperty());
        rotation.pivotYProperty().bind(translateYProperty());
        rotation.angleProperty().bind(toDegrees(
                atan2( endYProperty().subtract(startYProperty()),
                endXProperty().subtract(startXProperty()))
        ));
        
        arrow.getTransforms().add(rotation);
        
        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(- outbound.getRadius(), 0);
        arrow.getTransforms().add(t);
        
    }

    @Override
    public DefaultArrow getAttachedArrow() {
        return this.attachedArrow;
    }
    
}
