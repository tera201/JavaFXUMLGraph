package org.tera201.umlgraph.graphview.vertices.elements;

import org.tera201.umlgraph.graph.Vertex;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Element<T> {

    private Rectangle main;

    private final PaneElement paneElement;

    private final Label label;

    private ElementTypes elementTypes = ElementTypes.CLASS;

    public Element(Vertex<T> v) {
        this.label = new Label(v.getLabel());
        this.elementTypes = v.getType();
        switch (elementTypes) {
            case ENUM:
                paneElement = new Enumerations();
                break;
            case PACKAGE:
                paneElement = new Packages();
                break;
            case INTERFACE:
                paneElement = new Interfaces();
                break;
            case COMPONENT:
                paneElement = new Components();
                break;
            default: paneElement = new Classes();
        }
        init();
    }

    public Element(String name, Vertex<T> v) {
        this(v);
        this.label.setText(name);
    }

    public Element(String name, PaneElement paneElement) {
        this.label = new Label(name);
        this.paneElement = paneElement;
        init();
    }

    private void init(){
        label.getStyleClass().add("node-label");
        label.setLayoutX(0);
        label.setLayoutY(0);

        this.main = new Rectangle(0, 0);
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.main.setWidth(newVal.doubleValue() + this.main.getStrokeWidth() * 6 + this.paneElement.getWidth());
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
            case ENUM:
                setStyleClass("enumerations");
                break;
            case PACKAGE:
                setStyleClass("packages");
                break;
            case INTERFACE:
                setStyleClass("interfacesHard");
                break;
            case COMPONENT:
                setStyleClass("components");
                break;
            default: setStyleClass("classes");
        }
    };

    public Rectangle getMainBox() {
        return main;
    }

    public PaneElement getPaneElement() {
        return paneElement;
    }

    public double getWidth() {
        return this.main.getWidth();
    }

    public double getHeight() {
        return this.main.getHeight();
    }
    public void addTo(Pane pane) {
        pane.getChildren().addAll(main, label, paneElement);
    }
    public void setStyleClass(String cssClass) {
        clearStyleClass();
        addStyleClass(cssClass);
        this.paneElement.setStyleClass(cssClass);
    }
    public void addStyleClass(String cssClass) {
        this.main.getStyleClass().add(cssClass);
        this.paneElement.addStyleClass(cssClass);
    }
    public void clearStyleClass() {
        this.main.getStyleClass().clear();
    }
    public boolean removeStyleClass(String cssClass) {
        return this.main.getStyleClass().remove(cssClass) &&
                this.paneElement.removeStyleClass(cssClass);
    }

}
