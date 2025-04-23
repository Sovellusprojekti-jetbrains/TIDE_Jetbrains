package com.actions;

import com.api.ApiHandler;
import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.state.ActiveState;
import com.views.InfoView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ResetExercise extends AnAction {
    /**
     * This function is called when subtask is reset via TIDE dropdown menu action.
     * @param e AnActionEvent resulting from user interaction
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        Project project = e.getProject();
        if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            com.views.InfoView.displayWarning("No files open in editor!");
            return;
        }
        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        if (com.views.InfoView.displayOkCancelWarning("Confirm reset exercise?", "Reset exercise")) {
            return;
        }
        //ActiveState.getInstance().setSubmittable(file);
        //ActiveState.getInstance().messageChanges(); //Might be unnecessary but just in case
        TimTask.evaluateFile(file);
        if (!ActiveState.getInstance().isSubmittable()) {
            InfoView.displayWarning("File in editor is not a tim task!");
            return;
        }
        ApiHandler handler = new ApiHandler();
        ActiveState stateManager = ActiveState.getInstance();
        String coursePath = stateManager.getCourseName(file.getPath());
        try {
            handler.resetSubTask(file, coursePath);
        } catch (IOException ex) {
            com.api.LogHandler.logError("ResetExercise action performer", ex);
            com.api.LogHandler.logDebug(new String[]{"26 VirtualFile file", "36 String coursePath"},
                    new String[]{file.toString(), coursePath});
            InfoView.displayError(".timdata file not found!");
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            com.api.LogHandler.logError("ResetExercise action performer", ex);
            InfoView.displayError("An error occurred during task reset! Check Tide CLI");
            throw new RuntimeException(ex);
        }
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
