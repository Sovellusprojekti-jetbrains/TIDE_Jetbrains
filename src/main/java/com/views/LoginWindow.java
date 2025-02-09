package com.views;

import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;
import com.api.ApiHandler;
import javax.swing.*;

public class LoginWindow {
    private JPanel content;

    public LoginWindow(@NotNull ToolWindow toolWindow) {
        ApiHandler api = new ApiHandler();
        boolean isLoggedIn = api.isLoggedIn();
        if(isLoggedIn){
            content = new CourseView().getContent();
            //content = new CustomScreen().getContent();
        } else {
            content = new Loginview().getContent();
        }


    }


    public JPanel getContent() {

        ApiHandler api = new ApiHandler();
        boolean isLoggedIn = api.isLoggedIn();
        if(isLoggedIn){
            content = new CourseView().getContent();
            //content = new CustomScreen().getContent();
        } else {
            content = new Loginview().getContent();
        }



        return content;
    }
}
