package com.listeners

import com.actions.ActiveState
import com.api.TideCommandExecutor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages

class StartUp : StartupActivity {


      override fun runActivity(project: Project) {

        ApplicationManager.getApplication().invokeLater {
            val programName = "TIDE"
            if (System.getProperty("os.name").contains("Windows")) {


                val process = ProcessBuilder("where", programName).start()
                val exitcode = process.waitFor()
                if(exitcode != 0){
                    Messages.showMessageDialog(
                        "No TIDE installed",
                        "No Tide",
                        Messages.getInformationIcon()
                    )
                }

            } else {
                val process = ProcessBuilder("which", programName).start()
                val exitCode = process.waitFor()
                if(exitCode != 0){
                    Messages.showMessageDialog(
                        "No TIDE installed",
                        "No Tide",
                        Messages.getInformationIcon()
                    )
                }
            }


            //tabbedPane.remove(loginPane); // Hide Login tab
            TideCommandExecutor.checkLogin()
            val stateManager = ActiveState.getInstance()
            stateManager.updateCourses()

        }

    }

}