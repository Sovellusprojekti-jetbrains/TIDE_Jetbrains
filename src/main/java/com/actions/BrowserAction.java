package com.actions;

import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.state.ActiveState;
import org.jetbrains.annotations.NotNull;

public final class BrowserAction extends AnAction {

    private static final String BASE_URL = "https://tim.jyu.fi/view/";


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        TimTask.getInstance().openInBrowser(BASE_URL, ActiveState.getInstance().getProject());
    }

    /**
     * This function is called by the IDE when changes in the editor occur. Action's state will be updated.
     * @param e AnActionEvent originating from idea's internal messaging system.
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
