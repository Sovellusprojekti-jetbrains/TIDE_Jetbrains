package com.views;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;

public final class InfoView {
    /**
     * Displays error bubble on screen down right corner.
     * @param message Error message as String
     */
    public static void displayError(String message) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(message, NotificationType.ERROR)
                .notify(ProjectManager.getInstance().getOpenProjects()[0]);
    }

    /**
     * Displays warning message on screen with ok/cancel dialog.
     * @param message Warning message as String
     * @param title Title for warning message
     * @return true if user press cancel, false if user press ok
     */
    public static boolean displayOkCancelWarning(String message, String title) {
        int selection = Messages.showOkCancelDialog(message,
                title,
                "OK",
                "Cancel",
                Messages.getWarningIcon());
        return selection == 2;
    }


    /**
     * Hide utility class constructor so it can't be instantiated.
     */
    private InfoView() {

    }
}
