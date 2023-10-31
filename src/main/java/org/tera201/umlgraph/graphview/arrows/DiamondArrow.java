package org.tera201.umlgraph.graphview.arrows;

import org.tera201.umlgraph.graphview.edges.Edge;
import org.tera201.umlgraph.graphview.StyleProxy;
import javafx.scene.shape.Polygon;

/**
 * A shape of an arrow to be attached to a {@link Edge}.
 * 
 * @author r.naryshkin99
 */
public class DiamondArrow extends DefaultArrow {

    /* Styling proxy */
    private final StyleProxy styleProxy;
    private Polygon polygon;

    /**
     * Constructor
     *
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public DiamondArrow(double size) {
        super();
        
        /* Create this arrow shape */
        this.polygon = new Polygon(0, 0, - 2 * size, size, -4 * size, 0, -2 * size, -size);
        this.setHeight(size * 4);
        this.getChildren().add(this.polygon);

        /* Add the corresponding css class */
        styleProxy = new StyleProxy(polygon);
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
