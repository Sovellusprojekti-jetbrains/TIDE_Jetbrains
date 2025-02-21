package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class OutputWindowFactory implements ToolWindowFactory {

    private JTextArea textArea;
    private static OutputWindowFactory instance;

    public OutputWindowFactory() {
        instance = this; // Store instance for access
    }



    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel panel = new JPanel(new BorderLayout());

        textArea = new JBTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JBScrollPane(textArea);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearText());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static OutputWindowFactory getInstance() {
        return instance;
    }

    public void printText(String text) {
        if (textArea != null) {
            textArea.append(text + "\n");
        }
    }

    public void clearText() {
        if (textArea != null) {
            textArea.setText("");
        }
    }
}
