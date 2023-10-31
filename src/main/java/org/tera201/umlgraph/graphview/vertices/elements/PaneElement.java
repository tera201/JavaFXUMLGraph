package org.tera201.umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;

public abstract class PaneElement extends Pane implements Element {
    private double width, height;

    public abstract void setSizeByHeight(double height);

    public abstract void setSizeByWidth(double width);

    protected abstract void updateSize();

    @Override
    public abstract void addTo(Pane pane);
    @Override
    public void setStyleClass(String cssClass) {
        cleanStyleClass(cssClass);
        addStyleClass(cssClass);
    }
    @Override
    public abstract void addStyleClass(String cssClass);
    @Override
    public abstract void cleanStyleClass(String cssClass);
    @Override
    public abstract boolean removeStyleClass(String cssClass);
}
