package com.views;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.JPanel;

/**
 * Luokka, joka palauttaa tehtävän tiedot sisältävän sekä sen palauttamiseen ja
 * uudelleenaloittamiseen käytettävät toiminnot sisältävän paneelin.
 */
public class CoursePaneWindow {
    private JPanel coursePane;

    /**
     * haetaan Paneeli courseTaksPane luokalta.
     * @param toolWindow TODO: explain?
     */
    public CoursePaneWindow(ToolWindow toolWindow) {
        coursePane = new courseTaskPane().getContent();
    }

    /**
     * palautetaan paneeli.
     * @return paneeli
     */
    public JPanel getContent() {
        return coursePane;
    }
}
