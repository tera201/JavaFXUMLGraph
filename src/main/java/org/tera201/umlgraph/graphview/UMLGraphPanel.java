package org.tera201.umlgraph.graphview;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.arrows.DiamondArrow;
import org.tera201.umlgraph.graphview.arrows.SimpleArrow;
import org.tera201.umlgraph.graphview.arrows.TriangleArrow;
import org.tera201.umlgraph.graphview.edges.EdgeBase;
import org.tera201.umlgraph.graphview.edges.EdgeCurve;
import org.tera201.umlgraph.graphview.edges.EdgeLine;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

import java.util.*;

public class UMLGraphPanel<V, E> extends Pane {

    private Graph<V, E> graph;
    private final PlacementStrategy placementStrategy;
    private final Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes = new HashMap<>();
    private final Map<Edge<E, V>, EdgeBase<E, V>> edgeNodes = new HashMap<>();
    private final Map<Edge<E, V>, Integer> placedEdges = new HashMap<>();
    private boolean initialized = false;

    public UMLGraphPanel(Graph<V, E> graph, PlacementStrategy placementStrategy) {
        validateGraph(graph);
        this.graph = graph;
        this.placementStrategy = placementStrategy != null ? placementStrategy : new PlacementStrategy();
        loadStyle();
        initNodes();
    }

    private void validateGraph(Graph<V, E> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("The graph cannot be null.");
        }
    }

    public void setGraph(Graph<V, E> newGraph) {
        this.graph = newGraph;
        clearNodes();
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
        placementStrategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), graph, vertexNodes);
        for (GraphVertexPaneNode<V> vertexNode : vertexNodes.values()) {
            if (!this.getChildren().contains(vertexNode)) {
                this.getChildren().add(vertexNode);
            }
        }
    }

    private void setResizeListener() {
        this.getChildren().addListener((ListChangeListener<Node>) c -> updateSize());
    }

    private void updateSize() {
        double width = 0;
        double height = 0;
        for (Node node : this.getChildren()) {
            width = Math.max(width, node.getBoundsInParent().getMaxX());
            height = Math.max(height, node.getBoundsInParent().getMaxY());
        }
        this.setMinSize(width, height);
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

    private void updateNodes() {
        clearNodes();
        initNodes();
        placeVertices();
        setResizeListener();
    }

    public void resetPlaceStrategy() {
        placementStrategy.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(), graph, vertexNodes);
    }

    private void initNodes() {
        for (Vertex<V> vertex : graph.getVertices()) {
            GraphVertexPaneNode<V> vertexNode = new GraphVertexPaneNode<>(vertex);
            vertexNodes.put(vertex, vertexNode);
        }
        placeEdges();
    }

    private void placeEdges() {
        List<Edge<E, V>> edgesToPlace = new ArrayList<>(graph.getEdges());
        vertexNodes.keySet().forEach(vertex -> graph.getIncidentEdges(vertex).stream().filter(edgesToPlace::contains).forEach(edge -> {
            placeEdge(edge, vertex, edgesToPlace);
        }));
    }

    private void placeEdge(Edge<E, V> edge, Vertex<V> vertex, List<Edge<E, V>> edgesToPlace) {
        Vertex<V> oppositeVertex = graph.opposite(vertex, edge);
        GraphVertexPaneNode<V> graphVertexIn = vertexNodes.get(vertex);
        GraphVertexPaneNode<V> graphVertexOut = vertexNodes.get(oppositeVertex);

        graphVertexIn.addAdjacentVertex(graphVertexOut);
        graphVertexOut.addAdjacentVertex(graphVertexIn);

        EdgeBase<E, V> graphEdge = createEdge(edge, graphVertexIn, graphVertexOut);
        addEdge(graphEdge, edge);
        addArrow(edge, graphEdge);
        edgesToPlace.remove(edge);
    }

    private void addArrow(Edge<E, V> edge, EdgeBase<E, V> graphEdge) {
        DefaultArrow arrow = createArrow(edge);
        graphEdge.attachArrow(arrow);
        this.getChildren().add(arrow);
    }

    private DefaultArrow createArrow(Edge<E, V> edge) {
        return switch (edge.getArrowsType()) {
            case INHERITANCE, REALIZATION -> new TriangleArrow(5);
            case AGGREGATION, COMPOSITION -> new DiamondArrow(5);
            default -> new SimpleArrow(5);
        };
    }

    private EdgeBase<E, V> createEdge(Edge<E, V> edge, GraphVertexPaneNode<V> graphVertexIn, GraphVertexPaneNode<V> graphVertexOut) {
        int edgeIndex = placedEdges.getOrDefault(edge, 0);
        EdgeBase<E, V> graphEdge;
        if (getTotalEdgesBetween(graphVertexIn.getUnderlyingVertex(), graphVertexOut.getUnderlyingVertex()) > 1
                || graphVertexIn == graphVertexOut) {
            graphEdge = new EdgeCurve<>(edge, graphVertexIn, graphVertexOut, edgeIndex);
        } else {
            graphEdge = new EdgeLine<>(edge, graphVertexIn, graphVertexOut);
        }
        placedEdges.put(edge, ++edgeIndex);
        return graphEdge;
    }

    private void addEdge(EdgeBase<E, V> edge, Edge<E, V> originalEdge) {
        this.getChildren().add(0, (Node) edge);
        edgeNodes.put(originalEdge, edge);
        addEdgeLabel(edge, originalEdge);
    }

    private void addEdgeLabel(EdgeBase<E, V> edge, Edge<E, V> originalEdge) {
        Tooltip.install((Node) edge, new Tooltip(originalEdge.element().toString()));
    }

    private int getTotalEdgesBetween(Vertex<V> vertex1, Vertex<V> vertex2) {
        return (int) graph.getEdges().stream()
                .filter(edge -> (edge.vertices()[0].equals(vertex1) && edge.vertices()[1].equals(vertex2))
                        || (edge.vertices()[0].equals(vertex2) && edge.vertices()[1].equals(vertex1)))
                .count();
    }

    private void clearNodes() {
        this.getChildren().clear();
        edgeNodes.clear();
        vertexNodes.clear();
        placedEdges.clear();
    }

    private void loadStyle() {
        String css = Objects.requireNonNull(getClass().getClassLoader().getResource("uml.css")).toExternalForm();
        getStylesheets().add(css);
        getStyleClass().add("graph");
    }
}
