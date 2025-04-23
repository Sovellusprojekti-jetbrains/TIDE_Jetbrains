package com.actions;

import com.api.ApiHandler;
import com.api.LogHandler;
import com.customfile.TimTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.state.ActiveState;
import com.util.Util;
import com.views.CourseTaskPane;
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
        TimTask.getInstance().submit(project); //This action shouldn't be available if TimTask instance is null
        /*if (!FileEditorManager.getInstance(project).hasOpenFiles()) {
            InfoView.displayError("Please open a file to submit in the editor.");
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

        //ActiveState.getInstance().setSubmittable(file);
        TimTask.evaluateFile(file);
        if (!ActiveState.getInstance().isSubmittable()) {
            InfoView.displayWarning("File in editor is not a tim task!");
            return;
        }

        try {
            CourseTaskPane.getInstance().setProgress(true, "Submitting...");
        } catch (Exception ex) {
            LogHandler.logError("Submit action", ex);
        }
        new ApiHandler().submitExercise(file);*/
    }

    /**
     * This function is called by the IDE when changes in the editor occur. Action's state will be updated.
     * @param e AnActionEvent originating from idea's internal messaging system.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        if (!ActiveState.getInstance().getLogin()) {
            e.getPresentation().setEnabled(ActiveState.getInstance().getLogin());
            //ActiveState.getInstance().messageChanges(); //Update in this class is used to inform changes to CourseTaskPane.
            return;
        }
        e.getPresentation().setEnabled(ActiveState.getInstance().isSubmittable());
        //ActiveState.getInstance().messageChanges();
    }
}
