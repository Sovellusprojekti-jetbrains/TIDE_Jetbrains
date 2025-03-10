package com.views;

import com.actions.StateManager;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.List;

public class SubmitRenderer extends DefaultTreeCellRenderer {
    //AllIcons.Debugger.Db_set_breakpoint
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
        if (leaf && isSubmitted(value)) {
            setIcon(AllIcons.Debugger.Db_set_breakpoint);
        } //else {
            //setIcon(AllIcons.Debugger.Db_no_suspend_breakpoint);
        //}

        return this;
    }

    private boolean isSubmitted(Object value) {
        List<String> submits = ApplicationManager.getApplication().getService(StateManager.class).getSubmits();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String regex = node.getParent().getParent().toString() + "/" + node.getParent().toString() + "/" + node.toString();
        System.out.println(regex);
        for (String s : submits) {
            if (s.contains(regex)) {
                return true;
            }
        }
        return false;
    }
}
