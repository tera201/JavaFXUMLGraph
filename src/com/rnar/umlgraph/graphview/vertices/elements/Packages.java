package com.rnar.umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Packages extends PaneElement {

    private Rectangle main;

    private Polygon polygon;
    private double width=11.0, height=14.0;

    private static final double aspect_ratio = 14.0 / 11.0;

    public Packages() {
        super();
        this.main = new Rectangle(0, 0, this.width, this.height);
        this.polygon = new Polygon(0, 0, this.width * 5 / 7, 0, this.width / 2, this.height * 4 / 11, 0, this.height * 4 / 11);
        this.getChildren().addAll(main, polygon);
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
        this.main.setWidth(this.width);
        this.main.setHeight(this.height);
        this.polygon.getPoints().set(2, this.width * 5 / 7);
        this.polygon.getPoints().set(4, this.width / 2);
        this.polygon.getPoints().set(5, this.width * 4 / 11);
        this.polygon.getPoints().set(7, this.width * 4 / 11);
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
        this.polygon.getStyleClass().add(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.main.getStyleClass().remove(cssClass);
        this.polygon.getStyleClass().add(cssClass);
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.main.getStyleClass().remove(cssClass) &&
                this.polygon.getStyleClass().remove(cssClass);
        return ans;
    }

}
