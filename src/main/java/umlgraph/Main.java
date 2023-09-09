package umlgraph;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import umlgraph.containers.GraphDemoContainer;
import umlgraph.graphview.GraphPanel;
import umlgraph.graphview.StylableNode;
import umlgraph.graphview.arrows.ArrowTypes;
import umlgraph.graphview.strategy.TreeCentralPlacementStrategy;
import umlgraph.graphview.vertices.elements.ElementTypes;
import umlgraph.graph.Vertex;
import umlgraph.graph.Graph;
import umlgraph.graph.GraphEdgeList;
import umlgraph.graph.DigraphEdgeList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import umlgraph.graphview.strategy.PlacementStrategy;
import umlgraph.graph.Digraph;
import umlgraph.graphview.vertices.GraphVertex;

/**
 *
 * @author r.naryshkin99
 */
public class Main extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) {

        Graph<String, String> g = build_sample_digraph();
        //Graph<String, String> g = build_flower_graph();
        System.out.println(g);
        //TODO create strategy for uml model
        PlacementStrategy strategy = new TreeCentralPlacementStrategy();
        //SmartPlacementStrategy strategy = new SmartRandomPlacementStrategy();
        GraphPanel<String, String> graphView = new GraphPanel<>(g, strategy);

        /*
        After creating, you can change the styling of some element.
        This can be done at any time afterwards.
        */
        if (g.numVertices() > 0) {
            graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
        }

        /*
        Basic usage:            
        Use SmartGraphDemoContainer if you want zoom capabilities and automatic layout toggling
        */
        //Scene scene = new Scene(graphView, 1024, 768);
        Scene scene = new Scene(new GraphDemoContainer(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        /*
        IMPORTANT: Must call init() after scene is displayed so we can have width and height values
        to initially place the vertices according to the placement strategy
        */
        graphView.init();

        /*
        Bellow you can see how to attach actions for when vertices and edges are double clicked
         */        
        graphView.setVertexDoubleClickAction((GraphVertex<String> graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
                      
            //toggle different styling
            if( !graphVertex.removeStyleClass("myVertex") ) {
                /* for the golden vertex, this is necessary to clear the inline
                css class. Otherwise, it has priority. Test and uncomment. */
                //graphVertex.setStyle(null);
                
                graphVertex.addStyleClass("myVertex");
            }
            
            //want fun? uncomment below with automatic layout
            //g.removeVertex(graphVertex.getUnderlyingVertex());
            //graphView.update();
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            //dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            //uncomment to see edges being removed after click
            //Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            //g.removeEdge(underlyingEdge);
            //graphView.update();
        });

        /*
        Should proceed with automatic layout or keep original placement?
        If using SmartGraphDemoContainer you can toggle this in the UI 
         */
        //graphView.setAutomaticLayout(true);

        /* 
        Uncomment lines to test adding of new elements
         */
        //continuously_test_adding_elements(g, graphView);
        //stage.setOnCloseRequest(event -> {
        //    running = false;
        //});
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    private Graph<String, String> build_sample2_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A", ElementTypes.PACKAGE, "<<package>> A\n included: B, C, D");
        g.insertVertex("B", ElementTypes.INTERFACE);
        g.insertVertex("C", ElementTypes.COMPONENT);
        g.insertVertex("D", ElementTypes.COMPONENT);
//        g.insertVertex("main");

        g.insertEdge("A", "B", "AB", ArrowTypes.AGGREGATION);
        g.insertEdge("B", "C", "AC", ArrowTypes.DEPENDENCY);
        g.insertEdge("A", "D", "AD", ArrowTypes.COMPOSITION);

        return g;
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A", ElementTypes.PACKAGE, "<<package>> A\n included: B, C, D");
        g.insertVertex("B", ElementTypes.INTERFACE);
        g.insertVertex("C", ElementTypes.COMPONENT);
        g.insertVertex("D", ElementTypes.ENUM);
        g.insertVertex("E", ElementTypes.CLASS);
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB", ArrowTypes.AGGREGATION);
        g.insertEdge("B", "A", "AB2", ArrowTypes.DEPENDENCY);
        g.insertEdge("A", "C", "AC", ArrowTypes.COMPOSITION);
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD", ArrowTypes.REALIZATION);
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");
        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        g.insertVertex("main");
        g.insertVertex("mA");
        g.insertVertex("mB");
        g.insertVertex("mC");
        g.insertVertex("mD");

        g.insertEdge("main", "mA", "mA");
        g.insertEdge("main", "mB", "mB");
        g.insertEdge("main", "mC", "mC");
        g.insertEdge("mB", "mD", "mD");

        return g;
    }

    private Graph<String, String> build_flower_graph() {

        Graph<String, String> g = new GraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "C", "2");
        g.insertEdge("A", "D", "3");
        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");

        g.insertVertex("H");
        g.insertVertex("I");
        g.insertVertex("J");
        g.insertVertex("K");
        g.insertVertex("L");
        g.insertVertex("M");
        g.insertVertex("N");

        g.insertEdge("H", "I", "7");
        g.insertEdge("H", "J", "8");
        g.insertEdge("H", "K", "9");
        g.insertEdge("H", "L", "10");
        g.insertEdge("H", "M", "11");
        g.insertEdge("H", "N", "12");

        g.insertEdge("A", "H", "0");

        //g.insertVertex("ISOLATED");
        
        return g;
    }

    private static final Random random = new Random(/* seed to reproduce*/);

    private void continuously_test_adding_elements(Graph<String, String> g, GraphPanel<String, String> graphView) {
        //update graph
        running = true;
        final long ITERATION_WAIT = 3000; //milliseconds

        Runnable r;
        r = () -> {
            int count = 0;
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (running) {
                try {
                    Thread.sleep(ITERATION_WAIT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //generate new vertex with 2/3 probability, else connect two
                //existing
                String id = String.format("%02d", ++count);
                if (random.nextInt(3) < 2) {
                    //add a new vertex connected to a random existing vertex
                    Vertex<String> existing = get_random_vertex(g);
                    Vertex<String> vertexId = g.insertVertex(("V" + id));
                    g.insertEdge(existing, vertexId, ("E" + id));
                    
                    //this variant must be called to ensure the view has reflected the
                    //underlying graph before styling a node immediately after.
                    graphView.updateAndWait();
                    
                    //color new vertices
                    StylableNode stylableVertex = graphView.getStylableVertex(vertexId);
                    if(stylableVertex != null) {
                        stylableVertex.setStyle("-fx-fill: orange;");
                    }
                } else {
                    Vertex<String> existing1 = get_random_vertex(g);
                    Vertex<String> existing2 = get_random_vertex(g);
                    g.insertEdge(existing1, existing2, ("E" + id));
                    
                    graphView.update();
                }

                
            }
        };

        new Thread(r).start();
    }

    private static Vertex<String> get_random_vertex(Graph<String, String> g) {

        int size = g.numVertices();
        int rand = random.nextInt(size);
        Vertex<String> existing = null;
        int i = 0;
        for (Vertex<String> v : g.vertices()) {
            existing = v;
            if (i++ == rand) {
                break;
            }
        }
        return existing;
    }
}
