package com.actions;

import com.api.LogHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.ui.Messages;
import com.util.*;

/**
 * AnAction to view the about-screen.
 */
public class About extends AnAction {
    /**
     * View the about-screen.
     * @param e AnActionEvent originating from IntelliJ platform's internal messaging system.
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        String version;
        try {
            version = "Tide " + com.actions.PluginInfo.VERSION;
        } catch (Exception ex) {
            version = "Tide";
            LogHandler.logError("Submit action", ex);
        }
        Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
        var shortcuts = keymap.getShortcuts("com.actions.Submit");

        String message = String.format(
                """
                <html>
                    <body>
                        <p>%s</p>
                        <p>Learn how to use the extension: <a href='%s'>tide instructions</a></p>
                        <p>Keyboard shortcut for submit: %s</p>
                    </body>
                </html>""", version, Config.MANUAL_URL, KeymapUtil.getShortcutsText(shortcuts));
        Messages.showMessageDialog(
                message,
                "About",
                Messages.getInformationIcon()
        );
    }
}
