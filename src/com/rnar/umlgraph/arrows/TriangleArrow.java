package com.rnar.umlgraph.arrows;

import com.rnar.umlgraph.graphview.SmartGraphEdge;
import com.rnar.umlgraph.graphview.SmartStyleProxy;
import javafx.scene.shape.Polygon;

/**
 * A shape of an arrow to be attached to a {@link SmartGraphEdge}.
 * 
 * @author r.naryshkin99
 */
public class TriangleArrow extends DefaultArrow {

    /* Styling proxy */
    private final SmartStyleProxy styleProxy;
    private Polygon polygon;

    /**
     * Constructor
     *
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public TriangleArrow(double size) {
        super();
        
        /* Create this arrow shape */
        this.polygon = new Polygon(0, 0, - 2 * size, size, -2 * size, -size);
        this.setHeight(size * 4);
        this.getChildren().add(this.polygon);
        
        /* Add the corresponding css class */
        styleProxy = new SmartStyleProxy(polygon);
        styleProxy.addStyleClass("arrow-black");
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
