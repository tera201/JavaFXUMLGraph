package org.tera201.umlgraph;

import org.tera201.umlgraph.containers.GraphDemoContainer;
import org.tera201.umlgraph.graph.DigraphEdgeList;
import org.tera201.umlgraph.graphview.GraphPanel;
import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;
import org.tera201.umlgraph.graph.Vertex;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tera201.umlgraph.graph.Digraph;

/**
 *
 * @author r.naryshkin99
 */
public class Main extends Application {

    @Override
    public void start(Stage ignored) {

        Digraph<String, String> g = build_sample4_digraph();
        System.out.println(g);
        PlacementStrategy strategy = new PlacementStrategy();
        GraphPanel<String, String> graphView = new GraphPanel<>(g, null,  strategy, null);

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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    private Digraph<String, String> build_sample4_digraph() {

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
    private Digraph<String, String> build_sample3_digraph() {

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
}
