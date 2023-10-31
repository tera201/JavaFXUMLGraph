package org.tera201.umlgraph.graphview.vertices.elements;

import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class Interfaces extends PaneElement {

    private Circle main;
    private double width = 10.0, height = 10.0;
    private static final double aspect_ratio = 1.;

    public Interfaces() {
        super();
        this.main = new Circle(this.height / 2., this.height / 2., this.height / 2.);
        this.getChildren().add(main);
    }

    @Override
    public void setSizeByHeight(double height) {
        this.height = height;
        this.width = height * aspect_ratio;
        updateSize();
    }

    @Override
    public void setSizeByWidth(double width) {
        this.width = width;
        this.height = width / aspect_ratio;
        updateSize();
    }

    @Override
    protected void updateSize() {
        this.setHeight(this.height);
        this.setWidth(this.width);
        this.main.setRadius(this.height / 2);
        this.main.setCenterX(this.height / 2);
        this.main.setCenterY(this.height / 2);
    }
    @Override
    public void addTo(Pane pane) {
        pane.getChildren().add(this);
    }
    @Override
    public void setStyleClass(String cssClass) {
        cleanStyleClass(cssClass);
        addStyleClass(cssClass);
    }
    @Override
    public void addStyleClass(String cssClass) {
        this.main.getStyleClass().add(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.main.getStyleClass().clear();
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.main.getStyleClass().remove(cssClass);
        return ans;
    }

}
