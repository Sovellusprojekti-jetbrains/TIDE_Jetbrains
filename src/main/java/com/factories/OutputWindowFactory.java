package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.OutputWindow;
import org.jetbrains.annotations.NotNull;

/**
 * A factory to create an OutputWindow instance.
 */
public final class OutputWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        OutputWindow outputWindow = new OutputWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(outputWindow.getContent(), "Output", false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
    }
}
