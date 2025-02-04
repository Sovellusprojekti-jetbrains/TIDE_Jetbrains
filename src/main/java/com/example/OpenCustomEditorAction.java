package com.example;

import com.example.customfile.CustomFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class OpenCustomEditorAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
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
