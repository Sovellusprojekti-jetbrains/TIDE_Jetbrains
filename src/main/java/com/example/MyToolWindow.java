package com.example;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JPanel content;

    public MyToolWindow(ToolWindow toolWindow) {
        content = new CustomScreen().getContent();
    }

    public JPanel getContent() {
        return content;
    }
}
