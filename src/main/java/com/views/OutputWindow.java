package com.views;

import com.actions.ActiveStateManager;
import com.api.ActiveStateListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class OutputWindow {
    private JPanel panel;
    private JTextArea textArea;
    private static OutputWindow instance;

    /**
     * Tool window for the output of submitted tasks.
     * @param toolWindow A tool window.
     */
    public OutputWindow(@NotNull final ToolWindow toolWindow) {
        instance = this; // Store instance for access
        panel = new JPanel(new BorderLayout());

        textArea = new JBTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JBScrollPane(textArea);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearText());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);
    }

    /**
     * Gets the panel containing the content.
     * @return The panel.
     */
    public JPanel getContent() {
        return panel;
    }


    /**
     * Gets an instance of the toolwindow for calling purposes.
     * @return The toolwindow itself.
     */
    public static OutputWindow getInstance() {
        return instance;
    }

    /**
     * Prints text onto the output window.
     * @param text Text to print.
     */
    public void printText(String text) {
        if (textArea != null) {
            textArea.append(text + "\n");
        }
    }

    /**
     * Clears the text off the output window.
     */
    public void clearText() {
        ActiveStateManager stateManager = ActiveStateManager.getInstance();
        stateManager.increment();
        ActiveStateManager.getInstance().updateState(String.valueOf(stateManager.getCount()));
        if (textArea != null) {
            textArea.setText("");
        }

    }
}
