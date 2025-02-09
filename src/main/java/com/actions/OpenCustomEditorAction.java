package com.actions;

import com.customfile.CustomFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Class for opening a custom editor action.
 */
public class OpenCustomEditorAction extends AnAction {

    /**
     * What happens when the action is performed.
     * @param event An action event.
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        // Get the current project
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        // Create an instance of your custom virtual file
        VirtualFile file = new CustomFile();

        // Open the file in the editor
        FileEditorManager.getInstance(project).openFile(file, true);
    }
}
