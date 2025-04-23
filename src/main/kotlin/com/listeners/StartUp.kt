package com.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager

class StartUp : StartupActivity {


      override fun runActivity(project: Project) {

        ApplicationManager.getApplication().invokeLater {
            val programName = "tide"
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

            /*
            //tabbedPane.remove(loginPane); // Hide Login tab
            val api = ApiHandler()
            val result = async { api.checkLogin()}
            TideCommandExecutor.checkLogin()
            System.out.println()
            val stateManager = ActiveState.getInstance()
            stateManager.updateCourses()
            */
        }
          //showWindow(project)

    }

    /**
     * Displays the toolwindow.
     * @param project The current project
     */
    fun showWindow(project: Project) {

            val toolWindowManager = ToolWindowManager.getInstance(project)
            val window = checkNotNull(toolWindowManager.getToolWindow("TIDE Tool Window"))
            window.show(null)

    }

}