package org.tera201.umlgraph;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.UMLGraphPanel;
import org.tera201.umlgraph.graphview.UMLGraphControlPanel;
import org.tera201.umlgraph.graphview.arrows.ArrowTypes;
import org.tera201.umlgraph.graphview.strategy.PlacementStrategy;
import org.tera201.umlgraph.graphview.vertices.elements.ElementTypes;

public class UMLGraphMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        Graph<String, String> graph = GraphSamples.SAMPLE_1.createGraph();
        UMLGraphPanel<String, String> graphPanel = initializeGraphView(graph);
        setupStage(graphPanel);
        graphPanel.init();
    }

    private UMLGraphPanel<String, String> initializeGraphView(Graph<String, String> graph) {
        PlacementStrategy strategy = new PlacementStrategy();
        return new UMLGraphPanel<>(graph, strategy);
    }

    private void setupStage(UMLGraphPanel<String, String> graphView) {
        Scene scene = new Scene(new UMLGraphControlPanel<>(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX UML Graph");
        stage.setMinHeight(600);
        stage.setMinWidth(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

enum GraphSamples {
    SAMPLE_1 {
        @Override
        public Graph<String, String> createGraph() {
            Graph<String, String> graph = new Graph<>();
            Vertex<String> packageV = graph.getOrCreateVertex("package", ElementTypes.PACKAGE, "<<package>> A\n included: interface, component, enum");
            Vertex<String> interfaceV = graph.getOrCreateVertex("interface", ElementTypes.INTERFACE);
            Vertex<String> componentV = graph.getOrCreateVertex("component", ElementTypes.COMPONENT);
            Vertex<String> enumV = graph.getOrCreateVertex("enum", ElementTypes.ENUM);
            Vertex<String> classV = graph.getOrCreateVertex("class", ElementTypes.CLASS);
            Vertex<String> fClassV = graph.getOrCreateVertex("f class");

            graph.getOrCreateEdge(packageV, interfaceV, "package - interface", ArrowTypes.AGGREGATION);
            graph.getOrCreateEdge(interfaceV, packageV, "interface - package", ArrowTypes.DEPENDENCY);
            graph.getOrCreateEdge(packageV, componentV, "package - component", ArrowTypes.COMPOSITION);
            graph.getOrCreateEdge(packageV, enumV, "package - enum");
            graph.getOrCreateEdge(interfaceV, componentV, "interface - component");
            graph.getOrCreateEdge(componentV, enumV, "component - enum", ArrowTypes.REALIZATION);
            graph.getOrCreateEdge(interfaceV, classV, "interface - class");
            graph.getOrCreateEdge(enumV, fClassV, "enum - f class");
            graph.getOrCreateEdge(fClassV, enumV, "f class - enum");
            graph.getOrCreateEdge(packageV, packageV, "package - package");

            return graph;
        }
    },
    SAMPLE_2 {
        @Override
        public Graph<String, String> createGraph() {
            Graph<String, String> graph = new Graph<>();
            Vertex<String> packageV = graph.getOrCreateVertex("package", ElementTypes.PACKAGE, "<<package>> A\n included: B, C, D");
            Vertex<String> interfaceV = graph.getOrCreateVertex("interface", ElementTypes.INTERFACE);
            Vertex<String> aComponentV = graph.getOrCreateVertex("a component", ElementTypes.COMPONENT);
            Vertex<String> bComponentV = graph.getOrCreateVertex("b component", ElementTypes.COMPONENT);

            graph.getOrCreateEdge(packageV, interfaceV, "package - interface", ArrowTypes.AGGREGATION);
            graph.getOrCreateEdge(interfaceV, aComponentV, "interface - a component", ArrowTypes.DEPENDENCY);
            graph.getOrCreateEdge(packageV, bComponentV, "package - b component", ArrowTypes.COMPOSITION);

            return graph;
        }
    };

    public abstract Graph<String, String> createGraph();
}