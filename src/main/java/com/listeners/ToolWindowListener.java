package com.listeners;

import com.api.LogHandler;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.views.CourseMainPane;
import com.views.CourseTaskPane;

/**
 * Reacts to TIDE toolwindows when they are opened.
 */
@Service
public final class ToolWindowListener implements ToolWindowManagerListener  {

    @Override
    public void toolWindowShown(ToolWindow toolWindow) {
        LogHandler.logInfo("ToolWindowListener: Tool Window listener was called.");
        var contentManager = toolWindow.getContentManager();
        if ("TIDE Tool Window".equals(toolWindow.getId())) { // the toolWindow here is the tool window that was opened
            LogHandler.logInfo("ToolWindowListener: Login window was opened.");

            for (Content content : contentManager.getContents()) {
                if ("Courses".equals(content.getTabName())) {
                    // Select existing tab instead of adding a new one
                    contentManager.setSelectedContent(content);
                    LogHandler.logInfo("ToolWindowListener: Tab '" + "Courses" + "' already exists. Selecting it.");
                    return;
                }
            }
            toolWindow.getContentManager().addContent(
                    com.intellij.ui.content.ContentFactory.getInstance()
                            .createContent(new CourseMainPane(toolWindow)
                                    .getContent(), "Courses", false));
        }
        if ("Course Task".equals(toolWindow.getId())) {
            LogHandler.logInfo("ToolWindowListener: Course view was opened.");
            for (Content content : contentManager.getContents()) {
                if ("Task View".equals(content.getTabName())) {
                    // Select existing tab instead of adding a new one
                    contentManager.setSelectedContent(content);
                    LogHandler.logInfo("ToolWindowListener: Tab '" + "Course View" + "' already exists. Selecting it.");
                    return;
                }
            }
            toolWindow.getContentManager().addContent(
                    com.intellij.ui.content.ContentFactory.getInstance().createContent(
                            new CourseTaskPane(toolWindow).getContent(), "Course View", false));
        }
        if ("Output Window".equals(toolWindow.getId())) {
            LogHandler.logInfo("ToolWindowListener: Output window was opened.");
            for (Content content : contentManager.getContents()) {
                if ("Output".equals(content.getTabName())) {
                    // Select existing tab instead of adding a new one
                    contentManager.setSelectedContent(content);
                    LogHandler.logInfo("ToolWindowListener: Tab '" + "Output Window" + "' already exists. Selecting it.");
                    return;
                }
            }
        }

    }
}
