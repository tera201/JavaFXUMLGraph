package com.rnar.umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Enumerations extends PaneElement {

    private Rectangle main, enumLine;
    private double width=11.0, height=12.0;

    private static final double aspect_ratio = 11.0 / 12.0;

    public Enumerations() {
        super();
        this.main = new Rectangle(0, 0, this.width, this.height);
        this.enumLine = new Rectangle(0, 0, this.width, this.height / 2);
        this.getChildren().addAll(main, enumLine);
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
        this.enumLine.setWidth(this.width);
        this.enumLine.setHeight(this.height / 2);
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
        this.enumLine.getStyleClass().add(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.main.getStyleClass().clear();
        this.enumLine.getStyleClass().clear();
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.main.getStyleClass().remove(cssClass) &&
                this.enumLine.getStyleClass().remove(cssClass);
        return ans;
    }

}
