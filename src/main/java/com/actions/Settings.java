package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.state.StateManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * This class is used to display settings window to configure tide-settings.
 */
public class Settings extends AnAction {
    private static boolean visible = false; //To ensure that only one settings window can be open at a time.
    private static JFrame frame = null; //Object reference to JFrame containing window content.

    /**
     * If settings is called via an action, this method is called.
     * @param e Action event (not used)
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        ShowSettingsUtil.getInstance().showSettingsDialog(defaultProject, "TIDE Settings");
    }


    /**
     * Closes settings window.
     */
    public static void close() {
        frame.dispose();
        visible = false;
    }

    /**
     * Calls StateManager for getting user defined file path.
     * @return File path as a String
     */
    public static String getPath() {
       return ApplicationManager.getApplication().getService(StateManager.class).getPath();
    }

    /**
     * Calls StateManager for setting new path defined by user.
     * @param path File path as a String
     */
    public static void savePath(String path) {
        ApplicationManager.getApplication().getService(StateManager.class).setPath(path);
    }

    /**
     * Calls StateManager for getting CourseMainPane scroll speed.
     * @return Scroll speed value
     */
    public static int getScrollSpeed() {
        return ApplicationManager.getApplication().getService(StateManager.class).getScrollSpeed();
    }

    /**
     * Calls StateManager for setting CourseMainPane scroll speed.
     * @param speed New scroll speed value
     */
    public static void setScrollSpeed(int speed) {
        ApplicationManager.getApplication().getService(StateManager.class).setScrollSpeed(speed);
    }


    public static boolean getBrowserChoice() {
        return ApplicationManager.getApplication().getService(StateManager.class).getBrowserChoice();
    }

    public static void setBrowserChoice(boolean choice) {
        ApplicationManager.getApplication().getService(StateManager.class).setBrowserChoice(choice);
    }
}
