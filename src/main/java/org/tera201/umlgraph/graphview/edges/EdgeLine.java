package org.tera201.umlgraph.graphview.edges;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.vertices.UMLVertexNode;


public class EdgeLine<E, V> extends Line implements EdgeLineElement<E, V> {

    private final Edge<E, V> underlyingEdge;

    private final UMLVertexNode<V> inbound;
    private final UMLVertexNode<V> outbound;

    private DefaultArrow attachedArrow = null;

    public EdgeLine(Edge<E, V> edge, UMLVertexNode<V> inbound, UMLVertexNode<V> outbound) {
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
    public void addStyleClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return this.getStyleClass().remove(cssClass);
    }

    @Override
    public void clearStyleClass() {
        this.getStyleClass().clear();
    }

    @Override
    public Edge<E, V> getUnderlyingEdge() {
        return underlyingEdge;
    }

    @Override
    public void attachArrow(DefaultArrow arrow) {
        this.attachedArrow = arrow;
        if (underlyingEdge.getArrowsType() == ArrowTypes.COMPOSITION) attachedArrow.setStyleClass("arrow-white");

        DoubleBinding angle = atan2( endYProperty().subtract(startYProperty()),
                endXProperty().subtract(startXProperty()));
        DoubleBinding dx = inbound.widthProperty().divide(2).divide(abs(cos(angle)));
        DoubleBinding dy = inbound.heightProperty().divide(2).divide(abs(sin(angle)));
        DoubleBinding t = min(dx, dy);
        arrow.translateXProperty().bind(inbound.centerXProperty().subtract(t.multiply(cos(angle))));
        arrow.translateYProperty().bind(inbound.centerYProperty().subtract(t.multiply(sin(angle))));

        /* rotate arrow around itself based on this line's angle */
        Rotate rotation = new Rotate();
        rotation.pivotXProperty().bind(translateXProperty());
        rotation.pivotYProperty().bind(translateYProperty());
        rotation.angleProperty().bind(toDegrees(angle));

        arrow.getTransforms().add(rotation);

    }

    @Override
    public DefaultArrow getAttachedArrow() {
        return this.attachedArrow;
    }
}
