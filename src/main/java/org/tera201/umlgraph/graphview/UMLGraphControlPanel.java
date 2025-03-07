package org.tera201.umlgraph.graphview;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;

import java.awt.*;

public class UMLGraphControlPanel<V,E> extends BorderPane {
    private final UMLGraphPanel<V,E> graph;
    private final DoubleProperty scaleFactorProperty = new ReadOnlyDoubleWrapper(1);
    private final Slider slider = new Slider(MIN_SCALE, MAX_SCALE, MIN_SCALE);
    private final VBox paneSlider = new VBox(slider);
    Button resetPlaceButton = new Button("", new FontIcon(MaterialDesignR.RESTART));
    Button resetView = new Button("", new FontIcon(MaterialDesignI.IMAGE_FILTER_CENTER_FOCUS));

    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 2;
    private static final double SCROLL_DELTA = 0.05;

    public UMLGraphControlPanel(UMLGraphPanel<V,E> graph) {
        Tooltip resetTooltip = new Tooltip("Reset the graph elements to their starting positions.");
        Tooltip defaultViewTooltip = new Tooltip("Return the view to its default position.");
        this.graph = graph;
        setCenter(graph);
        setRight(updateSlider());
        resetView.setTooltip(defaultViewTooltip);
        resetPlaceButton.setTooltip(resetTooltip);
        resetPlaceButton.setOnAction(e -> this.graph.resetPlaceStrategy());
        resetView.setOnAction(event -> resetView());
        paneSlider.getChildren().add(resetPlaceButton);
        paneSlider.getChildren().add(resetView);
        enablePanAndZoom();
    }

    public void setBackgroundColor(Color c) {
        this.setStyle("-fx-background-color: rgb(%d, %d, %d);".formatted(c.getRed(), c.getGreen(), c.getBlue()));
    }

    public void setButtonColor(Color c) {
        resetPlaceButton.setStyle("-fx-background-color: rgb(%d, %d, %d);".formatted(c.getRed(), c.getGreen(), c.getBlue()));
        resetView.setStyle("-fx-background-color: rgb(%d, %d, %d);".formatted(c.getRed(), c.getGreen(), c.getBlue()));
    }

    public void resetView() {
        graph.setScaleX(1);
        graph.setScaleY(1);
        graph.setTranslateX(0);
        graph.setTranslateY(0);
        scaleFactorProperty.setValue(1);
    }

    public void update() {
        Platform.runLater(() -> {
            this.getChildren().remove(graph);
            this.getChildren().add(graph);
            this.getChildren().remove(paneSlider);
            this.setRight(paneSlider);
        });
    }

    private Node updateSlider() {
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(SCROLL_DELTA);
        slider.setPrefHeight(200);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(0.125f);
        slider.setSnapToTicks(true);

        paneSlider.setPadding(new Insets(10, 10, 10, 10));
        paneSlider.setSpacing(10);

        slider.valueProperty().bindBidirectional(this.scaleFactorProperty());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scale = newValue.doubleValue();
            graph.setScaleX(scale);
            graph.setScaleY(scale);
            scaleFactorProperty.setValue(scale);
        });

        return paneSlider;
    }

    public void setContentPivot(double x, double y) {
        graph.setTranslateX(graph.getTranslateX() - x);
        graph.setTranslateY(graph.getTranslateY() - y);
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

                graph.setScaleX(computedScale);
                graph.setScaleY(computedScale);
                scaleFactorProperty.setValue(computedScale);

                Bounds bounds = graph.localToScene(graph.getBoundsInLocal());
                double f = (computedScale / currentScale) - 1;
                double dx = (event.getX() - (bounds.getWidth() / 2 + bounds.getMinX()));
                double dy = (event.getY() - (bounds.getHeight() / 2 + bounds.getMinY()));

                setContentPivot(f * dx, f * dy);
//                }

            }
            //do not propagate
            event.consume();

        });

        final UMLGraphControlPanel.DragContext sceneDragContext = new UMLGraphControlPanel.DragContext();

        setOnMousePressed((MouseEvent event) -> {

            if (event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.MOVE);

                sceneDragContext.mouseAnchorX = event.getX();
                sceneDragContext.mouseAnchorY = event.getY();

                sceneDragContext.translateAnchorX = graph.getTranslateX();
                sceneDragContext.translateAnchorY = graph.getTranslateY();
            }

        });

        setOnMouseReleased((MouseEvent event) -> {
            getScene().setCursor(Cursor.DEFAULT);
        });

        setOnMouseDragged((MouseEvent event) -> {
            if (event.isPrimaryButtonDown()) {

                graph.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
                graph.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);
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
