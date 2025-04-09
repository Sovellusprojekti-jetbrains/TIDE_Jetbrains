package com.actions;

import com.api.ApiHandler;
import com.api.LogHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.state.ActiveState;
import com.util.Util;
import com.views.InfoView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Submit extends AnAction {
    /**
     * This function is called when subtask is submitted via TIDE dropdown menu action.
     * @param e AnActionEvent fired.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            com.views.CoursePaneWindow.getPane().printOutput("Please open a file to submit in the editor.");
            return;
        }
        Util.showWindow(project, "Output Window", true);

        VirtualFile file = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        // TODO: do something like the following to use the TIDE-CLI
        // function to submit all task files in a directory by checking
        // a checkbox, or find a more sensible way to implement it
        // boolean submitAll = submitAllInDirectoryCheckBox.isSelected();
        // String path = submitAll ? file.getParent().getPath() : file.getPath();

        try {
            ActiveState.getInstance().setSubmittable(file);
        } catch (IOException ex) {
            InfoView.displayError("An error occurred while evaluating if the file is a tim task!");
            LogHandler.logError("Submit action", ex);
            throw new RuntimeException(ex);
        }
        if (!ActiveState.getInstance().isSubmittable()) {
            InfoView.displayWarning("File in editor is not a tim task!");
            return;
        }

        com.views.CoursePaneWindow.getPane().setProgress(true, "Submitting...");
        new ApiHandler().submitExercise(file);
    }

    /**
     * This function is called by ActiveState to update the actions state (able/disabled).
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
