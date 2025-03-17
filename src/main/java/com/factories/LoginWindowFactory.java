package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.LoginWindow;
import org.jetbrains.annotations.NotNull;

public final class LoginWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Create an instance of your tool window class
        LoginWindow loginWindow = new LoginWindow(toolWindow);

        // Get the content factory instance
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create content for the tool window
        Content content = contentFactory.createContent(loginWindow.getContent(), "Courses", false);

        content.setCloseable(false);
        // Add the content to the tool window
        toolWindow.getContentManager().addContent(content);
    }
}
