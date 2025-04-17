package com.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.api.TideCommandExecutor
import kotlinx.coroutines.runBlocking


class ProjectStartUp : ProjectActivity {


    override suspend fun execute(project: Project) {
        com.intellij.openapi.project.DumbService.getInstance(project).runWhenSmart { //Project must be ready first
            runBlocking { //Tries to prevent initialization of windows until login status is checked
                TideCommandExecutor.checkLogin()
            }
            TideCommandExecutor.fetchCoursesAsync()
        }
    }
}

