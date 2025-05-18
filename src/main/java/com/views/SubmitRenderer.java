package com.views;

import com.intellij.openapi.util.IconLoader;
import com.state.StateManager;
import com.course.DemoTask;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * renderer for the leaf nodes of demo tasks that have been submitted.
 */
public class SubmitRenderer extends DefaultTreeCellRenderer {

    /**
     * The constructor for the tree rendering component.
     * @param tree      the receiver is being configured for
     * @param value     the value to render
     * @param sel       whether node is selected
     * @param expanded  whether node is expanded
     * @param leaf      whether node is a lead node
     * @param row       row index
     * @param hasFocus  whether node has focus
     * @return          the rendered component
     */
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        if (leaf) {
            setIcon(isSubmitted(value));
        }
        setBackgroundNonSelectionColor(JBColor.background());
        return this;
    }

    /**
     * Sets icons for demo tasks with 0 or more points.
     * @param value The node that the icon belongs to
     * @return The correct icon depending on the points given
     */
    private Icon isSubmitted(Object value) {
        List<String> submits = ApplicationManager.getApplication().getService(StateManager.class).getSubmits();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (submits != null) {
            for (String s : submits) {
                if (node.getChildCount() == 0 &&  s.contains(node.toString())) {
                    DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode) node.getParent();
                    DemoTask parent = (DemoTask) parentNode.getUserObject();
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) == 0
                            && s.contains(parent.getIdeTaskId())) {
                        return IconLoader.getIcon("/icons/0_points.svg");
                    }
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) < parent.getMaxPoints()
                            && s.contains(parent.getIdeTaskId())) {
                        return IconLoader.getIcon("/icons/some_points.svg");
                    }
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) == parent.getMaxPoints()
                            && s.contains(parent.getIdeTaskId())) {
                        return IconLoader.getIcon("/icons/oikein.svg");
                    }
                }
            }
        }
        return AllIcons.Empty;
    }
}
