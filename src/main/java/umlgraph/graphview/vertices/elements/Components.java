package umlgraph.graphview.vertices.elements;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Components extends PaneElement {

    private Rectangle main, addOne, addTwo;
    private double width=11.0, height=14.0;
    private static final double aspect_ratio_main = 11.0 / 14.0;
    private static final double aspect_ratio_from_left = 3.0 / 14.0;
    private static final double aspect_ratio_from_top_one = 2.0 / 14.0;
    private static final double aspect_ratio_from_top_two = 8.0 / 14.0;

    public Components() {
        super();
        this.main = new Rectangle(this.height * aspect_ratio_from_left, 0, this.width, this.height);
        this.addOne = new Rectangle(0, this.height * aspect_ratio_from_top_one,
                this.height / 2, this.height * 2 / 7);
        this.addTwo = new Rectangle(0, this.height * aspect_ratio_from_top_two,
                this.height / 2, this.height * 2 / 7);
        this.getChildren().addAll(main, addOne, addTwo);
    }

    @Override
    public void setSizeByHeight(double height) {
        this.height = height;
        this.width = height * aspect_ratio_main;
        updateSize();
    }

    @Override
    public void setSizeByWidth(double width) {
        this.width = width;
        this.height = width / aspect_ratio_main;
        updateSize();
    }

    @Override
    protected void updateSize() {
        this.setHeight(this.height);
        this.setWidth(this.height * aspect_ratio_from_left + this.width);
        this.main.setWidth(this.width);
        this.main.setHeight(this.height);
        this.main.setX(this.height * aspect_ratio_from_left);
        this.addOne.setWidth(this.height / 2);
        this.addOne.setHeight(this.height * 2 / 7);
        this.addOne.setY(this.height * aspect_ratio_from_top_one);
        this.addTwo.setWidth(this.height / 2);
        this.addTwo.setY(this.height * aspect_ratio_from_top_two);
        this.addTwo.setHeight(this.height * 2 / 7);
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
        this.addOne.getStyleClass().add(cssClass);
        this.addTwo.getStyleClass().add(cssClass);
    }
    @Override
    public void cleanStyleClass(String cssClass) {
        this.main.getStyleClass().clear();
        this.addOne.getStyleClass().clear();
        this.addTwo.getStyleClass().clear();
    }
    @Override
    public boolean removeStyleClass(String cssClass) {
        boolean ans = this.main.getStyleClass().remove(cssClass) &&
                this.addOne.getStyleClass().remove(cssClass) &&
                this.addTwo.getStyleClass().remove(cssClass);
        return ans;
    }
}
