package org.tera201.umlgraph.graphview;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.tera201.umlgraph.graph.*;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graphview.arrows.*;
import org.tera201.umlgraph.graphview.edges.*;
import org.tera201.umlgraph.graphview.labels.Label;
import org.tera201.umlgraph.graphview.strategy.*;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

public class GraphPanel<V, E> extends Pane {

    private final GraphProperties graphProperties;
    private Digraph<V, E> theGraph;
    private final PlacementStrategy placementStrategy;
    private final Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes = new HashMap<>();
    private final Map<Edge<E, V>, EdgeBase<E, V>> edgeNodes = new HashMap<>();
    private final Map<Pair<GraphVertexPaneNode<V>, GraphVertexPaneNode<V>>, Integer> placedEdges = new HashMap<>();
    private boolean initialized = false;
    private final boolean edgesWithArrows;

    public GraphPanel(Digraph<V, E> theGraph, GraphProperties properties, PlacementStrategy placementStrategy, URI cssFile) {
        if (theGraph == null) {
            throw new IllegalArgumentException("The graph cannot be null.");
        }

        this.theGraph = theGraph;
        this.graphProperties = properties != null ? properties : new GraphProperties();
        this.placementStrategy = placementStrategy != null ? placementStrategy : new DigraphTreePlacementStrategy();
        this.edgesWithArrows = this.graphProperties.getUseEdgeArrow();

        loadStylesheet(cssFile);
        initNodes();
    }

    public void setTheGraph(Digraph<V, E> newGraph) {
        this.theGraph = newGraph;
        removeNodes(); // Clear all elements before adding new ones
    }

    public void init() {
        validateStateForInitialization();
        placeVertices();
        setResizeListener();
        this.initialized = true;
    }

    private void validateStateForInitialization() {
        if (this.getScene() == null || this.getWidth() == 0 || this.getHeight() == 0 || this.initialized) {
            throw new IllegalStateException("Invalid state for initialization.");
        }
    }

