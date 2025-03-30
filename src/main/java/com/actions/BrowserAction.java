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
        String url = "https://timbeta01.tim.education/view/";
        VirtualFile taskFile = FileEditorManager
                .getInstance(project)
                .getSelectedEditor()
                .getFile();
        String[] path = taskFile.getPath().split("/");
        String filename = taskFile.getName();
        String task = path[path.length-2];
        String demoname = path[path.length-3];
        String courseName = path[path.length-4];
        List<Course> courses = ActiveState.getInstance().getCourses();
        for (Course course : courses){
            if(Objects.equals(course.getName(), courseName)) {
                for (CourseTask demo: course.getTasks()) {
                    if(Objects.equals(demo.getName(),demoname)){
                        url += demo.getPath();
                        url += "#" + task;
                        break;
                    }
                }
                break;
            }
        }
        // Set the website URL
        HtmlEditorProvider.setUrl(url);

        // Create a virtual file with the expected name
        VirtualFile file = new LightVirtualFile("website_view"); // Must match HtmlEditorProvider's `accept()`

        // Open the file in the editor
        FileEditorManager.getInstance(project).openFile(file, true);
    }
}
