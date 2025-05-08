package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.CourseTaskPane;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating a CourseTaskPane instance.
 */
public final class CourseTaskPaneWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CourseTaskPane courseTaskPane = new CourseTaskPane(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(courseTaskPane.getContent(), "Task View", false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
    }
}
