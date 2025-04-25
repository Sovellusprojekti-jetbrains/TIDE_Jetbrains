package com.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.api.TideCommandExecutor
import com.customfile.TimTask
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.wm.ToolWindowManager
import com.state.ActiveState
import kotlinx.coroutines.runBlocking


class ProjectStartUp : ProjectActivity {


    override suspend fun execute(project: Project) {
        com.intellij.openapi.project.DumbService.getInstance(project).runWhenSmart { //Project must be ready first
            // It seems that PersistentState is ready at this point
            runBlocking { //Tries to prevent initialization of windows until login status is checked
                TideCommandExecutor.checkLogin()
            }
            runBlocking { //This forces ActiveState instantiation I guess
                ActiveState.getInstance().initProjectDependents()
            }
            runBlocking { //Not ideal but maybe better solution will be found
                while (true) {
                    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("TIDE Tool Window")
                    if (toolWindow != null && (toolWindow.isAvailable)) {
                        break;
                    }
                }
            }
            TideCommandExecutor.fetchCoursesAsync()
            runBlocking { //This forces EditorFactory instantiation I guess
                EditorFactory.getInstance()
            }
            runBlocking {
                val fileEditorManager = FileEditorManager.getInstance(project)
                val selectedEditor = fileEditorManager.selectedEditor
                if (selectedEditor?.file != null) {
                    //ActiveState.getInstance().setSubmittable(selectedEditor.file)
                    TimTask.evaluateFile(selectedEditor.file)
                } else {
                    ActiveState.getInstance().messageChanges()
                    //If CourseTaskPane is not open, messages might be ignored
                    //Implementing CourseTaskPane differently with TimTask class will fix
                }
            }
        }
    }
}
