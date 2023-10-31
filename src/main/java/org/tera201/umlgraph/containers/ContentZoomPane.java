package org.tera201.umlgraph.containers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This class provides zooming and panning for a JavaFX node.
 *
 * It shows the zoom level with a slider control and reacts to mouse scrolls and
 * mouse dragging.
 *
 * The content node is out forward in the z-index so it can react to mouse
 * events first. The node should consume any event not meant to propagate to
 * this pane.
 *
 * @author r.naryshkin99
 */
public class ContentZoomPane extends BorderPane {

    /*
    PAN AND ZOOM
     */
    private final DoubleProperty scaleFactorProperty = new ReadOnlyDoubleWrapper(1);
    private final Node content;

    private static final double MIN_SCALE = 0.2;
    private static final double MAX_SCALE = 5;
    private static final double SCROLL_DELTA = 0.05;

    public ContentZoomPane(Node content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }

        this.content = content;

        Node center = content;
        content.toFront();

        setCenter(center);
        setRight(createSlider());

        enablePanAndZoom();
    }

    private Node createSlider() {

        Slider slider = new Slider(MIN_SCALE, MAX_SCALE, MIN_SCALE);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(SCROLL_DELTA);
        slider.setPrefHeight(200);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(0.125f);
        slider.setSnapToTicks(true);

        Text label = new Text("Zoom");

        VBox paneSlider = new VBox(slider, label);

        paneSlider.setPadding(new Insets(10, 10, 10, 10));
        paneSlider.setSpacing(10);

        slider.valueProperty().bindBidirectional(this.scaleFactorProperty());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scale = newValue.doubleValue();
            content.setScaleX(scale);
            content.setScaleY(scale);
            scaleFactorProperty.setValue(scale);
        });

        return paneSlider;
    }

    public void setContentPivot(double x, double y) {
        content.setTranslateX(content.getTranslateX() - x);
        content.setTranslateY(content.getTranslateY() - y);
    }

    public static double boundValue(double value, double min, double max) {

        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    private void enablePanAndZoom() {

        setOnScroll((ScrollEvent event) -> {

            double direction = event.getDeltaY() >= 0 ? 1 : -1;

            double currentScale = scaleFactorProperty.getValue();
            double computedScale = currentScale + direction * SCROLL_DELTA;

            computedScale = boundValue(computedScale, MIN_SCALE, MAX_SCALE);

            if (currentScale != computedScale) {

                content.setScaleX(computedScale);
                content.setScaleY(computedScale);

//                if (computedScale == 1) {
//                    content.setTranslateX(-getTranslateX());
//                    content.setTranslateY(-getTranslateY());
//                } else {
                    scaleFactorProperty.setValue(computedScale);

                    Bounds bounds = content.localToScene(content.getBoundsInLocal());
                    double f = (computedScale / currentScale) - 1;
                    double dx = (event.getX() - (bounds.getWidth() / 2 + bounds.getMinX()));
                    double dy = (event.getY() - (bounds.getHeight() / 2 + bounds.getMinY()));

                    setContentPivot(f * dx, f * dy);
//                }

            }
            //do not propagate
            event.consume();

        });

        final DragContext sceneDragContext = new DragContext();

        setOnMousePressed((MouseEvent event) -> {

            if (event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.MOVE);

                sceneDragContext.mouseAnchorX = event.getX();
                sceneDragContext.mouseAnchorY = event.getY();

                sceneDragContext.translateAnchorX = content.getTranslateX();
                sceneDragContext.translateAnchorY = content.getTranslateY();
            }

        });

        setOnMouseReleased((MouseEvent event) -> {
            getScene().setCursor(Cursor.DEFAULT);
        });

        setOnMouseDragged((MouseEvent event) -> {
            if (event.isPrimaryButtonDown()) {

                content.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
                content.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);
            }
        });

    }

    public DoubleProperty scaleFactorProperty() {
        return scaleFactorProperty;
    }

    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }

}
