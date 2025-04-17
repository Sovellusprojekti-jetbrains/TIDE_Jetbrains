package com.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.api.TideCommandExecutor
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.coroutines.runBlocking


class ProjectStartUp : ProjectActivity {


    override suspend fun execute(project: Project) {
        com.intellij.openapi.project.DumbService.getInstance(project).runWhenSmart { //Project must be ready first
            runBlocking { //Tries to prevent initialization of windows until login status is checked
                TideCommandExecutor.checkLogin()
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
        }
    }
}

