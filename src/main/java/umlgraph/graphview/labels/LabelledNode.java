package umlgraph.graphview.labels;

/**
 * A node to which a {@link Label} can be attached.
 * 
 * @author r.naryshkin99
 */
public interface LabelledNode {
    
    /**
     * Own and bind the <code>label</code> position to the desired position.
     * 
     * @param label     text label node
     */
    public void attachLabel(Label label);
    
    /**
     * Returns the attached text label, if any.
     * 
     * @return      the text label reference or null if no label is attached
     */
    public Label getAttachedLabel();
    
}
