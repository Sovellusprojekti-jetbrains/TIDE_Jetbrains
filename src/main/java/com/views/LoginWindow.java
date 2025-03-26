package com.views;

import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Login window logic.
 */
public class LoginWindow {
    private JPanel content;

    /**
     * Constructor for a login window.
     * @param toolWindow some toolwindow?
     */
    public LoginWindow(@NotNull final ToolWindow toolWindow) {
        content = new CustomScreen(toolWindow).getContent();

    }

    /**
     * Gets the contents of the toolwindow.
     * @return contents of the window.
     */
    public JPanel getContent() {
        return content;
    }
}
