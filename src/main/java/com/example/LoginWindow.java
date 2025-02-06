package com.example;

import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class LoginWindow {
    private JPanel content;

    public LoginWindow(@NotNull ToolWindow toolWindow) {
        content = new CustomScreen().getContent();

    }

    public JPanel getContent() {
        return content;
    }
}
