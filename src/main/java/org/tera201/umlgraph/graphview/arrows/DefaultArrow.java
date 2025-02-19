package org.tera201.umlgraph.graphview.arrows;

import javafx.scene.layout.Pane;

/**
 * A shape of an arrow to be attached to an Edge.
 * 
 * @author r.naryshkin99
 */
public abstract class DefaultArrow extends Pane {
    public void setStyleClass(String cssClass) {
        cleanStyleClass();
        addStyleClass(cssClass);
    }
    public void addStyleClass(String cssClass) {
        this.getStyleClass().add(cssClass);
    }
    public boolean removeStyleClass(String cssClass) {
        return this.getStyleClass().remove(cssClass);
    }
    public void cleanStyleClass() {
        this.getStyleClass().clear();
    }
}
