package com.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.views.CoursePaneWindow;
import org.jetbrains.annotations.NotNull;

// luokka, joka luo ikkunan joka sisältää CoursePaneWindow luokassa haetun paneelin
public class CoursePaneWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow coursePaneWindow) {
        // Create an instance of your tool window class
        CoursePaneWindow myToolWindow = new CoursePaneWindow(coursePaneWindow);

        // Get the content factory instance
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create content for the tool window
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        // Add the content to the tool window
        coursePaneWindow.getContentManager().addContent(content);
    }
}
