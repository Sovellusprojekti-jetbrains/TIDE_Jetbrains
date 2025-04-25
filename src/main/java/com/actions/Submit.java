package com.actions;

import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.state.ActiveState;
import org.jetbrains.annotations.NotNull;

public class Submit extends AnAction {
    /**
     * This function is called when subtask is submitted via TIDE dropdown menu action.
     * @param e AnActionEvent fired.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = ActiveState.getInstance().getProject();
        TimTask.getInstance().submit(project);
    }

    /**
     * This function is called by the IDE when changes in the editor occur. Action's state will be updated.
     * @param e AnActionEvent originating from idea's internal messaging system.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        if (!ActiveState.getInstance().getLogin()) {
            e.getPresentation().setEnabled(ActiveState.getInstance().getLogin());
            return;
        }
        e.getPresentation().setEnabled(ActiveState.getInstance().isSubmittable());
        ActiveState.getInstance().messageChanges();
    }
}
