package com.example;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class LoginWindow {
    private JPanel content;

    public LoginWindow(ToolWindow toolWindow) {
        content = new LoginScreen().getContent();
    }

    public JPanel getContent() {
        return content;
    }
}
