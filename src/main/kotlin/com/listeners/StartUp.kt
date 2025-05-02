package com.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.wm.ToolWindowManager
import com.views.InstallScreen
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class StartUp : StartupActivity {
    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            val programName = "tide"
            val pluginsPath = System.getProperty("idea.plugins.path")
            println(pluginsPath)
            if (System.getProperty("os.name").contains("Windows")) {
                val process = ProcessBuilder("where", programName).start()
                val exitcode = process.waitFor()
                if (exitcode != 0) {
                    showInstall()
                }
            } else {
                val process = ProcessBuilder("which", programName).start()
                val exitCode = process.waitFor()
                if (exitCode != 0) {
                    showInstall()
                }
            }
        }
    }

    /**
     * Displays the toolwindow.
     * @param project The current project
     */
    private fun showWindow(project: Project) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val window = checkNotNull(toolWindowManager.getToolWindow("TIDE Tool Window"))
        window.show(null)
    }

    private fun showInstall() {
        ApplicationManager.getApplication().invokeLater {
            val frame = JFrame("Settings")
            frame.add(InstallScreen().content)
            frame.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent) {
                    frame.isVisible = false
                }
            })
            frame.setSize(600, 600)
            frame.isVisible = true
        }
    }
}