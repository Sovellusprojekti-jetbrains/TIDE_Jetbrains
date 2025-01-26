package com.example;

import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyToolWindow {
    private JPanel content;

    public MyToolWindow(@NotNull ToolWindow toolWindow) {
        content = new CustomScreen().getContent();

    }

    public JPanel getContent() {
        return content;
    }
}
