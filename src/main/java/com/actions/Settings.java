package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.views.SettingsScreen;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class Settings extends AnAction {
    private SettingsScreen window = null;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.window = new SettingsScreen();
        showSettings();
    }

    private void showSettings() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame newWindow = new JFrame("Settings");
                newWindow.add(window.getContent());
                newWindow.setSize(400, 300);
                newWindow.setVisible(true);
            }
        });
    }

    public void displaySettings() {
        this.window = new SettingsScreen();
        showSettings();
    }
}
