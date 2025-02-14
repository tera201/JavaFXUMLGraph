package org.tera201.umlgraph.graphview;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.util.Pair;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;
import org.tera201.umlgraph.graphview.arrows.DiamondArrow;
import org.tera201.umlgraph.graphview.arrows.SimpleArrow;
import org.tera201.umlgraph.graphview.arrows.TriangleArrow;
import org.tera201.umlgraph.graphview.edges.EdgeBase;
import org.tera201.umlgraph.graphview.edges.EdgeCurve;
import org.tera201.umlgraph.graphview.edges.EdgeLine;
import org.tera201.umlgraph.graphview.labels.Label;
import org.tera201.umlgraph.graphview.labels.LabelSource;
import org.tera201.umlgraph.graphview.strategy.DigraphTreePlacementStrategy;
import org.tera201.umlgraph.graphview.utils.UtilitiesPoint2D;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Digraph;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * JavaFX {@link Pane} that is capable of plotting a {@link Graph} or {@link Digraph}.
 * <br>
 * Be sure to call {@link #init() } after the Stage is displayed.
 * <br>
 * Whenever changes to the underlying graph are made, you should call
 * {@link #update()} to force the rendering of any new elements and, also, the
 * removal of others, if applicable.
 * <br>
 * Vertices can be dragged by the user, if configured to do so. Consequently,
 * any connected edges will also adjust automatically to the new vertex positioning.
 *
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 *
 * @author r.naryshkin99
 */
public class GraphPanel<V, E> extends Pane {

    /*
    CONFIGURATION PROPERTIES
     */
    private final GraphProperties graphProperties;

    /*
    INTERNAL DATA STRUCTURE
     */
    private final Graph<V, E> theGraph;
    private final PlacementStrategy placementStrategy;
    private final Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes;
    private final Map<Edge<E, V>, EdgeBase<E, V>> edgeNodes;
    private final Map<Edge<E,V>, Pair<Vertex<V>, Vertex<V>>> connections;
    private final Map<Pair<GraphVertexPaneNode<V>, GraphVertexPaneNode<V>>, Integer> placedEdges = new HashMap<>();
    private boolean initialized = false;
    private final boolean edgesWithArrows;

    /**
     * Constructs a visualization of the graph referenced by
     * <code>theGraph</code>, using default properties and custom placement of
     * vertices.
     *
     * @param theGraph underlying graph
     * @param placementStrategy placement strategy, null for default
     */
    public GraphPanel(Graph<V, E> theGraph, PlacementStrategy placementStrategy) {
        this(theGraph, null, placementStrategy, null);
    }

    /**
     * Constructs a visualization of the graph referenced by
     * <code>theGraph</code>, using custom properties and custom placement of
     * vertices.
     *
     * @param theGraph underlying graph
     * @param properties custom properties, null for default
     * @param placementStrategy placement strategy, null for default
     * @param cssFile alternative css file, instead of default 'smartgraph.css'
     */
    public GraphPanel(Graph<V, E> theGraph, GraphProperties properties,
                      PlacementStrategy placementStrategy, URI cssFile) {

        if (theGraph == null) {
            throw new IllegalArgumentException("The graph cannot be null.");
        }
        this.theGraph = theGraph;
        this.graphProperties = properties != null ? properties : new GraphProperties();
        this.placementStrategy = placementStrategy != null ? placementStrategy : new DigraphTreePlacementStrategy();

        this.edgesWithArrows = this.graphProperties.getUseEdgeArrow();

        vertexNodes = new HashMap<>();
        edgeNodes = new HashMap<>();
        connections = new HashMap<>();

        //set stylesheet and class
        loadStylesheet(cssFile);

        initNodes();

    }

    /**
     * Runs the initial current vertex placement strategy.
     * <p>
     * This method should only be called once during the lifetime of the object
     * and only after the underlying {@link Scene} is displayed.
     *
     * Further required updates should be performed through the {@link #update()
     * } method.
     *
     * @throws IllegalStateException The exception is thrown if: (1) the Scene
     * is not yet displayed; (2) It has zero width and/or height, and; (3) If
     * this method was already called.
     */
    public void init() throws IllegalStateException {
        if (this.getScene() == null) {
            throw new IllegalStateException("You must call this method after the instance was added to a scene.");
        } else if (this.getWidth() == 0 || this.getHeight() == 0) {
            throw new IllegalStateException("The layout for this panel has zero width and/or height");
        } else if (this.initialized) {
            throw new IllegalStateException("Already initialized. Use update() method instead.");
        }

        if (placementStrategy != null) {
            // call strategy to place the vertices in their initial locations
            placementStrategy.place(this.widthProperty().doubleValue(),
                    this.heightProperty().doubleValue(),
                    this.theGraph,
                    this.vertexNodes);
        } else {
            new DigraphTreePlacementStrategy().place(this.widthProperty().doubleValue(),
                    this.heightProperty().doubleValue(),
                    this.theGraph,
                    this.vertexNodes);
        }

        this.getChildren().addListener((ListChangeListener<Node>) c -> {
            double width = 0;
            double height = 0;

            for (Node node : this.getChildren()) {
                if (node.getBoundsInParent().getMaxX() > width) {
                    width = node.getBoundsInParent().getMaxX();
                }
                if (node.getBoundsInParent().getMaxY() > height) {
                    height = node.getBoundsInParent().getMaxY();
                }
            }

            this.setMinSize(width, height);
        });

        this.initialized = true;
    }

    /**
     * Forces a refresh of the visualization based on current state of the
     * underlying graph, immediately returning to the caller.
     *
     * This method invokes the refresh in the graphical
     * thread through Platform.runLater(), so its not guaranteed that the visualization is in sync
     * immediately after this method finishes. That is, this method
     * immediately returns to the caller without waiting for the update to the
     * visualization.
     */
    public void update() {
        if (this.getScene() == null) {
            throw new IllegalStateException("You must call this method after the instance was added to a scene.");
        }

        if (!this.initialized) {
            throw new IllegalStateException("You must call init() method before any updates.");
        }

        //this will be called from a non-javafx thread, so this must be guaranteed to run of the graphics thread
        Platform.runLater(this::updateNodes);

    }

    /**
     * Forces a refresh of the visualization based on current state of the
     * underlying graph and waits for completion of the update.
     *
     * Use this variant only when necessary, e.g., need to style an element
     * immediately after adding it to the underlying graph. Otherwise, use
     * {@link #update() } instead for performance sake.
     * <p>
     * New vertices will be added close to adjacent ones or randomly for
     * isolated vertices.
     */
    public void updateAndWait() {
        if (this.getScene() == null) {
            throw new IllegalStateException("You must call this method after the instance was added to a scene.");
        }

        if (!this.initialized) {
            throw new IllegalStateException("You must call init() method before any updates.");
        }

        final FutureTask update = new FutureTask(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                updateNodes();
                return true;
            }
        });

        //
        if(!Platform.isFxApplicationThread()) {
            //this will be called from a non-javafx thread, so this must be guaranteed to run of the graphics thread
            Platform.runLater(update);

            //wait for completion, only outside javafx thread; otherwise -> deadlock
            try {
                update.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            updateNodes();
        }

    }

    public void resetPlaceStrategy() {
        if (placementStrategy != null) {
            // call strategy to place the vertices in their initial locations
            placementStrategy.place(this.widthProperty().doubleValue(),
                    this.heightProperty().doubleValue(),
                    this.theGraph,
                    this.vertexNodes);
        } else {
            //apply random placement
            new DigraphTreePlacementStrategy().place(this.widthProperty().doubleValue(),
                    this.heightProperty().doubleValue(),
                    this.theGraph,
                    this.vertexNodes);
        }
    }

    private synchronized void updateNodes() {
        removeNodes();
        insertNodes();
        updateLabels();
    }

    /*
    NODES CREATION/UPDATES
     */
    private void initNodes() {

        /* create vertex graphical representations */
        for (Vertex<V> vertex : listOfVertices()) {
            GraphVertexPaneNode<V> vertexAnchor = new GraphVertexPaneNode<>(vertex, 0, 0,
                    graphProperties.getVertexRadius(), graphProperties.getVertexAllowUserMove());
            vertexNodes.put(vertex, vertexAnchor);
        }

        /* create edges graphical representations between existing vertices */
        //this is used to guarantee that no duplicate edges are ever inserted
        List<Edge<E, V>> edgesToPlace = listOfEdges();

        for (Vertex<V> vertex : vertexNodes.keySet()) {

            Iterable<Edge<E, V>> incidentEdges = theGraph.incidentEdges(vertex);

            for (Edge<E, V> edge : incidentEdges) {

                //if already plotted, ignore edge.
                if (!edgesToPlace.contains(edge)) {
                    continue;
                }

                Vertex<V> oppositeVertex = theGraph.opposite(vertex, edge);

                GraphVertexPaneNode<V> graphVertexIn = vertexNodes.get(vertex);
                GraphVertexPaneNode<V> graphVertexOppositeOut = vertexNodes.get(oppositeVertex);

                graphVertexIn.addAdjacentVertex(graphVertexOppositeOut);
                graphVertexOppositeOut.addAdjacentVertex(graphVertexIn);

                EdgeBase<E, V> graphEdge = createEdge(edge, graphVertexIn, graphVertexOppositeOut);

                /* Track Edges already placed */
                connections.put(edge, new Pair<>(vertex, oppositeVertex));
                addEdge(graphEdge, edge);

                if (this.edgesWithArrows) {
                    DefaultArrow arrow = switch (edge.getArrowsType()) {
                        case INHERITANCE, REALIZATION -> new TriangleArrow(this.graphProperties.getEdgeArrowSize());
                        case AGGREGATION, COMPOSITION -> new DiamondArrow(this.graphProperties.getEdgeArrowSize());
                        default -> new SimpleArrow(this.graphProperties.getEdgeArrowSize());
                    };

                    switch (edge.getArrowsType()) {
                        case INHERITANCE, REALIZATION, AGGREGATION:
                            arrow.setStyleClass("arrow-white");
                            break;
                        default: arrow.setStyleClass("arrow-black");
                    }
                    graphEdge.attachArrow(arrow);
                    this.getChildren().add(arrow);
                }

                edgesToPlace.remove(edge);
            }

        }

        /* place vertices above lines */
        for (Vertex<V> vertex : vertexNodes.keySet()) {
            GraphVertexPaneNode<V> v = vertexNodes.get(vertex);

            addVertex(v);
        }
    }

    private EdgeBase<E, V> createEdge(Edge<E, V> edge, GraphVertexPaneNode<V> graphVertexInbound, GraphVertexPaneNode<V> graphVertexOutbound) {
        /*
        Even if edges are later removed, the corresponding index remains the same. Otherwise, we would have to
        regenerate the appropriate edges.
         */
        int edgeIndex = 0;
        Integer counter = placedEdges.get(new Pair<>(graphVertexInbound, graphVertexOutbound));
        if (counter != null) {
            edgeIndex = counter;
        }

        EdgeBase<E, V> graphEdge;

        if (getTotalEdgesBetween(graphVertexInbound.getUnderlyingVertex(), graphVertexOutbound.getUnderlyingVertex()) > 1
                || graphVertexInbound == graphVertexOutbound) {
            graphEdge = new EdgeCurve<>(edge, graphVertexInbound, graphVertexOutbound, edgeIndex);
        } else {
            graphEdge = new EdgeLine<>(edge, graphVertexInbound, graphVertexOutbound);
        }

        placedEdges.put(new Pair<>(graphVertexInbound, graphVertexOutbound), ++edgeIndex);

        return graphEdge;
    }

    private void addVertex(GraphVertexPaneNode<V> v) {
        this.getChildren().add(v);

        String labelText = v.getUnderlyingVertex().getLabel();

        // add pop-up property
        if (graphProperties.getUseVertexTooltip()) {
            Tooltip t = new Tooltip(v.getUnderlyingVertex().getNotes());
            Tooltip.install(v, t);
        }

        // add labels
        if (graphProperties.getUseVertexLabel()) {
            Label label = new Label(labelText);

            label.addStyleClass("vertex-label");
            this.getChildren().add(label);
//            v.attachLabel(label);
        }
    }

    private void addEdge(EdgeBase<E, V> e, Edge<E, V> edge) {
        //edges to the back
        this.getChildren().add(0, (Node) e);
        edgeNodes.put(edge, e);

        String labelText = generateEdgeLabel(edge.element());

        if (graphProperties.getUseEdgeTooltip()) {
            Tooltip t = new Tooltip(labelText);
            Tooltip.install((Node) e, t);
        }

        if (graphProperties.getUseEdgeLabel()) {
            Label label = new Label(labelText);

            label.addStyleClass("edge-label");
            this.getChildren().add(label);
            e.attachLabel(label);
        }
    }

    private void insertNodes() {
        Collection<Vertex<V>> unplottedVertices = unplottedVertices();

        List<GraphVertexPaneNode<V>> newVertices = null;

        Rectangle2D bounds = getPlotBounds();
        double mx = bounds.getMinX() + bounds.getWidth() / 2.0;
        double my = bounds.getMinY() + bounds.getHeight() / 2.0;

        if (!unplottedVertices.isEmpty()) {

            newVertices = new LinkedList<>();

            for (Vertex<V> vertex : unplottedVertices) {
                //create node
                //Place new nodes in the vicinity of existing adjacent ones;
                //Place them in the middle of the plot, otherwise.
                double x, y;
                Collection<Edge<E, V>> incidentEdges = theGraph.incidentEdges(vertex);
                if (incidentEdges.isEmpty()) {
                    /* not (yet) connected, put in the middle of the plot */
                    x = mx;
                    y = my;
                } else {
                    Edge<E, V> firstEdge = incidentEdges.iterator().next();
                    Vertex<V> opposite = theGraph.opposite(vertex, firstEdge);
                    GraphVertexPaneNode<V> existing = vertexNodes.get(opposite);

                    if(existing == null) {
                        /*
                        Updates may be coming too fast and we can get out of sync.
                        The opposite vertex exists in the (di)graph, but we have not yet
                        created it for the panel. Therefore, its position is unknown,
                        so place the vertex representation in the middle.
                        */
                        x = mx;
                        y = my;
                    } else {
                        /* TODO: fix -- the placing point can be set out of bounds*/
                        Point2D p = UtilitiesPoint2D.rotate(existing.getPosition().add(50.0, 50.0),
                                existing.getPosition(), Math.random() * 360);

                        x = p.getX();
                        y = p.getY();
                    }
                }

                GraphVertexPaneNode<V> newVertex = new GraphVertexPaneNode<>(vertex,
                        x, y, graphProperties.getVertexRadius(), graphProperties.getVertexAllowUserMove());

                //track new nodes
                newVertices.add(newVertex);
                //add to global mapping
                vertexNodes.put(vertex, newVertex);
            }

        }

        Collection<Edge<E, V>> unplottedEdges = unplottedEdges();
        if (!unplottedEdges.isEmpty()) {
            for (Edge<E, V> edge : unplottedEdges) {

                Vertex<V>[] vertices = edge.vertices();
                Vertex<V> u = vertices[0]; //oubound if digraph, by javadoc requirement
                Vertex<V> v = vertices[1]; //inbound if digraph, by javadoc requirement

                GraphVertexPaneNode<V> graphVertexOut = vertexNodes.get(u);
                GraphVertexPaneNode<V> graphVertexIn = vertexNodes.get(v);

                /*
                Updates may be coming too fast and we can get out of sync.
                Skip and wait for another update call, since they will surely
                be coming at this pace.
                */
                if(graphVertexIn == null || graphVertexOut == null) {
                    continue;
                }

                graphVertexOut.addAdjacentVertex(graphVertexIn);
                graphVertexIn.addAdjacentVertex(graphVertexOut);

                EdgeBase<E, V> graphEdge = createEdge(edge, graphVertexIn, graphVertexOut);

                if (this.edgesWithArrows) {
                    DefaultArrow arrow = new DiamondArrow(this.graphProperties.getEdgeArrowSize());
                    graphEdge.attachArrow(arrow);
                    this.getChildren().add(arrow);
                }

                 /* Track edges */
                connections.put(edge, new Pair<>(u, v));
                addEdge(graphEdge, edge);

            }
        }

        if (newVertices != null) {
            for (GraphVertexPaneNode<V> v : newVertices) {
                addVertex(v);
            }
        }

    }

    private void removeNodes() {
         //remove edges (graphical elements) that were removed from the underlying graph
        Collection<Edge<E, V>> removedEdges = removedEdges();
        for (Edge<E, V> e : removedEdges) {
            EdgeBase<E, V> edgeToRemove = edgeNodes.get(e);
            edgeNodes.remove(e);
            removeEdge(edgeToRemove);   //remove from panel

            //when edges are removed, the adjacency between vertices changes
            //the adjacency is kept in parallel in an internal data structure
            Pair<Vertex<V>, Vertex<V>> vertexPair = connections.get(e);

            if( getTotalEdgesBetween(vertexPair.getKey(), vertexPair.getValue()) == 0 ) {
                GraphVertexPaneNode<V> v0 = vertexNodes.get(vertexPair.getKey());
                GraphVertexPaneNode<V> v1 = vertexNodes.get(vertexPair.getValue());

                v0.removeAdjacentVertex(v1);
                v1.removeAdjacentVertex(v0);
            }

            connections.remove(e);
        }

        //remove vertices (graphical elements) that were removed from the underlying graph
        Collection<Vertex<V>> removedVertices = removedVertices();
        for (Vertex<V> removedVertex : removedVertices) {
            GraphVertexPaneNode<V> removed = vertexNodes.remove(removedVertex);
            removeVertex(removed);
        }

    }

    private void removeEdge(EdgeBase<E, V> e) {
        getChildren().remove((Node) e);

        DefaultArrow attachedArrow = e.getAttachedArrow();
        if (attachedArrow != null) {
            getChildren().remove(attachedArrow);
        }

        Text attachedLabel = e.getAttachedLabel();
        if (attachedLabel != null) {
            getChildren().remove(attachedLabel);
        }
    }

    private void removeVertex(GraphVertexPaneNode<V> v) {
        getChildren().remove(v);

        Text attachedLabel = v.getAttachedLabel();
        if (attachedLabel != null) {
            getChildren().remove(attachedLabel);
        }
    }

    /**
     * Updates node's labels
     */
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

    private String generateEdgeLabel(E edge) {

        try {
            Class<?> clazz = edge.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(LabelSource.class)) {
                    method.setAccessible(true);
                    Object value = method.invoke(edge);
                    return value.toString();
                }
            }
        } catch (SecurityException | IllegalAccessException  | IllegalArgumentException |InvocationTargetException ex) {
            Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return edge != null ? edge.toString() : "<NULL>";
    }

    /**
     * Computes the bounding box from all displayed vertices.
     *
     * @return bounding box
     */
    private Rectangle2D getPlotBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE,
                maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        if(vertexNodes.isEmpty()) return new Rectangle2D(0, 0, getWidth(), getHeight());

        for (GraphVertexPaneNode<V> v : vertexNodes.values()) {
            minX = Math.min(minX, v.getCenterX());
            minY = Math.min(minY, v.getCenterY());
            maxX = Math.max(maxX, v.getCenterX());
            maxY = Math.max(maxY, v.getCenterY());
        }

        return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
    }

    private int getTotalEdgesBetween(Vertex<V> v, Vertex<V> u) {
        int count = 0;
        for (Edge<E, V> edge : theGraph.edges()) {
            if (edge.vertices()[0] == v && edge.vertices()[1] == u
                    || edge.vertices()[0] == u && edge.vertices()[1] == v) {
                count++;
            }
        }
        return count;
    }

    private List<Edge<E, V>> listOfEdges() {
        return new LinkedList<>(theGraph.edges());
    }

    private List<Vertex<V>> listOfVertices() {
        return new LinkedList<>(theGraph.vertices());
    }

    /**
     * Computes the vertex collection of the underlying graph that are not
     * currently being displayed.
     *
     * @return collection of vertices
     */
    private Collection<Vertex<V>> unplottedVertices() {
        List<Vertex<V>> unplotted = new LinkedList<>();

        for (Vertex<V> v : theGraph.vertices()) {
            if (!vertexNodes.containsKey(v)) {
                unplotted.add(v);
            }
        }

        return unplotted;
    }

    /**
     * Computes the collection for vertices that are currently being displayed but do
     * not longer exist in the underlying graph.
     *
     * @return collection of vertices
     */
    private Collection<Vertex<V>> removedVertices() {
        List<Vertex<V>> removed = new LinkedList<>();

        Collection<Vertex<V>> graphVertices = theGraph.vertices();
        Collection<GraphVertexPaneNode<V>> plotted = vertexNodes.values();

        for (GraphVertexPaneNode<V> v : plotted) {
            if (!graphVertices.contains(v.getUnderlyingVertex())) {
                removed.add(v.getUnderlyingVertex());
            }
        }

        return removed;
    }

    /**
     * Computes the collection for edges that are currently being displayed but do
     * not longer exist in the underlying graph.
     *
     * @return collection of edges
     */
    private Collection<Edge<E, V>> removedEdges() {
        List<Edge<E, V>> removed = new LinkedList<>();

        Collection<Edge<E, V>> graphEdges = theGraph.edges();
        Collection<EdgeBase<E, V>> plotted = edgeNodes.values();

        for (EdgeBase<E, V> e : plotted) {
            if (!graphEdges.contains(e.getUnderlyingEdge())) {
                removed.add(e.getUnderlyingEdge());
            }
        }

        return removed;
    }

    /**
     * Computes the edge collection of the underlying graph that are not
     * currently being displayed.
     *
     * @return collection of edges
     */
    private Collection<Edge<E, V>> unplottedEdges() {
        List<Edge<E, V>> unplotted = new LinkedList<>();

        for (Edge<E, V> e : theGraph.edges()) {
            if (!edgeNodes.containsKey(e)) {
                unplotted.add(e);
            }
        }

        return unplotted;
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
