package com.rnar.umlgraph.graphview;

import javafx.scene.text.Text;

/**
 * A label contains text and can be attached to any {@link SmartLabelledNode}.
 * <br>
 * This class extends from {@link Text} and is allowed any corresponding
 * css formatting.
 * 
 * @author r.naryshkin99
 */
public class SmartLabel extends Text implements SmartStylableNode {
    
    private final SmartStyleProxy styleProxy;
    
    public SmartLabel() {
        this(0,0,"");
    }

    public SmartLabel(String text) {
        this(0, 0, text);
    }

    public SmartLabel(double x, double y, String text) {
        super(x, y, text);
        styleProxy = new SmartStyleProxy(this);
    }
    
    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }
    
}
