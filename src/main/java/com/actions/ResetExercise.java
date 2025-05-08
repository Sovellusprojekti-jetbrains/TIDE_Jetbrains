package com.actions;

import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.state.ActiveState;
import org.jetbrains.annotations.NotNull;

/**
 * AnAction to reset an exercise to the latest submitted version.
 */
public class ResetExercise extends AnAction {
    /**
     * This function is called when subtask is reset via TIDE dropdown menu action.
     * @param e AnActionEvent resulting from user interaction
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        if (com.views.InfoView.displayOkCancelWarning("Confirm reset exercise?", "Reset exercise")) {
            return;
        }
        TimTask.getInstance().resetExercise();
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
    }
}
