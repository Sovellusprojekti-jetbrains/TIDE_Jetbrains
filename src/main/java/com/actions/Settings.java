package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.views.SettingsScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is used to display settings window to configure tide-settings
 */
@State(
        name = "Settings",
        storages = @Storage(StoragePathMacros.WORKSPACE_FILE)
)
public class Settings extends AnAction implements PersistentStateComponent<com.actions.Settings.State> {
    private SettingsScreen window = null; //Object reference to window content
    private static boolean visible = false; //To ensure that only one settings window can be open at a time
    private static JFrame frame = null; //Object reference to JFrame containing window content
    private static final State settings = new State(); //To save settings

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

    public static class State {
        public String path = null;
        public final String DEFAULT_PATH = System.getProperty("user.dir");
    }

    @Override
    public @Nullable Settings.State getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull Settings.State state) {
        XmlSerializerUtil.copyBean(state, settings);
    }

    @Override
    public void initializeComponent() {
        PersistentStateComponent.super.initializeComponent();
    }

    public static String getPath() {
        if (settings.path == null) {
            return settings.DEFAULT_PATH;
        }
        return settings.path;
    }

    public static void savePath(String path) {
        settings.path = path;
    }
}
