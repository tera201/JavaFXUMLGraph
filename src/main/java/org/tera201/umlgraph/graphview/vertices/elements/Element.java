package org.tera201.umlgraph.graphview.vertices.elements;


import javafx.scene.layout.Pane;

public interface Element {

    public double getWidth();

    public double getHeight();

    public void addTo(Pane pane);

    public void setStyleClass(String cssClass);

    public void addStyleClass(String cssClass);

    public void cleanStyleClass(String cssClass);

    public boolean removeStyleClass(String cssClass);
}
