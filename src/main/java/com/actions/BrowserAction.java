package com.actions;

import com.course.SubTask;
import com.customfile.HtmlEditorProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

public class BrowserAction extends AnAction {

    private final String baseURL = "https://tim.jyu.fi/view/";


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        //get demo and task with path from open file
        String url = baseURL;
        VirtualFile taskFile = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();

        if (taskFile == null) {
            return;
        }
        ActiveState instance = ActiveState.getInstance();
        SubTask openTask = instance.getOpenTask(taskFile.getPath());
        //TODO: handle null case.
        url += openTask.getPath();
        url += "#" + openTask.getIdeTaskId();

        // Set the website URL
        HtmlEditorProvider.setUrl(url);

        // Create a virtual file with the expected name
        VirtualFile file = new LightVirtualFile("website_view"); // Must match HtmlEditorProvider's `accept()`

        // Open the file in the editor
        FileEditorManager.getInstance(project).openFile(file, true);
    }

    /**
     * This function is called by ActiveState to update the actions state (able/disabled).
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
