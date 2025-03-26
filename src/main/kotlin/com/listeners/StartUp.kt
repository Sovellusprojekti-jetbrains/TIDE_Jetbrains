package com.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.api.TideCommandExecutor
class StartUp : ProjectActivity {
    override suspend fun execute(project: Project) {

        ApplicationManager.getApplication().invokeLater {
            TideCommandExecutor.checkLogin()
        }

    }

}