package org.tera201.umlgraph.graphview.vertices;

import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.GraphPanel;
import org.tera201.umlgraph.graphview.StyleProxy;
import org.tera201.umlgraph.graphview.vertices.elements.Node;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal implementation of a graph vertex for the {@link GraphPanel}
 * class.
 * <br>
 * Visually it depicts a vertex as a circle, extending from {@link Circle}.
 * <br>
 * The vertex internally deals with mouse drag events that visually move
 * it in the {@link GraphPanel} when displayed, if parameterized to do so.
 * 
 * 
 *
 * @param <T> the type of the underlying vertex
 *
 * @see GraphPanel
 *
 * @author r.naryshkin99
 */
public class GraphVertexPaneNode<T> extends Pane implements GraphVertex<T> {

    private final Vertex<T> underlyingVertex;
    /* Critical for performance, so we don't rely on the efficiency of the Graph.areAdjacent method */
    private final Set<GraphVertexPaneNode<T>> adjacentVertices;

    private boolean isDragging = false;

    /* Styling proxy */
    private StyleProxy styleProxy = null;
    private Node nodeElement;

    /**
     * Constructor which sets the instance attributes.
     *
     * @param v the underlying vertex
     * @param x initial x position on the parent pane
     * @param y initial y position on the parent pane
     * @param radius radius of this vertex representation, i.e., a circle
     * @param allowMove should the vertex be draggable with the mouse
     */
    public GraphVertexPaneNode(Vertex<T> v, double x, double y, double radius, boolean allowMove) {
        super();

        this.underlyingVertex = v;
        this.isDragging = false;
        this.nodeElement = new Node<>(v);


        nodeElement.addTo(this);

        this.adjacentVertices = new HashSet<>();

        styleProxy = new StyleProxy(this);

        if (allowMove) {
            enableDrag();
        }
    }
    
    /**
     * Adds a vertex to the internal list of adjacent vertices.
     *
     * @param v vertex to add
     */
    public void addAdjacentVertex(GraphVertexPaneNode<T> v) {
        this.adjacentVertices.add(v);
    }

    /**
     * Removes a vertex from the internal list of adjacent vertices.
     *
     * @param v vertex to remove
     * @return true if <code>v</code> existed; false otherwise.
     */
    public boolean removeAdjacentVertex(GraphVertexPaneNode<T> v) {
        return this.adjacentVertices.remove(v);
    }

    /**
     * Removes a collection of vertices from the internal list of adjacent
     * vertices.
     *
     * @param col collection of vertices
     * @return true if any vertex was effectively removed
     */
    public boolean removeAdjacentVertices(Collection<GraphVertexPaneNode<T>> col) {
        return this.adjacentVertices.removeAll(col);
    }

    /**
     * Returns the current position of the instance in pixels.
     *
     * @return the x,y coordinates in pixels
     */
    public Point2D getPosition() {
        return new Point2D(this.getLayoutX(), this.getLayoutY());
    }

    /**
     * Sets the position of the instance in pixels.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public void setPosition(double x, double y) {
        if (isDragging) {
            return;
        }

        this.setLayoutX(x);
        this.setLayoutY(y);
    }
    
     @Override
    public double getPositionCenterX() {
        return this.getLayoutX();
    }

    @Override
    public double getPositionCenterY() {
        return this.getLayoutY();
    }

    /**
     * Make a node movable by dragging it around with the mouse primary button.
     */
    private void enableDrag() {

        final DragContext sceneDragContext = new DragContext();

        setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                getScene().setCursor(Cursor.MOVE);
                isDragging = true;

                mouseEvent.consume();

                sceneDragContext.mouseAnchorX = mouseEvent.getSceneX();
                sceneDragContext.mouseAnchorY = mouseEvent.getSceneY();

                // Запомните текущие смещения
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

    private double boundCenterCoordinate(double value, double min, double max) {
        double radius = getRadius();

        if (value < min + radius) {
            return min + radius;
        } else if (value > max - radius) {
            return max - radius;
        } else {
            return value;
        }
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

    public DoubleProperty centerXProperty() {
        return this.layoutXProperty();
    }

    public DoubleProperty centerYProperty() {
        return this.layoutYProperty();
    }

    @Override
    public Vertex<T> getUnderlyingVertex() {
        return underlyingVertex;
    }

    @Override
    public double getRadius() {
        return this.getHeight();
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

    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }
}
