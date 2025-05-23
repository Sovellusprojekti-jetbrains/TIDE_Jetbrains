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
 * AnAction to display the settings window to configure the TIDE plugin.
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

    /**
     * Calls StateManager to get value for whether to
     * open TIM documents in browser or IDE.
     * @return True for browser, false for IDE
     */
    public static boolean getBrowserChoice() {
        return ApplicationManager.getApplication().getService(StateManager.class).getBrowserChoice();
    }

    /**
     * Calls StateManager to set value for whether to
     * open TIM documents in browser or IDE.
     * @param choice True for browser, false for IDE
     */
    public static void setBrowserChoice(boolean choice) {
        ApplicationManager.getApplication().getService(StateManager.class).setBrowserChoice(choice);
    }

    /**
     * Calls StateManager for getting user defined tide install path.
     * @return File path as a String
     */
    public static String getTidePath() {
        return ApplicationManager.getApplication().getService(StateManager.class).getTidePath();
    }

    /**
     * Calls StateManager for setting new path defined by user.
     * @param path File path as a String
     */
    public static void saveTidePath(String path) {
        ApplicationManager.getApplication().getService(StateManager.class).setTidePath(path);
    }
}
