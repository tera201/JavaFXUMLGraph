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

    default DoubleBinding sin(final ObservableDoubleValue a) {
        return createDoubleBinding(() -> Math.sin(a.get()), a);
    }

    default DoubleBinding cos(final ObservableDoubleValue a) {
        return createDoubleBinding(() -> Math.cos(a.get()), a);
    }

    default DoubleBinding abs(final ObservableDoubleValue a) {
        return createDoubleBinding(() -> Math.abs(a.get()), a);
    }

    default DoubleBinding min(final ObservableDoubleValue y, final ObservableDoubleValue x) {
        return createDoubleBinding(() -> Math.min(y.get(), x.get()), y, x);
    }


    default DoubleBinding toDegrees(final ObservableDoubleValue angrad) {
        return createDoubleBinding(() -> Math.toDegrees(angrad.get()), angrad);
    }

    default void setStyleClass(String cssClass) {
        clearStyleClass();
        addStyleClass(cssClass);
    }
    void addStyleClass(String cssClass);
    void clearStyleClass();
    boolean removeStyleClass(String cssClass);
}
