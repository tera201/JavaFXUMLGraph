package com.rnar.umlgraph.graphview;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Shape;

/**
 * This class acts as a proxy for styling of nodes.
 * 
 * It essentially groups all the logic, avoiding code duplicate.
 * 
 * Classes that have this behavior can delegate the method calls to an instance
 * of this class.
 * 
 * @author r.naryshkin99
 */
public class SmartStyleProxy implements SmartStylableNode {

    private final Node client;
    
    public SmartStyleProxy(Shape client) {
        this.client = client;
    }

    public SmartStyleProxy(Parent client) {
        this.client = client;
    }
    
    @Override
    public void setStyle(String css) {
        client.setStyle(css);
    }

    @Override
    public void setStyleClass(String cssClass) {
        client.getStyleClass().clear();
        client.setStyle(null);
        client.getStyleClass().add(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        client.getStyleClass().add(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return client.getStyleClass().remove(cssClass);
    }
    
}
