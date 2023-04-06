package umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Classes extends PaneElement {

    private Rectangle main, funct, atributes;
    private double width=11.0, height=12.0;

    private static final double aspect_ratio = 11.0 / 12.0;

    public Classes() {
        super();
        this.main = new Rectangle(0, 0, this.width, this.height);
        this.atributes = new Rectangle(0, 0, this.width, this.height / 3);
        this.funct = new Rectangle(0, 0, this.width, this.height * 2 / 3);
        this.getChildren().addAll(main, funct, atributes);
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
        this.atributes.setWidth(this.width);
        this.atributes.setHeight(this.height / 3);
        this.funct.setWidth(this.width);
        this.funct.setHeight(this.height * 2 / 3);
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
        this.atributes.getStyleClass().add(cssClass);
        this.funct.getStyleClass().add(cssClass);
        this.main.getStyleClass().add(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.atributes.getStyleClass().clear();
        this.funct.getStyleClass().clear();
        this.main.getStyleClass().clear();
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.atributes.getStyleClass().remove(cssClass) &&
                this.funct.getStyleClass().remove(cssClass) &&
                this.main.getStyleClass().remove(cssClass);
        return ans;
    }
}
