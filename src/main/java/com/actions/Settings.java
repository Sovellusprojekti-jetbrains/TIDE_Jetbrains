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
    private static JFrame frame = null; //Object reference to JFrame containing window content

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
                    frame = new JFrame("Settings");
                    frame.add(window.getContent());
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            visible = false;
                        }
                    });
                    frame.setSize(400, 300);
                    frame.setVisible(true);
                }
            });
        }
    }

    public void displaySettings() {
        this.window = new SettingsScreen();
        showSettings();
    }

    public static void close() {
        frame.dispose();
        visible = false;
    }
}
