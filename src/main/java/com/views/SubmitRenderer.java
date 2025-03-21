package com.views;

import com.actions.ActiveState;
import com.actions.StateManager;
import com.api.JsonHandler;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * renderer for the leaf nodes of subtasks that have been submitted.
 */
public class SubmitRenderer extends DefaultTreeCellRenderer {

    /**
     * The constructor for the tree rendering component.
     * @param tree      the receiver is being configured for
     * @param value     the value to render
     * @param sel  whether node is selected
     * @param expanded  whether node is expanded
     * @param leaf      whether node is a lead node
     * @param row       row index
     * @param hasFocus  whether node has focus
     * @return the rendered component
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
     * method used to set icons for subtasks with 0 or more points.
     * @param value the node that the icon belongs to
     * @return the correct icon depending on the points given
     */
    private Icon isSubmitted(Object value) {
        List<String> submits = ApplicationManager.getApplication().getService(StateManager.class).getSubmits();
        JsonHandler hand = new JsonHandler();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;;
        //String regex = node.getParent().toString() + "/" + node.toString();
        if (submits != null) {
            for (String s : submits) {
                if (s.contains(node.toString())) {
                    float max = hand.getMaxPoints(node.getParent());
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) == 0 && node.getChildCount() == 0) {
                        return AllIcons.Debugger.Db_set_breakpoint;
                    }
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) > 0 && node.getChildCount() == 0) {
                        return AllIcons.Debugger.Db_no_suspend_breakpoint;
                    }
                    if (ApplicationManager.getApplication().getService(StateManager.class).getPoints(s) == && node.getChildCount() == 0);
                }

            }
        }
        return AllIcons.Empty;
    }
}
