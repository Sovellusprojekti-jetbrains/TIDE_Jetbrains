package com.util;

import com.api.LogHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.UIUtil;
import com.views.OutputWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Util {

    private Util() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Opens the given toolwindow.
     * @param project The project the window is in.
     * @param id Name of the toolwindow as specified in plugin.xml.
     * @param show A boolean value, true is show, false is hide.
     */
    public static void showWindow(Project project, String id, boolean show) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            assert window != null;
            if (show) {
                window.show(null);
            } else {
                window.hide(null);
            }
        });
    }


    /**
     * Changes the availability status of the toolwindow, and changes the icon.
     * @param project Project the window is in.
     * @param id Name of the window as specified in plugin.xml.
     * @param available Whether the window is set available or not.
     * @param iconPath Path to the icon svg.
     */
    public static void setWindowAvailable(Project project, String id, boolean available, String iconPath) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow(id);
            var callerClass = ReflectionUtil.getGrandCallerClass();
            if (callerClass != null) {
                window.setIcon(IconLoader.getIcon(iconPath, callerClass));
            }
            window.setAvailable(available);
            LogHandler.logInfo("Set Window Available: " + available + ", icon = " + iconPath);
        });
    }


    /**
     * Changes the icons of Course Task Pane and Output Window.
     * @param project Project they're in.
     * @param iconPath Path to the icon.
     */
    public static void setIcons(Project project, String iconPath) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow window = toolWindowManager.getToolWindow("Output Window");
            ToolWindow window2 = toolWindowManager.getToolWindow("Course Task");
            var callerClass = ReflectionUtil.getGrandCallerClass();
            if (callerClass != null) {
                window.setIcon(IconLoader.getIcon(iconPath, callerClass));
                window2.setIcon(IconLoader.getIcon(iconPath, callerClass));
            }
        });
    }


    /**
     * Print a message to the OutputWindow instance.
     * For now not used for anything; this is intentional.
     * Use for any of your printing needs.
     * @param project Current project
     * @param message The message to print
     */
    public static void printToOutput(Project project, String message) {
        showWindow(project, "Output Window", true);
        OutputWindow outputWindow = OutputWindow.getInstance();
        if (outputWindow != null) {
            OutputWindow.getInstance().printText(message);
        }
    }

    public static int getTextWidth(String text, JBFont font) {
        // Create a dummy image to get a Graphics object
        BufferedImage img = ImageUtil.createImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setFont(font);

        // Get the font metrics
        FontMetrics metrics = g.getFontMetrics(font);

        // Measure the text
        int width = metrics.stringWidth(text);

        // Clean up
        g.dispose();

        return width;
    }

    public static int getLabelTextWidth(JLabel label) {
        // Get the FontMetrics from the label itself
        FontMetrics metrics = label.getFontMetrics(label.getFont());

        // Get the text
        String text = label.getText();
        if (text == null) {
            return 0;
        }

        // Measure the text width
        return metrics.stringWidth(text);
    }
}
