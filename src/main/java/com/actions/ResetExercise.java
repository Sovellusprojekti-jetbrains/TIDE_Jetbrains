package com.actions;

import com.api.ApiHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
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
            com.views.InfoView.displayError("No files open in editor!", "task reset error");
            return;
        }
        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        if (com.views.InfoView.displayOkCancelWarning("Confirm reset exercise?", "Reset exercise")) {
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
            InfoView.displayError(".timdata file not found!", "Task reset error");
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            com.api.LogHandler.logError("ResetExercise action performer", ex);
            InfoView.displayError("An error occurred during task reset! Check Tide CLI", "Task reset error");
            throw new RuntimeException(ex);
        }
    }
}
