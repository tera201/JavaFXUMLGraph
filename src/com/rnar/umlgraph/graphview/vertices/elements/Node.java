package com.rnar.umlgraph.graphview.vertices.elements;

import com.rnar.umlgraph.graph.Vertex;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Node<T> implements Element {

    private Rectangle main;

    private PaneElement paneElement;

    private Label label;

    private ElementTypes elementTypes = ElementTypes.CLASS;

    public Node(String name, Vertex<T> v) {
        this.label = new Label(name);
        this.elementTypes = v.getType();
        switch (elementTypes) {
            case ENUM -> paneElement = new Enumerations();
            case PACKAGE -> paneElement = new Packages();
            case INTERFACE -> paneElement = new Interfaces();
            case COMPONENT -> paneElement = new Components();
            default -> paneElement = new Classes();
        }
        init();
    }

    public Node(String name, PaneElement paneElement) {
        this.label = new Label(name);
        this.paneElement = paneElement;
        init();
    }

    private void init(){
        label.setFont(new Font("Arial", 30));
        label.setLayoutX(0);
        label.setLayoutY(0);

        this.main = new Rectangle(0, 0);
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.main.setWidth(newVal.doubleValue() + this.main.getStrokeWidth() * 4 + this.paneElement.getWidth());
        });

        label.heightProperty().addListener((obs, oldVal, newVal) -> {
            this.main.setHeight(newVal.doubleValue() + this.main.getStrokeWidth());
            this.paneElement.setSizeByHeight(newVal.doubleValue() / 2);
            this.paneElement.setLayoutX(this.main.getStrokeWidth() * 2);
            this.paneElement.setLayoutY(newVal.doubleValue() / 4);
            this.label.setLayoutX(this.paneElement.getLayoutX() + this.paneElement.getWidth() + this.main.getStrokeWidth() * 2);
            this.main.setWidth(this.main.getWidth() + this.paneElement.getWidth());
        });


        switch (elementTypes) {
            case ENUM -> setStyleClass("enumerations");
            case PACKAGE -> setStyleClass("packages");
            case INTERFACE -> setStyleClass("interfacesHard");
            case COMPONENT -> setStyleClass("components");
            default -> setStyleClass("classes");
        }
    };

    public Rectangle getMainBox() {
        return main;
    }

    public PaneElement getPaneElement() {
        return paneElement;
    }

    @Override
    public double getWidth() {
        return this.main.getWidth();
    }

    @Override
    public double getHeight() {
        return this.main.getHeight();
    }
    @Override
    public void addTo(Pane pane) {
        pane.getChildren().addAll(main, label, paneElement);
    }
    @Override
    public void setStyleClass(String cssClass) {
        cleanStyleClass(cssClass);
        addStyleClass(cssClass);
        this.paneElement.setStyleClass(cssClass);
    }
    @Override
    public void addStyleClass(String cssClass) {
        this.main.getStyleClass().add(cssClass);
        this.paneElement.addStyleClass(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.main.getStyleClass().clear();
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.main.getStyleClass().remove(cssClass) &&
                this.paneElement.removeStyleClass(cssClass);
        return ans;
    }

}
