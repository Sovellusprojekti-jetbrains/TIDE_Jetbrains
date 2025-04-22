package com.actions;

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

    /**
     * Method for performed action.
     * @param e Action event from somewhere.
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        String version = com.actions.PluginInfo.VERSION;
        Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
        var shortcuts = keymap.getShortcuts("com.actions.Submit");
        StringBuilder submitShortcut = new StringBuilder();

        for (var key : shortcuts) {
            submitShortcut.append(key.toString());
        }
        String message = String.format(
                """
                Tide %s \n
                See instruction here: \n
                https://tim.jyu.fi/view/kurssit/tie/proj/2025/tide-jetbrains/tide-jetbrains-lisaosan-kayttoohjeet \n
                Default keyboard shortcut for submit: %s""", version, submitShortcut);
        Messages.showMessageDialog(
                message,
                "About",
                Messages.getInformationIcon()
        );
    }
}
