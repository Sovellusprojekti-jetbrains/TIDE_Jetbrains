package com.actions;

import com.course.Course;
import com.course.CourseTask;
import com.customfile.HtmlEditorProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BrowserAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;
        //get demo and task with path from open file
        String url = "https://tim.jyu.fi/view/";
        VirtualFile taskFile = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();
        String[] path = taskFile.getPath().split("/");
        String task = path[path.length-2];
        String demoname = path[path.length-3];
        //String courseName = path[path.length-4];
        ActiveState instance = ActiveState.getInstance();
        List<Course> courses = instance.getCourses();
        String courseName = instance.getCourseName(taskFile.getPath());
        String[] temp = instance.getTimWebPathAndId(taskFile.getPath());
        url += temp[0];
        url += "#" + temp[1];

        // Set the website URL
        HtmlEditorProvider.setUrl(url);

        // Create a virtual file with the expected name
        VirtualFile file = new LightVirtualFile("website_view"); // Must match HtmlEditorProvider's `accept()`

        // Open the file in the editor
        FileEditorManager.getInstance(project).openFile(file, true);
    }
}
