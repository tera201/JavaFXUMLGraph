package com.rnar.umlgraph.graphview.edges;

import com.rnar.umlgraph.graphview.arrows.DefaultArrow;
import com.rnar.umlgraph.graphview.*;
import com.rnar.umlgraph.graphview.utils.UtilitiesBindings;
import com.rnar.umlgraph.graphview.utils.UtilitiesPoint2D;
import com.rnar.umlgraph.graphview.vertices.GraphVertex;
import com.rnar.umlgraph.graphview.vertices.GraphVertexPaneNode;
import com.rnar.umlgraph.graphview.labels.Label;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import com.rnar.umlgraph.graph.Edge;

/**
 * Concrete implementation of a curved edge.
 * <br>
 * The edge binds its start point to the <code>outbound</code>
 * {@link GraphVertexPaneNode} center and its end point to the
 * <code>inbound</code> {@link GraphVertexPaneNode} center. As such, the curve
 * is updated automatically as the vertices move.
 * <br>
 * Given there can be several curved edges connecting two vertices, when calling
 * the constructor {@link #SmartGraphEdgeCurve(Edge,
 * GraphVertexPaneNode,
 * GraphVertexPaneNode, int) } the <code>edgeIndex</code>
 * can be specified as to create non-overlaping curves.
 *
 * @param <E> Type stored in the underlying edge
 * @param <V> Type of connecting vertex
 *
 * @author r.naryshkin99
 */
public class EdgeCurve<E, V> extends CubicCurve implements EdgeBase<E, V> {

    private static final double MAX_EDGE_CURVE_ANGLE = 20;

    private final Edge<E, V> underlyingEdge;

    private final GraphVertex<V> inbound;
    private final GraphVertex<V> outbound;

    private Label attachedLabel = null;
    private DefaultArrow attachedArrow = null;

    private double randomAngleFactor = 0;
    
    /* Styling proxy */
    private final StyleProxy styleProxy;

    public EdgeCurve(Edge<E, V> edge, GraphVertex inbound, GraphVertex outbound) {
        this(edge, inbound, outbound, 0);
    }

    public EdgeCurve(Edge<E, V> edge, GraphVertex inbound, GraphVertex outbound, int edgeIndex) {
        this.inbound = inbound;
        this.outbound = outbound;

        this.underlyingEdge = edge;

        styleProxy = new StyleProxy(this);
        switch (edge.getArrowsType()) {
            case DEPENDENCY, REALIZATION -> styleProxy.addStyleClass("edge-dash");
            default -> styleProxy.addStyleClass("edge");
        }


        //bind start and end positions to vertices centers through properties
        this.startXProperty().bind(outbound.centerXProperty());
        this.startYProperty().bind(outbound.centerYProperty());
        this.endXProperty().bind(inbound.centerXProperty());
        this.endYProperty().bind(inbound.centerYProperty());

        //TODO: improve this solution taking into account even indices, etc.
        randomAngleFactor = edgeIndex == 0 ? 0 : 1.0 / edgeIndex; //Math.random();

        //update();
        enableListeners();
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }
    
    private void update() {                
        if (inbound == outbound) {
            /* Make a loop using the control points proportional to the vertex radius */
            
            //TODO: take into account several "self-loops" with randomAngleFactor
            double midpointX1 = outbound.getCenterX() - inbound.getRadius() * 5;
            double midpointY1 = outbound.getCenterY() - inbound.getRadius() * 2;
            
            double midpointX2 = outbound.getCenterX() + inbound.getRadius() * 5;
            double midpointY2 = outbound.getCenterY() - inbound.getRadius() * 2;
            
            setControlX1(midpointX1);
            setControlY1(midpointY1);
            setControlX2(midpointX2);
            setControlY2(midpointY2);
            
        } else {          
            /* Make a curved edge. The curve is proportional to the distance  */
            double midpointX = (outbound.getCenterX() + inbound.getCenterX()) / 2;
            double midpointY = (outbound.getCenterY() + inbound.getCenterY()) / 2;

            Point2D midpoint = new Point2D(midpointX, midpointY);

            Point2D startpoint = new Point2D(inbound.getCenterX(), inbound.getCenterY());
            Point2D endpoint = new Point2D(outbound.getCenterX(), outbound.getCenterY());

            //TODO: improvement lower max_angle_placement according to distance between vertices
            double angle = MAX_EDGE_CURVE_ANGLE;

            double distance = startpoint.distance(endpoint);

            //TODO: remove "magic number" 1500 and provide a distance function for the 
            //decreasing angle with distance
            angle = angle - (distance / 1500 * angle);

            midpoint = UtilitiesPoint2D.rotate(midpoint,
                    startpoint,
                    (-angle) + randomAngleFactor * (angle - (-angle)));

            setControlX1(midpoint.getX());
            setControlY1(midpoint.getY());
            setControlX2(midpoint.getX());
            setControlY2(midpoint.getY());
        }

    }

    /*
    With a curved edge we need to continuously update the control points.
    TODO: Maybe we can achieve this solely with bindings.
    */
    private void enableListeners() {
        this.startXProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            update();
        });
        this.startYProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            update();
        });
        this.endXProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            update();
        });
        this.endYProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            update();
        });
    }

    @Override
    public void attachLabel(Label label) {
        this.attachedLabel = label;
        label.xProperty().bind(controlX1Property().add(controlX2Property()).divide(2).subtract(label.getLayoutBounds().getWidth() / 2));
        label.yProperty().bind(controlY1Property().add(controlY2Property()).divide(2).add(label.getLayoutBounds().getHeight() / 2));
    }

    @Override
    public Label getAttachedLabel() {
        return attachedLabel;
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
        rotation.angleProperty().bind(UtilitiesBindings.toDegrees(
                UtilitiesBindings.atan2(endYProperty().subtract(controlY2Property()),
                        endXProperty().subtract(controlX2Property()))
        ));

        arrow.getTransforms().add(rotation);

        /* add translation transform to put the arrow touching the circle's bounds */
        Translate t = new Translate(-outbound.getRadius(), 0);
        arrow.getTransforms().add(t);
    }

    @Override
    public DefaultArrow getAttachedArrow() {
        return this.attachedArrow;
    }
    
    @Override
    public StylableNode getStylableArrow() {
        return this.attachedArrow;
    }

    @Override
    public StylableNode getStylableLabel() {
        return this.attachedLabel;
    }
}
