package com.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.api.TideCommandExecutor


class ProjectStartUp : ProjectActivity {


    override suspend fun execute(project: Project) {
        TideCommandExecutor.checkLogin()
        TideCommandExecutor.fetchCoursesAsync()
    }
}

