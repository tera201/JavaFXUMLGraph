package org.tera201.umlgraph;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tera201.umlgraph.containers.GraphDemoContainer;
import org.tera201.umlgraph.graph.DigraphEdgeList;
import org.tera201.umlgraph.graphview.GraphPanel;
import org.tera201.umlgraph.graphview.StylableNode;
import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.strategy.DigraphTreePlacementStrategy;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import org.tera201.umlgraph.graph.Digraph;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;

/**
 *
 * @author r.naryshkin99
 */
public class Main extends Application {

    @Override
    public void start(Stage ignored) {

        Graph<String, String> g = build_sample4_digraph();
        System.out.println(g);
        PlacementStrategy strategy = new DigraphTreePlacementStrategy();
        GraphPanel<String, String> graphView = new GraphPanel<>(g, strategy);

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
    private Graph<String, String> build_sample4_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();
        Vertex a = g.insertVertex("A", ElementTypes.PACKAGE, "<<package>> A\n included: B, C, D");
        Vertex b = g.insertVertex("B", ElementTypes.INTERFACE);
        Vertex c = g.insertVertex("C", ElementTypes.COMPONENT);
        Vertex d = g.insertVertex("D", ElementTypes.ENUM);
        Vertex e = g.insertVertex("E", ElementTypes.CLASS);
        Vertex f =  g.insertVertex("F");
        Vertex mn = g.insertVertex("main");
        g.insertEdge(a, b, "AB", ArrowTypes.AGGREGATION);
        g.insertEdge(b, a, "AB2", ArrowTypes.DEPENDENCY);
        g.insertEdge(a, c, "AC", ArrowTypes.COMPOSITION);
        g.insertEdge(a, d, "AD");
        g.insertEdge(b, c, "BC");
        g.insertEdge(c, d, "CD", ArrowTypes.REALIZATION);
        g.insertEdge(b, e, "BE");
        g.insertEdge(d, f, "DF2");
        g.insertEdge(f, d, "DF");

        //yep, it's a loop!
        g.insertEdge(a, a, "Loop");

        return g;
    }
    private Graph<String, String> build_sample3_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        Vertex a = g.insertVertex("A", ElementTypes.PACKAGE, "<<package>> A\n included: B, C, D");
        Vertex b = g.insertVertex("B", ElementTypes.INTERFACE);
        Vertex c = g.insertVertex("C", ElementTypes.COMPONENT);
        Vertex d = g.insertVertex("D", ElementTypes.COMPONENT);
//        g.insertVertex("main");

        g.insertEdge(a, b, "AB", ArrowTypes.AGGREGATION);
        g.insertEdge(b, c, "AC", ArrowTypes.DEPENDENCY);
        g.insertEdge(a, d, "AD", ArrowTypes.COMPOSITION);

        return g;
    }

    private static final Random random = new Random(/* seed to reproduce*/);

    private void continuously_test_adding_elements(Graph<String, String> g, GraphPanel<String, String> graphView) {
        //update graph
        var running = true;
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
