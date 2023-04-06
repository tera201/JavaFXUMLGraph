package umlgraph.graphview.vertices.elements;


import umlgraph.graphview.GraphPanel;
import javafx.scene.layout.Pane;

/**
 * Abstracts the internal representation and behavior of a visualized graph vertex.
 * 
 * @param <V> Type stored in the underlying vertex
 * 
 * @see GraphPanel
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
