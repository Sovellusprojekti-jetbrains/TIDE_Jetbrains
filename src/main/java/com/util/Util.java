package com.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

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
}
