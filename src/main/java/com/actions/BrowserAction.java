package com.actions;

import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.state.ActiveState;
import com.util.Config;
import org.jetbrains.annotations.NotNull;

/**
 * AnAction to view the current exercise in TIM.
 */
public final class BrowserAction extends AnAction {
    /**
     * View the currently open exercise in TIM.
     * @param event AnActionEvent originating from IntelliJ platform's internal messaging system.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        TimTask.getInstance().openInBrowser(Config.BROWSER_BASE_URL, ActiveState.getInstance().getProject());
    }

    /**
     * This function is called by the IDE when changes in the editor occur. Action's state will be updated.
     * @param e AnActionEvent originating from IntelliJ platform's internal messaging system.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        //TODO: more generalized TIDE class that inherits anAction and handles checks for disable and enable
        if (!ActiveState.getInstance().getLogin()) {
            e.getPresentation().setEnabled(ActiveState.getInstance().getLogin());
            return;
        }
        e.getPresentation().setEnabled(ActiveState.getInstance().isSubmittable());
    }
}
