package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.state.ActiveState;
import com.views.CourseMainPane;
import com.views.CourseTaskPane;
import com.views.OutputWindow;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating an instance of the Login/Course view window.
 * Also initializes other ToolWindows so they exist when needed.
 */
public final class TideWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        initializeToolWindows(project);
        ActiveState.getInstance().initProjectDependents();
        CourseMainPane loginWindow = new CourseMainPane(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(loginWindow.getContent(), "Courses", false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
    }


    /**
     * Initializes other ToolWindows of this plugin.
     * @param project The current project
     */
    private void initializeToolWindows(Project project) {
        // Task ToolWindow:
        ToolWindow taskToolWindow = ToolWindowManager.getInstance(project)
                .registerToolWindow("Course Task", false, ToolWindowAnchor.RIGHT);
        var taskToolWindowContent = taskToolWindow.getContentManager()
                .getFactory()
                .createContent(new CourseTaskPane(taskToolWindow).getContent(), "Task", true);
        taskToolWindow.getContentManager().addContent(taskToolWindowContent);
        // Output ToolWindow:
        ToolWindow outputToolWindow = ToolWindowManager.getInstance(project)
                .registerToolWindow("Output Window", false, ToolWindowAnchor.BOTTOM);
        var outputToolWindowContent = outputToolWindow.getContentManager()
                .getFactory()
                .createContent(new OutputWindow(outputToolWindow).getContent(), "Output", true);
        outputToolWindow.getContentManager().addContent(outputToolWindowContent);
    }
}