    private void placeVertices() {
        placementStrategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), theGraph, vertexNodes);
        // Explicitly add vertices to the Pane
        for (GraphVertexPaneNode<V> vertexNode : vertexNodes.values()) {
            if (!this.getChildren().contains(vertexNode)) {
                this.getChildren().add(vertexNode);
            }
        }
    }

    private void setResizeListener() {
        this.getChildren().addListener((ListChangeListener<Node>) c -> {
            double width = 0;
            double height = 0;
            for (Node node : this.getChildren()) {
                width = Math.max(width, node.getBoundsInParent().getMaxX());
                height = Math.max(height, node.getBoundsInParent().getMaxY());
            }
            this.setMinSize(width, height);
        });
    }

    public void update() {
        validateStateForUpdate();
        Platform.runLater(this::updateNodes);
    }

    private void validateStateForUpdate() {
        if (this.getScene() == null || !this.initialized) {
            throw new IllegalStateException("Call init() before updating.");
        }
    }

    private synchronized void updateNodes() {
        removeNodes();
        initNodes();
        placeVertices();
        setResizeListener();
        updateLabels();
    }

    public void updateAndWait() {
        validateStateForUpdate();
        if (!Platform.isFxApplicationThread()) {
            FutureTask<Void> updateTask = new FutureTask<>(() -> {updateNodes();
                return null;
            });
            Platform.runLater(updateTask);
            waitForCompletion(updateTask);
        } else {
            updateNodes();
        }
    }

    private void waitForCompletion(FutureTask<Void> updateTask) {
        try {
            updateTask.get(1, TimeUnit.SECONDS);
        } catch (Exception ex) {
            Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetPlaceStrategy() {
        placementStrategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), theGraph, vertexNodes);
    }

    private void initNodes() {
        for (Vertex<V> vertex : listOfVertices()) {
            GraphVertexPaneNode<V> vertexNode = createVertexNode(vertex);
            vertexNodes.put(vertex, vertexNode);
        }
        placeEdges();
    }

    private GraphVertexPaneNode<V> createVertexNode(Vertex<V> vertex) {
        return new GraphVertexPaneNode<>(vertex, 0, 0, graphProperties.getVertexRadius(), graphProperties.getVertexAllowUserMove());
    }

    private void placeEdges() {
        List<Edge<E, V>> edgesToPlace = listOfEdges();
        for (Vertex<V> vertex : vertexNodes.keySet()) {
            for (Edge<E, V> edge : theGraph.incidentEdges(vertex)) {
                if (edgesToPlace.contains(edge)) {
                    placeEdge(edge, vertex);
                    edgesToPlace.remove(edge);
                }
            }
        }
    }

    private void placeEdge(Edge<E, V> edge, Vertex<V> vertex) {
        Vertex<V> oppositeVertex = theGraph.opposite(vertex, edge);
        GraphVertexPaneNode<V> graphVertexIn = vertexNodes.get(vertex);
        GraphVertexPaneNode<V> graphVertexOut = vertexNodes.get(oppositeVertex);

        graphVertexIn.addAdjacentVertex(graphVertexOut);
        graphVertexOut.addAdjacentVertex(graphVertexIn);

        EdgeBase<E, V> graphEdge = createEdge(edge, graphVertexIn, graphVertexOut);
        addEdge(graphEdge, edge);
        addArrowIfNeeded(edge, graphEdge);
    }

    private void addArrowIfNeeded(Edge<E, V> edge, EdgeBase<E, V> graphEdge) {
        if (this.edgesWithArrows) {
            DefaultArrow arrow = createArrow(edge);
            graphEdge.attachArrow(arrow);
            this.getChildren().add(arrow);
        }
    }

    private DefaultArrow createArrow(Edge<E, V> edge) {
        switch (edge.getArrowsType()) {
            case INHERITANCE:
            case REALIZATION:
                return new TriangleArrow(this.graphProperties.getEdgeArrowSize());
            case AGGREGATION:
            case COMPOSITION:
                return new DiamondArrow(this.graphProperties.getEdgeArrowSize());
            default:
                return new SimpleArrow(this.graphProperties.getEdgeArrowSize());
        }
    }

    private EdgeBase<E, V> createEdge(Edge<E, V> edge, GraphVertexPaneNode<V> graphVertexIn, GraphVertexPaneNode<V> graphVertexOut) {
        int edgeIndex = getEdgeIndex(graphVertexIn, graphVertexOut);
        EdgeBase<E, V> graphEdge;
        if (getTotalEdgesBetween(graphVertexIn.getUnderlyingVertex(), graphVertexOut.getUnderlyingVertex()) > 1
                || graphVertexIn == graphVertexOut) {
            graphEdge = new EdgeCurve<>(edge, graphVertexIn, graphVertexOut, edgeIndex);
        } else {
            graphEdge = new EdgeLine<>(edge, graphVertexIn, graphVertexOut);
        }
        placedEdges.put(new Pair<>(graphVertexIn, graphVertexOut), ++edgeIndex);
        return graphEdge;
    }

    private int getEdgeIndex(GraphVertexPaneNode<V> graphVertexIn, GraphVertexPaneNode<V> graphVertexOut) {
        Integer counter = placedEdges.get(new Pair<>(graphVertexIn, graphVertexOut));
        return counter != null ? counter : 0;
    }

    private void addEdge(EdgeBase<E, V> edge, Edge<E, V> originalEdge) {
        this.getChildren().add(0, (Node) edge);
        edgeNodes.put(originalEdge, edge);
        addEdgeLabel(edge, originalEdge);
    }

    private void addEdgeLabel(EdgeBase<E, V> edge, Edge<E, V> originalEdge) {
        String labelText = generateEdgeLabel(originalEdge.element());
        if (graphProperties.getUseEdgeTooltip()) {
            Tooltip.install((Node) edge, new Tooltip(labelText));
        }
        if (graphProperties.getUseEdgeLabel()) {
            Label label = new Label(labelText);
            label.addStyleClass("edge-label");
            this.getChildren().add(label);
            edge.attachLabel(label);
        }
    }

    private void insertNodes() {
        Collection<Vertex<V>> unplottedVertices = unplottedVertices();
        if (!unplottedVertices.isEmpty()) {
            placeUnplottedVertices(unplottedVertices);
        }

        Collection<Edge<E, V>> unplottedEdges = unplottedEdges();
        if (!unplottedEdges.isEmpty()) {
            placeUnplottedEdges(unplottedEdges);
        }
    }

    private void placeUnplottedVertices(Collection<Vertex<V>> unplottedVertices) {
        // Calculate the center of the plot
        Rectangle2D bounds = getPlotBounds();
        double mx = bounds.getMinX() + bounds.getWidth() / 2.0;
        double my = bounds.getMinY() + bounds.getHeight() / 2.0;

        int deltaX = 0, deltaY = 0;
        for (Vertex<V> vertex : unplottedVertices) {
            GraphVertexPaneNode<V> vertexNode = vertexNodes.get(vertex);
            if (vertexNode != null) {
                vertexNode.setTranslateX(mx + deltaX);
                vertexNode.setTranslateY(my + deltaY);
                deltaY += 50; // Adjust verticinsertNodesal spacing
                if (!this.getChildren().contains(vertexNode)) {
                    this.getChildren().add(vertexNode);
                }
            }
        }
    }

    private void placeUnplottedEdges(Collection<Edge<E, V>> unplottedEdges) {
        for (Edge<E, V> edge : unplottedEdges) {
            Vertex<V> source = edge.vertices()[0];
            Vertex<V> target = edge.vertices()[1];
            addEdge(createEdge(edge, vertexNodes.get(source), vertexNodes.get(target)), edge);
        }
    }

    private Collection<Vertex<V>> unplottedVertices() {
        return vertexNodes.keySet();
    }

    private Collection<Edge<E, V>> unplottedEdges() {
        return edgeNodes.keySet();
    }

    private String generateEdgeLabel(E edgeElement) {
        return edgeElement != null ? edgeElement.toString() : "No Label";
    }

    private Collection<Vertex<V>> listOfVertices() {
        return theGraph.vertices();
    }

    private List<Edge<E, V>> listOfEdges() {
        return new ArrayList<>(theGraph.edges());
    }

    private int getTotalEdgesBetween(Vertex<V> vertex1, Vertex<V> vertex2) {
        return (int) theGraph.edges().stream()
                .filter(edge -> (edge.vertices()[0].equals(vertex1) && edge.vertices()[1].equals(vertex2))
                        || (edge.vertices()[0].equals(vertex2) && edge.vertices()[1].equals(vertex1)))
                .count();
    }

    private Rectangle2D getPlotBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Node node : this.getChildren()) {
            minX = Math.min(minX, node.getBoundsInParent().getMinX());
            minY = Math.min(minY, node.getBoundsInParent().getMinY());
            maxX = Math.max(maxX, node.getBoundsInParent().getMaxX());
            maxY = Math.max(maxY, node.getBoundsInParent().getMaxY());
        }
        return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
    }

    private void removeNodes() {
        this.getChildren().clear();
        edgeNodes.clear();
        vertexNodes.clear();
        placedEdges.clear();
    }

    private void updateLabels() {
        theGraph.vertices().forEach((v) -> {
            GraphVertexPaneNode<V> vertexNode = vertexNodes.get(v);
            if (vertexNode != null) {
                Label label = vertexNode.getAttachedLabel();
                if(label != null) {
                    String text = v.getLabel();
                    label.setText( text );
                }

            }
        });

        theGraph.edges().forEach((e) -> {
            EdgeBase<E, V> edgeNode = edgeNodes.get(e);
            if (edgeNode != null) {
                Label label = edgeNode.getAttachedLabel();
                if (label != null) {
                    String text = generateEdgeLabel(e.element());
                    label.setText( text );
                }
            }
        });
    }

    /**
     * Loads the stylesheet and applies the .graph class to this panel.
     */
    private void loadStylesheet(URI cssFile) {
        try {
            String css;
            if( cssFile != null ) {
                css = cssFile.toURL().toExternalForm();
            } else {
                ClassLoader classLoader = getClass().getClassLoader();
                css = classLoader.getResource("smartgraph.css").toURI().toURL().toExternalForm();
            }

            getStylesheets().add(css);
            this.getStyleClass().add("graph");
        } catch (MalformedURLException ex) {
            Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
