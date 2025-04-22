package com.actions;

import com.api.LogHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.ui.Messages;

/**
 * Class for the about-screen.
 */
public class About extends AnAction {
    private final String manualUrl  = "https://tim.jyu.fi/view/kurssit/tie/proj/2025/tide-jetbrains/tide-jetbrains-lisaosan-kayttoohjeet";

    /**
     * Method for performed action.
     * @param e Action event from somewhere.
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
        StringBuilder submitShortcut = new StringBuilder();

        for (var key : shortcuts) {
            submitShortcut.append(key.toString());
            submitShortcut.append(System.lineSeparator());
        }
        String message = String.format(
                """
                %s \n
                See instruction here: \n
                %s \n
                Keyboard shortcut for submit: %s""", version, manualUrl, submitShortcut);
        Messages.showMessageDialog(
                message,
                "About",
                Messages.getInformationIcon()
        );
    }
}
