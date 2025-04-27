package com.listeners;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

public class SmartLabelResizer {

    private static Timer debounceTimer;
    private static final Map<JLabel, String> labelToFullText = new HashMap<>();

    public static void setupSmartResizeForLabels(java.util.List<JLabel> labels, ToolWindow toolWindow) {
        for (JLabel label : labels) {
            labelToFullText.put(label, label.getText());
        }

        JComponent component = toolWindow.getComponent();

        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (debounceTimer != null && debounceTimer.isRunning()) {
                    debounceTimer.restart();
                } else {
                    debounceTimer = new Timer(200, event -> updateLabels(labels, component.getWidth()));
                    debounceTimer.setRepeats(false);
                    debounceTimer.start();
                }
            }
        });
    }

    private static void updateLabels(java.util.List<JLabel> labels, int availableWidth) {
        System.out.println(labels.size());
        for (JLabel label : labels) {
            String fullText = labelToFullText.get(label);
            if (fullText == null) continue;

            FontMetrics metrics = label.getFontMetrics(label.getFont());
            int textWidth = metrics.stringWidth(fullText);

            System.out.println(textWidth + " | " + availableWidth);

            if (textWidth <= availableWidth) {
                label.setText(fullText);
            } else {
                String contracted = contractTextToFit(fullText, metrics, availableWidth);
                label.setText(contracted);
            }
            label.revalidate();
            label.repaint();
        }
    }

    private static String contractTextToFit(String text, FontMetrics metrics, int maxWidth) {
        String ellipsis = "...";
        int ellipsisWidth = metrics.stringWidth(ellipsis);

        if (ellipsisWidth > maxWidth) {
            return ""; // No space even for ellipsis
        }

        int available = maxWidth - ellipsisWidth;
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(c);
            if (metrics.stringWidth(sb.toString()) > available) {
                sb.setLength(sb.length() - 1); // remove last char that overflows
                break;
            }
        }

        return sb.toString() + ellipsis;
    }
}
