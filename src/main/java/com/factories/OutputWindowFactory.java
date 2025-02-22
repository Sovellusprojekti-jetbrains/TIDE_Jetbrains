package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.OutputWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class OutputWindowFactory implements ToolWindowFactory {

    private JTextArea textArea;
    private static OutputWindowFactory instance;


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow outputWindow) {
        // Create an instance of your tool window class
        OutputWindow myToolWindow = new OutputWindow(outputWindow);

        // Get the content factory instance
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create content for the tool window
        Content content = contentFactory.createContent(myToolWindow.getContent(), "Output", false);

        content.setCloseable(false);

        // Add the content to the tool window
        outputWindow.getContentManager().addContent(content);
    }

}
