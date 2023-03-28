package com.rnar.umlgraph.graphview.vertices;

import com.rnar.umlgraph.graphview.GraphPanel;
import com.rnar.umlgraph.graphview.StylableNode;
import com.rnar.umlgraph.graphview.StyleProxy;
import com.rnar.umlgraph.graphview.vertices.elements.*;
import com.rnar.umlgraph.graph.Vertex;
import com.rnar.umlgraph.graphview.labels.Label;
import com.rnar.umlgraph.graphview.labels.LabelledNode;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
public class GraphVertexPaneNode<T> extends Pane implements GraphVertex<T>, LabelledNode {

    private final Vertex<T> underlyingVertex;
    /* Critical for performance, so we don't rely on the efficiency of the Graph.areAdjacent method */
    private final Set<GraphVertexPaneNode<T>> adjacentVertices;

    private Label attachedLabel = null;
    private boolean isDragging = false;

    /*
    Automatic layout functionality members
     */
    private final PointVector forceVector = new PointVector(0, 0);
    private final PointVector updatedPosition = new PointVector(0, 0);

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
//        this.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.underlyingVertex = v;
        this.attachedLabel = null;
        this.isDragging = false;
        this.nodeElement = new Node<>("game.Square", v);


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
     * Checks whether <code>v</code> is adjacent this instance.
     *
     * @param v vertex to check
     * @return true if adjacent; false otherwise
     */
    public boolean isAdjacentTo(GraphVertexPaneNode<T> v) {
        return this.adjacentVertices.contains(v);
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
     * Sets the position of the instance in pixels.
     *
     * @param p coordinates
     */
    public void setPosition(Point2D p) {
        setPosition(p.getX(), p.getY());
    }

    /**
     * Resets the current computed external force vector.
     *
     */
    public void resetForces() {
        forceVector.x = forceVector.y = 0;
        updatedPosition.x = getCenterX();
        updatedPosition.y = getCenterY();
    }

    /**
     * Adds the vector represented by <code>(x,y)</code> to the current external
     * force vector.
     *
     * @param x x-component of the force vector
     * @param y y-component of the force vector
     *
     */
    public void addForceVector(double x, double y) {
        forceVector.x += x;
        forceVector.y += y;
    }

    /**
     * Returns the current external force vector.
     *
     * @return force vector
     */
    public Point2D getForceVector() {
        return new Point2D(forceVector.x, forceVector.y);
    }

    /**
     * Returns the future position of the vertex.
     *
     * @return future position
     */
    public Point2D getUpdatedPosition() {
        return new Point2D(updatedPosition.x, updatedPosition.y);
    }

    /**
     * Updates the future position according to the current internal force
     * vector.
     *
     * @see GraphPanel#updateForces()
     */
    public void updateDelta() {
        updatedPosition.x = updatedPosition.x /* + speed*/ + forceVector.x;
        updatedPosition.y = updatedPosition.y + forceVector.y;
    }

    /**
     * Moves the vertex position to the computed future position.
     * <p>
     * Moves are constrained within the parent pane dimensions.
     *
     * @see GraphPanel#applyForces()
     */
    public void moveFromForces() {

        //limit movement to parent bounds
        double height = getParent().getLayoutBounds().getHeight();
        double width = getParent().getLayoutBounds().getWidth();

        updatedPosition.x = boundCenterCoordinate(updatedPosition.x, 0, width);
        updatedPosition.y = boundCenterCoordinate(updatedPosition.y, 0, height);

        setPosition(updatedPosition.x, updatedPosition.y);
    }

    /**
     * Make a node movable by dragging it around with the mouse primary button.
     */
    private void enableDrag() {
        final PointVector dragDelta = new PointVector(0, 0);

        setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = mouseEvent.getX();
                dragDelta.y = mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
                isDragging = true;

                mouseEvent.consume();
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
                double newX = mouseEvent.getSceneX() - dragDelta.x;
                double x = boundCenterCoordinate(newX, 0, getParent().getLayoutBounds().getWidth());
                setCenterX(x);

                double newY = mouseEvent.getSceneY() - dragDelta.y;
                double y = boundCenterCoordinate(newY, 0, getParent().getLayoutBounds().getHeight());
                setCenterY(y);
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
    public void attachLabel(Label label) {
        this.attachedLabel = label;
        label.xProperty().bind(centerXProperty().subtract(label.getLayoutBounds().getWidth() / 2.0));
        label.yProperty().bind(centerYProperty().add(getRadius() + label.getLayoutBounds().getHeight()));
    }

    @Override
    public Label getAttachedLabel() {
        return attachedLabel;
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

    @Override
    public StylableNode getStylableLabel() {
        return this.attachedLabel;
    }

    /**
     * Internal representation of a 2D point or vector for quick access to its
     * attributes.
     */
    private class PointVector {

        double x, y;

        public PointVector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
