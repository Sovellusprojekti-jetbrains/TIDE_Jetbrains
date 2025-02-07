package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.views.SettingsScreen;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is used to display settings window to configure tide-settings
 */
public class Settings extends AnAction {
    private SettingsScreen window = null; //Object reference to window content
    private static boolean visible = false; //To ensure that only one settings window can be open at a time

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.window = new SettingsScreen();
        showSettings();
    }

    private void showSettings() {
        if (!visible) {
            visible = true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame newWindow = new JFrame("Settings");
                    newWindow.add(window.getContent());
                    newWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            visible = false;
                        }
                    });
                    newWindow.setSize(400, 300);
                    newWindow.setVisible(true);
                }
            });
        }
    }

    public void displaySettings() {
        this.window = new SettingsScreen();
        showSettings();
    }
}
