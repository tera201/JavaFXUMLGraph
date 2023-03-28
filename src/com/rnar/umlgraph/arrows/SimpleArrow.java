package com.rnar.umlgraph.arrows;

import com.rnar.umlgraph.graphview.SmartGraphEdge;
import com.rnar.umlgraph.graphview.SmartStyleProxy;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * A shape of an arrow to be attached to a {@link SmartGraphEdge}.
 * 
 * @author r.naryshkin99
 */
public class SimpleArrow extends DefaultArrow {

    /* Styling proxy */
    private final SmartStyleProxy styleProxy;
    private Path path;

    /**
     * Constructor
     *
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public SimpleArrow(double size) {
        super();
        
        /* Create this arrow shape */
        this.path = new Path();
        this.path.getElements().add(new MoveTo(0, 0));
        this.path.getElements().add(new LineTo(-2 * size, size));
        this.path.getElements().add(new MoveTo(0, 0));
        this.path.getElements().add(new LineTo(-2 * size, -size));

        this.getChildren().add(this.path);
        
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
