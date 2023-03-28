package com.rnar.umlgraph.containers;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import com.rnar.umlgraph.graphview.SmartGraphPanel;

/**
 *
 * @author r.naryshkin99
 */
public class SmartGraphDemoContainer extends BorderPane {

    public SmartGraphDemoContainer(SmartGraphPanel graphView) {
        
        setCenter(new ContentZoomPane(graphView));
        
        //create bottom pane with controls
        HBox bottom = new HBox(10);
        
        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());
        
        bottom.getChildren().add(automatic);
        
        setBottom(bottom);        
    }
    
    
    
}
