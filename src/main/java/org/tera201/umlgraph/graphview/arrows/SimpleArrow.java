package org.tera201.umlgraph.graphview.arrows;

import org.tera201.umlgraph.graphview.edges.EdgeBase;
import org.tera201.umlgraph.graphview.StyleProxy;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * A shape of an arrow to be attached to a {@link EdgeBase}.
 * 
 * @author r.naryshkin99
 */
public class SimpleArrow extends DefaultArrow {

    /* Styling proxy */
    private final StyleProxy styleProxy;
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
        styleProxy = new StyleProxy(this);
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
