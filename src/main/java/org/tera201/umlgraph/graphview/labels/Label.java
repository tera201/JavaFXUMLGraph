package org.tera201.umlgraph.graphview.labels;

import org.tera201.umlgraph.graphview.StylableNode;
import org.tera201.umlgraph.graphview.StyleProxy;
import javafx.scene.text.Text;

/**
 * A label contains text and can be attached to any {@link LabelledNode}.
 * <br>
 * This class extends from {@link Text} and is allowed any corresponding
 * css formatting.
 * 
 * @author r.naryshkin99
 */
public class Label extends Text implements StylableNode {
    
    private final StyleProxy styleProxy;
    
    public Label() {
        this(0,0,"");
    }

    public Label(String text) {
        this(0, 0, text);
    }

    public Label(double x, double y, String text) {
        super(x, y, text);
        styleProxy = new StyleProxy(this);
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
