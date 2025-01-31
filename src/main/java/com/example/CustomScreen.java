package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




public class CustomScreen {
    private JButton loginButton;
    private JPanel panel1;
    private JTabbedPane tabbedPane;
    private JPanel loginPane;
    private JPanel titlePanel;
    private JLabel courseLabel;
    private JPanel coursePanel;

    public CustomScreen() {

        // ilman setLayout-kutsua tämä kaatuu nullpointteriin
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        String[] courses = new String[]{"A", "B"};
        for (int i = 0; i < 2; i++) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel label = new JLabel();
            label.setText("Course " + courses[i]);
            label.setFont(new Font("Arial", Font.BOLD, 26));
            label.setHorizontalAlignment(SwingConstants.LEFT);
            coursePanel.add(label);

            for (int j = 0; j < 10; j++) {
                JPanel subPanel = new JPanel();
                subPanel.setLayout(new BorderLayout());
                JLabel labelWeek = new JLabel();
                labelWeek.setText("Label " + j);
                subPanel.add(labelWeek, BorderLayout.WEST);

                JButton dButton = new JButton();
                dButton.setText("Download");
                subPanel.add(dButton, BorderLayout.EAST);

                gbc.gridy = j;
                panel.add(subPanel, gbc);
            }
            coursePanel.add(panel);
        }



        panel1.revalidate();
        panel1.repaint();

        //currently assumes that the user has the TIM CLI installed
        //need some checks and tests in the future
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String command = "tide login";

                    ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    int exitCode = process.waitFor();
                    System.out.println("Process exited with code: " + exitCode);


                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public JPanel getContent() {
        return panel1;
    }
}
