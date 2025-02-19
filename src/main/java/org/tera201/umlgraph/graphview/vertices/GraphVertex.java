package org.tera201.umlgraph.graphview.vertices;

import org.tera201.umlgraph.graph.Vertex;
import javafx.beans.property.DoubleProperty;


public interface GraphVertex<V> {
    

    Vertex<V> getUnderlyingVertex();
    void setPosition(double x, double y);
    double getPositionCenterX();
    double getPositionCenterY();
    double getRadius();
    DoubleProperty centerXProperty();
    DoubleProperty centerYProperty();
    double getCenterX();
    double getCenterY();
    default void setStyleClass(String cssClass) {
        clearStyleClass();
        addStyleClass(cssClass);
    }
    void addStyleClass(String cssClass);
    void clearStyleClass();
    boolean removeStyleClass(String cssClass);
}
