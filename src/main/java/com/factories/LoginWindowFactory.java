package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.CustomScreen;
import org.jetbrains.annotations.NotNull;

public class LoginWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // Get the content factory instance
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create content for the tool window
        Content content = contentFactory.createContent(new CustomScreen().getContent(), "Courses", false);
        // Add the content to the tool window
        toolWindow.getContentManager().addContent(content);
    }
}
