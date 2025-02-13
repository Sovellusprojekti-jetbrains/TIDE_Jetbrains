package com.listeners;

import com.api.ApiHandler;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.views.CoursePaneWindow;
import com.views.CustomScreen;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Listen to all tool windows being opened.
 * TODO: rename the class to something more appropriate
 */
@Service
public final class LoginWindowListener implements ToolWindowManagerListener  {
    @Override
    public void toolWindowShown(ToolWindow toolWindow) {
        System.out.println("tool window listener was called");
        ApiHandler api = new ApiHandler();
        var ContentManager = toolWindow.getContentManager();
        if ("TIDE Tool Window".equals(toolWindow.getId())) { // the toolWindow here is the tool window that was opened
            System.out.println("login window was opened");
            boolean isLoggedIn = api.isLoggedIn();

            for (Content content : ContentManager.getContents()) {
                if ("Courses".equals(content.getTabName())) {
                    // Select existing tab instead of adding a new one
                    ContentManager.setSelectedContent(content);
                    System.out.println("Tab '" + "Courses" + "' already exists. Selecting it.");
                    return;
                }
            }
            toolWindow.getContentManager().addContent(
                    com.intellij.ui.content.ContentFactory.getInstance().createContent(new CustomScreen().getContent(), "Courses", false));
        }
        if ("Course Task".equals(toolWindow.getId())) {
            System.out.println("course view was opened");
            for (Content content : ContentManager.getContents()) {
                if ("Course View".equals(content.getTabName())) {
                    // Select existing tab instead of adding a new one
                    ContentManager.setSelectedContent(content);
                    System.out.println("Tab '" + "Course View" + "' already exists. Selecting it.");
                    return;
                }
            }
            toolWindow.getContentManager().addContent(
                    com.intellij.ui.content.ContentFactory.getInstance().createContent(new CoursePaneWindow(toolWindow).getContent(), "Course View", false));
        }

    }
    //was used to update the login and courses view content but was replaced by a simpler method
    private void updateToolWindowContent(@NotNull ToolWindow toolWindow, boolean isLoggedIn){
        //toolWindow.getContentManager().removeAllContents(true);
        //toolWindow.getContentManager().getContent(JPanel).getManager().removeAllContents(true);
        var ContentManager = toolWindow.getContentManager();
        JPanel panel;
        if (isLoggedIn) {
            //panel = new CourseView().getContent();  // UI for logged-in users
        } else {
            //panel = new Loginview().getContent();  // UI for login prompt
        }

        for (Content content : ContentManager.getContents()) {
            if ("Login and Courses".equals(content.getTabName())) {
                // Select existing tab instead of adding a new one
                ContentManager.setSelectedContent(content);
                System.out.println("Tab '" + "Login and Courses" + "' already exists. Selecting it.");
                return;
            }
        }


        //toolWindow.getContentManager().addContent(
        //        com.intellij.ui.content.ContentFactory.getInstance().createContent(panel, "Login and Courses", false));

    }

    //simple test method to test the listener
    private void myMethod() {
        // Your custom logic here
        System.out.println("Executing custom logic...");
    }

}
