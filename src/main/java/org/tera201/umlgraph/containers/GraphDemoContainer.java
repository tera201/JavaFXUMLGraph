package org.tera201.umlgraph.containers;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.tera201.umlgraph.graphview.GraphPanel;

/**
 *
 * @author r.naryshkin99
 */
public class GraphDemoContainer<V,E> extends BorderPane {
    private ContentZoomPane contentZoomPane;
    private final GraphPanel<V,E> graphView;

    public GraphDemoContainer(GraphPanel<V,E> graphView) {
        this.contentZoomPane = new ContentZoomPane(graphView);
        this.graphView = graphView;
        
        setCenter(contentZoomPane);
        
        //create bottom pane with controls
        HBox bottom = new HBox(10);
        Button resetPlaceButton = new Button("Reset places");
        Button resetView = new Button("Reset View");
        resetPlaceButton.setOnAction(e -> this.graphView.resetPlaceStrategy());
        resetView.setOnAction(event -> contentZoomPane.resetView());

        bottom.getChildren().add(resetPlaceButton);
        bottom.getChildren().add(resetView);
        
        setBottom(bottom);        
    }

    public void update() {
        contentZoomPane.update();
    }
    
    
}
