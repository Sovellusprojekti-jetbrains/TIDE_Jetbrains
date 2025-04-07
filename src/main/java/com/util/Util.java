package com.util;

import com.api.LogHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ReflectionUtil;

import javax.swing.*;

public final class Util {

    private Util() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Opens the given toolwindow.
     * @param project The project the window is in.
     * @param id Name of the toolwindow as specified in plugin.xml.
     * @param show A boolean value, true is show, false is hide.
     */
    public static void showWindow(Project project, String id, boolean show) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            assert window != null;
            if (show) {
                window.show(null);
            } else {
                window.hide(null);
            }
        });
    }


    /**
     * Changes the availability status of the toolwindow, and changes the icon.
     * @param project Project the window is in.
     * @param id Name of the window as specified in plugin.xml.
     * @param available Whether the window is set available or not.
     * @param iconPath Path to the icon svg.
     */
    public static void setWindowAvailable(Project project, String id, boolean available, String iconPath) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            var callerClass = ReflectionUtil.getGrandCallerClass();
            if (callerClass != null) {
                window.setIcon(IconLoader.getIcon(iconPath, callerClass));
            }
            window.setAvailable(available);
            LogHandler.logInfo("Set Window Available: " + available + ", icon = " + iconPath);
        });
    }


    /**
     * Changes the icons of Course Task Pane and Output Window.
     * @param project Project they're in.
     * @param iconPath Path to the icon.
     */
    public static void setIcons(Project project, String iconPath) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Output Window");
            ToolWindow window2 = toolWindowManager.getToolWindow("Course Task");
            var callerClass = ReflectionUtil.getGrandCallerClass();
            if (callerClass != null) {
                window.setIcon(IconLoader.getIcon(iconPath, callerClass));
                window2.setIcon(IconLoader.getIcon(iconPath, callerClass));
            }
        });
    }
}
