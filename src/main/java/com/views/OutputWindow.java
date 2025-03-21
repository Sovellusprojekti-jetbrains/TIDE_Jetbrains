package com.views;

import com.actions.ActiveState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OutputWindow {
    private JPanel panel;
    private JTextArea textArea;
    private static OutputWindow instance;
    private Project project;

    /**
     * Tool window for the output of submitted tasks.
     * @param toolWindow A tool window.
     */
    public OutputWindow(@NotNull final ToolWindow toolWindow) {
        this.project = toolWindow.getProject();
        instance = this; // Store instance for access
        panel = new JPanel(new BorderLayout());

        textArea = new JBTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JBScrollPane(textArea);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearText());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);

        ActiveState stateManager = ActiveState.getInstance();
        stateManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("logout".equals(evt.getPropertyName())) {
                    hideWindow();
                }
                if ("login".equals(evt.getPropertyName())) {
                    showWindow();
                }
            }
        });
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
            ApplicationManager.getApplication().invokeLater(() -> {
                textArea.append(text + "\n");
            });
        }
    }

    /**
     * Clears the text off the output window.
     */
    public void clearText() {
        if (textArea != null) {
            ApplicationManager.getApplication().invokeLater(() -> {
                textArea.setText("");
            });
        }
    }

    /**
     * Makes the toolwindow unavailable.
     */
    private void hideWindow() {
        SwingUtilities.invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Output Window");
            assert window != null;
            window.setAvailable(false);
            System.out.println("Hide Window");
        });
    }

    /**
     * Makes the toolwindow available.
     */
    private void showWindow() {
        SwingUtilities.invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Output Window");
            assert window != null;
            window.setAvailable(true);
            System.out.println("Show Window");
        });
    }
}
