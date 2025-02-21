package org.tera201.umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;

public abstract class PaneElement extends Pane {
    private double width, height;

    public abstract void setSizeByHeight(double height);

    public abstract void setSizeByWidth(double width);

    protected abstract void updateSize();

    public abstract void addTo(Pane pane);
    public void setStyleClass(String cssClass) {
        clearStyleClass();
        addStyleClass(cssClass);
    }
    public abstract void addStyleClass(String cssClass);
    public abstract void clearStyleClass();
    public abstract boolean removeStyleClass(String cssClass);
}
