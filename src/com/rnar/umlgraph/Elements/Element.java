package com.rnar.umlgraph.Elements;


import com.rnar.umlgraph.graphview.SmartGraphPanel;
import javafx.scene.layout.Pane;

/**
 * Abstracts the internal representation and behavior of a visualized graph vertex.
 * 
 * @param <V> Type stored in the underlying vertex
 * 
 * @see SmartGraphPanel
 * 
 * @author r.naryshkin99
 */
public interface Element {

    public double getWidth();

    public double getHeight();

    public void addTo(Pane pane);

    public void setStyleClass(String cssClass);

    public void addStyleClass(String cssClass);

    public void cleanStyleClass(String cssClass);

    public boolean removeStyleClass(String cssClass);
}
