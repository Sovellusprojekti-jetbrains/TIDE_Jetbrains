package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.views.SettingsScreen;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is used to display settings window to configure tide-settings.
 */
public class Settings extends AnAction {
    /**
     * Width of the settings screen.
     */
    private final int width = 600;
    /**
     * Height of the settings screen.
     */
    private final int height = 400;
    private SettingsScreen window = null; //Object reference to window content.
    private static boolean visible = false; //To ensure that only one settings window can be open at a time.
    private static JFrame frame = null; //Object reference to JFrame containing window content.

    /**
     * If settings is called via an action, this method is called.
     * @param e Action event (not used)
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.window = new SettingsScreen();
        showSettings();
    }

    /**
     * Displays settings window if one is not already open.
     */
    private void showSettings() {
        if (!visible) {
            visible = true;
            SwingUtilities.invokeLater(() -> {
                frame = new JFrame("Settings");
                frame.add(window.getContent());
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        visible = false;
                    }
                });
                frame.setSize(width, height);
                frame.setVisible(true);
            });
        }
    }

    /**
     * This method can be called from action listeners to open settings window.
     */
    public void displaySettings() {
        this.window = new SettingsScreen();
        showSettings();
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
}
