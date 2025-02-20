package org.tera201.umlgraph.graphview.vertices;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.vertices.elements.Node;

public class UMLVertexNode<T> extends Pane {

    private final Vertex<T> underlyingVertex;

    private boolean isDragging = false;

    private Node nodeElement;

    public UMLVertexNode(Vertex<T> v) {
        super();
        this.underlyingVertex = v;
        this.nodeElement = new Node<>(v);
        nodeElement.addTo(this);
        enableDrag();
    }

    public void setPosition(double x, double y) {
        if (isDragging) {
            return;
        }

        this.setLayoutX(x);
        this.setLayoutY(y);
    }


    private void enableDrag() {

        final DragContext sceneDragContext = new DragContext();

        setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.MOVE);
                isDragging = true;

                mouseEvent.consume();

                sceneDragContext.mouseAnchorX = mouseEvent.getSceneX();
                sceneDragContext.mouseAnchorY = mouseEvent.getSceneY();

                sceneDragContext.translateAnchorX = this.getLayoutX();
                sceneDragContext.translateAnchorY = this.getLayoutY();
            }

        });

        setOnMouseReleased((MouseEvent mouseEvent) -> {
            getScene().setCursor(Cursor.HAND);
            isDragging = false;

            mouseEvent.consume();
        });

        setOnMouseDragged((MouseEvent mouseEvent) -> {
            this.getTranslateX();
            if (mouseEvent.isPrimaryButtonDown()) {
                setManaged(false);
                double deltaX = mouseEvent.getSceneX() - sceneDragContext.mouseAnchorX;
                double deltaY = mouseEvent.getSceneY() - sceneDragContext.mouseAnchorY;
                this.setCenterX(sceneDragContext.translateAnchorX + deltaX);
                this.setCenterY(sceneDragContext.translateAnchorY + deltaY);
                mouseEvent.consume();
            }

        });

        setOnMouseEntered((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }

        });

        setOnMouseExited((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }

        });
    }

    public double getCenterX() {
        return this.getLayoutX();
    }

    public double getCenterY() {
        return this.getLayoutY();
    }

    public void setCenterX(double value) {
        this.setLayoutX(value);
    }

    public void setCenterY(double value) {
        this.setLayoutY(value);
    }

    public DoubleBinding centerXProperty() {
        return this.layoutXProperty().add(this.widthProperty().divide(2));
    }

    public DoubleBinding centerYProperty() {
        return this.layoutYProperty().add(this.heightProperty().divide(2));
    }

    public Vertex<T> getUnderlyingVertex() {
        return underlyingVertex;
    }

    public void addStyleClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }

    public void clearStyleClass() {
        this.getStyleClass().clear();
    }

    public boolean removeStyleClass(String cssClass) {
        return this.getStyleClass().remove(cssClass);
    }

    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }
}
