package com.views;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.JPanel;

/**
 * Luokka, joka palauttaa tehtävän tiedot sisältävän sekä sen palauttamiseen ja
 * uudelleenaloittamiseen käytettävät toiminnot sisältävän paneelin.
 */
public class CoursePaneWindow {
    /**
     * Panel that contains the task information and the buttons to submit and reset it.
     */
    private JPanel coursePane;

    /**
     * haetaan Paneeli courseTaksPane luokalta.
     * @param toolWindow the toolWindow that is used to contain the course task panel
     */
    public CoursePaneWindow(ToolWindow toolWindow) {coursePane = new courseTaskPane().getContent();}

    /**
     * palautetaan paneeli.
     * @return paneeli
     */
    public JPanel getContent() {
        return coursePane;
    }
}
