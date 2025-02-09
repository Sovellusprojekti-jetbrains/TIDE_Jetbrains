package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.views.SettingsScreen;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;



/**
 * Class for showing a settings screen. TODO: make it do something useful
 */
public class Settings extends AnAction {
    final int WIDTH = 400;
    final int HEIGHT = 300;

    private SettingsScreen window = null;

    /**
     * TODO: What does this do?
     * @param e TODO: what is this event?
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.window = new SettingsScreen();
        showSettings();
    }

    /**
     * Shows the settings window.
     */
    private void showSettings() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame newWindow = new JFrame("Settings");
                newWindow.add(window.getContent());
                newWindow.setSize(WIDTH, HEIGHT);
                newWindow.setVisible(true);
            }
        });
    }

    /**
     * TODO: explain
     */
    public void displaySettings() {
        this.window = new SettingsScreen();
        showSettings();
    }
}
