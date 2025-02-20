package org.tera201.umlgraph.graphview.edges;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.vertices.UMLVertexNode;

public class EdgeCurve<E, V> extends CubicCurve implements EdgeLineElement<E, V> {

    private static final double MAX_EDGE_CURVE_ANGLE = 20;

    private final Edge<E, V> underlyingEdge;

    private final UMLVertexNode<V> inbound;
    private final UMLVertexNode<V> outbound;

    private DefaultArrow attachedArrow = null;

    private double randomAngleFactor = 0;

    public EdgeCurve(Edge<E, V> edge, UMLVertexNode<V> inbound, UMLVertexNode<V> outbound, int edgeIndex) {
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

        //TODO: improve this solution taking into account even indices, etc.
        randomAngleFactor = edgeIndex == 0 ? 0 : 1.0 / edgeIndex; //Math.random();

        //update();
        enableListeners();
    }

    @Override
    public void addStyleClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return this.getStyleClass().remove(cssClass);
    }

    public void clearStyleClass() {
        this.getStyleClass().clear();
    }

    private void update() {
        if (inbound == outbound) {
            double midpointX1 = outbound.getCenterX() - inbound.getHeight() * 5;
            double midpointY1 = outbound.getCenterY() - inbound.getHeight() * 2;

            double midpointX2 = outbound.getCenterX() + inbound.getHeight() * 5;
            double midpointY2 = outbound.getCenterY() - inbound.getHeight() * 2;

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

            double angle = MAX_EDGE_CURVE_ANGLE;

            double distance = startpoint.distance(endpoint);

            angle = angle - (distance / 1500 * angle);

            midpoint = rotate(midpoint,
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
                atan2(endYProperty().subtract(controlY2Property()),
                        endXProperty().subtract(controlX2Property()))
        ));

        arrow.getTransforms().add(rotation);
    }

    @Override
    public DefaultArrow getAttachedArrow() {
        return this.attachedArrow;
    }
    
    public static Point2D rotate(final Point2D point, final Point2D pivot, double angle_degrees) {
        double angle = Math.toRadians(angle_degrees); //angle_degrees * (Math.PI/180); //to radians

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        Point2D result = point.subtract(pivot);

        Point2D rotatedOrigin = new Point2D(
                result.getX() * cos - result.getY() * sin,
                result.getX() * sin + result.getY() * cos);

        result = rotatedOrigin.add(pivot);

        return result;
    }
}
