package com.views;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;

/**
 * Luokka, joka palauttaa tehtävän tiedot sisältävän sekä sen palauttamiseen ja
 * uudelleenaloittamiseen käytettävät toiminnot sisältävän paneelin.
 */
public class CoursePaneWindow {
    /**
     * Panel that contains the task information and the buttons to submit and reset it.
     */
    private JPanel coursePane;
    private static CourseTaskPane pane;

    /**
     * haetaan Paneeli courseTaksPane luokalta.
     * @param toolWindow the toolWindow that is used to contain the course task panel
     */
    public CoursePaneWindow(final ToolWindow toolWindow) {
        pane = new CourseTaskPane(toolWindow);
        coursePane = pane.getContent();
    }

    /**
     * palautetaan paneeli.
     * @return paneeli
     */
    public JPanel getContent() {
        return coursePane;
    }

    /**
     * Getter for the instance of CourseTaskPane.
     * @return CourseTaskPane.
     */
    public static CourseTaskPane getPane() {
        return pane;
    }
}
