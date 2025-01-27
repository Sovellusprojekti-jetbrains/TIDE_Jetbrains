package com.example;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

//Luokka, joka palauttaa tehtävän tiedot sisältävän sekä sen palauttamiseen ja uudelleenaloittamiseen käytettävät toiminnot sisältävän paneelin
public class CoursePaneWindow {
    private JPanel coursePane;

    // haetaan Paneeli courseTaksPane luokalta
    public CoursePaneWindow(ToolWindow toolWindow) {
        coursePane = new courseTaskPane().getContent();
    }
// palautetaan paneeli
    public JPanel getContent() {
        return coursePane;
    }
}
