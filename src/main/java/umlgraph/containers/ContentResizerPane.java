package umlgraph.containers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 *
 * @author r.naryshkin99
 */
public class ContentResizerPane extends Pane {

    private final Node content;
    private final DoubleProperty resizeFActor = new SimpleDoubleProperty(1);

    public ContentResizerPane(Node content) {
        this.content = content;

        getChildren().add(content);

        Scale scale = new Scale(1, 1);
        content.getTransforms().add(scale);

        resizeFActor.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            scale.setX(newValue.doubleValue());
            scale.setY(newValue.doubleValue());
            requestLayout();
        });
    }


    @Override
    protected void layoutChildren() {
        Pos pos = Pos.TOP_LEFT;
        double width = getWidth();
        double height = getHeight();
        double top = getInsets().getTop();
        double right = getInsets().getRight();
        double left = getInsets().getLeft();
        double bottom = getInsets().getBottom();
        double contentWidth = (width - left - right) / resizeFActor.get();
        double contentHeight = (height - top - bottom) / resizeFActor.get();
        layoutInArea(content, left, top,
                contentWidth, contentHeight,
                0, null,
                pos.getHpos(),
                pos.getVpos());
    }

    public final Double getResizeFactor() {
        return resizeFActor.get();
    }

    public final void setResizeFactor(Double resizeFactor) {
        this.resizeFActor.set(resizeFactor);
    }

    public final DoubleProperty resizeFactorProperty() {
        return resizeFActor;
    }
}
