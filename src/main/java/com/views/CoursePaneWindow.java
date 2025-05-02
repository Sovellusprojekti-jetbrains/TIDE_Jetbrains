package com.views;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;

/**
 * Class that returns the panel containing the buttons and info to manipulate the task that is open in the editor.
 */
public class CoursePaneWindow {
    /**
     * Panel that contains the task information and the buttons to submit and reset it.
     */
    private JPanel coursePane;

    /**
     * Get the panel from CourseTaskPane.
     * @param toolWindow the toolWindow that is used to contain the course task panel
     */
    public CoursePaneWindow(final ToolWindow toolWindow) {
        coursePane = new CourseTaskPane(toolWindow).getContent();
    }

    /**
     * Returns the panel.
     * @return The panel.
     */
    public JPanel getContent() {
        return coursePane;
    }
}
