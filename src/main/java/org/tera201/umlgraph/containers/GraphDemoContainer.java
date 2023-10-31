package org.tera201.umlgraph.containers;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.tera201.umlgraph.graphview.GraphPanel;

/**
 *
 * @author r.naryshkin99
 */
public class GraphDemoContainer extends BorderPane {

    public GraphDemoContainer(GraphPanel graphView) {
        
        setCenter(new ContentZoomPane(graphView));
        
        //create bottom pane with controls
        HBox bottom = new HBox(10);
        
        CheckBox automatic = new CheckBox("Automatic layout");
        Button resetPlaceButton = new Button("Reset places");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());
        resetPlaceButton.setOnAction(e -> graphView.resetPlaceStrategy());
        
        bottom.getChildren().add(automatic);
        bottom.getChildren().add(resetPlaceButton);
        
        setBottom(bottom);        
    }
    
    
    
}
