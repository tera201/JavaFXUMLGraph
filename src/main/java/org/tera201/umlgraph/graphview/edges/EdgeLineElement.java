package org.tera201.umlgraph.graphview.edges;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graphview.arrows.DefaultArrow;

import static javafx.beans.binding.Bindings.createDoubleBinding;

public interface EdgeLineElement<E, V> {

    Edge<E, V> getUnderlyingEdge();

    DefaultArrow getAttachedArrow();

    void attachArrow(DefaultArrow arrow);

    default DoubleBinding atan2(final ObservableDoubleValue y, final ObservableDoubleValue x) {
        return createDoubleBinding(() -> Math.atan2(y.get(), x.get()), y, x);
    }


    default DoubleBinding toDegrees(final ObservableDoubleValue angrad) {
        return createDoubleBinding(() -> Math.toDegrees(angrad.get()), angrad);
    }

    default void setStyleClass(String cssClass) {
        cleanStyleClass();
        addStyleClass(cssClass);
    }
    void addStyleClass(String cssClass);
    void cleanStyleClass();
    boolean removeStyleClass(String cssClass);
}
