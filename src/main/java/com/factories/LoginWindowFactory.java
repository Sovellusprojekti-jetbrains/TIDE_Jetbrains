package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.LoginWindow;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating a LoginWindow instance.
 */
public final class LoginWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        LoginWindow loginWindow = new LoginWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(loginWindow.getContent(), "Courses", false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
    }
}
