package com.example;

import javax.swing.*;
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
    private JPanel courseAPanel;
    private JLabel courseALabel;
    private JPanel A02;
    private JLabel A02Name;
    private JButton A02Download;
    private JButton A02Open;
    private JPanel A01;
    private JPanel courseBPanel;
    private JLabel courseBLabel;

    public CustomScreen() {

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
