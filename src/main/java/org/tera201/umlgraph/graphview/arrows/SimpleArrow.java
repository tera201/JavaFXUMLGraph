package org.tera201.umlgraph.graphview.arrows;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * A shape of an arrow to be attached to an Edge.
 * 
 * @author r.naryshkin99
 */
public class SimpleArrow extends DefaultArrow {

    private Path path;

    /**
     * Constructor
     *
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public SimpleArrow(double size) {
        super();
        this.path = new Path();
        this.path.getElements().add(new MoveTo(0, 0));
        this.path.getElements().add(new LineTo(-2 * size, size));
        this.path.getElements().add(new MoveTo(0, 0));
        this.path.getElements().add(new LineTo(-2 * size, -size));
        this.getChildren().add(this.path);
        addStyleClass("arrow");
    }

    @Override
    public void addStyleClass(String cssClass) {
        path.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return path.getStyleClass().remove(cssClass);
    }

    @Override
    public void clearStyleClass() {
        path.getStyleClass().clear();
    }
    
}
