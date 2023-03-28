package com.rnar.umlgraph.graphview;

import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * A shape of an arrow to be attached to a {@link SmartGraphEdge}.
 * 
 * @author r.naryshkin99
 */
public class SmartArrow extends Path implements SmartStylableNode {
    
    /* Styling proxy */
    private final SmartStyleProxy styleProxy;
    
    /**
     * Constructor
     * 
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public SmartArrow(double size) {
        
        /* Create this arrow shape */
        getElements().add(new MoveTo(0, 0));
        getElements().add(new LineTo(-size, size));
        getElements().add(new MoveTo(0, 0));
        getElements().add(new LineTo(-size, -size));
        getElements().add(new MoveTo(-size, -size));
        getElements().add(new LineTo(-size, size));
        getElements().add(new ClosePath());

        /* Add the corresponding css class */
        styleProxy = new SmartStyleProxy(this);
        styleProxy.addStyleClass("arrow");      
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }
    
}
