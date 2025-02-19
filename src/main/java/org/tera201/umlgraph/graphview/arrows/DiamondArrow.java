package org.tera201.umlgraph.graphview.arrows;

import javafx.scene.shape.Polygon;

/**
 * A shape of an arrow to be attached to an Edge.
 * 
 * @author r.naryshkin99
 */
public class DiamondArrow extends DefaultArrow {

    private final Polygon polygon;

    /**
     * Constructor
     *
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public DiamondArrow(double size) {
        super();
        this.polygon = new Polygon(0, 0, - 2 * size, size, -4 * size, 0, -2 * size, -size);
        this.setHeight(size * 4);
        this.getChildren().add(this.polygon);
        addStyleClass("arrow-black");
    }

    @Override
    public void addStyleClass(String cssClass) {
        polygon.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return polygon.getStyleClass().remove(cssClass);
    }

    @Override
    public void clearStyleClass() {
        polygon.getStyleClass().clear();
    }
    
}
