package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.VersionUtil;
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
        //String version = VersionUtil.class.getPackage().getImplementationVersion();
        Messages.showMessageDialog(
                "Tide 1.0",
                "About",
                Messages.getInformationIcon()
        );
    }
}
