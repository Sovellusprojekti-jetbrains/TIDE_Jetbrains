package com.actions;

import com.customfile.CustomFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.state.ActiveState;
import org.jetbrains.annotations.NotNull;

/**
 * AnAction to show the course view in the editor.
 * Not used in the current build.
 */
public class OpenCustomEditorAction extends AnAction {

    /**
     * Show the course view in the editor.
     * @param event AnActionEvent originating from IntelliJ platform's internal messaging system.
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        // Get the current project
        Project project = ActiveState.getInstance().getProject();
        if (project == null) {
            return;
        }

        // Create a CustomFile instance
        VirtualFile file = new CustomFile();

        // Open the file in the editor
        FileEditorManager.getInstance(project).openFile(file, true);
    }
}
